package com.chromeckap.backend.bet.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BetParticipationRepository extends JpaRepository<BetParticipation, Long> {

    /**
     * Finds a bet participation for a given bet and user.
     *
     * @param betId     the ID of the bet
     * @param createdBy  the username of the user who participated
     * @return an Optional containing the found participation, or empty if not found
     */
    @Query("""
           SELECT p
           FROM BetParticipation p
           WHERE p.selectedOption.bet.id = :betId
           AND p.createdBy = :createdBy
           """)
    Optional<BetParticipation> findByBetIdAndCreatedBy(
            @Param("betId") Long betId,
            @Param("createdBy") String createdBy
    );

    /**
     * Checks whether a participation already exists for the given user and bet.
     * <p>
     * Uses JPQL to count matching participations.
     *
     * @param betId     the ID of the bet
     * @param userId the username (or ID) of the user
     * @return {@code true} if the user already participates in the bet, {@code false} otherwise
     */
    @Query("""
           SELECT COUNT(p) > 0
           FROM BetParticipation p
           WHERE p.selectedOption.bet.id = :betId
           AND p.createdBy = :createdBy
           """)
    boolean existsByBetIdAndCreatedBy(
            @Param("betId") Long betId,
            @Param("createdBy") String createdBy
    );
}
