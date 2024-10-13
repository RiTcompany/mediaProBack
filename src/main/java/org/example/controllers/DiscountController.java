package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.CollectStarsInfo;
import org.example.services.impl.LessonServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discount")
public class DiscountController {

    private final LessonServiceImpl lessonService;

    @Operation(summary = "Увеличить стрик уроков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Стрик успешно обновлен",
                    content = @Content(schema = @Schema(implementation = CollectStarsInfo.class))),
            @ApiResponse(responseCode = "404", description = "Урок не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @PostMapping("/{id}/streak")
    public CollectStarsInfo addLessonToStreak(@PathVariable Long id, @RequestParam LocalDateTime dateTime) {
        return lessonService.addLessonToStreak(id, dateTime);
    }

    @Operation(summary = "Получить актуальную информацию по стрику и звездам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация успешно получена",
                    content = @Content(schema = @Schema(implementation = CollectStarsInfo.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @GetMapping("/streak")
    public CollectStarsInfo getStreakAndStarsInfo() {
        return lessonService.getStreakAndStarsInfo();
    }
}
