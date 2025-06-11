package Controller;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
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
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);

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
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500);
        }
    }

    private void createMessageHandler(Context ctx) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            Message msg = ctx.bodyAsClass(Message.class);

            if (msg.getMessage_text() == null || msg.getMessage_text().isBlank() || msg.getMessage_text().length() > 255) {
                ctx.status(400);
                return;
            }

            PreparedStatement checkUser = connection.prepareStatement("SELECT * FROM account WHERE account_id = ?");
            checkUser.setInt(1, msg.getPosted_by());
            if (!checkUser.executeQuery().next()) {
                ctx.status(400);
                return;
            }

            PreparedStatement insert = connection.prepareStatement("INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insert.setInt(1, msg.getPosted_by());
            insert.setString(2, msg.getMessage_text());
            insert.setLong(3, msg.getTime_posted_epoch());
            insert.executeUpdate();

            ResultSet keys = insert.getGeneratedKeys();

            if(keys.next()) {
                msg.setMessage_id(keys.getInt(1));
                ctx.json(msg);
            }
            else {
                ctx.status(400);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500);
        }
    }

    private void getAllMessagesHandler (Context ctx) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            List<Message> messages = new ArrayList<>();
            String sql = "SELECT * FROM message";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong(
                        "time_posted_epoch")
                ));
            }
            ctx.json(messages);
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500);
        }
    }

    private void getMessageByIdHandler (Context ctx) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM message WHERE message_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message msg = new Message (
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                ctx.json(msg);
            } else {
                ctx.result("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.status(500);
        }
    }

}