package ua.epam.spring.hometask.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;


@Aspect
public class CounterAspect {
    private static final String CONSOLE_TEXT_PREFIX_CYAN = "\\u001B[46m";
    private static Map<Long, EventCounter> eventStatistics = new HashMap<>();

    @AfterReturning(pointcut = "execution(* ua.epam.spring.hometask.service.EventService.getByName(..))",
            returning= "result")
    public void countEventByNameCall(Object result) {
        Event event = (Event) result;

        if (!eventStatistics.keySet().contains(event.getId())) {
            eventStatistics.put(event.getId(), new EventCounter());
        }

        eventStatistics.get(event.getId()).timesCalledByName++;
    }

    @AfterReturning("execution(* ua.epam.spring.hometask.service.BookingService.getTicketPrice(..))")
    public void countEventPriceQuery(JoinPoint joinPoint) {
        Event event = (Event) joinPoint.getArgs()[0];

        if (!eventStatistics.keySet().contains(event.getId())) {
            eventStatistics.put(event.getId(), new EventCounter());
        }

        eventStatistics.get(event.getId()).timesPricesQueried++;
    }

    @After("execution(* ua.epam.spring.hometask.dao.TicketDao.save(..))")
    public void countEventByNameCall(JoinPoint joinPoint) {
        Ticket ticket = (Ticket) joinPoint.getArgs()[0];
        Event event = ticket.getEvent();

        if (!eventStatistics.keySet().contains(event.getId())) {
            eventStatistics.put(event.getId(), new EventCounter());
        }

        eventStatistics.get(event.getId()).ticketsBooked++;
    }

    /**
     * Prints statistics after program is finished
     */
    @After("execution(* ua.epam.spring.hometask.ApplicationLauncher.run(..))")
    public void logMainRun() {
        for (EventCounter counter : eventStatistics.values()) {
            System.out.println(counter);
        }
    }

    class EventCounter {
        String eventName;
        Long timesCalledByName = 0L;
        Long timesPricesQueried = 0L;
        Long ticketsBooked = 0L;

        @Override
        public String toString() {
            return CONSOLE_TEXT_PREFIX_CYAN + "EventCounter{" +
                    "eventName='" + eventName + '\'' +
                    ", timesCalledByName=" + timesCalledByName +
                    ", timesPricesQueried=" + timesPricesQueried +
                    ", ticketsBooked=" + ticketsBooked +
                    '}';
        }
    }
}
