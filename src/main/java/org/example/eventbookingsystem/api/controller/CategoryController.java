package org.example.eventbookingsystem.api.controller;

import jakarta.validation.Valid;
import org.example.eventbookingsystem.common.Service.CategoryService;
import org.example.eventbookingsystem.api.dto.categoryCreateRequestDTO;
import org.example.eventbookingsystem.api.dto.CategoryResponseDTO;
import org.example.eventbookingsystem.api.dto.UpdateCategoryRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getALl());
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody categoryCreateRequestDTO categoryCreateRequestDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryCreateRequestDTO));
    }

    @PostMapping("/patch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponseDTO>> patchCreate(@Valid @RequestBody List<categoryCreateRequestDTO> categoryCreateRequestDTOList) {
        return ResponseEntity.ok(categoryService.patchCreate(categoryCreateRequestDTOList));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(categoryService.deleteCategory(id)); // no content return when category is deleted
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Valid @RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(updateCategoryRequestDTO));
    }
}
