package com.chromeckap.backend.bet.option;

import com.chromeckap.backend.bet.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface BetOptionRepository extends JpaRepository<BetOption, Long> {

    /**
     * Finds a bet option by its ID and the associated bet.
     *
     * @param id the ID of the bet option
     * @param bet the {@link Bet} entity the option belongs to
     * @return an Optional containing the found bet option, or empty if not found
     */
    @Query("""
           SELECT o
           FROM BetOption o
           WHERE o.id = :id
           AND o.bet = :bet
           """)
    Optional<BetOption> findByIdAndBet(@Param("id") Long id, @Param("bet") Bet bet);

    /**
     * Finds all bet options belonging to a given bet.
     *
     * @param bet the {@link Bet} entity whose options are to be retrieved
     * @return a list of {@link BetOption} objects associated with the bet
     */
    @Query("""
           SELECT o
           FROM BetOption o
           WHERE o.bet = :bet
           """)
    List<BetOption> findAllByBet(@Param("bet") Bet bet);
}
