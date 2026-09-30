"use client"
import CategoryTree from "../category/CategoryTree"
import CategoryAnalytics from "./CategoryAnalytics"
import PieChart from "./PieChart"
import StudyLogOfDate from "./StudyLogOfDate"
import TotalAnalytics from "./TotalAnalytics"

export default function AnalyzeStudyLog() {

  return (
    <div className="mt-[60px] w-[970px] mx-auto flex items-center justify-center">
      <div className="w-[480px] flex flex-col justify-center items-start">
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


