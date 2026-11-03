import { apiFetch } from "@/lib/apiClient";
import { ApiResponse } from "@/types/api/api";
import { CategoryResponse, CreateCategoryRequest, UpdateCategoryRequest } from "@/types/category/category";
import { validateCategoryName } from "@/utils/validation/validation";

const CATEGORY_URL = "/api/category";


// カテゴリ取得
export async function getCategories(): Promise<ApiResponse<CategoryResponse[]>> {
  const response = await apiFetch(CATEGORY_URL, {
    method: "GET",
  });

  const result: ApiResponse<CategoryResponse[]> = await response.json();

  return result;
}

// カテゴリ作成
export async function createCategory(categoryName: string, parentCategoryId: number | null): Promise<ApiResponse<CategoryResponse>> {
  const errorMessage = validateCategoryName(categoryName);
  if(errorMessage) {
    throw new Error(errorMessage);
  }
  const req: CreateCategoryRequest = {categoryName, parentCategoryId};
  const response = await apiFetch(CATEGORY_URL, {
    method: "POST",
    body: JSON.stringify(req)
  });

  const result: ApiResponse<CategoryResponse> = await response.json();

  return result;
}

// カテゴリ更新
export async function updateCategory(categoryId: number, categoryName: string): Promise<ApiResponse<CategoryResponse>> {
  const errorMessage = validateCategoryName(categoryName);
  if(errorMessage) {
    throw new Error(errorMessage);
  }
  const req: UpdateCategoryRequest = { categoryName }; 

  const response = await apiFetch(`${CATEGORY_URL}/${categoryId}`,{
    method: "PUT",
    body: JSON.stringify(req)
  })

  const result: ApiResponse<CategoryResponse> = await response.json();

  return result;
}

//カテゴリ削除
export async function deleteCategory(categoryId: number): Promise<boolean> {
  const response = await apiFetch(`${CATEGORY_URL}/${categoryId}`, {
    method: "DELETE",
  })

  return true;
}

// 指定カテゴリの先祖カテゴリリストを取得
export async function getAncestorCategoryList(categoryId: number): Promise<ApiResponse<CategoryResponse[]>> {

  const response = await apiFetch(`${CATEGORY_URL}/ancestor/${categoryId}`, {
    method: "GET",
  });

  const result: ApiResponse<CategoryResponse[]> = await response.json();

  return result;
}