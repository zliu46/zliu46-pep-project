package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDao {
    
    public Account insertAccount (Account account) {
        
        return null;
    }

    public Account getAccountByUsernameAndPassword (String un, String pw) {
        return null;
    }

    public boolean isUsernameTaken(String un) {
        return false;
    }

    public boolean isUsernameTakenById (int id) {
        return false;
    }
}
