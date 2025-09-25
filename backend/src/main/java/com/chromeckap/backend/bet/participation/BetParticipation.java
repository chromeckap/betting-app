package com.chromeckap.backend.bet.participation;

import com.chromeckap.backend.bet.option.BetOption;
import com.chromeckap.backend.common.AuditingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "bet_participations")
public class BetParticipation extends AuditingEntity<BetParticipation> {

    @Builder.Default
    private boolean isCorrect = false;

    @ManyToOne
    @JoinColumn(name = "selected_option_id", nullable = false)
    private BetOption selectedOption;

    public BetParticipation withSelectedOption(BetOption selectedOption) {
        this.selectedOption = selectedOption;
        return this;
    }
}
