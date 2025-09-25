package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.common.AuditingEntity;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "group_memberships")
public class GroupMembership extends AuditingEntity<GroupMembership> {

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GroupRole role = GroupRole.MEMBER;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public GroupMembership withRole(GroupRole role) {
        this.role = role;
        return this;
    }
}
