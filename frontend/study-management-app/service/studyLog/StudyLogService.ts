import { apiFetch } from "@/lib/apiClient";
import { ApiResponse } from "@/types/api/api";
import { CreateStudyLogrequest, StudyLogResponse } from "@/types/studyLog/studyLog";
import { validateStudyMemo } from "@/utils/validation/validation";

const STUDY_LOG_URL = "/api/studyLog";

export async function createStudyLog(
  startTime: Date,
  endTime: Date,
  studySeconds: number,
  memo: string | null,
  categoryId: number,
): Promise<ApiResponse<StudyLogResponse>> {
  if(memo != null) {
    const errorMessage = validateStudyMemo(memo);
    if(errorMessage) {
      throw new Error(errorMessage);
    }
  }

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


  const response = await apiFetch(STUDY_LOG_URL, {
    method: "POST",
    body: JSON.stringify(req)
  });

  const result: ApiResponse<StudyLogResponse> = await response.json();
  
  if(!response.ok) {
    throw new Error(result.message);
  }

  return result;
}

export async function getStudyLogs(): Promise<ApiResponse<StudyLogResponse[]>> {

  const response = await apiFetch(STUDY_LOG_URL, {
    method: "GET",
  });

  const result: ApiResponse<StudyLogResponse[]> = await response.json();
  
  if(!response.ok) {
    throw new Error(result.message);
  }
  return result;
}

export async function getStudyLogByDate(date: Date) {
  const formattedDate = date.toISOString().split("T")[0];


  const response = await apiFetch(`${STUDY_LOG_URL}/date/${formattedDate}`, {
    method: "GET",
  });

  const result: ApiResponse<StudyLogResponse[]> = await response.json();

  if(!response.ok) {
    throw new Error(result.message);
  }

  return result;
}