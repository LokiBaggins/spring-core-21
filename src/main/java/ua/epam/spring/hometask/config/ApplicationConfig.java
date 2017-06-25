package ua.epam.spring.hometask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.dao.impl.AuditoriumDaoImpl;
import ua.epam.spring.hometask.dao.impl.EventDaoImpl;
import ua.epam.spring.hometask.dao.impl.TicketDaoImpl;
import ua.epam.spring.hometask.dao.impl.UserDaoImpl;
import ua.epam.spring.hometask.domain.User;

@Configuration
@PropertySource("classpath:auditoria.properties")
public class ApplicationConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public User registeredUser() {
        return new User("Bilbo", "Baggins", "bilbo.baggins@email.com");
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }

    @Bean
    public EventDao eventDao() {
        return new EventDaoImpl();
    }

    @Bean
    public TicketDao ticketDao() {
        return new TicketDaoImpl();
    }

    @Bean
    public AuditoriumDao auditoriumDao() {
        return new AuditoriumDaoImpl();
    }

//    <bean id="eventDao" class="ua.epam.spring.hometask.dao.impl.EventDaoImpl"/>
//    <bean id="ticketDao" class="ua.epam.spring.hometask.dao.impl.TicketDaoImpl"/>
//    <bean id="userDao" class="ua.epam.spring.hometask.dao.impl.UserDaoImpl"/>


}
