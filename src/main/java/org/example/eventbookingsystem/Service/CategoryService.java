package org.example.eventbookingsystem.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.eventbookingsystem.dto.CategoryRequestDTO;
import org.example.eventbookingsystem.dto.CategoryResponseDTO;
import org.example.eventbookingsystem.dto.UpdateCategoryRequestDTO;
import org.example.eventbookingsystem.entity.Category;
import org.example.eventbookingsystem.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = convertDTOTOCategory(categoryRequestDTO);
        categoryRepository.save(category);

        return convertCategoryToDTO(category);
    }

    public List<CategoryResponseDTO> patchCreate(List<CategoryRequestDTO> categoryRequestDTOList) {
        List<CategoryResponseDTO> categoryResponseDTOS = new ArrayList<>();
        categoryRequestDTOList.forEach(categoryRequestDTO -> {
            categoryResponseDTOS.add(createCategory(categoryRequestDTO));
        });
        return categoryResponseDTOS;
    }


    public CategoryResponseDTO getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found"));

        return convertCategoryToDTO(category);
    }

    public List<CategoryResponseDTO> getALl() {
        return categoryRepository.findAll().stream()
                .map(this::convertCategoryToDTO)
                .toList();
    }

    public CategoryResponseDTO deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found"));
        CategoryResponseDTO categoryResponseDTO = convertCategoryToDTO(category);
        categoryRepository.delete(category);
        return categoryResponseDTO;
    }

    public CategoryResponseDTO updateCategory(UpdateCategoryRequestDTO updateCategoryRequestDTO) {
        Category category = categoryRepository.findById(updateCategoryRequestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + updateCategoryRequestDTO.getId() + " not found to be updated"));
        category.setName(updateCategoryRequestDTO.getName());
        categoryRepository.save(category);
        return convertCategoryToDTO(category);
    }

    private CategoryResponseDTO convertCategoryToDTO(Category category) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());
        return categoryResponseDTO;
    }

    private Category convertDTOTOCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        return category;
    }
}
