package com.atguigu.ems.test;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EduCenterTest {
	
	private ApplicationContext ctx = null;
	
	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml", "applicationContext-beans.xml");
	}
	
	@Test
	public void test() {
		SessionFactory sessionFactory = ctx.getBean(SessionFactory.class);
		Session session = sessionFactory.openSession();
		
		String hql = "SELECT count(e.employeeId) "
				+ "FROM Employee e "
				+ "INNER JOIN e.roles r "
				+ "WHERE r.roleId = ?";
		Query query = session.createQuery(hql).setInteger(0, 7);
		System.out.println(query.uniqueResult());
		
		session.close();
	}

}
