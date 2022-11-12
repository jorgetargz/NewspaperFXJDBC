package dao;

import domain.modelo.Newspaper;
import domain.modelo.Reader;
import domain.modelo.Subscription;

import java.util.List;

public interface SubscriptionsDao {
    List<Subscription> getAll();

    List<Subscription> getAll(Newspaper newspaper);

    List<Subscription> getAll(Reader reader);

    Subscription get(Newspaper newspaper, Reader reader);

    int save(Subscription subscription);

    int update(Subscription subscription);

    int delete(Subscription subscription);

    List<Subscription> getOldestSubscriptions(Newspaper newspaper);
}
