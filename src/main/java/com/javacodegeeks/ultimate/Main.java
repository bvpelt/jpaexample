package com.javacodegeeks.ultimate;


import com.javacodegeeks.ultimate.Entities.Geek;
import com.javacodegeeks.ultimate.Entities.Period;
import com.javacodegeeks.ultimate.Entities.Person;
import com.javacodegeeks.ultimate.Entities.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        EntityManagerFactory factory = null;
        EntityManager entityManager = null;
        try {
            factory = Persistence.createEntityManagerFactory("PersistenceUnit");
            entityManager = factory.createEntityManager();
            persistPerson(entityManager);
            persistGeek(entityManager);
            getGeek(entityManager);
            persistProject(entityManager);

            String firstName = "Homer";
            List<Person> results = findPerson(entityManager, firstName);
            showPersonResults(results);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (factory != null) {
                factory.close();
            }
        }
    }

    private void showPersonResults(List<Person> results) {
        for (Person person : results) {
            StringBuilder sb = new StringBuilder();
            sb.append(person.getFirstName()).append(" ").append(person.getLastName());
            if (person instanceof Geek) {
                Geek geek = (Geek) person;
                sb.append(" ").append(geek.getFavouriteProgrammingLanguage());
            }
            logger.info(sb.toString());
        }
    }

    private void persistPerson(EntityManager entityManager) {
        logger.info("Start: Persist Person");
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Person person = new Person();
            person.setFirstName("Homer");
            person.setLastName("Simpson");
            entityManager.persist(person);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Exception occured {}", e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        logger.info("End  : Persist Person");
    }

    private void persistGeek(EntityManager entityManager) {
        logger.info("Start: Persist Geek");
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Geek geek = new Geek();
            geek.setFirstName("Gavin");
            geek.setLastName("Coffee");
            geek.setFavouriteProgrammingLanguage("Java");
            entityManager.persist(geek);
            geek = new Geek();
            geek.setFirstName("Thomas");
            geek.setLastName("Micro");
            geek.setFavouriteProgrammingLanguage("C#");
            entityManager.persist(geek);
            geek = new Geek();
            geek.setFirstName("Christian");
            geek.setLastName("Cup");
            geek.setFavouriteProgrammingLanguage("Java");
            entityManager.persist(geek);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Exception occured {}", e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        logger.info("End  : Persist Geek");
    }

    private void getGeek(EntityManager entityManager) {
        TypedQuery<Person> query = entityManager.createQuery("from Person", Person.class);
        List<Person> resultList = query.getResultList();
        showPersonResults(resultList);
    }

    private void persistProject(EntityManager entityManager) {
        String projectTitle;
        String programLanguage;

        programLanguage = "Java";

        projectTitle = "LV BAG";
        addProject(entityManager, projectTitle, programLanguage);
        projectTitle = "LV WOZ";
        addProject(entityManager, projectTitle, programLanguage);
        projectTitle = "PDOK";
        addProject(entityManager, projectTitle, programLanguage);
    }

    private void addProject(EntityManager entityManager, String projectTitle, String programLanguage) {
        List<Geek> resultList = entityManager.createQuery("from Geek g where g.favouriteProgrammingLanguage = :fpl", Geek.class).setParameter("fpl", programLanguage).
                getResultList();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Project project = new Project();
        project.setTitle(projectTitle);
        Period period = new Period();
        Date startDate = Date.from(Instant.parse("2007-12-03T10:15:30.00Z"));
        Date endDate = Date.from(Instant.parse("2008-12-03T10:15:30.00Z"));
        period.setStartDate(startDate);
        period.setEndDate(endDate);

        List<Period> billings= new ArrayList<Period>();
        billings.add(period);

        Period periodOld = new Period();
        startDate = Date.from(Instant.parse("2004-12-03T10:15:30.00Z"));
        endDate = Date.from(Instant.parse("2006-12-03T10:15:30.00Z"));
        periodOld.setStartDate(startDate);
        periodOld.setEndDate(endDate);
        billings.add(periodOld);


        project.setProjectPeriod(period);
        project.setBillingPeriods(billings);
        int i = 0;
        for (Geek geek : resultList) {
            if (i % 2 == 0) {
                project.setProjectType(Project.ProjectType.FIXED);
            } else {
                project.setProjectType(Project.ProjectType.TIME_AND_MATERIAL);
            }
            project.getGeeks().add(geek);
            geek.getProjects().add(project);
            i++;
        }
        entityManager.persist(project);
        transaction.commit();
    }

    public List<Person> findPerson(EntityManager entityManager, String firstName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        Root<Person> personRoot = query.from(Person.class);
        query.where(builder.equal(personRoot.get("firstName"), "Homer"));
        List<Person> resultList = entityManager.createQuery(query).getResultList();

        return resultList;
    }
}