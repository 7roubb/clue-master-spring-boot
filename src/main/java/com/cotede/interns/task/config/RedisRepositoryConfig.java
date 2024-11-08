package com.cotede.interns.task.config;
import com.cotede.interns.task.cases.Case;
import com.cotede.interns.task.games.Game;
import com.cotede.interns.task.rooms.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisRepositoryConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private String redisPort;
    @Value("${spring.data.redis.password}")
    private String password ;
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, Integer.parseInt(redisPort));
        lettuceConnectionFactory.setPassword(password);
        return lettuceConnectionFactory;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(serializer);
        return redisTemplate;
    }
    @Bean
    public RedisTemplate<String, Case> caseRedisTemplate() {
        RedisTemplate<String, Case> caseRedisTemplate = new RedisTemplate<>();
        caseRedisTemplate.setConnectionFactory(redisConnectionFactory());
        caseRedisTemplate.setKeySerializer(new StringRedisSerializer());
        caseRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Case.class));
        return caseRedisTemplate;
    }
    @Bean
    public RedisTemplate<String, Character> characterRedisTemplate() {
        RedisTemplate<String, Character> characterRedisTemplate = new RedisTemplate<>();
        characterRedisTemplate.setConnectionFactory(redisConnectionFactory());
        characterRedisTemplate.setKeySerializer(new StringRedisSerializer());
        characterRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Character.class));
        return characterRedisTemplate;
    }
    @Bean
    public RedisTemplate<String, Game> gameRedisTemplate() {
        RedisTemplate<String, Game> gameRedisTemplate = new RedisTemplate<>();
        gameRedisTemplate.setConnectionFactory(redisConnectionFactory());
        gameRedisTemplate.setKeySerializer(new StringRedisSerializer());
        gameRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Game.class));
        return gameRedisTemplate;
    }


}