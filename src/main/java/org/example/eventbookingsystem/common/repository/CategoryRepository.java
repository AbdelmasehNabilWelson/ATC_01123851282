package org.example.eventbookingsystem.common.repository;

import org.example.eventbookingsystem.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
