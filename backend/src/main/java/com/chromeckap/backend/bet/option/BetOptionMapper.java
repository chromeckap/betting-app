package com.chromeckap.backend.bet.option;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class BetOptionMapper {

    public BetOption toEntity(BetOptionRequest request) {
        return BetOption.builder()
                .name(request.name())
                .build();
    }

    public BetOption updateEntityAttributes(BetOption betOption, @Valid BetOptionRequest request) {
        return betOption
                .withName(request.name());
    }
}
