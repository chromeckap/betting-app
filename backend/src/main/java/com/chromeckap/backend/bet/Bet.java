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
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "bets")
public class Bet extends AuditingEntity<Bet> {

    //todo er-diagram has title, change attribute to name
    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Builder.Default
    private boolean isResolved = false;

    @ManyToOne
    @JoinColumn(name = "correct_option_id")
    private BetOption correctOption;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BetStatus status = BetStatus.OPEN;

    @Enumerated(EnumType.STRING)
    private BetType type;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "bet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<BetOption> options = new ArrayList<>();

    public Bet withName(String name) {
        this.name = name;
        return this;
    }
    public Bet withDescription(String description) {
        this.description = description;
        return this;
    }

    public Bet withDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
        return this;
    }

    public Bet withCategory(Category category) {
        this.category = category;
        return this;
    }

    public Bet withCorrectOption(BetOption option) {
        this.correctOption = option;
        return this;
    }

    public Bet withStatus(BetStatus status) {
        this.status = status;
        return this;
    }

    public Bet withResolved(boolean isResolved) {
        this.isResolved = isResolved;
        return this;
    }

}
