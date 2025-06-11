package Controller;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Util.ConnectionUtil;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.get("example-endpoint", this::exampleHandler);

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
    
        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context ctx) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            Account account = ctx.bodyAsClass(Account.class);

            if (account.getUsername() == null || account.getUsername().isBlank() || account.getPassword().length() < 4) {
                ctx.status(400);
                return;
            }

            PreparedStatement check = connection.prepareStatement("SELECT * FROM account WHERE username = ?");
            check.setString(1, account.getUsername());
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                ctx.status(400);
                return;
            }

            PreparedStatement insert = connection.prepareStatement("INSERT INTO account (username, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            insert.setString(1, account.getUsername());
            insert.setString(2, account.getPassword());
            insert.executeUpdate();
            ResultSet keys = insert.getGeneratedKeys();
            if (keys.next()) {
                account.setAccount_id(keys.getInt(1));
                ctx.json(account);
            }
            else {
                ctx.status(400);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500).result("Database error");
        }
    }

    private void loginHandler (Context ctx) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            Account account = ctx.bodyAsClass(Account.class);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM account WHERE username = ? AND password = ?");
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account found = new Account (
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                ctx.json(found);
            }
            else {
                ctx.status(401);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500);
        }
    }


}