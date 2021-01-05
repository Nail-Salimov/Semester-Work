package server.bl.services.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.addition.email.services.MailConfirmationService;
import server.entities.user.dto.UserDataDto;
import server.jwt.Tokenizer;

@Aspect
@Component
public class AfterSaveUserAspect {

    @Autowired
    private MailConfirmationService mailService;

    @Autowired
    Tokenizer tokenizer;

    @AfterReturning(value = "execution(* server.bl.services.UserService.saveUser(..))", returning = "result")
    public void sendEmail(JoinPoint point, Object result) {

        Boolean booleanResult = (Boolean) result;
        if (booleanResult.equals(true)) {

            UserDataDto dto = (UserDataDto) point.getArgs()[0];
            String token = tokenizer.getToken(dto.getName(), dto.getMail());

            mailService.send("Подтверждение", "pologies12314s@mail.ru", dto.getMail(), dto.getName(), dto.getMail(), token, dto.getName(), dto.getMail());
        }
    }
}
