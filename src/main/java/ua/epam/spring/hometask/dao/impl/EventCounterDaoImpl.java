package ua.epam.spring.hometask.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.epam.spring.hometask.aop.dto.EventCounter;
import ua.epam.spring.hometask.dao.EventCounterDao;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class EventCounterDaoImpl implements EventCounterDao {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public EventCounter saveOrUpdate(@Nonnull EventCounter counter) {
        String insertQueryString = "INSERT INTO EVENT_COUNTERS (event_id_ref, event_name, times_called_by_name, times_prices_queried, times_prices_queried) " +
                "VALUES ?,?,?,?,?";
        String updateQueryString = "UPDATE EVENT_COUNTERS SET EVENT_ID_REF=?, EVENT_NAME=?, times_called_by_name=?, times_prices_queried=?, times_prices_queried=?";

        try (Connection connection = dataSource.getConnection()) {
            String queryString = null;

            if (counter.getId() != null && getById(counter.getId()) != null) {
                queryString = insertQueryString;
            } else {
                queryString = updateQueryString;
            }

            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setLong(1, counter.getEventId());
            statement.setString(2, counter.getEventName());
            statement.setLong(3, counter.getTimesCalledByName());
            statement.setLong(4, counter.getTimesPricesQueried());
            statement.setLong(5, counter.getTicketsBooked());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Can't save object to DB. Object: " + counter.toString() + e.getStackTrace());
        }

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
