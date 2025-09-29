package com.chromeckap.backend.bet.participation;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.bet.BetService;
import com.chromeckap.backend.bet.option.BetOption;
import com.chromeckap.backend.bet.option.BetOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetParticipationService {
    private final BetParticipationRepository betParticipationRepository;
    private final BetParticipationMapper betParticipationMapper;
    private final BetParticipationValidator betParticipationValidator;
    private final BetOptionService betOptionService;
    private final BetService betService;

    /**
     * Allows a user to participate in a bet by selecting a specific option.
     * <p>
     * The method performs the following steps:
     * <ul>
     *     <li>Ensures that the user is a member of the given group.</li>
     *     <li>Fetches the selected bet option by its id.</li>
     *     <li>Validates that the option belongs to the given bet.</li>
     *     <li>Creates a new {@link BetParticipation} entity linked to the user and the option.</li>
     *     <li>Persists the participation in the repository.</li>
     * </ul>
     *
     * @param groupId   the id of the group where the bet resides
     * @param categoryId the id of the category to which the bet belongs to
     * @param betId     the id of the bet to participate in
     * @param request   the participation request containing the selected option id
     * @return the id of the newly created bet participation
     * @throws IllegalArgumentException if the selected option does not belong to the given bet
     */
    @Transactional
    @PreAuthorize("@groupMembershipPermission.isGroupMember(#groupId)")
    public Long participateInBet(final Long groupId, final Long categoryId,final Long betId, final BetParticipationRequest request) {
        log.debug("User participating in bet with id {} in category {} and group {}", betId, categoryId, groupId);

        Bet bet = betService.findBetByIdAndCategoryId(betId, categoryId);
        betParticipationValidator.validateBetOpen(bet);

        BetOption selectedOption = betOptionService.findOptionByIdAndBet(request.optionId(), bet);

        BetParticipation betParticipation = betParticipationMapper.toEntity(request)
                .withSelectedOption(selectedOption)
                .withCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        BetParticipation savedBetParticipation = betParticipationRepository.save(betParticipation);
        log.info("Successfully created bet participation {}", savedBetParticipation);

        return  savedBetParticipation.getId();
    }

    /**
     * Allows a user to cancel their participation in a bet.
     * <p>
     * The method performs the following steps:
     * <ul>
     *     <li>Ensures that the user is a member of the given group.</li>
     *     <li>Fetches the bet by its id and category id.</li>
     *     <li>Validates that the bet is still open and not resolved.</li>
     *     <li>Finds the user's existing participation in the bet.</li>
     *     <li>Deletes the participation from the repository.</li>
     * </ul>
     *
     * @param groupId    the id of the group where the bet resides
     * @param categoryId the id of the category to which the bet belongs
     * @param betId      the id of the bet from which the user wants to cancel participation
     * @throws IllegalArgumentException if the user has no participation in the bet
     */
    @Transactional
    @PreAuthorize("@groupMembershipPermission.isGroupMember(#groupId)")
    public void cancelParticipationInBet(final Long groupId, final Long categoryId, final Long betId) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("User={} requests cancellation of participation in bet={} in group {}", currentUserId, betId, groupId);

        Bet bet = betService.findBetByIdAndCategoryId(betId, categoryId);
        betParticipationValidator.validateBetOpen(bet);

        BetParticipation participation = betParticipationRepository.findByBetIdAndCreatedBy(betId, currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("No participation found for user " + currentUserId + " in bet " + betId));

        betParticipationRepository.delete(participation);

        log.info("User={} successfully canceled participation in bet={}", currentUserId, betId);
    }

}
