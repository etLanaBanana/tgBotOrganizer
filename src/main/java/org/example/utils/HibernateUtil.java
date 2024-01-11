package org.example.utils;

import org.example.model.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml")
                        .build();

                Metadata metadata = new MetadataSources(standardServiceRegistry)
                        .addAnnotatedClass(User.class)
                        .getMetadataBuilder()
                        .build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                System.out.println("Исключение! " + e);
            }
        }
        return sessionFactory;
    }
}