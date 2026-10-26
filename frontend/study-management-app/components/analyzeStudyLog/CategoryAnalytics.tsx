"use client";

import { useAnalytics } from "@/hooks/analytics/useAnalytics";


// 分析項目：　選択したカテゴリの合計学習時間・初回学習日・最終学習日・対象カテゴリの学習時間が総合学習時間に占める割合・対象カテゴリの祖先要素を１つ選択し、それとその子孫カテゴリの総学習時間に占める対象カテゴリの学習時間

export default function CategoryAnalytics() {
  const analytics = useAnalytics();
  const categoryAnalytics = analytics.categoryAnalytics;
  const targetCategory = analytics.targetCategory;

  // number型の時刻を文字列で返す
  const formatTotalStudySeconds = (totalStudySeconds: number | undefined | null) => {
    if (totalStudySeconds == null) {
      return "";
    }

    const hours = Math.floor(totalStudySeconds / 3600);
    const minutes = Math.floor((totalStudySeconds % 3600) / 60);

    return `${hours}時間 ${minutes}分`;
  };

  // string型の日付をDate型で返す
  const formatDate = (date: string | undefined) => {
    if(!date) {
      return "";
    }
    return new Date(date).toLocaleString("ja-JP", {
      year: "numeric",
      month: "numeric",
      day: "numeric",
    });
  };

  // 数値を百分率にして返す
  const formatPercentage = (percentage: number | null | undefined) => {
    if(!percentage) {
      return "";
    } else {
      return `${Math.floor(percentage * 100)} %`
    } 
  }

  return (
    <div>
      <div className="mt-7 mb-1 text-lg">
        カテゴリ名：
        {targetCategory?.categoryName}
      </div>
      <div className=" border border-[#B7B7B7] w-[250px] h-[150px] px-2 py-2">
        <ul className="h-full flex flex-col justify-between">
          <li>合計学習時間: {formatTotalStudySeconds(categoryAnalytics?.totalStudySeconds)}</li>
          <li>最初に学習した日: {formatDate(categoryAnalytics?.firstTimeStudied)}</li>
          <li>最後に学習した日: {formatDate(categoryAnalytics?.lastTimeStudied)}</li>
          <li>全体に占める割合: {formatPercentage(categoryAnalytics?.percentageOfTotal)}</li>
          <li>
            {analytics.ancestorCategories.length != 0 ? 
              (
                <>
                  <span>
                    <select
                      className="w-[105px] text-center overflow-ellipsis border rounded-sm"
                      onChange={(e) => 
                        analytics.setTargetParentCategoryId(
                          e.target.value === "" ? null : Number(e.target.value
                        ))
                      }
                    >
                      <option value="">親カテゴリ</option>
                      {analytics.ancestorCategories.map(category => {
                        return (
                          <option 
                            value={category.categoryId} 
                            key={category.categoryId}
                          >
                            {category.categoryName}
                          </option>
                        )
                      })}
                    </select>
                  </span>
                    以下のカテゴリに占める割合:
                    {formatPercentage(categoryAnalytics?.percentageOfDescendantCategory)}
                </>
              )
              : (
                <span className="text-[#E1E1E1]">親カテゴリがありません</span>
              )
            }
          </li>
        </ul>
      </div>
    </div> 
  );
}



