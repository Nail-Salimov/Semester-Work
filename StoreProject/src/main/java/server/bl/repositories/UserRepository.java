package server.bl.repositories;

import server.entities.user.model.UserDataModel;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Long, UserDataModel> {
    Optional<UserDataModel> findById(Long id);
    Optional<UserDataModel> findByMail(String mail);
    void confirmUser(String mail);
    void changeName(String newName, String mail);
    void changeAddress(String newAddress, String mail);

}
