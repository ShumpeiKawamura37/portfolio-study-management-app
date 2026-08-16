"use client"

import { getUser } from "@/service/user/userService";
import { useEffect, useState } from "react";
import { getStudyLogs } from "@/service/studyLog/StudyLogService";
import { CategoryResponse } from "@/types/category/category";
import { getAnalytics } from "@/service/analytics/AnalyticsService";

type StudyLog = {
  studyLogId: number,
  category: CategoryResponse,
  startTime: number,
  endTime: number,
  studySeconds: number,
  memo: string | null
}

type StudySecondsByCategory = {
  categoryId: number;
  categoryName: string;
  studySeconds: number;
}[];

type Analytics = {
  totalStudySeconds: number,
  studyDayCount: number,
  averageStudySeconds: number,
  CategoryNameLongestStudied: string,
  studyDayRate: number,
  studyStreak: number
};

export const useAnalyticsState = () => {
  const [username, setUsername] = useState<string>("");
  const [studyLogs, setStudyLogs] = useState<StudyLog[]>([]);
  const [studySecondsByCategory, setStudySecondsByCategory] = useState<StudySecondsByCategory>([]);
  const [analytics, setAnalytics] = useState<Analytics>();


  useEffect(() => {
    const fetchUser = async () => {
      const resOfUser = await getUser();
      setUsername(resOfUser.data.username);
    };

    const fetchStudyLogs = async () => {
      const resOfStudyLogs = await getStudyLogs();
      setStudyLogs(resOfStudyLogs.data);
    }

    const fetchAnalytics = async() => {
      const resOfAnalytics = await getAnalytics();
      setAnalytics(resOfAnalytics.data);
    }

    fetchUser();
    fetchStudyLogs();
    fetchAnalytics();
  }, []);

  useEffect(() => {
  const result = studyLogs.reduce<StudySecondsByCategory>(
    (acc, studyLog) => {
      const categoryId = studyLog.category.categoryId;
      const categoryName = studyLog.category.categoryName;

      const existingCategory = acc.find(
        (category) => category.categoryId === categoryId
      );

      if (existingCategory) {
        existingCategory.studySeconds += studyLog.studySeconds;
      } else {
        acc.push({
          categoryId,
          categoryName,
          studySeconds: studyLog.studySeconds,
        });
      }
      return acc;
    },
    []
  );

  setStudySecondsByCategory(result);
}, [studyLogs]);

  return {
    username,
    studyLogs,
    analytics,
    studySecondsByCategory,
  }
}

