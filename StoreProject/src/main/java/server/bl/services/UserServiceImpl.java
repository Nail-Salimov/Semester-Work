package server.bl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import server.bl.repositories.UserRepository;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.Role;
import server.entities.user.model.State;
import server.entities.user.model.UserDataModel;
import server.jwt.Tokenizer;

import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Tokenizer tokenizer;

    @Override
    public Optional<UserDataDto> findUserByMail(String mail) {
        Optional<UserDataModel> optional = userRepository.findByMail(mail);
        return optional.map(UserDataDto::getUserDataDto);
    }

    @Override
    public Optional<UserDataDto> findUserById(Long id) {
        Optional<UserDataModel> optional = userRepository.findById(id);
        return optional.map(UserDataDto::getUserDataDto);
    }

    @Override
    public boolean saveUser(UserDataDto form) {

        if (!findUserByMail(form.getMail()).isPresent()) {
            UserDataModel userModel = UserDataModel.builder()
                    .mail(form.getMail())
                    .hashPassword(passwordEncoder.encode(form.getPassword()))
                    .name(form.getName())
                    .role(Role.USER)
                    .state(State.NOT_CONFIRMED)
                    .build();
            userRepository.save(userModel);
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmUser(String name, String mail, String token) {
        boolean result = tokenizer.checkClient(token, name, mail);
        if (result) {
            userRepository.confirmUser(mail);
            return true;
        }
        return false;
    }

    @Override
    public void updateUserName(String newName, UserDataDto userDataDto) {
        userRepository.changeName(newName, userDataDto.getMail());
    }

    @Override
    public void updateUserAddress(String newAddress, UserDataDto userDataDto) {
        userRepository.changeAddress(newAddress, userDataDto.getMail());
    }
}
