package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.EventService;

//@Service("eventService")
@Component
public class EventServiceImpl implements EventService{

    @Autowired
    private EventDao eventDao;

    @Override
    public Event getByName(@Nonnull final String name) {
        return eventDao.getByName(name);
    }

    @Override
    public Set<Event> getForDateRange(@Nonnull final LocalDateTime from, @Nonnull final LocalDateTime to) {
        return eventDao.getForDateRange(from, to);
    }

    @Override
    public Set<Event> getUpcomingEvents(@Nonnull final LocalDateTime to) {
        return eventDao.getUpcomingEvents(to);
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

    public EventDao getEventDao() {
        return eventDao;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
