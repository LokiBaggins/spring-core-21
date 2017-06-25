package ua.epam.spring.hometask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.config.AopConfig;
import ua.epam.spring.hometask.config.ApplicationConfig;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;

public class ApplicationLauncher {
    private static Scanner scanner = new Scanner(System.in);

    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private EventService eventService;

    public ApplicationLauncher() {
    }

    public ApplicationLauncher(UserService userService, BookingService bookingService, EventService eventService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.eventService = eventService;
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class, AopConfig.class);

//        UserService userService = context.getBean(UserService.class);
//        EventService eventService = context.getBean(EventService.class);
//        BookingService bookingService = context.getBean(BookingService.class);
//        ApplicationLauncher launcher = new ApplicationLauncher(userService, bookingService, eventService);

        ApplicationLauncher launcher = context.getBean(ApplicationLauncher.class);
        launcher.initUserDao(context);
        launcher.initEventDao(context);

        launcher.run(1, 2, new HashSet<>(Arrays.asList(3L, 5L, 7L)), "some.user@anymail.com");

        scanner.close();
    }

    private void initUserDao(ConfigurableApplicationContext context) {
        User registeredUser = context.getBean("registeredUser", User.class);
        System.out.println("Saving initial user...");
        userService.save(registeredUser);
        System.out.println(String.format("Initial user '%s %s' saved successfully! Email: %s", registeredUser.getFirstName(), registeredUser.getLastName(), registeredUser.getEmail()));
    }

    private void initEventDao(ConfigurableApplicationContext context) {
        System.out.println("\nSaving initial events...");
        Event event1 = context.getBean("event1", Event.class);
        Event event2 = context.getBean("event2", Event.class);

        eventService.save(event1);
        System.out.println(String.format("Initial movie '%s' saved successfully!", event1.getName()));

        eventService.save(event2);
        System.out.println(String.format("Initial movie '%s' saved successfully!", event2.getName()));

    }

    public void run(Integer eventNumber, Integer airDateNumber, Set<Long> seatsNumbers, String userEmail) {
        System.out.println("\nLet's start booking!");

        List<Event> upcomingEvents = new ArrayList<>(eventService.getUpcomingEvents(LocalDateTime.now().plusDays(10)));

        System.out.println("Choose the movie to watch:");
        int i = 1;
        for (Event event : upcomingEvents) {
            System.out.println(i++ + ". " + event.getName());
        }


        //        show available airDates for chosen event
        if (eventNumber != null) {
            System.out.println(String.format("PREDEFINED event number is '%s' (%s)", eventNumber, upcomingEvents.get(eventNumber - 1).getName()));
        } else {
            eventNumber = readInputNumber(upcomingEvents.size(), "There is no movie in list with number %d");
        }

        Event chosenEvent = eventService.getByName(upcomingEvents.get(eventNumber - 1).getName()); // it's not racianal, but needed for AOP to count calls
        System.out.println();
        System.out.println(chosenEvent.getName());
        System.out.println("Rating: " + chosenEvent.getRating());
        List<LocalDateTime> airDates = new ArrayList<>(chosenEvent.getAirDates());

        for (int j = 1; j <= airDates.size(); j++) {
            //            TODO: format dateTime
            System.out.println(j + ". " + airDates.get(j - 1));
        }

        // show available seats
        if (airDateNumber != null) {
            System.out.println(String.format("PREDEFINED air date is '%s' (%s)", airDateNumber, airDates.get(airDateNumber - 1)));
        } else {
            airDateNumber = readInputNumber(airDates.size(), "There is no air on %s");
        }

        LocalDateTime chosenAirDate = airDates.get(airDateNumber - 1);
        Auditorium airHall = chosenEvent.getAuditoriums().get(chosenAirDate);

        Set<Long> reservedSeats = bookingService.getReservedSeatsForEvent(chosenEvent, chosenAirDate);

        Set<Long> availableVipSeats = airHall.getVipSeats();
        availableVipSeats.removeAll(reservedSeats);

        Set<Long> availableSeats = airHall.getAllSeats();
        availableSeats.removeAll(reservedSeats);
        availableSeats.removeAll(availableVipSeats);

        System.out.println();
        printSeats(availableSeats, bookingService.getTicketPrice(chosenEvent, chosenAirDate, availableSeats.stream().findFirst().get()), false);
        printSeats(availableVipSeats, bookingService.getTicketPrice(chosenEvent, chosenAirDate, availableVipSeats.stream().findFirst().get()), true);

//        choose seats
        if (seatsNumbers != null) {
            System.out.println(String.format("PREDEFINED seta numbers are: %s.", Arrays.toString(seatsNumbers.toArray())));
        } else {
            seatsNumbers = readInputSeatsNumbers(availableSeats, availableVipSeats);
        }

        Double totalCost = bookingService.getTicketsTotalPrice(chosenEvent, chosenAirDate, seatsNumbers);
        System.out.println(String.format("You have chosen %s seats.\nTotal cost is: %s", new Object[]{seatsNumbers.size(), totalCost}));

//        logging in
        System.out.println("\nHave a profile?\nEnter email if registered (see init logs) or just skip this step if not:");

        if (userEmail != null && !userEmail.isEmpty()) {
            System.out.println(String.format("PREDEFINED user email is: '%s'.", userEmail));
        } else {
            userEmail = scanner.nextLine();
        }

        User currentUser = null;
        if (userEmail != null && !userEmail.isEmpty()) {
            currentUser = userService.getUserByEmail(userEmail);

            if (currentUser != null) {
                System.out.println(String.format("\nGlad to see you again, %s %s!", currentUser.getFirstName(), currentUser.getLastName()));
            } else {
                System.out.println(String.format("\nWe can't find user with email '%s'. Weloome, mr. Anonymous!", userEmail));
            }
        }

//        purchasing tickets
        Set<Ticket> ticketsToBuy = new HashSet<>();
        for (Long seat : seatsNumbers) {
            ticketsToBuy.add(new Ticket(currentUser, chosenEvent, chosenAirDate, seat));
        }
        bookingService.bookTickets(ticketsToBuy);

//        check and confirm success transaction result
        Set<Ticket> allBookedTickets = bookingService.getPurchasedTicketsForEvent(chosenEvent, chosenAirDate);
        Set<Long> ticketsToBuyIDs = ticketsToBuy.stream()
                .map(Ticket::getId)
                .collect(Collectors.toSet());

        System.out.println("You have booked next tickets:");
        for (Ticket bookedTicket : allBookedTickets) {
            if (ticketsToBuyIDs.contains(bookedTicket.getId())) {
                System.out.println("\t" + bookedTicket.toString());
            }
        }
        System.out.println();
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
