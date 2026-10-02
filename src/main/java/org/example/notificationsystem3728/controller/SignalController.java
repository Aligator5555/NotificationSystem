package org.example.notificationsystem3728.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.example.notificationsystem3728.service.SignalService;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class SignalController {

    private final SignalService signalService;
    // Объявляем базовый путь как поле класса, чтобы использовать в downloadSignal
    private final String soundsBasePath;

    public SignalController(SignalService signalService,
                            @Value("${app.sounds.path}") String soundsDirPath) {
        this.signalService = signalService;
        this.soundsBasePath = soundsDirPath;
    }

    @GetMapping("/saveSignal")
    public String showSaveForm(Model model) {
        return "saveSignal";
    }

    @PostMapping("/signals")
    public String uploadSignal(
            @RequestParam("nameSignal") String nameSignal,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        try {
            signalService.saveSignal(nameSignal, file);
            redirectAttributes.addFlashAttribute("successMessage", "Сигнал успешно загружен.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при сохранении файла: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Произошла непредвиденная ошибка.");
            e.printStackTrace();
        }
        return "redirect:/list";
    }

    @GetMapping("/deleteSignal/{id}")
    public String deleteSignal(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            signalService.deleteSignal(id);
            redirectAttributes.addFlashAttribute("successMessage", "Сигнал успешно удалён.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Не удалось удалить сигнал: на него есть активные привязки (тревоги). Сначала отвяжите его."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Произошла ошибка при удалении.");
            e.printStackTrace();
        }
        return "redirect:/list";
    }

    // Исправленный метод скачивания
    @GetMapping("/signals/download/{fileName}")
    public ResponseEntity<Resource> downloadSignal(@PathVariable String fileName) throws IOException {
        // Формируем полный путь на основе soundsBasePath из конструктора
        Path filePath = Paths.get(soundsBasePath, fileName);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(filePath.toFile());

        // Получаем MIME-тип из БД по имени файла (для этого нужен метод в сервисе)
        String mimeType = signalService.getMimeTypeByFileName(fileName);

        if (mimeType == null || mimeType.isBlank()) {
            mimeType = "audio/wav"; // запасной вариант
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .contentLength(Files.size(filePath))
                .body(resource);
    }
}
