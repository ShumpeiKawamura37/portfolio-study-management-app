export type CreateStudyLogrequest = {
  startTime: Date,
  endTime: Date,
  studySeconds: number,
  memo: String | null,
  categoryId: number,
}

export type  StudyLogResponse = {
  studyLogId: number,
  categoryName: string,
  startTime: number,
  endTime: number,
  studySeconds: number,
  memo: string
}