"use client"

import DisplayUsername from "./DisplayUsername"
import PieChart from "./PieChart"

export default function AnalyzeStudyLog() {

  return (
    <div className="mt-[60px] mx-[30px] w-[970px] flex items-center justify-between">
      <div className="w-[480px]">
        <DisplayUsername />
        <PieChart/>
        
        {/* 
        categoryTree
        analytics(バックエンドの追加実装が必要) */}
      </div>

      <div className="w-[480px]">
        {/* TotalAnalytics
        studyLogofDate */}
      </div>
    </div>
  )
}