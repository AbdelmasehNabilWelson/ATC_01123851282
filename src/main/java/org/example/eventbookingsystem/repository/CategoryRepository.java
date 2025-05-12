package org.example.eventbookingsystem.repository;

import org.example.eventbookingsystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
