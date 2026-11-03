"use client";

import { ProblemDetail } from "@/types/api/api";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export async function apiFetch(
  path: string,
  option: RequestInit = {}
): Promise<Response> {
  const token = localStorage.getItem("token");

  const res = await fetch(`${BASE_URL}${path}`, {
    ...option,
    headers: {
      "Content-Type": "application/json",
      Authorization: token ? `Bearer ${token}` : "",
    },
  });

  if (res.status === 401) {
    localStorage.removeItem("token"); 
    window.location.replace("/auth");
  }

  if (!res.ok) {
    const problem: ProblemDetail | null = await res.json().catch(() => null);
    throw new Error(problem?.detail ?? "エラーが発生しました。");
  }

  return res;
}