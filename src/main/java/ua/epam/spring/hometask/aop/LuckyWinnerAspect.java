package ua.epam.spring.hometask.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Ticket;

@Aspect
@Component
public class LuckyWinnerAspect {
    private static final String CONSOLE_TEXT_PREFIX_MAGENTA = "\u001B[35m";
    private static final String CONSOLE_TEXT_PREFIX_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

//    there is no public method for single ticket booking in basic implementation, so we'll iterate over each ticket in set
    @Around("execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..))")
    public void checkLucky(ProceedingJoinPoint joinPoint) {
        Set<Ticket> tickets = (Set<Ticket>) joinPoint.getArgs()[0];

        boolean luckHasSmiled = false;
        for (Ticket ticket : tickets) {
            if (isTicketLucky(ticket)) {
                luckHasSmiled = true;
            }
        }

        if (!luckHasSmiled) {
            System.out.println(CONSOLE_TEXT_PREFIX_MAGENTA + "It's a pity - no free tickets this time. Book more and good luck!" + ANSI_RESET);
        }

        try {
            joinPoint.proceed(new Set[]{tickets});

        } catch (Throwable throwable) {
            throw new RuntimeException("error while aspect processing: " + throwable.getStackTrace());
        }
    }

    private boolean isTicketLucky(Ticket ticket) {
        long magicNumber = new Date().getTime() + ticket.getEvent().getName().hashCode() + ticket.getSeat();

        if (magicNumber % 7 == 0) {
//            luckHasSmiled = true;
            ticket.setPrice(0D);
            System.out.println(CONSOLE_TEXT_PREFIX_GREEN + String.format("Magic is near, lucky buddy! Your ticket for the seat no.%s is free for you =)" +
                    "", ticket.getSeat()) + ANSI_RESET);
        }

        return ticket.getPrice().equals(0D);
    }

}
