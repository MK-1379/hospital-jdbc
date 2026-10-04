package org.mk13.util;
import org.mk13.exception.DataAccessException;

import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection()
    {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encuentra db.properties. Copia db.properties.example y rellénalo.");
            }
            props.load(in);
            return DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));
        }
        catch (IOException | SQLException e) {
            throw new DataAccessException("No se pudo conectar con la base de datos", e);
        }
    }

}
