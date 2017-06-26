package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.aop.dto.EventCounter;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface EventCounterDao {

    EventCounter save(@Nonnull EventCounter object);

    EventCounter getById(@Nonnull Long id);

    @Nonnull
    Collection<EventCounter> getAll();

}
