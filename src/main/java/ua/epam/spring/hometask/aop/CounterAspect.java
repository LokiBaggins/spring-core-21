package ua.epam.spring.hometask.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.aop.dto.EventCounter;
import ua.epam.spring.hometask.dao.EventCounterDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;


@Aspect
@Component
public class CounterAspect {
    private static Map<Long, EventCounter> eventStatistics = new HashMap<>();

    @Autowired
    EventCounterDao eventCounterDao;

    @AfterReturning(pointcut = "execution(* ua.epam.spring.hometask.service.EventService.getByName(..))",
            returning= "result")
    public void countEventByNameCall(Object result) {
        Event event = (Event) result;

        EventCounter counter = getCounterForEvent(event);
        counter.setTimesCalledByName(counter.getTimesCalledByName() + 1);
    }

//    it's not a mistake. Mapping pointcut to .getTicketsPrice would give incorrect result because of lots of inner service calls
//    if we need UX result to know users interest to event - here it is
    @AfterReturning("execution(* ua.epam.spring.hometask.service.BookingService.getPurchasedTicketsForEvent(..))")
    public void countEventPriceQuery(JoinPoint joinPoint) {
        Event event = (Event) joinPoint.getArgs()[0];

        EventCounter counter = getCounterForEvent(event);
        counter.setTimesPricesQueried(counter.getTimesPricesQueried() +1);
    }

    @After("execution(* ua.epam.spring.hometask.dao.TicketDao.save(..))")
    public void countEventByNameCall(JoinPoint joinPoint) {
        Ticket ticket = (Ticket) joinPoint.getArgs()[0];
        Event event = ticket.getEvent();

        EventCounter counter = getCounterForEvent(event);
        counter.setTicketsBooked(counter.getTicketsBooked() + 1);
    }

    /**
     * Prints statistics after program is finished
     */
    @After("execution(* ua.epam.spring.hometask.Application.run(..))")
    public void printStatistics() {
        for (EventCounter counter : eventStatistics.values()) {
            System.out.println(counter);
        }
    }

    private EventCounter getCounterForEvent(Event event) {
        if (eventStatistics.keySet().contains(event.getId())) {
            eventCounterDao.saveOrUpdate(eventStatistics.get(event.getId()));
            return eventStatistics.get(event.getId());
        }

        eventCounterDao.saveOrUpdate(new EventCounter(event.getId(), event.getName()));
        return eventStatistics.put(event.getId(), new EventCounter(event.getId(), event.getName()));
    }
}
