package dao.impl;

import dao.DBConnection;
import dao.NewspapersDao;
import dao.ReadersDao;
import dao.SubscriptionsDao;
import common.Constantes;
import dao.utils.SQLQueries;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.modelo.Subscription;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class SubscriptionsDaoImpl implements SubscriptionsDao {

    private final DBConnection dbConnection;
    private final ReadersDao readersDao;
    private final NewspapersDao newspapersDao;

    @Inject
    public SubscriptionsDaoImpl(DBConnection dbConnection, ReadersDao readersDao, NewspapersDao newspapersDao) {
        this.dbConnection = dbConnection;
        this.readersDao = readersDao;
        this.newspapersDao = newspapersDao;
    }

    @Override
    public List<Subscription> getAll() {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_SUBSCRIPTIONS_QUERY)) {

            ResultSet rs = preparedStatement.executeQuery();
            return getSubscriptionsFromRS(rs);

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Subscription> getAll(Newspaper newspaper) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_SUBSCRIPTIONS_BY_NEWSPAPER_QUERY)) {

            preparedStatement.setInt(1, newspaper.getId());
            ResultSet rs = preparedStatement.executeQuery();
            return getSubscriptionsFromRS(rs);

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Subscription> getAll(Reader reader) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_SUBSCRIPTIONS_BY_READER_QUERY)) {

            preparedStatement.setInt(1, reader.getId());
            ResultSet rs = preparedStatement.executeQuery();
            return getSubscriptionsFromRS(rs);

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    @Override
    public Subscription get(Newspaper newspaper, Reader reader) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_SUBSCRIPTION_BY_NEWSPAPER_AND_READER_QUERY)) {

            preparedStatement.setInt(1, newspaper.getId());
            preparedStatement.setInt(2, reader.getId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Subscription subscription = new Subscription();
                subscription.setReader(readersDao.get(reader.getId()));
                subscription.setNewspaper(newspapersDao.get(newspaper.getId()).get());
                subscription.setSigningDate(rs.getDate(Constantes.SIGNING_DATE).toLocalDate());
                Date cancellationDate = rs.getDate(Constantes.CANCELLATION_DATE);
                if (cancellationDate != null) {
                    subscription.setCancellationDate(cancellationDate.toLocalDate());
                }
                return subscription;
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }


    @Override
    public int save(Subscription subscription) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.INSERT_SUBSCRIPTION)) {

            preparedStatement.setInt(1, subscription.getReader().getId());
            preparedStatement.setInt(2, subscription.getNewspaper().getId());
            preparedStatement.setDate(3, Date.valueOf(subscription.getSigningDate()));
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    @Override
    public int update(Subscription subscription) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.UPDATE_SUBSCRIPTION)) {

            preparedStatement.setDate(1, Date.valueOf(subscription.getCancellationDate()));
            preparedStatement.setInt(2, subscription.getNewspaper().getId());
            preparedStatement.setInt(3, subscription.getReader().getId());
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    @Override
    public int delete(Subscription subscription) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.DELETE_SUBSCRIPTION)) {

            preparedStatement.setInt(1, subscription.getReader().getId());
            preparedStatement.setInt(2, subscription.getNewspaper().getId());
            preparedStatement.executeUpdate();
            return Constantes.SUCCESS;
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
            return Constantes.SQL_EXCEPTION;
        }
    }

    @Override
    public List<Subscription> getOldestSubscriptions(Newspaper newspaper) {
        try (Connection con = dbConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueries.SELECT_OLDEST_SUBSCRIPTIONS_BY_NEWSPAPER_QUERY)) {

            preparedStatement.setInt(1, newspaper.getId());
            ResultSet rs = preparedStatement.executeQuery();
            return getSubscriptionsFromRS(rs);

        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Collections.emptyList();
    }

    private List<Subscription> getSubscriptionsFromRS(ResultSet rs) throws SQLException {
        List<Subscription> subscriptions = new ArrayList<>();
        while (rs.next()) {
            Subscription subscription = new Subscription();
            subscription.setReader(readersDao.get(rs.getInt(Constantes.ID_READER)));
            subscription.setNewspaper(newspapersDao.get(rs.getInt(Constantes.ID_NEWSPAPER)).get());
            subscription.setSigningDate(rs.getDate(Constantes.SIGNING_DATE).toLocalDate());
            Date cancellationDate = rs.getDate(Constantes.CANCELLATION_DATE);
            if (cancellationDate != null) {
                subscription.setCancellationDate(cancellationDate.toLocalDate());
            }
            subscriptions.add(subscription);
        }
        return subscriptions;
    }


}