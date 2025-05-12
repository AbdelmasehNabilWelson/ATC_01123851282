package org.example.eventbookingsystem.controller;

import jakarta.validation.Valid;
import org.example.eventbookingsystem.Service.CategoryService;
import org.example.eventbookingsystem.dto.CategoryRequestDTO;
import org.example.eventbookingsystem.dto.CategoryResponseDTO;
import org.example.eventbookingsystem.dto.UpdateCategoryRequestDTO;
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
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequestDTO));
    }

    @PostMapping("/patch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponseDTO>> patchCreate(@Valid @RequestBody List<CategoryRequestDTO> categoryRequestDTOList) {
        return ResponseEntity.ok(categoryService.patchCreate(categoryRequestDTOList));
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
