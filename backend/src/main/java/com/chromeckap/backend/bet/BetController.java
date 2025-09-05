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

    @GetMapping
    public ResponseEntity<List<BetResponse>> getBetsInCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{betId}")
    public ResponseEntity<BetResponse> getBetById(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long betId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @Valid @RequestBody final BetRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{betId}")
    public ResponseEntity<Void> updateBet(
            @PathVariable final Long groupId,
            @PathVariable final Long betId,
            @Valid @RequestBody final BetRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{betId}/close")
    public ResponseEntity<Void> closeBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long betId
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{betId}/evaluate")
    public ResponseEntity<Void> evaluateBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long betId
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{betId}")
    public ResponseEntity<Void> deleteBet(
            @PathVariable final Long groupId,
            @PathVariable final Long categoryId,
            @PathVariable final Long betId
    ) {
        return ResponseEntity.ok().build();
    }

}