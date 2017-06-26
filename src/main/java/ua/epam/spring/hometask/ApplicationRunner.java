package ua.epam.spring.hometask;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.epam.spring.hometask.config.AopConfig;
import ua.epam.spring.hometask.config.ApplicationConfig;

import java.util.Arrays;
import java.util.HashSet;

public class ApplicationRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class, AopConfig.class);

        Application application = context.getBean(Application.class);
        application.initUserDao(context);
        application.initEventDao(context);

        application.run(1, 2, new HashSet<>(Arrays.asList(3L, 5L, 7L)), "some.user@anymail.com");
    }
}
