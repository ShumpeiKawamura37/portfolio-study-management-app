"use client";

import { StudyLog } from "@/hooks/analytics/useAnalyticsState";

type StudyLogAnalyticsProps = {
  studyLog: StudyLog
}

export default function StudyLogAnalyticsItem({ 
  studyLog }
  : StudyLogAnalyticsProps) {
    const formatDate = (date: string | undefined) => {
      if(!date) {
        return "";
      }
      return new Date(date).toLocaleString("ja-JP", {
        year: "numeric",
        month: "numeric",
        day: "numeric",
        hour: "numeric",
        minute: "numeric",
      });
    } ;
  return (
    <ul className="flex flex-col gap-1 border-b mb-5 pb-4">
      <li className="flex">
        <span className="w-18 text-right">
          カテゴリ: 
        </span>
        <span className="ml-3">
            {studyLog.category.categoryName}
        </span>
      </li>
      <li className="flex">
        <span className="w-18 text-right">
          開始時間: 
        </span>
        <span className="ml-3">
          {formatDate(studyLog.startTime.toString())}
        </span>
      </li>
      <li className="flex">
        <span className="w-18 text-right">
          終了時間: 
        </span>
        <span className="ml-3">
          {formatDate(studyLog.endTime.toString())}
        </span>
      </li>
      <li className="flex">
        <span className="w-18 text-right">
          メモ: 
        </span>
        {
          studyLog.memo!= null 
            ? <span className="text-[#E1E1E1] ml-3">
                メモはありません。
              </span> 
            : <span className="ml-3">
              {studyLog.memo}
              </span>
        }
      </li>
    </ul>
  )
}