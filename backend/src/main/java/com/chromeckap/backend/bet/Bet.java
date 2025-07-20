package com.chromeckap.backend.bet;

import com.chromeckap.backend.bet.option.BetOption;
import com.chromeckap.backend.category.Category;
import com.chromeckap.backend.common.AuditingEntity;
import jakarta.persistence.*;
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
@Table(name = "bets")
public class Bet extends AuditingEntity {

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Builder.Default
    private boolean resolved = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BetStatus status = BetStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private BetType type;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "bet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<BetOption> options = new ArrayList<>();

}
