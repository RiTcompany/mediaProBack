package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.entities.Lesson;
import org.example.entities.Test;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.TestDto;
import org.example.services.impl.LessonServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonServiceImpl lessonService;

    @Operation(summary = "Получить информацию об уроке по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация об уроке успешно получена",
                    content = @Content(schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "404", description = "Урок не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @GetMapping("/{id}")
    public Lesson getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    @Operation(summary = "Установить/снять флаг избранного для урока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Флаг избранного успешно обновлен",
                    content = @Content(schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "404", description = "Урок не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @PostMapping("/{id}/favourite")
    public Lesson setFavourite(@PathVariable Long id, @RequestParam boolean isFavourite) {
        return lessonService.setFavourite(id, isFavourite);
    }

    @Operation(summary = "Установить/снять флаг завершенности урока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Флаг завершенности успешно обновлен",
                    content = @Content(schema = @Schema(implementation = Lesson.class))),
            @ApiResponse(responseCode = "404", description = "Урок не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @PostMapping("/{id}/complete")
    public Lesson setComplete(@PathVariable Long id, @RequestParam boolean isComplete) {
        return lessonService.setComplete(id, isComplete);
    }

    @Operation(summary = "Получить практическое задание к уроку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Практическое задание к уроку успешно получено",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}/practice")
    public String getPracticeTask(@PathVariable Long id) {
        return lessonService.getPracticeTask(id);
    }
}
