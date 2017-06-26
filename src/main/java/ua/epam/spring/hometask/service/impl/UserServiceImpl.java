package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.dao.impl.UserDaoImpl;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

//@Component
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
