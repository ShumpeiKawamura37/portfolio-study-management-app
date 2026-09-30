export type CategoryAnalyticsResponse = {
  categoryId: number,
  totalStudySeconds: number,
  firstTimeStudied: string,
  lastTimeStudied: string,
  percentageOfTotal: number,
  percentageOfDescendantCategory: number | null
}