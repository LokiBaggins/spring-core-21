package ua.epam.spring.hometask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Aliaksei_Miashkou on 6/13/2017.
 */
public class ApplicationRunner {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-config.xml");
    }
}
