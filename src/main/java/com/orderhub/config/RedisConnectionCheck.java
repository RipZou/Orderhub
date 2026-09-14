package com.orderhub.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

@Component
public class RedisConnectionCheck implements CommandLineRunner {

    private final StringRedisTemplate redis;

    public RedisConnectionCheck(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public void run(String... args) {
        redis.opsForValue().set("orderhub:ping", "pong");
        System.out.println("Redis check: " + redis.opsForValue().get("orderhub:ping"));
    }
}
