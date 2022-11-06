package domain.services.impl;

import dao.CredentialsDao;
import dao.ReadersDao;
import dao.impl.CredentialsDaoImpl;
import domain.modelo.Login;
import domain.modelo.Reader;
import domain.services.ServicesCredentials;
import jakarta.inject.Inject;

public class ServicesCredentialsImpl implements ServicesCredentials {

    private final CredentialsDao credentialsDao;
    private final ReadersDao readersDao;

    @Inject
    public ServicesCredentialsImpl(CredentialsDaoImpl credentialsDao, ReadersDao readersDao) {
        this.credentialsDao = credentialsDao;
        this.readersDao = readersDao;
    }

    @Override
    public Reader scLogin(String username, String password) {
        Login login = credentialsDao.get(username, password);
        return login != null ? readersDao.get(login.getIdReader()) : null;
    }

}
