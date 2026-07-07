import { ApiResponse, ErrorResponse } from "@/types/api/api";
import { AuthRequest, AuthResponse } from "@/types/auth/auth";
import { CreateUserRequest } from "@/types/user/user";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;


export async function login(request: AuthRequest): Promise<ApiResponse<AuthResponse>> {
  const response = await fetch(`${BASE_URL}/api/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const result: ApiResponse<AuthResponse> = await response.json();

  if (!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function register(request: CreateUserRequest): Promise<ApiResponse<null>> {
  const response = await fetch(`${BASE_URL}/api/user`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const result: ApiResponse<null> = await response.json();

  if (!response.ok) {
    throw new Error(result.message);
  }
  return result;
}