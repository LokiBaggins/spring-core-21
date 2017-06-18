package ua.epam.spring.hometask.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.EventService;

/**
 * Created by Aliaksei Miashkou on 12.06.17.
 */
public class EventDaoImpl implements EventService {
    private static Map<Long, Event> events;
    private static Long eventIdAutoincrement = 1L;

    public EventDaoImpl() {
        events = new HashMap<>();
    }

    @Override
    public Event getByName(@Nonnull final String name) {
        for (final Event event : events.values()) {
            if(name.equals(event.getName())) {
                return event;
            }
        }

        throw new RuntimeException(String.format("Event named '%s' does not exist", name));
    }

    @Nonnull
    @Override
    public Set<Event> getForDateRange(@Nonnull final LocalDateTime from, @Nonnull final LocalDateTime to) {
        Set<Event> result = new HashSet<>();
        for (final Event event : events.values()) {
            if(event.getAirDates().lower(to) != null && event.getAirDates().higher(from) != null) {
                result.add(event);
                break;
            }
        }
        return result;
    }

    @Override
    public Set<Event> getUpcomingEvents(final LocalDateTime to) {
        return getForDateRange(LocalDateTime.now(), to);
    }

    @Override
    public Event save(@Nonnull final Event event) {
        if (event.getId() == null) {
            event.setId(eventIdAutoincrement++);
        }

        events.put(event.getId(), event);
        return null;
    }

    @Override
    public void remove(@Nonnull final Event event) {
        if(!events.containsValue(event)) {
            throw new RuntimeException(String.format("Event with id '%s' does not exist and therefore could not be removed", event.getId()));
        }

        events.remove(event.getId());
    }

    @Override
    public Event getById(@Nonnull final Long id) {
        if (!events.containsKey(id)) {
            throw new RuntimeException(String.format("Event with id '%s' does not exist", id));
        }

        return events.get(id);
    }

    @Nonnull
    @Override
    public Set<Event> getAll() {
        return new HashSet<>(events.values());
    }
}
