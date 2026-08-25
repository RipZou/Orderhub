package com.orderhub.repository;
import com.orderhub.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, String> {


    /**
     * 在 Interface 中，普通方法默认是 public
     * 那么 Interface 中能不能用 private 呢？
     * 接口中的抽象方法（没有具体实现的）不能是 private，因为没法被类看到，也就没法实现它
     * 接口中的 private 方法只能被自己使用，主要是给内部的 default 方法当助手用的
     * 在类实现这些 Default 方法时，也必须是 public
     * @param product
     */


}
