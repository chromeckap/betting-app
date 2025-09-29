package com.chromeckap.backend.bet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
interface BetRepository extends JpaRepository<Bet, Long> {

    /**
     * Retrieves all bets within a given category, paginated.
     *
     * @param categoryId the ID of the category to search bets in
     * @param pageable   pagination and sorting information
     * @return a page of {@link Bet} objects belonging to the category
     */
    @Query("""
           SELECT b
           FROM Bet b
           WHERE b.category.id = :categoryId
           """)
    Page<Bet> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * Retrieves a bet by its ID and the ID of its category.
     *
     * @param id         the ID of the bet
     * @param categoryId the ID of the category the bet belongs to
     * @return an Optional containing the found bet, or empty if not found
     */
    @Query("""
           SELECT b
           FROM Bet b
           WHERE b.id = :id
           AND b.category.id = :categoryId
           """)
    Optional<Bet> findByIdAndCategoryId(@Param("id") Long id, @Param("categoryId") Long categoryId);

    /**
     * Closes all bets that are still OPEN and whose deadline has passed.
     * Updates the status to {@code CLOSED} and sets the last updated time.
     *
     * @param currentTime the current time used to check the deadline
     * @return the number of bets that were closed
     */
    @Modifying
    @Query(value = """
        UPDATE Bet b
        SET b.status = 'CLOSED', b.updatedAt = :currentTime
        WHERE b.status = 'OPEN'
        AND b.deadline < :currentTime
        """)
    int closeExpiredBets(@Param("currentTime") LocalDateTime currentTime);
}