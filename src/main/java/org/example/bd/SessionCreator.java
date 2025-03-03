package org.example.bd;

import lombok.SneakyThrows;
import org.example.entity.City;
import org.example.entity.Country;
import org.example.entity.CountryLanguage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

import static java.util.Objects.nonNull;

public class SessionCreator {

    private final SessionFactory sessionFactory;

    public SessionCreator() {
        this.sessionFactory = buildSessionFactory();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    @SneakyThrows
    private SessionFactory buildSessionFactory() {

            Properties properties = new Properties();
            properties.load(Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("application.properties"));

            return new Configuration()
                    .setProperties(properties)
                    .addAnnotatedClass(City.class)
                    .addAnnotatedClass(Country.class)
                    .addAnnotatedClass(CountryLanguage.class)
                    .buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
    }
}
