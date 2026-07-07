export type ApiResponse<T> = {
  status: string;
  data: T;
  message: string;
}

export type ErrorResponse = {
  message: string;
};