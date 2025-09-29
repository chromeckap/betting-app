package com.chromeckap.backend.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Retrieves all categories belonging to a given group.
     *
     * @param groupId the ID of the group
     * @return a list of {@link Category} objects belonging to the specified group
     */
    @Query("""
           SELECT c
           FROM Category c
           WHERE c.group.id = :groupId
           """)
    List<Category> findAllByGroupId(@Param("groupId") Long groupId);

    /**
     * Finds a category by its ID and the ID of its group.
     *
     * @param id      the ID of the category
     * @param groupId the ID of the group the category belongs to
     * @return an Optional containing the found category, or empty if not found
     */
    @Query("""
           SELECT c
           FROM Category c
           WHERE c.id = :id
           AND c.group.id = :groupId
           """)
    Optional<Category> findByIdAndGroupId(@Param("id") Long id, @Param("groupId") Long groupId);
}
