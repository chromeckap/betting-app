package com.chromeckap.backend.category;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.common.AuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "categories")
public class Category extends AuditingEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    @OrderBy("createdAt asc")
    private List<Bet> bets = new ArrayList<>();

}
