package com.chromeckap.backend.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Finds a group by its invite code.
     *
     * @param inviteCode the invite code of the group
     * @return an Optional containing the found group, or empty if not found
     */
    @Query("""
           SELECT g
           FROM Group g
           WHERE g.inviteCode = :inviteCode
           """)
    Optional<Group> findByInviteCode(@Param("inviteCode") String inviteCode);

    /**
     * Checks if a group exists with the given invite code.
     *
     * @param inviteCode the invite code to check for existence
     * @return true if a group exists with the given invite code, false otherwise
     */
    @Query("""
           SELECT COUNT(g) > 0
           FROM Group g
           WHERE g.inviteCode = :inviteCode
           """)
    boolean existsByInviteCode(@Param("inviteCode") String inviteCode);
}
