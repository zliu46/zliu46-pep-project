package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDao {
    
    public Account insertAccount (Account account) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO account (username. password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                return new Account (generatedId, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
