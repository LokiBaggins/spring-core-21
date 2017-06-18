package ua.epam.spring.hometask.service.impl;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.AuditoriumDaoImpl;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;

/**
 * Created by Aliaksei Miashkou on 18.06.17.
 */
public class AuditoriumServiceImpl implements AuditoriumService {
    private AuditoriumDaoImpl auditoriumDao;

    public AuditoriumServiceImpl(final AuditoriumDaoImpl auditoriumDao) {
        this.auditoriumDao = auditoriumDao;
    }

    @Override
    public Auditorium getByName(final String name) {
        return auditoriumDao.getByName(name);
    }

    @Override
    public Auditorium save(@Nonnull final Auditorium auditorium) {
        return auditoriumDao.save(auditorium);
    }

    @Override
    public void remove(@Nonnull final Auditorium auditorium) {
        auditoriumDao.remove(auditorium);
    }

    @Override
    public Auditorium getById(@Nonnull final Long id) {
        return auditoriumDao.getById(id);
    }

    @Nonnull
    @Override
    public Collection<Auditorium> getAll() {
        return auditoriumDao.getAll();
    }

    public void setAll(final Set<Auditorium> auditoriums) {
        auditoriumDao.setAll(auditoriums);
    }
}
