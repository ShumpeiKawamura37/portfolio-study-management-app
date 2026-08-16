"use client"

import CategoryTree from "../category/CategoryTree"
import CategoryAnalytics from "./CategoryAnalytics"
import DisplayUsername from "./DisplayUsername"
import PieChart from "./PieChart"

export default function AnalyzeStudyLog() {

  return (
    <div className="mt-[60px] mx-[30px] w-[970px] flex items-center justify-between">
      <div className="w-[480px]">
        <DisplayUsername />
        <PieChart/>
        <CategoryTree />
        <CategoryAnalytics />

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

// タイマー押すと画面遷移してしまう・
// 保存したばかりのカテゴリの学習時間が画面自動遷移後のPieに入っていない。useEffectで再取得すればいける？
