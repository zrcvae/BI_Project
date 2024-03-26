package com.yupi.springbootinit.config;

import io.swagger.models.auth.In;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zrc
 * @date 2024/03/18
 *  @ConfigurationPropertie 从application.yml文件中读取前缀为“spring.redis”的配置项
 *  但是需要变量名和配置类中的名称保持一直才能获取到
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {
    private Integer database;
    private String host;
    private Integer port;

    @Bean
    public RedissonClient getRedissonClient(){
        // 1、创建对象
        Config config = new Config();
        // 添加单机Redisson配置
        config.useSingleServer()
        // 设置数据库（一般将存储和redisson数据库分开）
                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port);

        // 2、创建Redisson实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
