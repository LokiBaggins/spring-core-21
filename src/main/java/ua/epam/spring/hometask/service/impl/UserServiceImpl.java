package ua.epam.spring.hometask.service.impl;

import java.util.Collection;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.UserDaoImpl;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

/**
 * Created by Aliaksei Miashkou on 04.06.17.
 */
public class UserServiceImpl implements UserService {
    private UserDaoImpl userDao;

    @Override
    public User save(@Nonnull final User user) {
        return userDao.save(user);
    }

    @Override
    public void remove(@Nonnull final User user) {
        userDao.remove(user);
    }

    @Override
    public User getById(@Nonnull final Long id) {
        return userDao.getById(id);
    }

    @Override
    public User getUserByEmail(@Nonnull final String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    @Nonnull
    public Collection<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public boolean isRegistered(@Nonnull User user) {
        return userDao.isRegistered(user);
    }

    public UserDaoImpl getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }
}
