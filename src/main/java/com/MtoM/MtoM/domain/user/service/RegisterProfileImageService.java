package com.MtoM.MtoM.domain.user.service;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class RegisterProfileImageService {
    private final S3Service s3Service;
    private final UserRepository userRepository;

    @Transactional
    public void execute(MultipartFile file, String id) throws IOException {
        String profile = s3Service.uploadImage(file, id);

        UserDomain user = userRepository.findById(id)
                .orElseThrow(() -> new IDNotFoundException("user not found", ErrorCode.ID_NOTFOUND));

        user.registerProfileImage(profile);

    }
}
