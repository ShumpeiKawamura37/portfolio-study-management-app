import { ApiResponse } from "@/types/api/api";
import { CreateStudyLogrequest, StudyLogResponse } from "@/types/studyLog/studyLog";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export async function createStudyLog(
  startTime: Date,
  endTime: Date,
  studySeconds: number,
  memo: string | null,
  categoryId: number,
): Promise<ApiResponse<StudyLogResponse>> {
  const token = localStorage.getItem("token");
  const req: CreateStudyLogrequest = {
    startTime,
    endTime,
    studySeconds,
    memo,
    categoryId
  };

  const response = await fetch(`${BASE_URL}/api/studyLog`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(req)
  });

  const result: ApiResponse<StudyLogResponse> = await response.json();
  
  if(!response.ok) {
    throw new Error(result.message);
  }

  return result;
}

// export async function getStudyLogs(): Promise<ApiResponse<StudyLogResponse[]>> {

// }