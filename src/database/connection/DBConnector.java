/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Dulanjaya
 */
public class DBConnector {
    public Connection conn;
    private Statement statement;
    public static DBConnector db;
    private DBConnector() {
        String url= "jdbc:mysql://localhost:3306/";
        String dbName = "instantmessengerplus";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "4211";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized DBConnector getDbCon() {
        if ( db == null ) {
            db = new DBConnector();
        }
        return db;
 
    }
    public Connection getConnection() {
        return conn;
    }
}
