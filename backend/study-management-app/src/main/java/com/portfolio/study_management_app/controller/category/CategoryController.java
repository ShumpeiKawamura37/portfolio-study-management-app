package com.portfolio.study_management_app.controller.category;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.study_management_app.dto.category.CategoryResponseDto;
import com.portfolio.study_management_app.dto.category.CreateCategoryRequestDto;
import com.portfolio.study_management_app.dto.common.ApiResponseDto;
import com.portfolio.study_management_app.service.category.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  public ApiResponseDto<CategoryResponseDto> createCategory(@RequestBody CreateCategoryRequestDto req) {
    CategoryResponseDto res = categoryService.createCategory(req);
    return new ApiResponseDto<>("SUCCESS", res, null);
  }
}
