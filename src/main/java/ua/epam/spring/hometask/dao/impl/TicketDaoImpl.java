package ua.epam.spring.hometask.dao.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;

public class TicketDaoImpl implements TicketDao {

    private static Map<Long, Ticket> tickets = new HashMap<>();
    private static Long ticketsAutoincrement = 1L;

    @Override
    public Set<Ticket> getByEvent(final Event event, final LocalDateTime dateTime) {
        Set<Ticket> set = new HashSet<>();
        for (Ticket ticket : tickets.values()) {
            if (ticket.getEvent().equals(event)) {
                set.add(ticket);
            }
        }
        return set;
    }

    @Override
    public boolean isTicketBooked(final Ticket ticket) {
        return tickets.containsValue(ticket);
    }

    @Override
    public Ticket save(@Nonnull final Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(ticketsAutoincrement++);
        }

        tickets.put(ticket.getId(), ticket);
        return tickets.get(ticket.getId());
    }

    @Override
    public void remove(@Nonnull final Ticket ticket) {
        if(!tickets.containsValue(ticket)) {
            throw new RuntimeException(String.format("Ticket with id '%s' does not exist and therefore could not be removed",
                    ticket.getId()));
        }

        tickets.remove(ticket.getId());
    }

    @Override
    public Ticket getById(@Nonnull final Long id) {
        if (tickets.containsKey(id)) {
            throw new RuntimeException(String.format("Ticket with id '%s' does not exist", id));
        }

        return tickets.get(id);
    }

    @Nonnull
    @Override
    public Collection<Ticket> getAll() {
        return new HashSet<>(tickets.values());
    }

}
