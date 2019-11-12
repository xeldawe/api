package com.xel.apigateway.local.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class ConnectionService {

	@SuppressWarnings("static-access")
	private static DataSource dataSource = CoreDataSource.getPostgresDataSource();

	public static Connection getConn() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			return null;
		}
	}

}
