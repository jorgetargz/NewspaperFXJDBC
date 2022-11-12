package domain.services.impl;

import dao.CredentialsDao;
import dao.DBConnection;
import dao.ReadersDao;
import dao.impl.CredentialsDaoImpl;
import domain.modelo.Login;
import domain.modelo.Reader;
import domain.services.ServicesCredentials;
import jakarta.inject.Inject;

public class ServicesCredentialsImpl implements ServicesCredentials {

    private final CredentialsDao credentialsDao;
    private final ReadersDao readersDao;
    private final DBConnection dbConnection;

    @Inject
    public ServicesCredentialsImpl(CredentialsDaoImpl credentialsDao, ReadersDao readersDao, DBConnection dbConnection) {
        this.credentialsDao = credentialsDao;
        this.readersDao = readersDao;
        this.dbConnection = dbConnection;
    }

    @Override
    public Reader scLogin(String username, String password) {
        Login login = credentialsDao.get(username, password);
        return login != null ? readersDao.get(login.getIdReader()) : null;
    }

    @Override
    public void scCloseApp() {
        dbConnection.closePool();
    }

}
