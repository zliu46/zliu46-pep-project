package Service;

import java.sql.SQLException;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
    
    AccountDao accountDao = new AccountDao();

    public Account register(Account account) throws SQLException {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null;
        }

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        if (accountDao.isUsernameTaken(account.getUsername())) {
            return null;
        }
        
        return accountDao.insertAccount(account);
    }

    public Account login(String un, String pw) {
        return null;
    }
}
