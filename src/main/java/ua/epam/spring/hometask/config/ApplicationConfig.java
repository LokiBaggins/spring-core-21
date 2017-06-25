package ua.epam.spring.hometask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.dao.impl.AuditoriumDaoImpl;
import ua.epam.spring.hometask.dao.impl.EventDaoImpl;
import ua.epam.spring.hometask.dao.impl.TicketDaoImpl;
import ua.epam.spring.hometask.dao.impl.UserDaoImpl;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import static ua.epam.spring.hometask.domain.EventRating.*;


@Configuration
@PropertySource("classpath:auditoria.properties")
public class ApplicationConfig {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Value("${auditorium1.name}") String auditoriumName1;
    @Value("${auditorium1.number_of_seats}") Long auditoriumSeats1;
    @Value("#{'${auditorium1.vip_seats}'.split(',')}") Set<Long> vipSeats1;
    @Value("${auditorium2.name}") String auditoriumName2;
    @Value("${auditorium2.number_of_seats}") Long auditoriumSeats2;
    @Value("#{'${auditorium2.vip_seats}'.split(',')}") Set<Long> vipSeats2;
    @Value("${auditorium3.name}") String auditoriumName3;
    @Value("${auditorium3.number_of_seats}") Long auditoriumSeats3;
    @Value("#{'${auditorium3.vip_seats}'.split(',')}") Set<Long> vipSeats3;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }



    @Bean
    public Event event1() {
        LocalDateTime date1 = LocalDateTime.parse("2017-06-30 10:00", dateFormatter);
        LocalDateTime date2 = LocalDateTime.parse("2017-06-30 14:00", dateFormatter);
        LocalDateTime date3 = LocalDateTime.parse("2017-06-30 19:00", dateFormatter);

        TreeSet<LocalDateTime> airDates = new TreeSet<>();
        airDates.add(date1);
        airDates.add(date2);
        airDates.add(date3);

        TreeMap<LocalDateTime, Auditorium> audiitoriumsMap = new TreeMap<>();
        audiitoriumsMap.put(date1, hall1());
        audiitoriumsMap.put(date2, hall1());
        audiitoriumsMap.put(date3, hall1());

        return new Event("Rogue One: A Star Wars Story", 100.00, MID, airDates, audiitoriumsMap);
    }

    @Bean
    public Event event2() {
        LocalDateTime date4 = LocalDateTime.parse("2017-06-29 10:00", dateFormatter);
        LocalDateTime date5 = LocalDateTime.parse("2017-06-29 14:00", dateFormatter);
        LocalDateTime date6 = LocalDateTime.parse("2017-06-29 19:00", dateFormatter);

        TreeSet<LocalDateTime> airDates = new TreeSet<>();
        airDates.add(date4);
        airDates.add(date5);
        airDates.add(date6);

        TreeMap<LocalDateTime, Auditorium> audiitoriumsMap = new TreeMap<>();
        audiitoriumsMap.put(date4, hall1());
        audiitoriumsMap.put(date5, hall1());
        audiitoriumsMap.put(date6, hall1());

        return new Event("Green Mile", 100.00, HIGH, airDates, audiitoriumsMap);
    }

    @Bean
    public Auditorium hall1() {
        return new Auditorium(auditoriumName1, auditoriumSeats1, vipSeats1);
    }
    
    @Bean
    public Auditorium hall2() {
        return new Auditorium(auditoriumName2, auditoriumSeats2, vipSeats2);
    }

    @Bean
    public Auditorium hall3() {
        return new Auditorium(auditoriumName3, auditoriumSeats3, vipSeats3);
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

}
