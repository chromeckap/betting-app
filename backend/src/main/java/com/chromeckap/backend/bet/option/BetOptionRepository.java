package com.chromeckap.backend.bet.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BetOptionRepository extends JpaRepository<BetOption, Long> {
}
