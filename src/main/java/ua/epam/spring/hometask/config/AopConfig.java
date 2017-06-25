package ua.epam.spring.hometask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import ua.epam.spring.hometask.aop.CounterAspect;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {

    @Bean
    public CounterAspect counterAspect() {
        return new CounterAspect();
    }

}
