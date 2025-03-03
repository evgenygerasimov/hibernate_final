package org.example.bd;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.SneakyThrows;

import java.util.Properties;

import static java.util.Objects.nonNull;

public class RedisConnectionCreator {

    private final RedisClient redisClient;

    public RedisConnectionCreator() {

        this.redisClient = prepareRedisClient();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    @SneakyThrows
    private RedisClient prepareRedisClient() {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("redis.properties"));

        return RedisClient.create(RedisURI.create(properties.getProperty("redis.connection.url")));
    }

    public StatefulRedisConnection<String, String> getConnection() {
        return redisClient.connect();
    }

    public void shutdown() {
        if (nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }
}
