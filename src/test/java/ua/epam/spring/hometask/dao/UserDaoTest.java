package ua.epam.spring.hometask.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import ua.epam.spring.hometask.domain.User;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Aliaksei Miashkou on 04.06.17.
 */
public class UserDaoTest {

    private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test-context.xml");
    private UserDao userDao;
    List<User> fewUsersList;

    @Before
    public void reset() {
        userDao = context.getBean("userDao", UserDao.class);
        fewUsersList = (List<User>) context.getBean("userList1");
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

        userDao.setRegisteredUsers(fewUsersList);
        int lenghtBefore = fewUsersList.size();
        User removedUser = fewUsersList.get(0);

        userDao.remove(removedUser);
//      we have no need for userDao.getRegistredUsers method, so comparison is based on idea that userDao keeps just link to fewUsersList object
        assertEquals(lenghtBefore - 1, fewUsersList.size());
        for (User user : fewUsersList) {
            assertNotEquals(user, removedUser);
        }
    }

    @Test
    public void getAll() throws Exception {
        assertTrue(userDao.getAll().isEmpty());

        userDao.setRegisteredUsers(fewUsersList);
        assertEquals(fewUsersList.size(), userDao.getAll().size());
        assertThat(fewUsersList, is(userDao.getAll()));
    }

    @Test
    public void getById() throws Exception {
        userDao.setRegisteredUsers(fewUsersList);
        User userToFind = fewUsersList.get(fewUsersList.size() - 1);

        assertEquals(userToFind, userDao.getById(userToFind.getId()));
    }

    @Test
    public void getUserByEmail() throws Exception {
        userDao.setRegisteredUsers(fewUsersList);
        User userToFind = fewUsersList.get(fewUsersList.size() - 1);

        assertEquals(userToFind, userDao.getUserByEmail(userToFind.getEmail()));
    }

}