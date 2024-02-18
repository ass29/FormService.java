//package com.excel;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.UUID;
//
//@Service
//public class UserService implements UserDetailsService {
//
//    Statement stmt = null;
//    Connection conn = null;
//    ResultSet resultSet = null;
//
//    public void createUsersTable() throws SQLException
//    {
//        try
//        {
//            conn = SpringBootApp.getDBConnection();
//            stmt = conn.createStatement();
//
//            String usersTable =     "CREATE TABLE IF NOT EXISTS Users" +
//                                    "( " +
//                                    "UserID VARCHAR(36) NOT NULL," +
//                                    "PRIMARY KEY (UserID), " +
//                                    "username VARCHAR(200) NOT NULL," +
//                                    "password VARCHAR NOT NULL," +
//                                    "role VARCHAR(200) NOT NULL " +
//                                    ");";
//
//            stmt.executeUpdate(usersTable);
//        }
//
//        catch (SQLException e)
//        {
//            throw new SQLException();
//        }
//    }
//
//    public User findUserByID(UUID id) throws SQLException
//    {
//
//        User user = null;
//        try
//        {
//            conn = SpringBootApp.getDBConnection();
//            stmt = conn.createStatement();
//
//            String query = "SELECT * FROM Users " +
//                            "WHERE UserID = " + id + ";";
//
//            resultSet = stmt.executeQuery(query);
//            while(resultSet.next())
//            {
//                String userID = resultSet.getString("userid");
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//
//                user = new User(UUID.fromString(userID), username, password);
//            }
//
//            return user;
//        }
//        catch (SQLException e)
//        {
//            throw new SQLException();
//        }
//    }
//
//    public User findUserByUsername(String username) throws SQLException
//    {
//
//        User user = null;
//        try
//        {
//            conn = SpringBootApp.getDBConnection();
//            stmt = conn.createStatement();
//
//            String query =  "SELECT * FROM Users " +
//                            "WHERE username = " + username + ";";
//
//            resultSet = stmt.executeQuery(query);
//            while(resultSet.next())
//            {
//                String userID = resultSet.getString("userid");
//                String uName = resultSet.getString("username");
//                String password = resultSet.getString("password");
//
//                user = new User(UUID.fromString(userID), username, password);
//            }
//
//            return user;
//        }
//        catch (SQLException e)
//        {
//            throw new SQLException();
//        }
//    }
//
//    @Override
//    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException
//    {
//
//
//        return null;
//    }
//}
