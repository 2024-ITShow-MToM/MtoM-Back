package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.req.LoginUserRequestDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterProfileInfoDto;
import com.MtoM.MtoM.domain.user.dto.req.RegisterRequestDto;
import com.MtoM.MtoM.domain.user.repository.SkillRepository;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.*;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    public void registerProfileInfo(RegisterProfileInfoDto requestDto){
        String id = requestDto.getUserId().getId();
        checkId(id);

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
        user.setMentoring_topics(requestDto.getMentoring_topics());

        userRepository.save(user);
        List<SkillDomain> skillDomainList = requestDto.toSkillEntity();
        for(SkillDomain skill : skillDomainList)
            skillRepository.save(skill);
    }

    public String loginUser(LoginUserRequestDto requestDto){
        String id = requestDto.getId();
        checkId(id);

        String password = requestDto.getPassword();

        Optional<UserDomain> optionalUser = userRepository.findById(id);
        UserDomain user = optionalUser.get();
        String hashedPasword = user.getPassword();

        checkPassword(password, hashedPasword);

        return id;
    }

    public UserDomain findByUser(String id){
        checkId(id);

        Optional<UserDomain> optionalUser = userRepository.findById(id);
        UserDomain user = optionalUser.get();
        return user;
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

