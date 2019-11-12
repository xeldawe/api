package com.xel.apigateway.local.datasource;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.xel.apigateway.ApiGatewayApplication;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan
public class CoreDataSource {

	private static HikariDataSource dsOracle = null;
	private static HikariDataSource dsPostgres = null;

	@Bean
	public static DataSource getOracleDataSource() {
		if (dsOracle == null) {
			HikariConfig config = new HikariConfig();
			try {
				config.setJdbcUrl("jdbc:oracle:thin:@YOUR_DOMAIN:YOUR_PORT:YOUR_DB");
				config.setUsername("YOUR_USERNAME");
				config.setPassword("YOUR_PASSWORD");
				config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
				config.addDataSourceProperty("oracle.jdbc.editionName", "YOUR_EDITION");
//				config.setAutoCommit(false);
				config.setMaximumPoolSize(10);
				config.setMinimumIdle(1);
				config.setMaxLifetime(1800000);
				config.setPoolName("XelPoolOracle");
				config.setIdleTimeout(600000);
				config.setConnectionTimeout(30000);
				dsOracle = new HikariDataSource(config);
			} catch (Exception e) {

			}

		}
		return dsOracle;
	}
	
	@Primary
	@Bean
	public static DataSource getPostgresDataSource() {
		if (dsPostgres == null) {
			HikariConfig config = new HikariConfig();
			try {
				config.setJdbcUrl(ApiGatewayApplication.bean.getDbHost());
				config.setUsername(ApiGatewayApplication.bean.getDbUsername());
				config.setPassword(ApiGatewayApplication.bean.getDbPassword());
				config.setDriverClassName("org.postgresql.Driver");
//				config.setAutoCommit(false);
				config.setMaximumPoolSize(10);
				config.setMinimumIdle(1);
				config.setMaxLifetime(1800000);
				config.setPoolName("XelPoolPostgres");
				config.setIdleTimeout(600000);
				config.setConnectionTimeout(30000);
				dsPostgres = new HikariDataSource(config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dsPostgres;
	}
	
}
