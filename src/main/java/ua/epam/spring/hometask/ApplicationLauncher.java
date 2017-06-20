package ua.epam.spring.hometask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;

public class ApplicationLauncher {
    private static ApplicationContext context = new ClassPathXmlApplicationContext("service-config.xml");
    private static Scanner scanner = new Scanner(System.in);
    UserService userService;
    BookingService bookingService;
    EventService eventService;


    public static void main(String[] args) {
        ApplicationLauncher launcher = context.getBean("launcher", ApplicationLauncher.class);
        launcher.initUserDao();
        launcher.initEventDao();
        launcher.run();

        scanner.close();
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

    private void initUserDao() {
        User registeredUser = context.getBean("registeredUser", User.class);
        System.out.println("Saving initial user...");
        userService.save(registeredUser);
        System.out.println(String.format("Initial user '%s %s' saved successfully! Email: %s", registeredUser.getFirstName(), registeredUser.getLastName(), registeredUser.getEmail()));
    }

    private void initEventDao() {
        System.out.println("\nSaving initial events...");
        Event event1 = context.getBean("event1", Event.class);
        Event event2 = context.getBean("event2", Event.class);

        eventService.save(event1);
        System.out.println(String.format("Initial movie '%s' saved successfully!", event1.getName()));

        eventService.save(event2);
        System.out.println(String.format("Initial movie '%s' saved successfully!", event2.getName()));

    }

    private void run() {
        System.out.println("\nLet's start booking!");

        List<Event> upcomingEvents = new ArrayList<>(eventService.getUpcomingEvents(LocalDateTime.now().plusDays(10)));

        System.out.println("Choose the movie to watch:");
        int i = 1;
        for (Event event : upcomingEvents) {
            System.out.println(i++ + ". " + event.getName());
        }


        //        show available airDates for chosen event
        int chosenEventNumber = readInputNumber(upcomingEvents.size(), "There is no movie in list with number %d");
        Event chosenEvent = upcomingEvents.get(chosenEventNumber - 1);
        System.out.println(chosenEvent.getName());
        System.out.println("Rating: " + chosenEvent.getRating());
        List<LocalDateTime> airDates = new ArrayList<>(chosenEvent.getAirDates());

        for (int j = 1; j <= airDates.size(); j++) {
            //            TODO: format dateTime
            System.out.println(j + ". " + airDates.get(j-1));
        }

        // show available seats
        int chosenAirDateNumber = readInputNumber(airDates.size(), "There is no air on %s");
        LocalDateTime chosenAirDate = airDates.get(chosenAirDateNumber - 1);
        Auditorium airHall = chosenEvent.getAuditoriums().get(chosenAirDate);

        Set<Long> reservedSeats = bookingService.getReservedSeatsForEvent(chosenEvent, chosenAirDate);

        Set<Long> availableVipSeats = airHall.getVipSeats();
        availableVipSeats.removeAll(reservedSeats);

        Set<Long> availableSeats = airHall.getAllSeats();
        availableSeats.removeAll(reservedSeats);
        availableSeats.removeAll(availableVipSeats);

        printSeats(availableSeats, bookingService.getTicketPrice(chosenEvent, chosenAirDate, availableSeats.stream().findFirst().get()), false);
        printSeats(availableVipSeats, bookingService.getTicketPrice(chosenEvent, chosenAirDate, availableVipSeats.stream().findFirst().get()), true);

//        choose seats
        Set<Long> chosenSeats = readInputSeatsNumbers(availableSeats, availableVipSeats);
        Double totalCost = bookingService.getTicketsTotalPrice(chosenEvent, chosenAirDate, chosenSeats);
        System.out.println(String.format("You have chosen %s seats.\nTotal cost is: %s", new Object[]{chosenSeats.size(), totalCost}));

//        logging in
//        System.out.println("Have a profile?\nEnter email if registered (see init logs) or just skip this step if not:");
//        String inputEmail = scanner.nextLine();
//        User currentUser = null;
//        if (inputEmail != null && !inputEmail.isEmpty()) {
//            currentUser = userService.getUserByEmail(inputEmail);
//        }


    }

    private Set<Long> readInputSeatsNumbers(Set<Long> availableSeats, Set<Long> availableVipSeats) {
        Set<Long> allAvailableSeats = new TreeSet<>();
        allAvailableSeats.addAll(availableSeats);
        allAvailableSeats.addAll(availableVipSeats);

        return readInputSeatsNumbers(allAvailableSeats);
    }

    private Set<Long> readInputSeatsNumbers(Set<Long> allAvailableSeats) {
        System.out.println("\n(enter seat number(s))");
        Set<Long> result = new TreeSet<>();

        String inputRow = scanner.nextLine();

        for (String seatNumber : inputRow.split("[, ]")) {
            if (!isInputSeatValid(seatNumber, allAvailableSeats)) {
                System.out.println(String.format("Wrong seat number '%s'. Possible reasons: no such seat in auditorium; the seat is already reserved or there is a mistape occured.", seatNumber));
                System.out.println("Try again or enter '-1' to exit.");
                readInputSeatsNumbers(allAvailableSeats);
            }

            result.add(Long.parseLong(seatNumber));
        }

        return result;
    }

    private boolean isInputSeatValid(String seatNumber, Set<Long> allAvailableSeats) {
        Long seat;
        try {
            seat = Long.parseLong(seatNumber);
        } catch (NumberFormatException e) {
            return false;
        }

        return allAvailableSeats.contains(seat);
    }

    private int readInputNumber(int maxValue, String errorMessage) {
        System.out.println("\n(enter the number)");

        int input = scanner.nextInt();
        scanner.nextLine();
        if (input == -1) {
            System.out.println("Good buy!");
            System.exit(0);
        }

        if (input > maxValue) {
            System.out.println(String.format(errorMessage, input));
            System.out.println("Try again or enter '-1' to exit.");
            readInputNumber(maxValue, errorMessage);
        }


        return input;
    }

    private User loginUser() {


        return null;
    }

    private void printSeats(@Nonnull Set<Long> seats, Double seatPrice, boolean isVip) {
        if (seats.isEmpty()) {
            return;
        }

        String title = isVip ? "Seat price: " + seatPrice : "VIP Seat price: " + seatPrice;
        System.out.println(title);
        System.out.print("|| ");
        for (Long seat : seats) {
            System.out.print(seat + " ");
        }
        System.out.println(" ||");

    }

}
