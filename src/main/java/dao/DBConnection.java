package dao;

import jakarta.annotation.PreDestroy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnection {
    Connection getConnection() throws SQLException;

    DataSource getDataSource();

    void closeConnection(Connection connArg);

    @PreDestroy
    void closePool();
}
