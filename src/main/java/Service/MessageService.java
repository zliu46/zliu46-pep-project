package Service;

import DAO.AccountDao;
import DAO.MessageDao;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageService {
    MessageDao messageDao = new MessageDao();
    AccountDao accountDao = new AccountDao();
    
    public Message createMessage(Message message) throws SQLException {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() ||
                message.getMessage_text().length() > 255 ) {
                    return null;
        }

        if (!accountDao.isUsernameTakenById(message.getPosted_by())) {
            return null;
        }

        return messageDao.insertMessage(message);
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDao.getAllMessages();
    }

    public Message getMessageById(int id) throws SQLException {
        return messageDao.getMessageById(id);
    }
    
    public Message deletMessageById(int id) throws SQLException {
        return messageDao.deletMessageById(id);
    }

    public Message updatMessage(int id, String newMessage) {
        return null;
    }

    public List<Message> getMessagesByUserId(int userId) throws SQLException {
        return messageDao.getMessagesByUserId(userId);
    }
}
