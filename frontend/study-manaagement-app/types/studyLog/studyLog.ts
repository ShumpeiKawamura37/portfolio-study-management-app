import { CategoryResponse } from "../category/category"

export type CreateStudyLogrequest = {
  startTime: Date,
  endTime: Date,
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

