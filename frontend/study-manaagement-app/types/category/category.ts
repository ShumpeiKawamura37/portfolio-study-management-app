export type CategoryResponse = {
  categoryId: number;
  categoryName: string;
  children: CategoryResponse[];
};

export type CreateCategoryRequest = {
  categoryName: string;
  parentCategoryId: number | null;
}

export type UpdateCategoryRequest = {
  categoryName: string
}
