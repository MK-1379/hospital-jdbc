package org.mk13.util;
import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection con = null;
    public static Connection getConnection()
    {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encuentra db.properties. Copia db.properties.example y rellénalo.");
            }
            props.load(in);
            con = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

}
