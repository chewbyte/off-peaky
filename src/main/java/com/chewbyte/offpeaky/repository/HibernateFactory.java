package com.chewbyte.offpeaky.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateFactory {

	private static SessionFactory factory;
	
	public static Session get() {
		if(factory == null) {
			factory = new Configuration().configure().buildSessionFactory();
		}
		return factory.openSession();
	}
}
