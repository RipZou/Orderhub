package com.orderhub.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderhub.domain.Product;
import com.orderhub.repository.ProductRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private StringRedisTemplate redis;
    private ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepo, StringRedisTemplate redis, ObjectMapper objectMapper) {
        this.productRepo = productRepo;
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    /**
     * POST Products
     * @param productId
     * @param ProductName
     * @param price
     * @param stock
     * @return
     */
    public Product postProduct(String productId, String ProductName, double price, int stock) {

        Product product = new Product(productId, ProductName, price, stock);

        productRepo.save(product);

        /**
         * Why do we delete data in Redis after new write?
         * e.g. we delete it, another request read it before update, write it to redis
         * then we update it, now the data in DB and Redis inconsistent
         */
        String key = "product:" + productId;
        redis.delete(key);
        System.out.println("cache evicted: " + key);

        return product;
    }

    /**
     * GET Product
     * @param productId
     * @return
     */
    public Product getProduct(String productId) {

        String key = "product:" + productId;
        String json = redis.opsForValue().get(key);

        if(json != null) {
            System.out.println("cache hit:" + key);
            try {
                var node = objectMapper.readTree(json);
                return new Product(
                        node.get("id").asText(),
                        node.get("name").asText(),
                        node.get("price").asDouble(),
                        node.get("stock").asInt()
                );
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read product from cache", e);
            }
        }

        Optional<Product> optional = productRepo.findById(productId);
        if (optional.isEmpty()) throw new IllegalArgumentException("Product does not exist!");
        Product product = optional.get();

        try {
            String toCache = objectMapper.writeValueAsString(Map.of(
                    "id", product.getId(),
                    "name", product.getName(),
                    "price", product.getPrice(),
                    "stock", product.getStock()
            ));
            redis.opsForValue().set(key, toCache, Duration.ofMinutes(10));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write product to cache", e);
        }

        return product;
    }

}
