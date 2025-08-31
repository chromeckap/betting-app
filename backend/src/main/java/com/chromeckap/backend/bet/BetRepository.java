package com.chromeckap.backend.bet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BetRepository extends JpaRepository<Bet, Long> {
}
