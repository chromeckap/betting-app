package com.chromeckap.backend.bet.participation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/categories/{categoryId}/bets")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BetParticipationController {
    private final BetParticipationService betParticipationService;

    @PostMapping("/{betId}/participate")
    public ResponseEntity<Long> participateInBet(
            @PathVariable @NotNull(message = "{group.id.required}") final Long groupId,
            @PathVariable @NotNull(message = "{category.id.required}") final Long categoryId,
            @PathVariable @NotNull(message = "{bet.id.required}") final Long betId,
            @RequestBody @Valid final BetParticipationRequest request
    ) {
        log.info("User participating in bet with id {} in category {} and group {}", betId, categoryId, groupId);
        Long response = betParticipationService.participateInBet(groupId, categoryId, betId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{betId}/participation")
    public ResponseEntity<Void> cancelParticipationInBet(
            @PathVariable @NotNull(message = "{group.id.required}") final Long groupId,
            @PathVariable @NotNull(message = "{category.id.required}") final Long categoryId,
            @PathVariable @NotNull(message = "{bet.id.required}") final Long betId
    ) {
        log.info("User requests cancellation of participation in bet {} for category {} and group {}", betId, categoryId, groupId);
        betParticipationService.cancelParticipationInBet(groupId, categoryId, betId);
        return ResponseEntity.noContent().build();
    }

}
