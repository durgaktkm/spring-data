package co.mimosa.nms.config;

import co.mimosa.nms.redis.RedisOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPool;

/**
 * Created by ramdurga on 3/22/15.
 */
@Configuration
@ComponentScan(basePackages = {"co.mimosa"})
@PropertySource(value = { "classpath:nms.properties","file:nms_override.properties"}, ignoreResourceNotFound = true )
public class AppConfig {
    @Resource
    private Environment env;
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(env.getProperty("redis-host"));
        jedisConnectionFactory.setPort(Integer.parseInt(env.getProperty("redis-port")));
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }
    @Bean
    public RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }
    @Bean
    public RedisOperations redisOperations(){
        return new RedisOperations();
    }
    @Bean
    public RedisCacheManager cacheManager() {
        return new RedisCacheManager(redisTemplate());
    }
    @Bean
    public JedisPool pool(){
        return new JedisPool(env.getProperty("redis-host"),Integer.parseInt(env.getProperty("redis-port")));
    }

}
