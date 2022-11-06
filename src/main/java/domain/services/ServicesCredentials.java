package domain.services;

import domain.modelo.Reader;

public interface ServicesCredentials {
    Reader scLogin(String username, String password);
}
