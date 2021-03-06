package ua.epam.spring.hometask.dao.impl;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

public class UserDaoImpl implements UserDao {
    private static Map<Long, User> registeredUsers = new HashMap();
    private static Long userIdAutoincrement = 1L;

    @Override
    public User save(@Nonnull User object) {
        if (object.getId() == null) {
            object.setId(userIdAutoincrement++);
        }

        //        TODO: add vaildation on e-mail uniqueness
        registeredUsers.put(object.getId(), object);
        return registeredUsers.get(object.getId());
    }

    @Override
    public void remove(@Nonnull User object) {
        if (object.getId() == null) {
            throw new RuntimeException("Can' remove user: empty ID");
        }
        if ( !registeredUsers.containsKey(object.getId())) {
            throw new RuntimeException("Can' find user with ID '" + object.getId() + "'");
        }

        registeredUsers.remove(object.getId());
    }

    @Override
    public User getById(@Nonnull Long id) {
        if ( !registeredUsers.containsKey(id)) {
            throw new RuntimeException("Can' find user with ID '" + id + "'");
        }

        return registeredUsers.get(id);
    }

    @Nonnull
    @Override
    public Collection<User> getAll() {
        return registeredUsers.values();
    }

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
//        TODO: should we prevent empty emails calls?
        for (User user : getAll()) {
            if (email.equals(user.getEmail())) {
                return user;
            }
        }

        return null;
    }

    @Override
    public boolean isRegistered(@Nonnull User user) {
        return registeredUsers.containsKey(user.getId());
    }

    //    TODO: throw out this and use different pre-difined beans fot tests
    public static void setRegisteredUsers(Map<Long, User> registeredUsers) {
        UserDaoImpl.registeredUsers = registeredUsers;
    }
}
