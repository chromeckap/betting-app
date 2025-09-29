package com.chromeckap.backend.bet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class BetExpirationScheduler {
    private final BetRepository betRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    public void closeExpiredBets() {
        int closedBetsNum = betRepository.closeExpiredBets(LocalDateTime.now());

        if (closedBetsNum > 0) {
            log.info("Automatically closed {} expired bets", closedBetsNum);
        }
    }
}
