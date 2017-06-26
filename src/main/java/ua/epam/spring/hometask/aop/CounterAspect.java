package ua.epam.spring.hometask.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;


@Aspect
@Component
public class CounterAspect {
    private static final String CONSOLE_TEXT_PREFIX_CYAN = "\u001B[36m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static Map<Long, EventCounter> eventStatistics = new HashMap<>();

    @AfterReturning(pointcut = "execution(* ua.epam.spring.hometask.service.EventService.getByName(..))",
            returning= "result")
    public void countEventByNameCall(Object result) {
        Event event = (Event) result;

        checkForCounterPresense(event);
        eventStatistics.get(event.getId()).timesCalledByName++;
    }

//    it's not a mistake. Mapping pointcut to .getTicketsPrice would give incorrect result because of lots of inner service calls
//    if we need UX result to know users interest to event - here it is
    @AfterReturning("execution(* ua.epam.spring.hometask.service.BookingService.getPurchasedTicketsForEvent(..))")
    public void countEventPriceQuery(JoinPoint joinPoint) {
        Event event = (Event) joinPoint.getArgs()[0];

        checkForCounterPresense(event);
        eventStatistics.get(event.getId()).timesPricesQueried++;
    }

    @After("execution(* ua.epam.spring.hometask.dao.TicketDao.save(..))")
    public void countEventByNameCall(JoinPoint joinPoint) {
        Ticket ticket = (Ticket) joinPoint.getArgs()[0];
        Event event = ticket.getEvent();

        checkForCounterPresense(event);
        eventStatistics.get(event.getId()).ticketsBooked++;
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

    private void checkForCounterPresense(Event event) {
        if (!eventStatistics.keySet().contains(event.getId())) {
            addNewCounter(event);
        }
    }

    private void addNewCounter(Event event) {
        eventStatistics.put(event.getId(), new EventCounter(event.getName()));
    }

    class EventCounter {
        String eventName;
        Long timesCalledByName = 0L;
        Long timesPricesQueried = 0L;
        Long ticketsBooked = 0L;

        EventCounter(String eventName) {
            this.eventName = eventName;
        }

        @Override
        public String toString() {
            return CONSOLE_TEXT_PREFIX_CYAN + "EventCounter{" +
                    "eventName='" + eventName + '\'' +
                    ", timesCalledByName=" + timesCalledByName +
                    ", timesPricesQueried=" + timesPricesQueried +
                    ", ticketsBooked=" + ticketsBooked +
                    '}' +
                    ANSI_RESET;
        }
    }
}
