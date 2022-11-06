package domain.services;

import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.modelo.Subscription;
import io.vavr.control.Either;

import java.time.LocalDate;
import java.util.List;

public interface ServicesSubscriptions {
    List<Subscription> getSubscriptions();

    List<Subscription> getSubscriptionsByNewspaper(Newspaper newspaper);

    List<Subscription> getOldestSubscriptionsByNewspaper(Newspaper newspaper);

    List<Subscription> getSubscriptionsByReader(Reader reader);

    Either<String, Boolean> addSubscription(int idReader, int idNewspaper, LocalDate signingDate);

    Either<String, Boolean> cancelSubscription(Subscription subscription);

    Either<String, Boolean> deleteSubscription(Subscription subscription);
}
