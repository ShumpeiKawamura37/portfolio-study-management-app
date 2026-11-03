export type AnalyticsResponse = {
  totalStudySeconds: number,
  studyDayCount: number,
  averageStudySeconds: number,
  CategoryNameLongestStudied: string | null,
  studyDayRate: number,
  studyStreak: number
}