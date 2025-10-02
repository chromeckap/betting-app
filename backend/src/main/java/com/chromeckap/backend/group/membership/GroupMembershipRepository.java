package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {

    /**
     * Checks if a user is a member of a given group.
     *
     * @param group  the {@link Group} entity
     * @param userId the user's unique identifier
     * @return true if the user is a member of the group, false otherwise
     */
    @Query("""
           SELECT COUNT(gm) > 0
           FROM GroupMembership gm
           WHERE gm.group = :group
           AND gm.createdBy = :userId
           """)
    boolean existsByGroupAndCreatedBy(@Param("group") Group group, @Param("userId") String userId);

    /**
     * Checks if a user is a member of a group by the group's invite code.
     *
     * @param inviteCode the invite code of the group
     * @param userId     the user's unique identifier
     * @return true if the user is a member of the group with the given invite code, false otherwise
     */
    @Query("""
           SELECT COUNT(gm) > 0
           FROM GroupMembership gm
           WHERE gm.group.inviteCode = :inviteCode
           AND gm.createdBy = :userId
           """)
    boolean existsByGroupInviteCodeAndCreatedBy(@Param("inviteCode") String inviteCode, @Param("userId") String userId);

    /**
     * Finds a group membership by group ID and user ID.
     *
     * @param groupId the ID of the group
     * @param userId  the user's unique identifier
     * @return an {@link Optional} containing the membership if found
     */
    @Query("""
           SELECT gm
           FROM GroupMembership gm
           WHERE gm.group.id = :groupId
           AND gm.createdBy = :userId
           """)
    Optional<GroupMembership> findByGroupIdAndCreatedBy(@Param("groupId") Long groupId, @Param("userId") String userId);

    /**
     * Retrieves all memberships in a given group, ordered by creation date ascending.
     *
     * @param groupId the ID of the group
     * @return a list of {@link GroupMembership} for the specified group
     */
    @Query("""
           SELECT gm
           FROM GroupMembership gm
           WHERE gm.group.id = :groupId
           ORDER BY gm.role ASC
           """)
    List<GroupMembership> findAllByGroupId(@Param("groupId") Long groupId);

    /**
     * Retrieves all memberships for a given user.
     *
     * @param userId the user's unique identifier
     * @return a list of {@link GroupMembership} entities for the specified user
     */
    @Query("""
           SELECT gm
           FROM GroupMembership gm
           WHERE gm.createdBy = :userId
           ORDER BY gm.createdAt ASC
           """)
    List<GroupMembership> findAllByCreatedBy(@Param("userId") String userId);
}
