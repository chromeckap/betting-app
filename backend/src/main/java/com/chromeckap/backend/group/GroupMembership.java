package com.chromeckap.backend.group;

import com.chromeckap.backend.common.AuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "group_memberships")
public class GroupMembership extends AuditingEntity {

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GroupRole role = GroupRole.MEMBER;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

}
