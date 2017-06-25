package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.UserService;

@Service("bookingService")
public class BookingServiceImpl implements BookingService {
    //        TODO: move consts to .properties
    private static final Double VIP_SEAT_PRICE_MULTIPLIER = 1.5;
    private static final Double RATING_PRICE_MULTIPLIER_BASE = 1.0;
    private static final Double RATING_PRICE_MULTIPLIER_LOW = 0.9;
    private static final Double RATING_PRICE_MULTIPLIER_MID = 1.0;
    private static final Double RATING_PRICE_MULTIPLIER_HIGH = 1.2;

    @Autowired private TicketDao ticketDao;
    @Autowired private UserService userService;

    @Override
    public Double getTicketsTotalPrice(final Event event, final LocalDateTime dateTime, final Set<Long> seats) {
        Double totalPrice = 0.00;

        for (Long seat : seats) {
            totalPrice += getTicketPrice(event, dateTime, seat);
        }

        return totalPrice;
    }

    @Nonnull
    @Override
    public Double getTicketPrice(Event event, LocalDateTime dateTime, Long seat) {
        return event.getBasePrice() * getEventRatingChargeMultiplier(event) * getVipSeatPriceMultiplier(event, dateTime, seat);
    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            bookTicket(ticket);
        }
    }

    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(final Event event, final LocalDateTime date) {
        return ticketDao.getByEvent(event, date);
    }

    @Override
    public Set<Long> getReservedSeatsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        return getPurchasedTicketsForEvent(event, dateTime).stream().map(Ticket::getSeat).collect(Collectors.toSet());
    }

    private Ticket bookTicket(Ticket ticket) {
        if (ticketDao.isTicketBooked(ticket)) {
            throw new RuntimeException("Unable to book ticket. Ticket already booked.");
        }

        Ticket result = ticketDao.save(ticket);

        if (result != null && ticket.getUser() != null && userService.isRegistered(ticket.getUser())) {
            ticket.getUser().getTickets().add(result);
        }

        return ticket;
    }

    private Double getEventRatingChargeMultiplier(Event event) {
        switch (event.getRating()) {
            case LOW:
                return RATING_PRICE_MULTIPLIER_LOW;
            case MID:
                return RATING_PRICE_MULTIPLIER_MID;
            case HIGH:
                return RATING_PRICE_MULTIPLIER_HIGH;
        }

        return RATING_PRICE_MULTIPLIER_BASE;
    }

    private Double getVipSeatPriceMultiplier(Event event, LocalDateTime dateTime, Long seat) {
        if (!event.getAuditoriums().isEmpty() && event.getAuditoriums().get(dateTime).getVipSeats().contains(seat)) {
            return VIP_SEAT_PRICE_MULTIPLIER;
        }

        return 1.0;
    }


    public TicketDao getTicketDao() {
        return ticketDao;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
