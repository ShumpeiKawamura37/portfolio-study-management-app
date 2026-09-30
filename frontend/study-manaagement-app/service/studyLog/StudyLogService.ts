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

  const formatDateTime = (date: Date) => {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}T${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}:${String(date.getSeconds()).padStart(2, "0")}`;
  };

  const req: CreateStudyLogrequest = {
    startTime: formatDateTime(startTime),
    endTime: formatDateTime(endTime),
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

export async function getStudyLogs(): Promise<ApiResponse<StudyLogResponse[]>> {
  const token = localStorage.getItem("token");

  const response = await fetch(`${BASE_URL}/api/studyLog`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  const result: ApiResponse<StudyLogResponse[]> = await response.json();
  
  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function getStudyLogByDate(date: Date) {
  const token = localStorage.getItem("token");
  const formattedDate = date.toISOString().split("T")[0];


  const response = await fetch(`${BASE_URL}/api/studyLog/date/${formattedDate}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  const result: ApiResponse<StudyLogResponse[]> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }

  return result;
}