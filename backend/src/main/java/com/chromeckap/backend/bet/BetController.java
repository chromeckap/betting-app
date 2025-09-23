package com.chromeckap.backend.bet;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/categories/{categoryId}/bets")
@RequiredArgsConstructor
@Slf4j
public class BetController {
    private final BetService betService;

    @GetMapping
    public ResponseEntity<List<BetResponse>> getBetsInCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            Authentication connectedUser
    ) {
        log.info("Fetching bets in category {} of group {}", categoryId, groupId);
        List<BetResponse> responses = betService.getBetsInCategory(groupId, categoryId, connectedUser);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BetResponse> getBetById(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id,
            Authentication connectedUser
    ) {
        log.info("Fetching bet {} in category {} of group {}", id, categoryId, groupId);
        BetResponse response = betService.getBetById(groupId, categoryId, id, connectedUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Long> createBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @Valid @RequestBody final BetRequest request,
            Authentication connectedUser
    ) {
        log.info("Creating bet {} in category {} of group {}", request, categoryId, groupId);
        Long response = betService.createBet(groupId, categoryId, request, connectedUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id,
            @Valid @RequestBody final BetRequest request,
            Authentication connectedUser
    ) {
        log.info("Updating bet {} in category {} of group {} with body {}", id, categoryId, groupId, request);
        Long response = betService.updateBet(groupId, categoryId, id, request, connectedUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id,
            Authentication connectedUser
    ) {
        log.info("Closing bet {} in category {} of group {}", id, categoryId, groupId);
        betService.closeBet(groupId, categoryId, id, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/evaluate/{correctOptionId}")
    public ResponseEntity<Void> evaluateBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id,
            @PathVariable final Long correctOptionId,
            Authentication connectedUser
    ) {
        log.info("Evaluating bet {} in category {} of group {}", id, categoryId, groupId);
        betService.evaluateBet(groupId, categoryId, id, correctOptionId, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id,
            Authentication connectedUser
    ) {
        log.info("Deleting bet {} in category {} of group {}", id, categoryId, groupId);
        betService.deleteBet(groupId, categoryId, id, connectedUser);
        return ResponseEntity.noContent().build();
    }
}