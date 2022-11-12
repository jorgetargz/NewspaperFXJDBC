package dao.impl;

import dao.CredentialsDao;
import dao.DBConnection;
import dao.common.Constantes;
import dao.utils.SQLQueries;
import domain.modelo.Login;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log4j2
public class CredentialsDaoImpl implements CredentialsDao {

    private final DBConnection dbConnection;

    @Inject
    public CredentialsDaoImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Login get(String username, String password) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_READER_FROM_LOGIN_QUERY)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            return getLoginFromRS(rs);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Login get(int idReader) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_LOGIN_FROM_ID_QUERY)) {
            preparedStatement.setInt(1, idReader);
            ResultSet rs = preparedStatement.executeQuery();
            return getLoginFromRS(rs);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public int save(Login login) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.INSERT_LOGIN_QUERY)) {
            preparedStatement.setString(1, login.getUsername());
            preparedStatement.setString(2, login.getPassword());
            preparedStatement.setInt(3, login.getIdReader());
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Constantes.SQL_EXCEPTION;
    }

    @Override
    public int update(Login login) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.UPDATE_LOGIN_QUERY)) {
            preparedStatement.setString(1, login.getPassword());
            preparedStatement.setString(2, login.getUsername());
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Constantes.SQL_EXCEPTION;
    }

    @Override
    public int delete(Login login) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.DELETE_LOGIN_QUERY)) {
            preparedStatement.setString(1, login.getUsername());
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Constantes.SQL_EXCEPTION;
    }

    private Login getLoginFromRS(ResultSet rs) throws SQLException {
        if (rs.next()) {
            int idReader = rs.getInt(Constantes.ID_READER);
            String username = rs.getString(Constantes.USERNAME);
            String pass = rs.getString(Constantes.PASSWORD);
            return new Login(username, pass, idReader);
        }
        return null;
    }
}
