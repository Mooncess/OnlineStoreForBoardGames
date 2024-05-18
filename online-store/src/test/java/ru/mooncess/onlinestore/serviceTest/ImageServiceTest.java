package ru.mooncess.onlinestore.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import ru.mooncess.onlinestore.entity.Article;
import ru.mooncess.onlinestore.service.ImageService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService();
    }

    @Test
    void testAddImage() {
        String fileName = "test-file";
        MultipartFile image = new MockMultipartFile("test.jpg", new byte[]{});

        String generatedFileName = imageService.addImage(fileName, image);

        assertNotNull(generatedFileName);
        assertTrue(generatedFileName.startsWith(fileName));
    }

    @Test
    void testUpdateImage() {
        Article article = new Article();
        article.setImageURN("test-image.jpg");
        MultipartFile image = new MockMultipartFile("test.jpg", new byte[]{});

        String updatedFileName = imageService.updateImage(article, image);

        assertNotNull(updatedFileName);
        assertEquals(article.getImageURN(), updatedFileName);
    }
}
