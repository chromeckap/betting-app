package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Long> {
    boolean existsByGroupAndCreatedBy(Group group, String userId);
    Optional<GroupMembership> findByGroupIdAndCreatedBy(Long groupId, String userId);
    List<GroupMembership> findByGroupId(Long groupId);
}
