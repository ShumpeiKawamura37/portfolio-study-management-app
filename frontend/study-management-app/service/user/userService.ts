import { ApiResponse } from "@/types/api/api";
import { CreateUserRequest, UpdateUserRequest, UserResponse } from "@/types/user/user";
import { validateEmail, validatePassword, validateUsername } from "@/utils/validation/validation";

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

export async function register(request: CreateUserRequest): Promise<ApiResponse<null>> {
  const response = await fetch(`${BASE_URL}/api/user`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  const errorMessageOfEmail = validateEmail(request.email);
  if(errorMessageOfEmail) {
    throw new Error(errorMessageOfEmail);
  }
  const errorMessageOfPassword = validatePassword(request.password);
  if(errorMessageOfPassword) {
    throw new Error(errorMessageOfPassword);
  }

  const result: ApiResponse<null> = await response.json();

  if (!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function updateUser(user: UpdateUserRequest) {
  const token = localStorage.getItem("token");

  const errorMessageOfName = validateUsername(user.username);
  if(errorMessageOfName) {
    throw new Error(errorMessageOfName);
  }

  const errorMessageOfEmail = validateEmail(user.email);
  if(errorMessageOfEmail) {
    throw new Error(errorMessageOfEmail);
  }

  const errorMessageOfPassword = validatePassword(user.password);
  if(errorMessageOfPassword) {
    throw new Error(errorMessageOfPassword);
  }

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