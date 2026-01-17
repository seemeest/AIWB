package com.aiwb.marketplace.adapters.auth;

import com.aiwb.marketplace.application.auth.AuthException;
import com.aiwb.marketplace.application.moderation.ModerationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthException ex) {
        HttpStatus status = switch (ex.getError()) {
            case EMAIL_ALREADY_USED -> HttpStatus.CONFLICT;
            case INVALID_CREDENTIALS, INVALID_REFRESH_TOKEN, INVALID_ACCESS_TOKEN -> HttpStatus.UNAUTHORIZED;
            case EMAIL_NOT_VERIFIED, USER_BLOCKED -> HttpStatus.FORBIDDEN;
            case USER_NOT_FOUND, VERIFICATION_TOKEN_INVALID, PASSWORD_RESET_TOKEN_INVALID -> HttpStatus.NOT_FOUND;
            case TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED;
        };
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getError().name(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR", "Invalid request"));
    }

    @ExceptionHandler(ModerationException.class)
    public ResponseEntity<ErrorResponse> handleModeration(ModerationException ex) {
        HttpStatus status = switch (ex.getError()) {
            case APPEAL_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case APPEAL_WINDOW_EXPIRED, APPEAL_ALREADY_EXISTS, COMPLAINT_NOT_FOUND -> HttpStatus.BAD_REQUEST;
        };
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getError().name(), ex.getMessage()));
    }
}
