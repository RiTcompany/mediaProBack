package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.*;
import org.example.services.impl.LessonServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final LessonServiceImpl lessonService;

    @Operation(summary = "Получить актуальную подписку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация успешно получена",
                    content = @Content(schema = @Schema(implementation = SubscriptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ResourceNotFoundException.class)))
    })
    @GetMapping()
    public SubscriptionResponse getCurrentSubscription() {
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

    @Operation(summary = "Указать купленную подписку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подписка успешно присвоена",
                    content = @Content(schema = @Schema(implementation = SubscriptionAddInfo.class))),
    })
    @PatchMapping("/add")
    public SubscriptionAddInfo addSubscription(@RequestBody SubscriptionAddRequest request) {
        return lessonService.addSubscription(request.getSubscription(), request.getDateTime());
    }

    @Operation(summary = "Добавить пользователю Телеграм Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Телеграм Id успешно присвоен",
                    content = @Content(schema = @Schema(implementation = Long.class))),
    })
    @PatchMapping("/tg")
    public Long addTgId(@RequestBody TgAddRequest request) throws NoPermissionException {
        return lessonService.addTgId(request);
    }

}
