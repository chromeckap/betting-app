package com.chromeckap.backend.bet.option;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.exception.BetNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetOptionService {

    private static final String YES = "Yes";
    private static final String NO = "No";

    private final BetOptionRepository betOptionRepository;
    private final BetOptionMapper betOptionMapper;

    @Transactional(readOnly = true)
    public BetOption findOptionByIdAndBet(Long optionId, Bet bet) {
        return betOptionRepository.findByIdAndBet(optionId, bet)
                .orElseThrow(() -> new BetNotFoundException(
                        "Bet option with id %s was not found."
                                .formatted(optionId)
                ));
    }

    @Transactional
    public void manageBetOptions(Bet bet, List<BetOptionRequest> optionRequests) {
        log.debug("Managing bet options for bet {} of type {}", bet.getId(), bet.getType());

        switch (bet.getType()) {
            case YES_NO -> this.manageYesNoOptions(bet);
            case MULTIPLE_CHOICE -> this.manageMultipleChoiceOptions(bet, optionRequests);
            default -> throw new IllegalArgumentException("Unsupported bet type: " + bet.getType());
        }
    }

    private void manageYesNoOptions(Bet bet) {
        log.debug("Managing YES_NO options for bet {}", bet.getId());

        List<BetOption> existingOptions = betOptionRepository.findByBet(bet);

        boolean hasYes = existingOptions.stream()
                .anyMatch(opt -> YES.equalsIgnoreCase(opt.getName()));
        boolean hasNo = existingOptions.stream()
                .anyMatch(opt -> NO.equalsIgnoreCase(opt.getName()));

        List<BetOption> createdOptions = Stream.of(
                        hasYes ? null : YES,
                        hasNo ? null : NO
                )
                .filter(Objects::nonNull)
                .map(text -> new BetOption(text, bet))
                .toList();

        List<BetOption> invalidOptions = existingOptions.stream()
                .filter(opt -> !YES.equalsIgnoreCase(opt.getName()) &&
                        !NO.equalsIgnoreCase(opt.getName()))
                .toList();

        if (!invalidOptions.isEmpty()) {
            betOptionRepository.deleteAll(invalidOptions);
            log.debug("Deleted {} invalid options from YES_NO bet", invalidOptions.size());
        }

        if (!createdOptions.isEmpty()) {
            betOptionRepository.saveAll(createdOptions);
            log.debug("Created {} missing YES_NO options", createdOptions.size());
        } else {
            log.debug("All YES_NO options already exist, no new options created");
        }

        log.info("Successfully synchronized YES_NO options for bet {}", bet.getId());
    }

    private void manageMultipleChoiceOptions(Bet bet, List<BetOptionRequest> optionRequests) {
        log.debug("Managing MULTIPLE_CHOICE options for bet {}", bet.getId());

        if (optionRequests == null || optionRequests.isEmpty()) {
            log.warn("No bet options provided for MULTIPLE_CHOICE bet {}", bet.getId());
            return;
        }

        List<BetOption> existing = betOptionRepository.findByBet(bet);
        Map<Long, BetOption> existingMap = existing.stream()
                .filter(opt -> opt.getId() != null)
                .collect(Collectors.toMap(BetOption::getId, Function.identity()));

        List<BetOption> created = this.getOptionsToCreate(bet, optionRequests);
        List<BetOption> updated = this.getOptionsToUpdate(bet, optionRequests, existingMap);
        List<BetOption> deleted = this.getOptionsToDelete(optionRequests, existing);

        if (!created.isEmpty()) betOptionRepository.saveAll(created);
        if (!updated.isEmpty()) betOptionRepository.saveAll(updated);
        if (!deleted.isEmpty()) betOptionRepository.deleteAll(deleted);

        log.info("MULTIPLE_CHOICE options for bet {} synchronized: {} created, {} updated, {} deleted",
                bet.getId(), created.size(), updated.size(), deleted.size());
    }

    private List<BetOption> getOptionsToCreate(Bet bet, List<BetOptionRequest> requests) {
        return requests.stream()
                .filter(r -> r.id() == null)
                .map(r -> betOptionMapper.toEntity(r)
                        .withBet(bet))
                .toList();
    }

    private List<BetOption> getOptionsToUpdate(Bet bet, List<BetOptionRequest> requests, Map<Long, BetOption> existingMap) {
        return requests.stream()
                .filter(r -> r.id() != null)
                .map(r -> {
                    BetOption existing = existingMap.get(r.id());
                    if (existing == null) {
                        throw new BetNotFoundException(
                                "Bet option with id %s not found for bet %s"
                                        .formatted(r.id(), bet.getId()));
                    }
                    return betOptionMapper.updateEntityAttributes(existing, r);
                })
                .toList();
    }

    private List<BetOption> getOptionsToDelete(List<BetOptionRequest> requests, List<BetOption> existing) {
        Set<Long> requestedIds = requests.stream()
                .map(BetOptionRequest::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return existing.stream()
                .filter(opt -> opt.getId() != null && !requestedIds.contains(opt.getId()))
                .toList();
    }
}

