package com.chromeckap.backend.user;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.category.Category;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupMembership;
import com.chromeckap.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private static final int LAST_ACTIVE_INTERVAL = 5;

    private String username;

    private String email;

    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "createdBy")
    @Builder.Default
    private List<GroupMembership> groupMemberships = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    @Builder.Default
    private List<Bet> bets = new ArrayList<>();


    @OneToMany(mappedBy = "createdBy")
    @Builder.Default
    private List<Category> createdCategories = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    @Builder.Default
    private List<Group> createdGroups = new ArrayList<>();

    @Transient
    public boolean isUserOnline() {
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }

}