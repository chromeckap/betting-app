package com.chromeckap.backend.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByInviteCodeEquals(String inviteCode);

    boolean existsByInviteCode(String inviteCode);
}
