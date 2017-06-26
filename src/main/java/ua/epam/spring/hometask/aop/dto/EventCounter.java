package ua.epam.spring.hometask.aop.dto;

public class EventCounter {
    private static final String CONSOLE_TEXT_PREFIX_CYAN = "\u001B[36m";
    private static final String ANSI_RESET = "\u001B[0m";

    private Long id;
    private Long eventId;
    private String eventName;
    private Long timesCalledByName = 0L;
    private Long timesPricesQueried = 0L;
    private Long ticketsBooked = 0L;

    public EventCounter(Long eventId, String eventName) {
        this.eventId= eventId;
        this.eventName = eventName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getTimesCalledByName() {
        return timesCalledByName;
    }

    public void setTimesCalledByName(Long timesCalledByName) {
        this.timesCalledByName = timesCalledByName;
    }

    public Long getTimesPricesQueried() {
        return timesPricesQueried;
    }

    public void setTimesPricesQueried(Long timesPricesQueried) {
        this.timesPricesQueried = timesPricesQueried;
    }

    public Long getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(Long ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
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
