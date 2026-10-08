package org.mk13.util;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;

import org.mk13.exception.DataAccessException;

/**
 * Crea el DataSource de MySQL a partir de db.properties.
 * Los DAO no la usan directamente: reciben el DataSource por el constructor,
 * y así en los tests se les puede pasar una base de datos H2 en memoria.
 */
public final class DatabaseConnection {

    private DatabaseConnection() {
    }

    public static DataSource createDataSource() {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encuentra db.properties. Copia db.properties.example y rellénalo.");
            }
            props.load(in);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo leer db.properties", e);
        }

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(props.getProperty("db.url"));
        dataSource.setUser(props.getProperty("db.user"));
        dataSource.setPassword(props.getProperty("db.password"));
        return dataSource;
    }
}
