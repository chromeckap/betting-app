package com.chromeckap.backend.bet.participation;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.bet.option.BetOption;
import com.chromeckap.backend.common.AuditingEntity;
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
@Table(name = "bet_participations")
public class BetParticipation extends AuditingEntity {

    private boolean isCorrect = false;

    @ManyToOne
    @JoinColumn(name = "bet_id")
    private Bet bet;

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = false)
    private BetOption selectedOption;
}
