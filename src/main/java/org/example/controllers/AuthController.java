package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exceptions.EmailPinNotFoundException;
import org.example.exceptions.InvalidPasswordException;
import org.example.exceptions.InvalidPinException;
import org.example.exceptions.UserUsernameAlreadyExistsException;
import org.example.pojo.*;
import org.example.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация", content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос", content = @Content(schema = @Schema(implementation = BadCredentialsException.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким именем уже существует", content = @Content(schema = @Schema(implementation = UserUsernameAlreadyExistsException.class)))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @Operation(summary = "Авторизация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная авторизация", content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные учетные данные", content = @Content(schema = @Schema(implementation = BadCredentialsException.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content(schema = @Schema(implementation = UsernameNotFoundException.class)))
    })
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @Operation(summary = "Отправка запроса на подтверждение почты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение отправлено"),
            @ApiResponse(responseCode = "400", description = "Неверный формат email")
    })
    @PostMapping("/email")
    public ResponseEntity<String> sendValidationMsgToEmail(@RequestParam String email) {
        authenticationService.sendValidationMsgToEmail(email);
        return ResponseEntity.ok(email);
    }

    @Operation(summary = "Подтверждение почты по пин коду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Почта подтверждена"),
            @ApiResponse(responseCode = "400", description = "Неверный пин-код или ошибка валидации", content = @Content(schema = @Schema(implementation = InvalidPinException.class))),
            @ApiResponse(responseCode = "404", description = "Email не найден", content = @Content(schema = @Schema(implementation = EmailPinNotFoundException.class)))
    })
    @DeleteMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestParam String email, @RequestParam Long pin) {
        authenticationService.validateEmail(email, pin);
        return ResponseEntity.ok(email);
    }

    @Operation(summary = "Проверка на наличие пользователя по почте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден или не найден"),
            @ApiResponse(responseCode = "400", description = "Неверный формат email")
    })
    @GetMapping("/check")
    public ResponseEntity<Boolean> doesUserAlreadyExist(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.doesUserAlreadyExist(email));
    }

    @Operation(summary = "Смена пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль изменен"),
            @ApiResponse(responseCode = "400", description = "Неверный старый или новый пароль", content = @Content(schema = @Schema(implementation = InvalidPasswordException.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content(schema = @Schema(implementation = UsernameNotFoundException.class)))
    })
    @PatchMapping("/password")
    public ResponseEntity<Long> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(authenticationService.changePassword(changePasswordRequest));
    }

    @Operation(summary = "Забыл пароль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сообщение отправлено"),
            @ApiResponse(responseCode = "400", description = "Неверный формат email")
    })
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authenticationService.forgotPassword(email);
        return ResponseEntity.ok(email);
    }

    @Operation(summary = "Восстановление пароля по пин-коду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль восстановлен"),
            @ApiResponse(responseCode = "400", description = "Неверный пин-код", content = @Content(schema = @Schema(implementation = InvalidPinException.class))),
            @ApiResponse(responseCode = "404", description = "Email не найден", content = @Content(schema = @Schema(implementation = EmailPinNotFoundException.class)))
    })
    @PatchMapping("/restore")
    public ResponseEntity<Long> changeForgottenPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return ResponseEntity.ok(authenticationService. changeForgottenPassword(forgotPasswordRequest));
    }
}
