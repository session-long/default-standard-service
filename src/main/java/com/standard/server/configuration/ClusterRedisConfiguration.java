package com.standard.server.configuration;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;

/**
 * Redis配置类
 */
@Slf4j
@Data
@EnableCaching
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@ConditionalOnProperty("spring.redis.cluster.nodes")
public class ClusterRedisConfiguration {

    private long timeout;

    @Autowired
    private ClusterRedisProperties clusterRedisProperties;

    @Autowired
    private RedisProperties properties;

    @PostConstruct
    public void init() {
      log.info("Redis集群配置类装载完成。 {} {}", properties.getCluster().getNodes() );
    }

    /**
     * 注入RedisTemplate对象
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Data
    @ToString
    @Configuration
    @ConfigurationProperties(prefix = "spring.redis.cluster")
    public static class ClusterRedisProperties{

        private String nodes;

        private int maxRedirects;

    }

}
