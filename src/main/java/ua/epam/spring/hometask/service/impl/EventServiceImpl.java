package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.EventDaoImpl;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.EventService;

/**
 * Created by Aliaksei Miashkou on 12.06.17.
 */
public class EventServiceImpl implements EventService{

    private EventDaoImpl eventDao;

    @Override
    public Event getByName(@Nonnull final String name) {
        return eventDao.getByName(name);
    }

    @Override
    public Set<Event> getForDateRange(@Nonnull final LocalDateTime from, @Nonnull final LocalDateTime to) {
        return eventDao.getForDateRange(from, to);
    }

    @Override
    public Set<Event> getNextEvents(@Nonnull final LocalDateTime to) {
        return eventDao.getNextEvents(to);
    }

    @Override
    public Event save(@Nonnull final Event event) {
        return eventDao.save(event);
    }

    @Override
    public void remove(@Nonnull final Event event) {
        eventDao.remove(event);
    }

    @Override
    public Event getById(@Nonnull final Long id) {
        return eventDao.getById(id);
    }

    @Nonnull
    @Override
    public Collection<Event> getAll() {
        return eventDao.getAll();
    }

}
