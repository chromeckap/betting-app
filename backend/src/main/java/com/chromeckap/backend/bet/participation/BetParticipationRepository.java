package com.chromeckap.backend.bet.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BetParticipationRepository extends JpaRepository<BetParticipation, Long> {
    Optional<BetParticipation> findBySelectedOption_Bet_IdAndCreatedBy(Long betId, String createdBy);
}
