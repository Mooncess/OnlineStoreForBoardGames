package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.mooncess.onlinestore.entity.Article;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    public String addImage(String fileName, MultipartFile image) {
        // Генерируем уникальное имя файла
        Random random = new Random();
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String generatedFileName = fileName + '-' + Integer.toString(random.nextInt(900000) + 100000) + "." + extension;

        try {
            // Сохраняем файл
            File file = new File(UPLOAD_DIR + generatedFileName);
            image.transferTo(file);

            // Возвращаем имя сохраненного файла
            return generatedFileName;
        } catch (IOException e) {
            // Обрабатываем ошибку сохранения изображения
            e.printStackTrace();
            return null; // или выбрасываем исключение
        }
    }

    public String updateImage(Article article, MultipartFile image) {
        // Генерируем уникальное имя файла
        String generatedFileName = article.getImageURN();
        try {
            // Сохраняем файл
            File file = new File(UPLOAD_DIR + generatedFileName);
            image.transferTo(file);
            // Возвращаем имя сохраненного файла
            return generatedFileName;
        } catch (IOException e) {
            // Обрабатываем ошибку сохранения изображения
            e.printStackTrace();
            return null; // или выбрасываем исключение
        }
    }

    public void deleteImage(String imageURN) {
        File file = new File(UPLOAD_DIR + imageURN);
    }
}
