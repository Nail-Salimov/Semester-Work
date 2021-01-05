package server.addition.chat.bussineslogic.repositories;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.addition.chat.entity.message.MessageModel;
import server.addition.chat.entity.room.RoomModel;
import server.addition.chat.entity.state.MessageState;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class MessageRepositoryImpl implements MessageRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public MessageModel addMessage(MessageModel message) {
        entityManager.persist(message);
        for (MessageState state : message.getMessageStates()) {
            entityManager.persist(state);
        }
        return message;
    }

    @Override
    @Transactional
    public List<MessageModel> findAllMessages(RoomModel roomModel) {
        return entityManager.createQuery("SELECT m FROM MessageModel m WHERE m.room=:room", MessageModel.class)
                .setParameter("room", roomModel)
                .getResultList();
    }

    @Override
    @Transactional
    public List<MessageModel> findAllNewMessages(RoomModel roomModel, Long userId) {
        List<MessageState> states = entityManager.createQuery("SELECT s FROM MessageState s where s.isRead=false and s.userId=:userId and s.message.room=:room", MessageState.class)
                .setParameter("userId", userId).setParameter("room", roomModel).getResultList();

        List<MessageModel> messageModelList = new LinkedList<>();
        for (MessageState state : states) {
            messageModelList.add(state.getMessage());
        }
        return messageModelList;
    }

    @Override
    public List<MessageModel> findAllOldMessages(RoomModel roomModel, Long userId) {
        List<MessageState> states = entityManager.createQuery("SELECT s FROM MessageState s where s.isRead=true and s.userId=:userId and s.message.room=:room", MessageState.class)
                .setParameter("userId", userId).setParameter("room", roomModel).getResultList();

        List<MessageModel> messageModelList = new LinkedList<>();
        for (MessageState state : states) {
            messageModelList.add(state.getMessage());
        }
        return messageModelList;
    }

    @Override
    public Optional<MessageModel> findMessageById(Long messageId) {
        try {
            return Optional.of(entityManager.createQuery("SELECT m FROM MessageModel m WHERE m.id=:id", MessageModel.class)
                    .setParameter("id", messageId).getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public MessageState update(MessageState messageState) {
        entityManager.merge(messageState);
        return messageState;
    }

    @Override
    @Transactional
    public Optional<MessageState> findMessageState(Long userId, MessageModel message) {
        try {
            return Optional.of(entityManager.createQuery("SELECT s from MessageState s WHERE s.userId=:userId and s.message=:message", MessageState.class)
                    .setParameter("userId", userId)
                    .setParameter("message", message)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void readMessage(Long messageId, Long userId) {
        entityManager.createQuery("UPDATE MessageState s SET s.isRead=true WHERE s.message.id=:messageId and s.userId=:userId")
                .setParameter("messageId", messageId).setParameter("userId", userId).executeUpdate();
    }
}
