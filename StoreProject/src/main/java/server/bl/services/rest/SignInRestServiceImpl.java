package server.bl.services.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import server.bl.repositories.UserRepository;
import server.entities.signIn.dto.SignInDto;
import server.entities.token.dto.TokenDto;
import server.entities.user.model.UserDataModel;

import java.util.Optional;

@Component
public class SignInRestServiceImpl implements SignInRestService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;


    @Override
    public TokenDto signIn(SignInDto signInDto) {
        Optional<UserDataModel> optionalUser = userRepository.findByMail(signInDto.getMail());

        if(optionalUser.isPresent()){
            UserDataModel user = optionalUser.get();

            if(passwordEncoder.matches(signInDto.getPassword(), user.getHashPassword())){
                String token = Jwts.builder()
                        .setSubject(user.getId().toString())
                        .claim("mail", user.getMail())
                        .claim("name", user.getName())
                        .claim("role", user.getRole().name())
                        .claim("address", user.getAddress())
                        .claim("state", user.getState().name())
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
                return new TokenDto(token);
            }else {
                throw new AccessDeniedException("Wrong email or password");
            }

        }else {
            throw new AccessDeniedException("User not found");
        }
    }

    @Override
    public TokenDto signInWithHashPassword(SignInDto signInDto) {
        Optional<UserDataModel> optionalUser = userRepository.findByMail(signInDto.getMail());

        if(optionalUser.isPresent()){
            UserDataModel user = optionalUser.get();

            if(user.getHashPassword().equals(signInDto.getPassword())){
                String token = Jwts.builder()
                        .setSubject(user.getId().toString())
                        .claim("mail", user.getMail())
                        .claim("name", user.getName())
                        .claim("role", user.getRole().name())
                        .claim("address", user.getAddress())
                        .claim("state", user.getState().name())
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
                return new TokenDto(token);
            }else {
                throw new AccessDeniedException("Wrong email or password");
            }

        }else {
            throw new AccessDeniedException("User not found");
        }
    }
}
