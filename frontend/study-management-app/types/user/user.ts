export type CreateUserRequest = {
  email: string;
  password: string;
}

export type UpdateUserRequest = {
  username: string,
  email: string,
  password: string
}

export type UserResponse = {
  username: string;
  email: string;
}