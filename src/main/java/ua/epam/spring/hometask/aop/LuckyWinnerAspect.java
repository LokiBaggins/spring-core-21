package ua.epam.spring.hometask.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.Date;
import java.util.Set;

import ua.epam.spring.hometask.domain.Ticket;

@Aspect
public class LuckyWinnerAspect {
    private static final String CONSOLE_TEXT_PREFIX_CYAN = "\\u001B[46m";

//    there is no public method for single ticket booking in basic implementation, so we'll iterate over each ticket in set
    @Before("execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..))")
    public void checkLucky(JoinPoint joinPoint) {
        Set<Ticket> tickets = (Set<Ticket>) joinPoint.getArgs()[0];

        for (Ticket ticket : tickets) {
            long magicNumber = new Date().getTime() + ticket.getEvent().getName().hashCode() + ticket.getSeat();

            if (magicNumber % 2 == 0) {
                ticket.setPrice(0D);
                System.out.println(CONSOLE_TEXT_PREFIX_CYAN + String.format("Magic is near, lucky buddy! Your ticket for the seat no.%s is free for you =)", ticket.getSeat()));
            }
        }
    }

}
