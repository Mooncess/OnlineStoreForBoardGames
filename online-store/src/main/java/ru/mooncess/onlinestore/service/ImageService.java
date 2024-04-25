package ru.mooncess.onlinestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final String UPLOAD_DIR = "C:/IJProjects/OnlineStoreForBoardGames/online-store/src/main/resources/static/";

    public String addImage(MultipartFile image) {
        // Генерируем уникальное имя файла
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String generatedFileName = UUID.randomUUID().toString() + "." + extension;

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
}
