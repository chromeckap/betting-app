package com.chromeckap.backend.bet;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
            @PathVariable final Long categoryId
    ) {
        log.info("Fetching bets in category {} of group {}", categoryId, groupId);
        List<BetResponse> responses = betService.getBetsInCategory(groupId, categoryId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BetResponse> getBetById(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id
    ) {
        log.info("Fetching bet {} in category {} of group {}", id, categoryId, groupId);
        BetResponse response = betService.getBetById(groupId, categoryId, id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Long> createBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @Valid @RequestBody final BetRequest request
    ) {
        log.info("Creating bet {} in category {} of group {}", request, categoryId, groupId);
        Long response = betService.createBet(groupId, categoryId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id,
            @Valid @RequestBody final BetRequest request
    ) {
        log.info("Updating bet {} in category {} of group {} with body {}", id, categoryId, groupId, request);
        Long response = betService.updateBet(groupId, categoryId, id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id
    ) {
        log.info("Closing bet {} in category {} of group {}", id, categoryId, groupId);
        betService.closeBet(groupId, categoryId, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/evaluate/{correctOptionId}")
    public ResponseEntity<Void> evaluateBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id,
            @PathVariable final Long correctOptionId
    ) {
        log.info("Evaluating bet {} in category {} of group {}", id, categoryId, groupId);
        betService.evaluateBet(groupId, categoryId, id, correctOptionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long id
    ) {
        log.info("Deleting bet {} in category {} of group {}", id, categoryId, groupId);
        betService.deleteBet(groupId, categoryId, id);
        return ResponseEntity.noContent().build();
    }
}