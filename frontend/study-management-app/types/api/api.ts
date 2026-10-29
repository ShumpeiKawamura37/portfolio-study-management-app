export type ApiResponse<T> = {
  status: string;
  data: T;
  message: string;
}

export type ProblemDetail = {
  type: string;
  title: string;
  status: number;
  detail?: string;
  instance?: string;
};