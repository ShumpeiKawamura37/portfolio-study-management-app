"use client";

import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import ItemHeading from "../layout/ItemHeading";

export default function TotalAnalytics() {
  const analytics = useAnalytics().analytics;

  const formatStudySeconds = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const remainingSeconds = seconds % 60;

    return `${hours}時間 ${minutes}分 ${remainingSeconds}秒`;
  }
  return (
    <div className="w-[400px] border border-[#B7B7B7] p-1 flex flex-col items-center mb-3">
      <h2 className="text-center text-2xl mb-4">ユーザーデータ</h2>
      <ul>
        <li className="flex flex-row justify-between  items-center">
          <div className="text-left">
            <ItemHeading title="平均学習時間"/>
          </div>
          <div className="text-right text-lg">
            {
              analytics?.averageStudySeconds && 
              formatStudySeconds(analytics.averageStudySeconds)
            }
          </div>
        </li>
        <li className="flex flex-row justify-between items-center">
          <div className="text-left">
            <ItemHeading title="学習日数"/>
          </div>
          <div className="text-right text-lg">
            {analytics?.studyDayCount} 日　{Number(analytics?.studyStreak) > 1 && (
              <span className="text-[#DE5353] text-sm">
                ({analytics?.studyStreak}日連続！)
              </span>
            )}
          </div>
        </li>
        <li className="flex flex-row justify-between items-center">
          <div className="text-left">
            <ItemHeading title="合計学習時間"/>
          </div>
          <div className="text-right text-lg">
            {
              analytics?.totalStudySeconds && 
              formatStudySeconds(analytics.totalStudySeconds)
            }
          </div>
        </li>
        <li className="flex flex-row justify-between items-center">
          <div className="text-left">
            <ItemHeading title="最も学習しているカテゴリ"/>
          </div>
          <div className="text-right  text-lg">
            {analytics?.CategoryNameLongestStudied}
          </div>
        </li>
      </ul>
    </div>
  )
}

items-centerにしているのにラベルが上寄せになる