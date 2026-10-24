"use client"
import CategoryTree from "../category/CategoryTree"
import CategoryAnalytics from "./CategoryAnalytics"
import PieChart from "./PieChart"
import StudyLogOfDate from "./StudyLogOfDate"
import TotalAnalytics from "./TotalAnalytics"

export default function AnalyzeStudyLog() {

  return (
    <div className="mt-[80px] max-w-[370px] md:max-w-[970px] mx-auto flex flex-col md:flex-row items-center justify-center">
      <div className="md:w-[480px] flex flex-col justify-center mb-[30px] items-center md:items-start md:mb-0">
        <PieChart/>
        <div className="self-center md:self-start">
          <CategoryTree />
        </div>
        <CategoryAnalytics />
      </div>

      <div className="md:w-[480px] flex flex-col justify-center items-center">
        <TotalAnalytics />
        <StudyLogOfDate />
      </div>
    </div>
  )
}

