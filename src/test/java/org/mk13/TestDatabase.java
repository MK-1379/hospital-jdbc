package org.mk13;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

/**
 * Crea una base de datos H2 en memoria, nueva y vacía, para cada test.
 * Así los tests no dependen de MySQL ni unos de otros.
 */
public final class TestDatabase {

    private TestDatabase() {
    }

    public static DataSource create() {
        JdbcDataSource dataSource = new JdbcDataSource();
        // Nombre único por test. DB_CLOSE_DELAY=-1 mantiene la base de datos viva
        // aunque los DAO cierren la conexión después de cada operación.
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            for (String sql : readSchema().split(";")) {
                if (!sql.isBlank()) {
                    st.execute(sql);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo crear el esquema de pruebas", e);
        }
        return dataSource;
    }

    private static String readSchema() {
        try (InputStream in = TestDatabase.class.getResourceAsStream("/schema.sql")) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer schema.sql", e);
        }
    }
}
