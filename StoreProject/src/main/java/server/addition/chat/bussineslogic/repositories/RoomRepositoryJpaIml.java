package server.addition.chat.bussineslogic.repositories;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.addition.chat.entity.link.LinkModel;
import server.addition.chat.entity.room.RoomModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class RoomRepositoryJpaIml implements RoomRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public RoomModel addRoom(RoomModel roomModel) {
        entityManager.persist(roomModel);
        for (LinkModel linkModel : roomModel.getLinkModels()) {
            addLink(linkModel);
        }
        return roomModel;
    }

    @Override
    @Transactional
    public Optional<RoomModel> findRoomById(Long id) {
        try {
            return Optional.of(entityManager.createQuery("SELECT r FROM RoomModel r WHERE r.id=:id", RoomModel.class)
                    .setParameter("id", id).getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public LinkModel addLink(LinkModel linkModel) {
        entityManager.persist(linkModel);
        return linkModel;
    }

    @Override
    @Transactional
    public Optional<LinkModel> findLinks(Long userId, RoomModel roomModel) {
        try {
        return Optional.of(entityManager.createQuery("SELECT l FROM LinkModel l WHERE l.userId=:userId AND " +
                "l.room=:roomModel", LinkModel.class)
                .setParameter("userId", userId)
                .setParameter("roomModel", roomModel)
                .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Long> findRoomId(Long firstId, Long secondId) {
        List<Long> firstList = entityManager.createQuery("SELECT l.room.id FROM LinkModel l WHERE l.userId=:firstId", Long.class)
                .setParameter("firstId", firstId).getResultList();
        List<Long> secondList = entityManager.createQuery("SELECT l.room.id FROM LinkModel l WHERE l.userId=:secondId", Long.class)
                .setParameter("secondId", secondId).getResultList();


        System.out.println(firstList);
        System.out.println(secondList);
        for (Long id : secondList) {
            if (firstList.contains(id)) {
                return Optional.of(id);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<LinkModel> findLinks(RoomModel roomModel) {
        return entityManager.createQuery("SELECT l FROM LinkModel l WHERE l.room=:roomModel", LinkModel.class)
                .setParameter("roomModel", roomModel).getResultList();
    }

    @Override
    public List<RoomModel> findRoomsByUserId(Long userId) {
        List<LinkModel>  linkModelList = entityManager.createQuery("SELECT l FROM LinkModel l WHERE l.userId=:userId", LinkModel.class)
                .setParameter("userId", userId).getResultList();

        List<RoomModel> roomModelList = new LinkedList<>();
        for (LinkModel link : linkModelList){
            roomModelList.add(link.getRoom());
        }
        return roomModelList;
    }

}
