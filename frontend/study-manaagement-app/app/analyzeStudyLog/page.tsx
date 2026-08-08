"use client"

import AnalyzeStudyLog from "@/components/analyzeStudyLog/AnalyzeStudyLog"
import { AnalyticsProvider } from "@/context/AnalyticsContext"

export default function AnalyzeStudyLogPage() {
  return (
    <AnalyticsProvider>
      <AnalyzeStudyLog />
    </AnalyticsProvider>
  )
}