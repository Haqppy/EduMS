package com.atguigu.ems.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.atguigu.ems.dao.EmployeeDao;
import com.atguigu.ems.entities.Department;
import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.entities.Role;
import com.atguigu.ems.hibernate.HibernateDao;
import com.atguigu.ems.orm.Page;
import com.atguigu.ems.orm2.PropertyFilter;
import com.atguigu.ems.orm2.PropertyFilter.MatchType;
import com.atguigu.ems.utils.ReflectionUtils;

public class CriteriaTest {

	private ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	private SessionFactory sessionFactory = ctx.getBean(SessionFactory.class);
	
//	private com.atguigu.ems.dao2.EmployeeDao employeeDao = 
//			ctx.getBean(com.atguigu.ems.dao2.EmployeeDao.class);
	
	@Test
	public void testHibernateDaoFindPage(){
		HibernateDao<Employee, Integer> hibernateDao = 
				new HibernateDao<>(sessionFactory, Employee.class);
		
		com.atguigu.ems.orm2.Page<Employee> page = new com.atguigu.ems.orm2.Page();
		page.setPageNo(2);
		page.setPageSize(5);
		
		page.setOrder("ASC,DESC");
		page.setOrderBy("loginName,email");
		
		List<PropertyFilter> filters = new ArrayList<>();
		filters.add(new PropertyFilter("GTI_employeeId", 5));
		filters.add(new PropertyFilter("LIKES_loginName_OR_email", 'a'));
		filters.add(new PropertyFilter("ISNULLO_department", null));
		
		page = hibernateDao.findPage(page, filters);
		System.out.println(page.getTotalCount()); 
	}
	
	@Test
	public void testHibernateDaoCountCriteriaResult(){
		HibernateDao<Employee, Integer> hibernateDao = 
				new HibernateDao<>(sessionFactory, Employee.class);
		
		Criterion criterion = Restrictions.like("loginName", "a", MatchMode.ANYWHERE);
		Criteria criteria = hibernateDao.createCriteria(criterion);
		criteria.addOrder(Order.asc("email"));
		
		System.out.println(criteria.list().size());

		CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;

//		System.out.println(hibernateDao.countCriteriaResult(criteria)); 
		//设置 orderEntities 为空的 List: new ArrayList();
		//1.1 获取 orderEntities 属性值
		List orderEntries = (List) ReflectionUtils.getFieldValue(criteria, "orderEntries");
		System.out.println(orderEntries);
		
		//2. 获取之前的 projection
		Projection projection = criteriaImpl.getProjection();
		
		//3. 获取之前的 ResultTransformer
		ResultTransformer resultTransformer = criteriaImpl.getResultTransformer();
		
		//1.2. 设置 orderEntities 为空的 List
		ReflectionUtils.setFieldValue(criteria, "orderEntries", new ArrayList<>()); 
		
		criteria.setProjection(Projections.count(hibernateDao.getIdName()));
		Object count = criteria.uniqueResult();
		System.out.println(count); 
		
		
		//1.3. 再把 orderEntries 恢复回来
		ReflectionUtils.setFieldValue(criteria, "orderEntries", orderEntries);
		//2.1 把 Projection 再赋值回去
		criteriaImpl.setProjection(projection);
		//3.2 把 ResultTransformer 再赋值回去
		criteriaImpl.setResultTransformer(resultTransformer);
		
		System.out.println(criteria.list().size());
	}
	
	@Test
	public void testHibernateDaoBuildPropertyFilterCriterions(){
		HibernateDao<Employee, Integer> hibernateDao = 
				new HibernateDao<>(sessionFactory, Employee.class);

		List<PropertyFilter> filters = new ArrayList<>();
		filters.add(new PropertyFilter("GTI_id", 5));
		filters.add(new PropertyFilter("LIKES_loginName_OR_email", 'a'));
		filters.add(new PropertyFilter("ISNULLO_department", null));
		
//		Criterion[] criterions = hibernateDao.buildPropertyFilterCriterions(filters);
//		for(Criterion c: criterions){
//			System.out.println(c);
//		}
		
		Employee employee = hibernateDao.findUnique(filters);
		System.out.println(employee); 
	}
	
	@Test
	public void testHibernateDaoBuildPropertyFilterCriterion(){
		HibernateDao<Employee, Integer> hibernateDao = 
				new HibernateDao<>(sessionFactory, Employee.class);
		Criterion criterion = 
				hibernateDao.buildPropertyFilterCriterion("id", 5, Integer.class, MatchType.GE);
		System.out.println(criterion);
	}
	
	@Test
	public void testInitEntity(){
		Session session = sessionFactory.openSession();
		
		Department dept = (Department) session.get(Department.class, 1);
		System.out.println(dept.getDepartmentName());
		//对关联的对象进行初始化
		Hibernate.initialize(dept.getManager());
		
		session.close();
		
		System.out.println(dept.getManager().getEmail());
	}
	
	@Test
	public void testGetIdName(){
		Class clazz = Role.class;
		
		ClassMetadata cmd = sessionFactory.getClassMetadata(clazz);
		System.out.println(cmd.getIdentifierPropertyName()); 
	}
	
	@Test
	public void testFindUniqueBy(){
		
		Session session = sessionFactory.openSession();
		
		//属性名
		String propertyName = "manager";
		//属性值
		Employee mgr = new Employee();
		mgr.setEmployeeId(13);
		
		//创建 eq 的查询条件
		Criterion criterion = Restrictions.eq(propertyName, mgr);
		
		//创建 Criteria, 并添加查询条件
		Criteria criteria = session.createCriteria(Department.class);
		criteria.add(criterion);
		
		//执行查询
		Department dept = (Department) criteria.uniqueResult();
		
		System.out.println(dept.getDepartmentName());
	}
	
	@Test
	public void test() {
		
		Session session = null;
		
		Criteria criteria = session.createCriteria(Employee.class);
		
		criteria.list();
		
		criteria.uniqueResult();
		
		Criterion criterion = Restrictions.like("loginName", "a", MatchMode.ANYWHERE);
		criteria.add(criterion);
		
		criteria.setProjection(Projections.max("salary"));
		
		
		criteria.addOrder(Order.asc("salary"))
		        .addOrder(Order.desc("age"));
		
		int firstResult = 0;
		int maxResults = 0;
		
		criteria.setFirstResult(firstResult)
		        .setMaxResults(maxResults)
		        .list();
		
		
		//1. 关于 QBC
		//1.1
		
		//1.2
		
		//1.3
		
		//2. SimpleHibernateDao
		
		
		//3. HibernateDao 
		
	}

}
