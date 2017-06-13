package ua.epam.spring.hometask.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

import ua.epam.spring.hometask.domain.User;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Aliaksei Miashkou on 04.06.17.
 */
public class UserDaoTest {

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("test-context.xml");
    private UserDaoImpl userDao;
    Map<Long, User> fewUsersMap;

    @Before
    public void reset() {
//        userDao = context.getBean("userDao", UserDaoImpl.class);
        userDao = new UserDaoImpl();
        fewUsersMap = (Map<Long, User>) context.getBean("userMap1");
    }

    @Test
    public void save() throws Exception {
        User user1 = context.getBean("user1", User.class);

//        TODO: that's confusing - we have to test one method via others. How can we be sure that tose other methods works fine?
//        TODO: doesn't it break isolation concept of unit testing? (my skype: loki_baggins)
        userDao.save(user1);
        assertEquals(1, userDao.getAll().size());
        assertEquals(user1, userDao.getById(user1.getId()));
    }

    @Test
    public void remove() throws Exception {

        userDao.setRegisteredUsers(fewUsersMap);
        int lenghtBefore = fewUsersMap.size();
        User removedUser = fewUsersMap.get(1L); // TODO: too hard-wired solution for taking "any" element of Map

        userDao.remove(removedUser);
//      we have no need for userDao.getRegistredUsers method, so comparison is based on idea that userDao keeps just link to fewUsersMap object
        assertEquals(lenghtBefore - 1, fewUsersMap.size());
        for (User user : fewUsersMap.values()) {
            assertNotEquals(user, removedUser);
        }
    }

    @Test
    public void getAll() throws Exception {
        assertTrue(userDao.getAll() == null || userDao.getAll().isEmpty());

        userDao.setRegisteredUsers(fewUsersMap);
        assertEquals(fewUsersMap.size(), userDao.getAll().size());
        assertThat(fewUsersMap.values(), is(userDao.getAll()));
    }

    @Test
    public void getById() throws Exception {
        userDao.setRegisteredUsers(fewUsersMap);
        User userToFind = fewUsersMap.get(3L);

        assertEquals(userToFind, userDao.getById(userToFind.getId()));
    }

    @Test
    public void getUserByEmail() throws Exception {
        userDao.setRegisteredUsers(fewUsersMap);
        User userToFind = fewUsersMap.get(3L);

        assertEquals(userToFind, userDao.getUserByEmail(userToFind.getEmail()));
    }

}