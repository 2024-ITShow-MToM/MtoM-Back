package com.MtoM.MtoM.domain.user.facade;

import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.EmailDuplicateException;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.IdDuplicateException;
import com.MtoM.MtoM.global.exception.PasswordNotMatchException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class UserFacade {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void duplicateId(String id){
        if(userRepository.existsById(id)){
            throw new IdDuplicateException("id duplicated", ErrorCode.ID_DUPLICATION);
        }
    }


    public void duplicatedEmail(String email){
        if(userRepository.existsByEmail(email)){
            throw new EmailDuplicateException("email duplicated", ErrorCode.EMAIL_DUPLICATION);
        }
    }

    public void checkId(String id){
        if(!userRepository.existsById(id)){
            throw new IDNotFoundException("id not found", ErrorCode.ID_NOTFOUND);
        }
    }

    public void checkPassword(String password, String hashedPassword){
        if(!passwordEncoder.matches(password, hashedPassword)){
            throw new PasswordNotMatchException("password not found", ErrorCode.PASSWORD_NOTMATCH);
        }
    }
}
