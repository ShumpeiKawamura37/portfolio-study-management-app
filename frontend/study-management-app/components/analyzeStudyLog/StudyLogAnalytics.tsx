"use client";

import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import { getStudyLogByDate } from "@/service/studyLog/StudyLogService";
import { useEffect } from "react";
import StudyLogAnalyticsItem from "./StudyLogAnalyticsItem";

// 対象日に開始された学習ログの一覧を表示する

export default function StudyLogAnalytics() {
  const {targetStudyLog, setTargetStudyLog} = useAnalytics();

  // 初回描画時は今日のログを表示する。
  useEffect(() => {
    const fetchTodayStudyLog = async () => {
      const today = new Date();
      const res = await getStudyLogByDate(today);
      setTargetStudyLog(res.data);
    }
    fetchTodayStudyLog();
  }, [])

  return (
    <div className="px-5 h-[300px]">
      {targetStudyLog?.length == 0 ? (
        <p className="text-center">学習ログがありません。</p>
      ):(
        <ul>
          {targetStudyLog?.map((studyLog) => {
            return (
              <li 
                key={targetStudyLog.indexOf(studyLog)}
                className="mb-4"
              >
                <StudyLogAnalyticsItem studyLog={studyLog}/>
              </li>
            )
          })}
        </ul>
      )}
    </div>
  )
}

