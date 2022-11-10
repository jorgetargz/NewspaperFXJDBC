package dao.impl;

import dao.CredentialsDao;
import dao.DBConnection;
import dao.ReadersDao;
import dao.common.Constantes;
import dao.utils.SQLQueries;
import domain.modelo.ArticleType;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class ReadersDaoImpl implements ReadersDao {


    private final DBConnection dbConnection;
    private final CredentialsDao credentialsDao;

    @Inject
    public ReadersDaoImpl(DBConnection dbConnection, CredentialsDao credentialsDao) {
        this.dbConnection = dbConnection;
        this.credentialsDao = credentialsDao;
    }

    @Override
    public List<Reader> getAll() {
        try (Connection con = dbConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_READERS_QUERY)) {
            ResultSet rs = preparedStatement.executeQuery();
            return getReadersFromRS(rs);

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Reader> getAll(Newspaper newspaper) {
        try (Connection con = dbConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_READERS_BY_NEWSPAPER_QUERY)) {
            preparedStatement.setInt(1, newspaper.getId());
            ResultSet rs = preparedStatement.executeQuery();
            return getReadersFromRS(rs);

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Reader> getAll(ArticleType articleType) {
        try (Connection con = dbConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_READERS_BY_ARTICLE_TYPE_QUERY)) {
            preparedStatement.setInt(1, articleType.getId());
            ResultSet rs = preparedStatement.executeQuery();
            return getReadersFromRS(rs);

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public Reader get(int id) {
        try (Connection con = dbConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_READER_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return getReaderFromRow(rs);
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public int save(Reader reader) {
        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);
            return executeSavingReaderStatements(reader, con);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }

    }

    @Override
    public int update(Reader reader) {
        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);
            return executeUpdatingReaderStatements(reader, con);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    @Override
    public int delete(Reader reader) {
        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);
            return executeDeletingReaderStatements(reader, con);
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    private int executeSavingReaderStatements(Reader reader, Connection con) {
        try (PreparedStatement preparedStatementInsertReader = con.prepareStatement(SQLQueries.INSERT_READER, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementInsertCredentials = con.prepareStatement(SQLQueries.INSERT_LOGIN_QUERY)) {

            preparedStatementInsertReader.setString(1, reader.getName());
            preparedStatementInsertReader.setDate(2, Date.valueOf(reader.getDateOfBirth()));
            preparedStatementInsertReader.executeUpdate();
            ResultSet rs = preparedStatementInsertReader.getGeneratedKeys();
            if (rs.next()) {
                //Add the id to the reader object in order to display it in the view
                reader.setId(rs.getInt(1));
                reader.getLogin().setIdReader(rs.getInt(1));
            }

            preparedStatementInsertCredentials.setString(1, reader.getLogin().getUsername());
            preparedStatementInsertCredentials.setString(2, reader.getLogin().getPassword());
            preparedStatementInsertCredentials.setInt(3, reader.getId());
            preparedStatementInsertCredentials.executeUpdate();

            con.commit();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                return Constantes.SQL_EXCEPTION;
            }
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    private int executeUpdatingReaderStatements(Reader reader, Connection con) {
        try (PreparedStatement preparedStatementUpdateReader = con.prepareStatement(SQLQueries.UPDATE_READER);
             PreparedStatement preparedStatementUpdateCredentials = con.prepareStatement(SQLQueries.UPDATE_LOGIN_QUERY)) {

            preparedStatementUpdateReader.setString(1, reader.getName());
            preparedStatementUpdateReader.setDate(2, java.sql.Date.valueOf(reader.getDateOfBirth()));
            preparedStatementUpdateReader.setInt(3, reader.getId());
            preparedStatementUpdateReader.executeUpdate();

            preparedStatementUpdateCredentials.setString(1, reader.getLogin().getPassword());
            preparedStatementUpdateCredentials.setString(2, reader.getLogin().getUsername());
            preparedStatementUpdateCredentials.executeUpdate();

            con.commit();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    private int executeDeletingReaderStatements(Reader reader, Connection con) {
        try (PreparedStatement preparedStatementDeleteReader = con.prepareStatement(SQLQueries.DELETE_READER);
             PreparedStatement preparedStatementDeleteReaderRatings = con.prepareStatement(SQLQueries.DELETE_RATINGS_BY_READER_ID_QUERY);
             PreparedStatement preparedStatementDeleteReaderSubscriptions = con.prepareStatement(SQLQueries.DELETE_SUBSCRIPTIONS_BY_READER_ID);
             PreparedStatement preparedStatementDeleteReaderCredentials = con.prepareStatement(SQLQueries.DELETE_LOGIN_BY_READER_ID)) {

            preparedStatementDeleteReaderRatings.setInt(1, reader.getId());
            preparedStatementDeleteReaderRatings.executeUpdate();

            preparedStatementDeleteReaderSubscriptions.setInt(1, reader.getId());
            preparedStatementDeleteReaderSubscriptions.executeUpdate();

            preparedStatementDeleteReaderCredentials.setInt(1, reader.getId());
            preparedStatementDeleteReaderCredentials.executeUpdate();

            preparedStatementDeleteReader.setInt(1, reader.getId());
            preparedStatementDeleteReader.executeUpdate();

            con.commit();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
                return Constantes.SQL_EXCEPTION;
            }
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    private List<Reader> getReadersFromRS(ResultSet rs) throws SQLException {
        List<Reader> readers = new ArrayList<>();
        while (rs.next()) {
            Reader reader = getReaderFromRow(rs);
            if (reader.getId() > Constantes.MIN_ID_READER) {
                readers.add(reader);
            }
        }
        return readers;
    }

    private Reader getReaderFromRow(ResultSet rs) throws SQLException {
        Reader reader = new Reader();
        reader.setId(rs.getInt(Constantes.ID));
        reader.setName(rs.getString(Constantes.NAME_READER));
        reader.setDateOfBirth(rs.getDate(Constantes.BIRTH_READER).toLocalDate());
        reader.setLogin(credentialsDao.get(rs.getInt(Constantes.ID)));
        return reader;
    }
}