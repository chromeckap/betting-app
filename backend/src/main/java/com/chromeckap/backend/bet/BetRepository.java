package com.chromeckap.backend.bet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByCategoryId(Long categoryId);
    Bet findBetByCategoryId(Long categoryId);
    Optional<Bet> findByIdAndCategoryId(Long id, Long categoryId);
}
