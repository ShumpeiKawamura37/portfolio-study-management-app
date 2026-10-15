import { apiFetch } from "@/lib/apiClient";
import { ApiResponse } from "@/types/api/api";
import { CreateUserRequest, UpdateUserRequest, UserResponse } from "@/types/user/user";
import { validateEmail, validatePassword, validateUsername } from "@/utils/validation/validation";

const USER_URL = "/api/user";

export async function getUser(): Promise<ApiResponse<UserResponse>> {
  const response = await apiFetch(USER_URL, {
    method: "GET",
  })

  const result: ApiResponse<UserResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function register(request: CreateUserRequest): Promise<ApiResponse<null>> {
  const response = await apiFetch(USER_URL, {
    method: "POST",
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

  const response = await apiFetch(USER_URL, {
    method: "PUT",
    body: JSON.stringify(user),
  });

  const result: ApiResponse<UserResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }

  return result;
}

export async function deleteUser() {
  const response = await apiFetch(USER_URL, {
    method: "DELETE",
  })
  
  const result: ApiResponse<UserResponse> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}