package com.chromeckap.backend.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByInviteCodeEquals(String inviteCode);
}
