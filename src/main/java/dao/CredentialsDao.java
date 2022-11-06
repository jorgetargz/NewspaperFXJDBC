package dao;

import domain.modelo.Login;

public interface CredentialsDao {

    Login get(String username, String password);

    Login get(int idReader);

    int save(Login login);

    int update(Login login);

    int delete(Login login);
}
