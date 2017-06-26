package ua.epam.spring.hometask.dao.impl;

import ua.epam.spring.hometask.aop.dto.EventCounter;
import ua.epam.spring.hometask.dao.EventCounterDao;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;

public class EventCounterDaoImpl implements EventCounterDao {
    @Override
    public EventCounter save(@Nonnull EventCounter object) {
        Connection connection = db

        String queryString = "";
        PreparedStatement saveStatement =
        return null;
    }

    @Override
    public EventCounter getById(@Nonnull Long id) {
        return null;
    }

    @Nonnull
    @Override
    public Collection<EventCounter> getAll() {
        return null;
    }
}
