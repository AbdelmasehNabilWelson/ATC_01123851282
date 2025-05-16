package org.example.eventbookingsystem.domain.Service.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.eventbookingsystem.api.dto.categoryCreateRequestDTO;
import org.example.eventbookingsystem.api.dto.CategoryResponseDTO;
import org.example.eventbookingsystem.api.dto.UpdateCategoryRequestDTO;
import org.example.eventbookingsystem.domain.entity.entity.Category;
import org.example.eventbookingsystem.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO createCategory(categoryCreateRequestDTO categoryCreateRequestDTO) {
        Category category = convertDTOTOCategory(categoryCreateRequestDTO);
        categoryRepository.save(category);

        return convertCategoryToDTO(category);
    }

    public List<CategoryResponseDTO> patchCreate(List<categoryCreateRequestDTO> categoryCreateRequestDTOList) {
        List<CategoryResponseDTO> categoryResponseDTOS = new ArrayList<>();
        categoryCreateRequestDTOList.forEach(categoryCreateRequestDTO -> {
            categoryResponseDTOS.add(createCategory(categoryCreateRequestDTO));
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

    private Category convertDTOTOCategory(categoryCreateRequestDTO categoryCreateRequestDTO) {
        Category category = new Category();
        category.setName(categoryCreateRequestDTO.getName());
        return category;
    }
}
