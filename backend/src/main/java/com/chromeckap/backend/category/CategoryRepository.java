package com.chromeckap.backend.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByGroupId(Long groupId);

    Optional<Category> findByIdAndGroupId(Long id, Long groupId);
}
