package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.dao.impl.AuditoriumDaoImpl;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;

@Service("auditoriumService")
public class AuditoriumServiceImpl implements AuditoriumService {

    @Autowired
    private AuditoriumDao auditoriumDao;

    public AuditoriumServiceImpl() {
    }

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
