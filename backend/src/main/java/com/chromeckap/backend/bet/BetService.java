package com.chromeckap.backend.bet;

import com.chromeckap.backend.bet.option.BetOption;
import com.chromeckap.backend.bet.option.BetOptionService;
import com.chromeckap.backend.category.Category;
import com.chromeckap.backend.category.CategoryService;
import com.chromeckap.backend.exception.BetNotFoundException;
import com.chromeckap.backend.group.membership.GroupMembershipValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BetService {
    private final BetRepository betRepository;
    private final BetMapper betMapper;
    private final CategoryService categoryService;
    private final BetOptionService betOptionService;
    private final GroupMembershipValidator groupMembershipValidator;

    /**
     * Helper method for finding a bet by id and category id.
     *
     * @param id         bet id
     * @param categoryId category id
     * @return found {@link Bet}
     * @throws BetNotFoundException if bet is not found in the given category
     */
    @Transactional(readOnly = true)
    public Bet findBetByIdAndCategoryId(final Long id, final Long categoryId) {
        return betRepository.findByIdAndCategoryId(id, categoryId)
                .orElseThrow(() -> new BetNotFoundException(
                        String.format("Bet with id %s was not found in category %s.", id, categoryId)
                ));
    }

    /**
     * Retrieve all bets in a given category.
     *
     * @param groupId    group id
     * @param categoryId category id
     * @return list of {@link BetResponse}
     */
    @Transactional(readOnly = true)
    public List<BetResponse> getBetsInCategory(final Long groupId, final Long categoryId) { //todo replace list with page
        log.debug("Getting bets in category with id {}", categoryId);

        groupMembershipValidator.requireUserIsGroupMember(groupId);

        List<BetResponse> responses = betRepository.findByCategoryId(categoryId).stream()
                .map(betMapper::toResponse)
                .toList();

        log.info("Fetched {} bets in category with id {}", responses.size(), groupId);
        return responses;
    }

    /**
     * Retrieve a bet by id in a given category.
     *
     * @param groupId    group id
     * @param categoryId category id
     * @param id         bet id
     * @return {@link BetResponse} for the found bet
     */
    @Transactional(readOnly = true)
    public BetResponse getBetById(final Long groupId, final Long categoryId, final Long id) {
        log.debug("Getting bet with id {}", id);

        groupMembershipValidator.requireUserIsGroupMember(groupId);

        Bet bet = betRepository.findBetByCategoryId(categoryId);

        log.info("Fetched bet with id {}", id);
        return betMapper.toResponse(bet);
    }

    /**
     * Create a new bet inside a category.
     *
     * @param groupId    group id
     * @param categoryId category id
     * @param request    bet request
     * @return id of created bet
     */
    @Transactional
    public Long createBet(final Long groupId, final Long categoryId, @Valid final BetRequest request) {
        log.debug("Creating bet {} in category with id {}", request, categoryId);

        groupMembershipValidator.requireUserIsGroupAdmin(groupId);
        Category category = categoryService.findCategoryByIdAndGroupId(groupId, categoryId);

        Bet bet = betMapper.toEntity(request);
        bet.setCategory(category);

        Bet savedBet = betRepository.save(bet);
        betOptionService.manageBetOptions(savedBet, request.options());

        log.info("Successfully created bet with id {} in category {}", savedBet.getId(), categoryId);

        return savedBet.getId();
    }

    /**
     * Update an existing bet in a category.
     *
     * @param groupId    group id
     * @param categoryId category id
     * @param id         bet id
     * @param request    bet request
     * @return id of updated bet
     */
    @Transactional
    public Long updateBet(final Long groupId, final Long categoryId, final Long id, @Valid final BetRequest request) {
        log.debug("Updating bet with id {} in category {} using {}", id, categoryId, request);

        groupMembershipValidator.requireUserIsGroupAdmin(groupId);

        Bet bet = this.findBetByIdAndCategoryId(id, categoryId);
        bet = betMapper.updateEntityAttributes(bet, request);

        Bet savedBet = betRepository.save(bet);
        betOptionService.manageBetOptions(savedBet, request.options());

        log.info("Successfully updated bet with id {} in category {}", savedBet.getId(), categoryId);

        return savedBet.getId();
    }

    /**
     * Close a bet in a category (set its status to CLOSED).
     *
     * @param groupId    group id
     * @param categoryId category id
     * @param id         bet id
     */
    @Transactional
    public void closeBet(final Long groupId, final Long categoryId, final Long id) {
        log.debug("Closing bet {} in category with id {}", id, categoryId);

        groupMembershipValidator.requireUserIsGroupAdmin(groupId);

        Bet bet = this.findBetByIdAndCategoryId(id, categoryId);
        bet.setStatus(BetStatus.CLOSED);

        betRepository.save(bet);
        log.info("Successfully closed bet with id {} in category {}", bet.getId(), categoryId);
    }

    /**
     * Evaluate a bet in a category (set correct option and mark as resolved).
     *
     * @param groupId    group id
     * @param categoryId category id
     * @param id         bet id
     */
    @Transactional
    public void evaluateBet(final Long groupId, final Long categoryId, final Long id, final Long correctOptionId) {
        log.debug("Evaluating bet with id {} in category {}", id, categoryId);

        groupMembershipValidator.requireUserIsGroupAdmin(groupId);

        Bet bet = this.findBetByIdAndCategoryId(id, categoryId);
        BetOption correctOption = betOptionService.findBetOptionByIdAndBet(correctOptionId, bet);

        bet.setCorrectOption(correctOption);
        bet.setStatus(BetStatus.RESOLVED);
        bet.setResolved(true);

        betRepository.save(bet);
        log.info("Successfully resolved bet with id {} in category {}", bet.getId(), categoryId);
    }

    /**
     * Delete a bet from a category.
     *
     * @param groupId    group id
     * @param categoryId category id
     * @param id         bet id
     */
    @Transactional
    public void deleteBet(final Long groupId, final Long categoryId, final Long id) {
        log.debug("Deleting bet with id {} from category {}", id, categoryId);

        groupMembershipValidator.requireUserIsGroupAdmin(groupId);

        Bet bet = this.findBetByIdAndCategoryId(id, categoryId);
        betRepository.delete(bet);
        log.info("Successfully deleted bet with id {} from category {}", id, categoryId);
    }
}
