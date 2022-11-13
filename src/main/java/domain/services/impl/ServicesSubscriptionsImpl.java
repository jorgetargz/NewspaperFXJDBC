package domain.services.impl;

import dao.NewspapersDao;
import dao.ReadersDao;
import dao.SubscriptionsDao;
import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.modelo.Subscription;
import domain.services.ServicesSubscriptions;
import io.vavr.control.Either;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

public class ServicesSubscriptionsImpl implements ServicesSubscriptions {

    private final SubscriptionsDao daoSubscriptions;
    private final ReadersDao daoReaders;
    private final NewspapersDao daoNewspapers;

    @Inject
    public ServicesSubscriptionsImpl(SubscriptionsDao daoSubscriptions, ReadersDao daoReaders, NewspapersDao daoNewspapers) {
        this.daoSubscriptions = daoSubscriptions;
        this.daoReaders = daoReaders;
        this.daoNewspapers = daoNewspapers;
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return daoSubscriptions.getAll();
    }


    @Override
    public List<Subscription> getSubscriptionsByNewspaper(Newspaper newspaper) {
        return daoSubscriptions.getAll(newspaper);
    }

    @Override
    public List<Subscription> getOldestSubscriptionsByNewspaper(Newspaper newspaper) {
        return daoSubscriptions.getOldestSubscriptions(newspaper);
    }

    @Override
    public List<Subscription> getSubscriptionsByReader(Reader reader) {
        return daoSubscriptions.getAll(reader);
    }

    @Override
    public Either<String, Boolean> addSubscription(int idReader, int idNewspaper, LocalDate signingDate) {
        Newspaper newspaper = daoNewspapers.get(idNewspaper).get();
        Reader reader = daoReaders.get(idReader);
        if (newspaper == null) {
            return Either.left("Newspaper not found");
        }
        if (reader == null) {
            return Either.left("Reader not found");
        }
        Subscription subscription = new Subscription(reader, newspaper, signingDate, null);
        return daoSubscriptions.save(subscription) == 0 ? Either.right(true) : Either.left("Error saving the subscription");
    }

    @Override
    public Either<String, Boolean> cancelSubscription(Subscription subscription) {
        LocalDate today = LocalDate.now();
        subscription.setCancellationDate(today);
        return daoSubscriptions.update(subscription) == 0 ? Either.right(true) : Either.left("Error canceling the subscription");
    }

    @Override
    public Either<String, Boolean> deleteSubscription(Subscription subscription) {
        return daoSubscriptions.delete(subscription) == 0 ? Either.right(true) : Either.left("Error deleting the subscription");
    }
}
