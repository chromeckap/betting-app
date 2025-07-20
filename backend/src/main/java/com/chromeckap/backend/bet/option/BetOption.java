package com.chromeckap.backend.bet.option;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bet_options")
public class BetOption extends BaseEntity {
    private String name;

    @ManyToOne
    @JoinColumn(name = "bet_id")
    private Bet bet;
}
