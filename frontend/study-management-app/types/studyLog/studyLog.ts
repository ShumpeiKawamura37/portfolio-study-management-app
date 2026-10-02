import { CategoryResponse } from "../category/category"

export type CreateStudyLogrequest = {
  startTime: string,
  endTime: string,
  studySeconds: number,
  memo: string | null,
  categoryId: number,
}

export type  StudyLogResponse = {
  studyLogId: number,
  category: CategoryResponse,
  startTime: number,
  endTime: number,
  studySeconds: number,
  memo: string | null
}

