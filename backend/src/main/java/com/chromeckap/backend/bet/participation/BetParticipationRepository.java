package com.chromeckap.backend.bet.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BetParticipationRepository extends JpaRepository<BetParticipation, Long> {
}
