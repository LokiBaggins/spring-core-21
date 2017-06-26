package ua.epam.spring.hometask.dao;

import java.util.Set;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.AuditoriumService;

/**
 * Created by Aliaksei Miashkou on 25.06.17.
 */
public interface AuditoriumDao extends AuditoriumService {
    void setAll(Set<Auditorium> auditoriumSet);
}
