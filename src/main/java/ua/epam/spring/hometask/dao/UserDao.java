package ua.epam.spring.hometask.dao;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

/**
 * Created by Aliaksei Miashkou on 04.06.17.
 */
public class UserDao implements UserService {
    private static Collection<User> registeredUsers;

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        return null;
    }

    @Override
    public User save(@Nonnull User object) {
        return null;
    }

    @Override
    public void remove(@Nonnull User object) {

    }

    @Override
    public User getById(@Nonnull Long id) {
        return null;
    }

    @Nonnull
    @Override
    public Collection<User> getAll() {
        return null;
    }

    public static Collection<User> getRegisteredUsers() {
        return registeredUsers;
    }

    public static void setRegisteredUsers(Collection<User> registeredUsers) {
        UserDao.registeredUsers = registeredUsers;
    }
}
