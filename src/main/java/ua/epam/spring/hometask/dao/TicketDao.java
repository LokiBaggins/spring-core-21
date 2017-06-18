package ua.epam.spring.hometask.dao;

import java.time.LocalDateTime;
import java.util.Set;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.AbstractDomainObjectService;

public interface TicketDao extends AbstractDomainObjectService<Ticket> {

    Set<Ticket> getByEvent(Event event, LocalDateTime dateTime);

    boolean isTicketBooked(Ticket ticket);

}
