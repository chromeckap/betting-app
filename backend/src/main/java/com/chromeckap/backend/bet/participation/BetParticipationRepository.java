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
}
