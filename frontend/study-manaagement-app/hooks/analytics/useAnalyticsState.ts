"use client";

import { getUser } from "@/service/user/userService";
import { useEffect, useMemo, useState } from "react";
import { getStudyLogByDate, getStudyLogs } from "@/service/studyLog/StudyLogService";
import { CategoryResponse } from "@/types/category/category";
import {
  getAnalytics,
  getCategoryAnalytics,
} from "@/service/analytics/AnalyticsService";
import { useRecord } from "../record/useRecord";
import { useCategory } from "../category/UseCategory";
import { getAncestorCategoryList } from "@/service/category/CategoryService";

export type StudyLog = {
  studyLogId: number;
  category: CategoryResponse;
  startTime: number;
  endTime: number;
  studySeconds: number;
  memo: string | null;
};

type Category = {
  categoryId: number;
  categoryName: string;
  children: Category[];
};

type StudySecondsByCategory = {
  categoryId: number;
  categoryName: string;
  studySeconds: number;
}[];

type Analytics = {
  totalStudySeconds: number;
  studyDayCount: number;
  averageStudySeconds: number;
  CategoryNameLongestStudied: string;
  studyDayRate: number;
  studyStreak: number;
};

type CategoryAnalytics = {
  categoryId: number;
  totalStudySeconds: number;
  firstTimeStudied: string;
  lastTimeStudied: string;
  percentageOfTotal: number;
  percentageOfDescendantCategory: number | null;
};

export type Period = "week" | "month" | "year" | "total";

export const useAnalyticsState = () => {
  const [username, setUsername] = useState<string>("");
  const [period, setPeriod] = useState<Period>("total");
  const [studyLogs, setStudyLogs] = useState<StudyLog[]>([]);
  const [ancestorCategories, setAncestorCategories] = useState<Category[]>([]);
  const [targetParentCategoryId, setTargetParentCategoryId] = useState<
    number | null
  >(null);
  const [studySecondsByCategory, setStudySecondsByCategory] =
    useState<StudySecondsByCategory>([]);
  const [analytics, setAnalytics] = useState<Analytics>();
  const [categoryAnalytics, setCategoryAnalytics] =
    useState<CategoryAnalytics>();
  const [targetDate, setTargetDate] = useState<Date>(new Date());
  const [targetStudyLog, setTargetStudyLog] = useState<StudyLog[] | null>(null);
  const { targetCategoryId, studyLogVersion } = useRecord();
  const { categories } = useCategory();

  const getTargetCategory = (categories: Category[]): Category | undefined => {
    for (const category of categories) {
      if (category.categoryId === targetCategoryId) {
        return category;
      }

      if (category.children && category.children.length > 0) {
        const targetCategory = getTargetCategory(category.children);

        if (targetCategory) {
          return targetCategory;
        }
      }
    }
    return undefined;
  };

  const filterStudyLogByPeriod = (studyLogs: StudyLog[]): StudyLog[] => {
    const now = new Date();
    let periodDate: Date | null = new Date(now);

    switch (period) {
      case "week":
        periodDate.setDate(now.getDate() - 7);
        break;
      case "month":
        periodDate.setMonth(now.getMonth() - 1);
        break;
      case "year":
        periodDate.setFullYear(now.getFullYear() - 1);
        break;
      case "total":
        periodDate = null;
        break;
      default:
    }

    if (periodDate == null) {
      return studyLogs;
    }

    return studyLogs.filter((studyLog) => {
      const startDate = new Date(studyLog.startTime);

      return startDate >= periodDate;
    });
  };

  // ユーザー名を取得
  useEffect(() => {
    const fetchUser = async () => {
      const res = await getUser();
      setUsername(res.data.username);
    };

    fetchUser();
  }, []);

  // 学習ログと学習分析を取得
  useEffect(() => {
    const fetchStudyLogs = async () => {
    const resOfStudyLogs = await getStudyLogs();
    setStudyLogs(resOfStudyLogs.data);
  };

    const fetchAnalytics = async () => {
      const resOfAnalytics = await getAnalytics();
      setAnalytics(resOfAnalytics.data);
    };

    fetchStudyLogs();
    fetchAnalytics();
  }, [categories, studyLogVersion]);

  // 円グラフ用 periodで指定した期間のログを、pie用データに変換
  useEffect(() => {
    const filteredStudyLogs = filterStudyLogByPeriod(studyLogs);

    const result = filteredStudyLogs.reduce<StudySecondsByCategory>((acc, studyLog) => {
      const categoryId = studyLog.category.categoryId;
      const categoryName = studyLog.category.categoryName;

      const existingCategory = acc.find(
        (category) => category.categoryId === categoryId,
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
    }, []);
    setStudySecondsByCategory(result);
  }, [period, studyLogs]);

  //カテゴリ分析を取得
  useEffect(() => {
    let cancelled = false;
    const fetchCategoryAnalytics = async () => {
      // nullチェック
      if (targetCategoryId == null) {
        setCategoryAnalytics(undefined);
        return;
      }

      // 選択中のカテゴリが削除済み
      if (!getTargetCategory(categories)) {
        setCategoryAnalytics(undefined);
        return;
      }

      try {
        const res = await getCategoryAnalytics(
          targetCategoryId,
          targetParentCategoryId,
        );
        if (cancelled) {
          return;
        }
        setCategoryAnalytics(res.data);
      } catch (error: Error | any) {
        if (cancelled) {
          return;
        }
        console.error(error);
        setCategoryAnalytics(undefined);
      }
    };

    fetchCategoryAnalytics();

    return () => {
      cancelled = true;
    };
  }, [targetCategoryId, categories, studyLogVersion, targetParentCategoryId]);

  // カテゴリ分析用　選択カテゴリの先祖一覧を取得
  useEffect(() => {
    const fetchAncestorCategoryList = async () => {
      if (targetCategoryId == null) {
        setAncestorCategories([]);
        return;
      }
      const res = await getAncestorCategoryList(targetCategoryId);
      setAncestorCategories(res.data);
    };
    fetchAncestorCategoryList();
  }, [targetCategoryId]);

  // categoriesか対象が変わった時、targetCategory useMemoなら無駄な際レンダリング防げる
  const targetCategory = useMemo(() => {
    return getTargetCategory(categories);
  }, [categories, targetCategoryId]);

  // 日付に対応した学習ログを取得
  useEffect(() => {
    const fetchStudyLogByDate = async() => {
      const res = await getStudyLogByDate(targetDate);
      setTargetStudyLog(res.data);
    };
    fetchStudyLogByDate();
  }, [targetDate]);

  return {
    username,
    studyLogs,
    analytics,
    period,
    setPeriod,
    studySecondsByCategory,
    categoryAnalytics,
    targetParentCategoryId,
    setTargetParentCategoryId,
    ancestorCategories,
    targetCategory,
    targetDate,
    setTargetDate,
    targetStudyLog,
    setTargetStudyLog
  };
};


