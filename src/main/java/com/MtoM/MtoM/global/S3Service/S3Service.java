package com.MtoM.MtoM.global.S3Service;

import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.exception.IDNotFoundException;
import com.MtoM.MtoM.global.exception.error.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;

    @Value("${could.aws.s3.bucket}")
    private String bucketName;

    public void uploadProfileFile(String id, MultipartFile file, String type) throws IOException {

        checkId(id);

        String fileName = generateFileName(id, type);

        Optional<UserDomain> optionalUser = userRepository.findById(id);
        UserDomain user = optionalUser.get();
        user.setProfile(fileName);
        userRepository.save(user);

        uploadFileToS3(fileName, file);
    }

    public String uploadImage(MultipartFile file, String type) throws IOException {
        String fileName = generateFileName(UUID.randomUUID().toString(), type);
        return uploadFileToS3(fileName, file);
    }

    private String generateFileName(String id, String type){
        return UUID.randomUUID().toString() + "_" + type + "_" + id;
    }

    private String uploadFileToS3(String fileName, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public String getImagePath (String id){
        System.out.println(id);
        checkId(id);
        Optional<UserDomain> optionalUser = userRepository.findById(id);
        UserDomain user = optionalUser.get();
        String profilePath =user.getProfile();
        return amazonS3.getUrl(bucketName, profilePath).toString();
    }

    private void checkId (String id){
        if(!userRepository.existsById(id))
            throw new IDNotFoundException("id not found", ErrorCode.ID_NOTFOUND);
    }

    public void deleteImage(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(bucketName, fileName);
    }
}
