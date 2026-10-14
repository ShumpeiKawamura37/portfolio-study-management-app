"use client";

import { apiFetch } from "@/lib/apiClient";
import { AnalyticsResponse } from "@/types/analytics/analytics";
import { CategoryAnalyticsResponse } from "@/types/analytics/categoryAnalytics";
import { ApiResponse } from "@/types/api/api";

const ANALYTICS_URL = "/api/studyLog/analytics";

export async function getAnalytics(): Promise<ApiResponse<AnalyticsResponse>> {
  const response = await apiFetch(ANALYTICS_URL, {
    method: "GET",
  });

  const result: ApiResponse<AnalyticsResponse> = await response.json();

  if (!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function getCategoryAnalytics(
  categoryId: number,
  targetParentCategoryId: number | null,
) {
  const url =
    targetParentCategoryId === null
      ? `${ANALYTICS_URL}/${categoryId}`
      : `${ANALYTICS_URL}/${categoryId}/${targetParentCategoryId}`;

  const response = await apiFetch(url, {
    method: "GET",
  });

  const result: ApiResponse<CategoryAnalyticsResponse> = await response.json();

  if (!response.ok) {
    throw new Error(result.message);
  }

  return result;
}
