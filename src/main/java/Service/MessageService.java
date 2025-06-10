package Service;

import DAO.AccountDao;
import DAO.MessageDao;
import Model.Message;
import java.util.List;

public class MessageService {
    MessageDao messageDao = new MessageDao();
    AccountDao accountDao = new AccountDao();
    
    public Message createMessage(Message message) {
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDao.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDao.getMessageById(id);
    }
    
    public Message deletMessageById(int id) {
        return messageDao.deletMessageById(id);
    }

    public Message updatMessage(int id, String newMessage) {
        return null;
    }

    public List<Message> getMessagesByUserId(int userId) {
        return messageDao.getMessagesByUserId(userId);
    }
}
