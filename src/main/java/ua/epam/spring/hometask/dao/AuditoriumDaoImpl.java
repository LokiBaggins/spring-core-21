package ua.epam.spring.hometask.dao;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;


@Component
public class AuditoriumDaoImpl implements AuditoriumService {
    private static Map<Long, Auditorium> auditoria = new HashMap<>();
    private static Long auditoriaAutoincrement = 1L;

    @Nonnull
    @Override
    public Collection<Auditorium> getAll() {
        return new HashSet<>(auditoria.values());
    }

    @Override
    public Auditorium getByName(final String name) {
        for (final Auditorium auditorium : auditoria.values()) {
            if(name.equals(auditorium.getName())) {
                return auditorium;
            }
        }

        throw new RuntimeException(String.format("Auditorium named '%s' does not exist", name));
    }

    @Override
    public Auditorium save(@Nonnull final Auditorium auditorium) {
        if (auditorium.getId() == null) {
            auditorium.setId(auditoriaAutoincrement++);
        }

        auditoria.put(auditorium.getId(), auditorium);
        return auditoria.get(auditorium.getId());
    }

    @Override
    public void remove(@Nonnull final Auditorium auditorium) {
        if(!auditoria.containsValue(auditorium)) {
            throw new RuntimeException(String.format("Auditorium with id '%s' does not exist and therefore could not be removed", auditorium.getId()));
        }

        auditoria.remove(auditorium.getId());
    }

    @Override
    public Auditorium getById(@Nonnull final Long id) {
        if (!auditoria.containsKey(id)) {
            throw new RuntimeException(String.format("Auditorium with id '%s' does not exist", id));
        }

        return auditoria.get(id);
    }

    public void setAll(final Set<Auditorium> auditoriumSet) {
        auditoriumSet.forEach(this::save);
    }
}
