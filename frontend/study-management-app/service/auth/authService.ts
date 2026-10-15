import { apiFetch } from "@/lib/apiClient";
import { ApiResponse, } from "@/types/api/api";
import { AuthRequest, AuthResponse } from "@/types/auth/auth";

const AUTH_URL = "/api/auth/login";

export async function login(request: AuthRequest): Promise<ApiResponse<AuthResponse>> {
  const response = await apiFetch(AUTH_URL, {
    method: "POST",
    body: JSON.stringify(request),
  });

  const result: ApiResponse<AuthResponse> = await response.json();

  if (!response.ok) {
    throw new Error(result.message);
  }
  return result;
}
