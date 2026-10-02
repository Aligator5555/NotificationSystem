package org.example.notificationsystem3728.service;

import org.example.notificationsystem3728.model.Signal;
import org.example.notificationsystem3728.repository.SignalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class SignalService {

    private final SignalRepository signalRepository;
    private final Path soundsDirectory; // только final-поле, без @Value

    public SignalService(
            SignalRepository signalRepository,
            @Value("${app.sounds.path:/data/sounds}") String directoryPath
    ) {
        this.signalRepository = signalRepository;
        this.soundsDirectory = Paths.get(directoryPath).toAbsolutePath();

        try {
            Files.createDirectories(this.soundsDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для звуков: " + this.soundsDirectory, e);
        }

        System.out.println("=== SignalService создан ===");
        System.out.println("Реальный soundsDirectory: " + soundsDirectory);
        System.out.println("Существует ли папка: " + Files.exists(soundsDirectory));
        System.out.println("Можно ли писать: " + Files.isWritable(soundsDirectory));
        System.out.println("===========================");
    }


    public void saveSignal(String nameSignal, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        // Создаём директорию, если её нет (безопасно, даже если она уже есть — будет исключение, которое мы обработаем)
        try {
            Files.createDirectories(soundsDirectory);
        } catch (Exception e) {
            // Если директория уже существует, Files.createDirectories выбросит исключение.
            // Проверяем, существует ли она, и если да — игнорируем ошибку.
            if (!Files.exists(soundsDirectory)) {
                throw new RuntimeException("Не удалось создать директорию для звуков: " + soundsDirectory, e);
            }
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        // Уникальное имя, чтобы не было коллизий
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;
        Path filePath = soundsDirectory.resolve(uniqueFileName);

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        Signal signal = new Signal();
        signal.setNameSignal(nameSignal);

        // ВАЖНО: сохраняем только имя файла, а не полный путь
        signal.setFilePath(uniqueFileName);

        signal.setMimeType(file.getContentType() != null ? file.getContentType() : "audio/wav");

        signalRepository.save(signal);
    }

    public void deleteSignal(Long id) {
        Signal signal = signalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сигнал не найден: " + id));

        // Сначала удаляем файл с диска, если он есть
        if (signal.getFilePath() != null) {
            Path filePath = soundsDirectory.resolve(signal.getFilePath());
            try {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } catch (IOException e) {
                // Логируй ошибку, но не прерывай удаление из БД, если файл уже удалён
            }
        }

        signalRepository.deleteById(id);
    }

    public String getMimeTypeByFileName(String fileName) {
        // fileName здесь — это полный путь/имя файла (UUID_name.wav)
        return signalRepository.findByFilePath(fileName)
                .map(Signal::getMimeType)
                .orElse(null);
    }

}
