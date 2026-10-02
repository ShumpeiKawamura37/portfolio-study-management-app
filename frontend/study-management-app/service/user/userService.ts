import { ApiResponse } from "@/types/api/api";
import { UpdateUserRequest, UserResponse } from "@/types/user/user";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export async function getUser(): Promise<ApiResponse<UserResponse>> {
  const token = localStorage.getItem("token");
  const response = await fetch(`${BASE_URL}/api/user`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    }
  });

  const result: ApiResponse<UserResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function updateUser(user: UpdateUserRequest) {
  const token = localStorage.getItem("token");
  const response = await fetch(`${BASE_URL}/api/user`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(user),
  });

  const result: ApiResponse<UserResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }

  return result;
}

export async function deleteUser() {
  const token = localStorage.getItem("token");
  const response = await fetch(`${BASE_URL}/api/user`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  const result: ApiResponse<UserResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}