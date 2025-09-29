package com.chromeckap.backend.bet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByCategoryId(Long categoryId);
    Bet findBetByCategoryId(Long categoryId);
    Page<Bet> findBetByCategoryId(Long categoryId, Pageable pageable);
    Optional<Bet> findByIdAndCategoryId(Long id, Long categoryId);

    @Modifying
    @Query(value = """
        UPDATE Bet b
        SET b.status = 'CLOSED', b.updatedAt = :currentTime
        WHERE b.status = 'OPEN'
        AND b.deadline < :currentTime
        """)
    int closeExpiredBets(@Param("currentTime") LocalDateTime currentTime);
}
