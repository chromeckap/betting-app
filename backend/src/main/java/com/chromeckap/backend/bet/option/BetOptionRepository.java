package com.chromeckap.backend.bet.option;

import com.chromeckap.backend.bet.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface BetOptionRepository extends JpaRepository<BetOption, Long> {
    Optional<BetOption> findByIdAndBet(Long correctOptionId, Bet bet);

    List<BetOption> findByBet(Bet bet);
}
