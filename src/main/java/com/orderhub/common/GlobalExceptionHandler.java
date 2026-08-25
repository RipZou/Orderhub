package com.orderhub.common;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalArgumentException
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {

        ApiError apiError = new ApiError();
        apiError.setStatus(400);
        apiError.setError("Bad Request");
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());
        return ResponseEntity.status(400).body(apiError);

    }

    /**
     * IllegalStateException
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request
    ) {

        ApiError apiError = new ApiError();
        apiError.setStatus(409);
        apiError.setError("Conflict");
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());
        return ResponseEntity.status(409).body(apiError);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handelValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError();
        apiError.setStatus(400);
        apiError.setError("Bad Request");
        apiError.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());
        apiError.setPath(request.getRequestURI());
        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLock(
            OptimisticLockingFailureException ex,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError();
        apiError.setStatus(409);
        apiError.setError("Conflict");
        apiError.setMessage("Order was updated by another request, please retry");
        apiError.setPath(request.getRequestURI());
        return ResponseEntity.status(409).body(apiError);
    }



}
