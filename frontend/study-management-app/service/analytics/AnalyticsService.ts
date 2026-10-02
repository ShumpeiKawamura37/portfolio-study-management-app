"use client";

import { AnalyticsResponse } from "@/types/analytics/analytics";
import { CategoryAnalyticsResponse } from "@/types/analytics/categoryAnalytics";
import { ApiResponse } from "@/types/api/api";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export async function getAnalytics(): Promise<ApiResponse<AnalyticsResponse>> {
  const token = localStorage.getItem("token");
  const response = await fetch(`${BASE_URL}/api/studyLog/analytics`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    }
  });

  const result: ApiResponse<AnalyticsResponse> = await response.json();
    
  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function getCategoryAnalytics (
  categoryId: number, 
  targetParentCategoryId: number | null
) {
  const token = localStorage.getItem("token");
  const url =
    targetParentCategoryId === null
      ? `${BASE_URL}/api/studyLog/analytics/${categoryId}`
      : `${BASE_URL}/api/studyLog/analytics/${categoryId}/${targetParentCategoryId}`;

  const response = await fetch(url, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
  }});
  const result: ApiResponse<CategoryAnalyticsResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }

  return result;
}
