package com.chromeckap.backend.bet.option;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "bet_options")
public class BetOption extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "bet_id")
    private Bet bet;

    public BetOption withName(String name) {
        this.name = name;
        return this;
    }

    public BetOption withBet(Bet bet) {
        this.bet = bet;
        return this;
    }
}
