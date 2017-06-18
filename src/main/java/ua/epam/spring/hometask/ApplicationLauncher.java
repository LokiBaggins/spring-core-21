package ua.epam.spring.hometask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.util.Set;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;

public class ApplicationLauncher {
    private static ApplicationContext context = new ClassPathXmlApplicationContext("service-config.xml");
    UserService userService;
    BookingService bookingService;
    EventService eventService;


    private void initUserDao() {
        User registeredUser = context.getBean("registeredUser", User.class);
        System.out.println("Saving initial user...");
        userService.save(registeredUser);
        System.out.println(String.format("Initial user '%s %s' saved successfully! \n\n", registeredUser.getFirstName(), registeredUser.getLastName()));
    }


    private void initEventDao() {
        Event event1 = context.getBean("event1", Event.class);
        Event event2 = context.getBean("event2", Event.class);

        eventService.save(event1);
        eventService.save(event2);

    }

    public static void main(String[] args) {
        ApplicationLauncher launcher = context.getBean("launcher", ApplicationLauncher.class);
        launcher.initUserDao();
        launcher.initEventDao();
        launcher.run();

    }

    private void run() {
        System.out.println("Let's start booking!");

        Set<Event> upcomingEvents = eventService.getUpcomingEvents(LocalDateTime.now().plusDays(7));
        System.out.println("Choose the movie to watch:\n");
        int i = 1;
        for (Event event : upcomingEvents) {
            System.out.println(i++ + ". " + event.getName());
        }
    }


    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
