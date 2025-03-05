package com.exoma.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Cria a configuração do Hibernate a partir do arquivo hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure(); // Isso carrega o hibernate.cfg.xml

			// Constrói o SessionFactory
			return configuration.buildSessionFactory();
		} catch (Throwable ex) {
			// Em caso de erro, lança uma exceção
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}