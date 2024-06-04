package com.MtoM.MtoM.domain.qna.posts.controller;

import com.MtoM.MtoM.domain.posts.controller.PostController;
import com.MtoM.MtoM.domain.posts.service.PostService;
import com.MtoM.MtoM.domain.user.domain.UserDomain;
import com.MtoM.MtoM.domain.user.repository.UserRepository;
import com.MtoM.MtoM.global.S3Service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private S3Service s3Service;

    private UserDomain user;

    @BeforeEach
    public void setup() {
        user = new UserDomain();
        user.setId("testUserId");
        user.setName("Test User");

        Mockito.when(userRepository.findById("testUserId")).thenReturn(Optional.of(user));
    }

    @Test
    public void createPost_withImage_shouldReturnCreated() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "img",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes());

        MockMultipartFile title = new MockMultipartFile("title", "", "text/plain", "Test Title".getBytes());
        MockMultipartFile content = new MockMultipartFile("content", "", "text/plain", "Test Content".getBytes());
        MockMultipartFile hashtags = new MockMultipartFile("hashtags", "", "text/plain", "Test Hashtags".getBytes());
        MockMultipartFile userId = new MockMultipartFile("userId", "", "text/plain", "testUserId".getBytes());

        Mockito.when(s3Service.uploadImage(Mockito.any(), Mockito.eq("post"))).thenReturn("https://s3-url/test-image.jpg");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/posts")
                        .file(imageFile)
                        .file(title)
                        .file(content)
                        .file(hashtags)
                        .file(userId))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\": \"게시물이 성공적으로 생성되었습니다.\"}"));
    }
}