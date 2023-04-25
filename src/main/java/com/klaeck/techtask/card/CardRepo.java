package com.klaeck.techtask.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface CardRepo extends JpaRepository<Card, Integer> {
    @Modifying
    @Transactional
    @Query(value = "delete from card_to_category where card_id = ?1", nativeQuery = true)
    void deleteAllCardCategories(int id);
}
