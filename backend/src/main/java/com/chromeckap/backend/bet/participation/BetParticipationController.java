package com.chromeckap.backend.bet.participation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/{groupId}/categories/{categoryId}/bets")
@RequiredArgsConstructor
@Slf4j
public class BetParticipationController {

    @GetMapping("/{betId}/results")
    public ResponseEntity<BetParticipationResponse> getUsersBetResults(
            @PathVariable int groupId,
            @PathVariable int categoryId,
            @PathVariable int betId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{betId}/participate")
    public ResponseEntity<Void> participateInBet(
            @PathVariable long groupId,
            @PathVariable long categoryId,
            @PathVariable final Long betId
    ) {
        return ResponseEntity.ok().build();
    }

}
