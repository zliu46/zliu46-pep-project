package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.util.ArrayList;
import java.sql.*;

public class MessageDao {
    
    public Message insertMessage (Message message) throws SQLException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                return new Message (id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Message> getAllMessages() {
        return new ArrayList<>();
    }

    public Message getMessageById(int id) {
        return null;
    }

    public Message updateMessage(int id, String newText) {
        return null;
    }

    public ArrayList<Message> getMessagesByUserId(int userId) {
        return new ArrayList<>();
    }

    public Message deletMessageById(int id) {
        return null;
    }
}
