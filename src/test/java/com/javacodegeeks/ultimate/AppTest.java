package com.javacodegeeks.ultimate;

import com.javacodegeeks.ultimate.Entities.Geek;
import com.javacodegeeks.ultimate.Entities.Person;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);
    private EntityManagerFactory factory = null;
    private EntityManager entityManager = null;

    @Rule
    public TestName name = new TestName();

    public AppTest() {
        try {
            factory = Persistence.createEntityManagerFactory("PersistenceUnit");
            entityManager = factory.createEntityManager();
        } catch (Exception e) {
            logger.error("Error in constructor {}", e);
        }
    }

    @Test
    public void savePerson() {
        logger.info("Start test: {}", name.getMethodName());
        try {
            JPATest jpaTest = new JPATest();
            Person person = new Person();
            person.setFirstName("Homer");
            person.setLastName("Simpson");

            jpaTest.persistPerson(entityManager, person);

            List<Person> persons = jpaTest.findPerson(entityManager, person.getFirstName());
            Assert.assertEquals(1, persons.size());
            Assert.assertEquals(person.getLastName(), persons.get(0).getLastName());
        } catch (Exception e) {
            logger.error("Error in test {}: {}", name.getMethodName(), e);
        }
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void persistGeek() {
        logger.info("Start test: {}", name.getMethodName());
        try {

            JPATest jpaTest = new JPATest();
            Geek geek = new Geek();
            geek.setFirstName("Gavin");
            geek.setLastName("Coffee");
            geek.setFavouriteProgrammingLanguage("Java");
            jpaTest.persistGeek(entityManager, geek);

            List<Person> persons = jpaTest.getGeek(entityManager);
            Assert.assertEquals(1,persons.size());
            Assert.assertEquals(geek.getFirstName(), persons.get(0).getFirstName());
            Assert.assertEquals(geek.getLastName(), persons.get(0).getLastName());

        } catch (Exception e) {
            logger.error("Error in test {}: {}", name.getMethodName(), e);
        }
        logger.info("End   test: {}", name.getMethodName());
    }


}
