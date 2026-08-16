"use client";

import { AnalyticsResponse } from "@/types/analytics/analytics";
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
