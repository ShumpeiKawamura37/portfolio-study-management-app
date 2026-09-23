"use client"
import CategoryTree from "../category/CategoryTree"
import CategoryAnalytics from "./CategoryAnalytics"
import PieChart from "./PieChart"
import StudyLogOfDate from "./StudyLogOfDate"
import TotalAnalytics from "./TotalAnalytics"

export default function AnalyzeStudyLog() {

  return (
    <div className="mt-[60px] mx-[30px] w-[970px] flex items-start justify-between">
      <div className="w-[480px] flex flex-col justify-center items-center">
        <PieChart/>
        <CategoryTree />
        <CategoryAnalytics />
      </div>

      <div className="w-[480px] flex flex-col justify-center items-center">
        <TotalAnalytics />
        <StudyLogOfDate />
      </div>
    </div>
  )
}

