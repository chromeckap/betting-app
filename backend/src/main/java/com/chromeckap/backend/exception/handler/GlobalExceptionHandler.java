package com.chromeckap.backend.exception.handler;

import com.chromeckap.backend.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handle(MethodArgumentNotValidException e) {
        String message = messageSource.getMessage(
                "validation.failed",
                null,
                LocaleContextHolder.getLocale()
        );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);

        List<Map<String, String>> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    assert error.getDefaultMessage() != null;
                    return Map.of(
                            "field", error.getField(),
                            "message", error.getDefaultMessage()
                    );
                }).toList();

        problemDetail.setProperty("errors", fieldErrors);

        log.error("Validation failed: {}", fieldErrors, e);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(BetClosedException.class)
    public ProblemDetail handle(BetClosedException e) {
        String message = messageSource.getMessage(
                "bet.closed",
                new Object[]{e.getBetId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Bet is already closed: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(BetNotClosableException.class)
    public ProblemDetail handle(BetNotClosableException e) {
        String message = messageSource.getMessage(
                "bet.not.closable",
                new Object[]{e.getBetId(), e.getBetStatus()},
                LocaleContextHolder.getLocale()
        );
        log.error("Bet cannot be closed: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(BetNotFoundException.class)
    public ProblemDetail handle(BetNotFoundException e) {
        String message = messageSource.getMessage(
                "bet.not.found",
                new Object[]{e.getBetId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Bet was not found: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(BetNotResolvableException.class)
    public ProblemDetail handle(BetNotResolvableException e) {
        String message = messageSource.getMessage(
                "bet.not.resolvable",
                new Object[]{e.getBetId(), e.getBetStatus()},
                LocaleContextHolder.getLocale()
        );
        log.error("Bet cannot be resolved: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(BetOptionNotFoundException.class)
    public ProblemDetail handle(BetOptionNotFoundException e) {
        String message = messageSource.getMessage(
                "bet.option.not.found",
                new Object[]{e.getOptionId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Bet option was not found: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ProblemDetail handle(CategoryNotFoundException e) {
        String message = messageSource.getMessage(
                "category.not.found",
                new Object[]{e.getCategoryId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Category was not found: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(GroupMembershipNotFoundException.class)
    public ProblemDetail handle(GroupMembershipNotFoundException e) {
        String message = messageSource.getMessage(
                "group.membership.not.found",
                new Object[]{e.getGroupId(), e.getUserId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Group membership was not found: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ProblemDetail handle(GroupNotFoundException e) {
        String message;

        if (e.getGroupId() != null) {
            message = messageSource.getMessage(
                    "group.not.found.by.id",
                    new Object[]{e.getGroupId()},
                    LocaleContextHolder.getLocale()
            );
        } else {
            message = messageSource.getMessage(
                    "group.not.found.by.code",
                    new Object[]{e.getInviteCode()},
                    LocaleContextHolder.getLocale()
            );
        }
        log.error("Group was not found: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(LastAdminDemotionException.class)
    public ProblemDetail handle(LastAdminDemotionException e) {
        String message = messageSource.getMessage(
                "group.admin.demote.last",
                new Object[]{e.getGroupId(), e.getUserId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Last admin of the group cannot be demoted: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(LastAdminRemovalException.class)
    public ProblemDetail handle(LastAdminRemovalException e) {
        String message = messageSource.getMessage(
                "group.admin.remove.last",
                new Object[]{e.getGroupId(), e.getUserId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Last admin of the group cannot be removed from the group: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(LastMemberRemovalException.class)
    public ProblemDetail handle(LastMemberRemovalException e) {
        String message = messageSource.getMessage(
                "group.member.remove.last",
                new Object[]{e.getGroupId(), e.getUserId()},
                LocaleContextHolder.getLocale()
        );
        log.error("Last member of the group cannot be removed from the group: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ProblemDetail handle(UserNotAuthenticatedException e) {
        String message = messageSource.getMessage(
                "user.not.authenticated",
                null,
                LocaleContextHolder.getLocale()
        );
        log.error("User is not authenticated: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message);
    }

}
