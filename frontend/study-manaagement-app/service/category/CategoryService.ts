import { ApiResponse } from "@/types/api/api";
import { CategoryResponse, CreateCategoryRequest, UpdateCategoryRequest } from "@/types/category/category";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

// カテゴリ取得
export async function getCategories(): Promise<ApiResponse<CategoryResponse[]>> {
  const token = localStorage.getItem("token");
  const response = await fetch(`${BASE_URL}/api/category`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    }
  });

  const result: ApiResponse<CategoryResponse[]> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

// カテゴリ作成
export async function createCategory(categoryName: string, parentCategoryId: number | null): Promise<ApiResponse<CategoryResponse>> {
  const token = localStorage.getItem("token");
  const req: CreateCategoryRequest = {categoryName, parentCategoryId};
  const response = await fetch(`${BASE_URL}/api/category`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(req)
  });

  const result: ApiResponse<CategoryResponse> = await response.json();
  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

// カテゴリ更新
export async function updateCategory(categoryId: number, categoryName: string): Promise<ApiResponse<CategoryResponse>> {
  const token = localStorage.getItem("token");
  const req: UpdateCategoryRequest = {categoryName}; 
  const response = await fetch(`${BASE_URL}/api/category/${categoryId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(req)
  });

  const result: ApiResponse<CategoryResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

//カテゴリ削除
export async function deleteCategory(categoryId: number): Promise<boolean> {
  const token = localStorage.getItem("token");
  const response = await fetch(`${BASE_URL}/api/category/${categoryId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
  const result: ApiResponse<null> = await response.json();  
  if(!response.ok) {
    throw new Error(result.message);
  }
  return true;
}