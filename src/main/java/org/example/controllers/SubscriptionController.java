package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.SubscriptionDto;
import org.example.pojo.SubscriptionsInfo;
import org.example.services.impl.LessonServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final LessonServiceImpl lessonService;

    @Operation(summary = "Получить актуальную подписку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация успешно получена",
                    content = @Content(schema = @Schema(implementation = SubscriptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @GetMapping()
    public SubscriptionDto getCurrentSubscription() {
        return lessonService.getCurrentSubscription();
    }

    @Operation(summary = "Получить список подписок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация успешно получена",
                    content = @Content(schema = @Schema(implementation = SubscriptionsInfo.class))),
    })
    @GetMapping("/all")
    public SubscriptionsInfo getAllSubscriptions() {
        return lessonService.getAllSubscriptions();
    }

}
