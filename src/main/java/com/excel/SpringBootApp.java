package com.excel;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

@SpringBootApplication
@EnableWebMvc

public class SpringBootApp {

    static final String url = "jdbc:postgresql://localhost:5432/postgres";
    static final String user = "postgres";
    static final String password = "Maklooba2003@!";

    static Connection conn;


    public static void main (String[] args) throws Exception {

        BasicConfigurator.configure();
        SpringApplication app = new SpringApplication(SpringBootApp.class);

        initDBConnection();

//        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));

        // using http://localhost:8081/
        app.run(SpringBootApp.class, args);
    }

    static void initDBConnection () throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(false);
    }


    static Connection getDBConnection () throws SQLException {
        if (conn == null || conn.isClosed())
            initDBConnection();

        return conn;
    }
}


