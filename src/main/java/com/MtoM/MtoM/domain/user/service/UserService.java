package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.RegisterProfileInfoDto;
import com.MtoM.MtoM.domain.user.dto.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.repository.SkillRepository;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.EmailDuplicateException;
import com.MtoM.MtoM.global.exception.EmailNotFoundException;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import com.MtoM.MtoM.global.exception.IdDuplicateException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterRequestDto requestDto){
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        duplicateId(id);
        duplicatedEmail(email);

        userRepository.save(requestDto.toEntity(password));
    }

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

    @Transactional
    public void registerProfileInfo(RegisterProfileInfoDto requestDto){
        String id = requestDto.getUserId().getId();

        if(!userRepository.existsById(id)){
            throw new IDNotFoundException("id not found", ErrorCode.ID_NOTFOUND);
        }

        Optional<UserDomain> optionalUser = userRepository.findById(id);
        UserDomain user = optionalUser.get();

        user.setName(requestDto.getName());
        user.setStudent_id(requestDto.getStudent_id());
        user.setBirthday(requestDto.getBirthday());
        user.setGender(requestDto.getGender());
        user.setPhonenumber(requestDto.getPhonenumber());
        user.setMajor(requestDto.getMajor());
        user.setMbti(requestDto.getMbti());
        user.setPersonal(requestDto.getPersonal());
        user.setImogi(requestDto.getImogi());

        userRepository.save(user);
        skillRepository.save(requestDto.toSkillEntity());
    }
}

