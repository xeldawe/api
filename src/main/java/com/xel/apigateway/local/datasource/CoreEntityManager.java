package com.xel.apigateway.local.datasource;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * 
 * @author XelDawe
 * For multi DB
 */
public class CoreEntityManager {
	
	private static EntityManager oracle;
	private static EntityManager postgres;

	public static EntityManager getOracleEntityManager() {
		if(oracle == null) {
		 LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		    em.setDataSource( CoreDataSource.getOracleDataSource());
		    em.setPackagesToScan( "com.xel.apigateway.local.oracle.bean" );
		    em.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
		    
		    Properties hibernateProp =  new Properties();
		    hibernateProp.put( "hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect" );
		    hibernateProp.put( "hibernate.connection.driver_class", "oracle.jdbc.driver.OracleDriver" );
		    hibernateProp.put( "hibernate.hbm2ddl.auto", "validate" );
		    
		    em.setJpaProperties( hibernateProp );
		    em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		    em.afterPropertiesSet();
		    
		    EntityManagerFactory  t = em.getObject();
		    oracle =  t.createEntityManager();
		}
		return oracle;
	}
	
	
	public static EntityManager getPostgresEntityManager() {
		if(postgres == null) {
		 LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		    em.setDataSource(CoreDataSource.getPostgresDataSource());
		    em.setPackagesToScan("com.xel.apigateway.local.jpa.bean");
		    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		    
		    Properties hibernateProp =  new Properties();
		    hibernateProp.put( "hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect" );
		    hibernateProp.put( "hibernate.connection.driver_class", "org.postgresql.Driver" );
		    hibernateProp.put( "hibernate.hbm2ddl.auto", "update" );

		    
		    em.setJpaProperties( hibernateProp );
		    em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		    em.afterPropertiesSet();
		    
		    EntityManagerFactory  t = em.getObject();
		    postgres =  t.createEntityManager();
		}
		return postgres;
	}
}
