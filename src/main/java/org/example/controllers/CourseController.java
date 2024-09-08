package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.entities.Course;
import org.example.entities.Test;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.CourseDto;
import org.example.pojo.FavouritesDto;
import org.example.pojo.TestDto;
import org.example.services.impl.CourseServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseServiceImpl courseService;

    @Operation(summary = "Получить список всех курсов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список курсов успешно получен",
                    content = @Content(schema = @Schema(implementation = CourseDto.class)))
    })
    @GetMapping
    public List<CourseDto> getCourses() {
        return courseService.getAllCourses();
    }

    @Operation(summary = "Установить/снять флаг избранного для курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Флаг избранного успешно обновлен",
                    content = @Content(schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @PostMapping("/{id}/favourite")
    public Course setFavourite(@PathVariable Long id, @RequestParam boolean isFavourite) {
        return courseService.setFavourite(id, isFavourite);
    }

    @Operation(summary = "Установить/снять флаг завершенности курса")
    @Deprecated
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Флаг завершенности успешно обновлен",
                    content = @Content(schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @PostMapping("/{id}/complete")
    public Course setComplete(@PathVariable Long id, @RequestParam boolean isComplete) {
        return courseService.setComplete(id, isComplete);
    }


    @Operation(summary = "Получить итоговый тест курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Итоговый тест успешно получен",
                    content = @Content(schema = @Schema(implementation = Test.class)))
    })
    @GetMapping("/{id}/test")
    public TestDto getCourseTest(@PathVariable Long id) {
        return courseService.getCourseTest(id);
    }


    @Operation(summary = "Получить список избранного")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список избранного успешно получен",
                    content = @Content(schema = @Schema(implementation = FavouritesDto.class)))
    })
    @GetMapping("/favourites")
    public FavouritesDto getFavourites() {
        return courseService.getFavourites();
    }

}
