package com.chromeckap.backend.bet.option;

import com.chromeckap.backend.bet.Bet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetOptionService {
    private final BetOptionRepository betOptionRepository;

    public void manageBetOptions(final Bet bet, final List<@Valid BetOptionRequest> options) {

    }

    public BetOption findBetOptionByIdAndBet(Long correctOptionId, Bet bet) {
        return null;
    }
}
