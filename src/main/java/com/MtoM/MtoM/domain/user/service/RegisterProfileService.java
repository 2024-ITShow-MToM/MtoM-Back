package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.SkillDomain;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.dto.req.RegisterProfileInfoDto;
import com.MtoM.MtoM.domain.user.facade.UserFacade;
import com.MtoM.MtoM.domain.user.repository.SkillRepository;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegisterProfileService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserFacade userFacade;
    private final S3Service s3UploadService;

    @Transactional
    public void execute(RegisterProfileInfoDto requestDto) throws IOException {
        String id = requestDto.getUserId();

        userFacade.checkId(id);

        UserDomain user = userRepository.findById(id)
                .orElseThrow(() -> new IDNotFoundException("user not found", ErrorCode.ID_NOTFOUND));

        user.registerProfile(
                requestDto.getName(),
                requestDto.getStudent_id(),
                requestDto.getBirthday(),
                requestDto.getGender(),
                requestDto.getPhonenumber(),
                requestDto.getMajor(),
                requestDto.getMbti(),
                requestDto.getPersonal(),
                requestDto.getImogi(),
                requestDto.getMentoring_topics(),
                requestDto.getIntroduction()
        );

        userRepository.save(user);

        List<SkillDomain> skillDomainList = requestDto.toSkillEntity(user);
        skillRepository.saveAll(skillDomainList);
    }
}
