package com.orderhub.domain;

public enum OrderStatus {
    CREATED,
    PAID,
    SHIPPED,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus target) {
        switch(this){

            case CREATED:
                return target == PAID || target == CANCELLED;

            case PAID:
                return target == SHIPPED || target == CANCELLED;

            case SHIPPED:
                return target == COMPLETED;

            case COMPLETED:

            case CANCELLED:
                return false;

            default:
                return false;
        }
    }

}
