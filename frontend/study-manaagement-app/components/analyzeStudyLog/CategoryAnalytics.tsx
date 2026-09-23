"use client";

import { useAnalytics } from "@/hooks/analytics/useAnalytics";

export default function CategoryAnalytics() {
  const analytics = useAnalytics();
  const categoryAnalytics = analytics.categoryAnalytics;
  const targetCategory = analytics.targetCategory;
  const formatTotalStudySeconds = (totalStudySeconds: number | undefined | null) => {
    if (totalStudySeconds == null) {
      return "";
    }

    const hours = Math.floor(totalStudySeconds / 3600);
    const minutes = Math.floor((totalStudySeconds % 3600) / 60);

    return `${hours}時間 ${minutes}分`;
};

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

  const formatPercentage = (percentage: number | null | undefined) => {
    if(!percentage) {
      return "";
    } else {
      return `${Math.floor(percentage * 100)} %`
    } 
  }

  return (
    <div>
      <div className="mb-1">
        カテゴリ名：　
        {targetCategory?.categoryName}
      </div>
      <div className=" border border-[#B7B7B7] w-[400px] h-[150px] px-2 py-2">
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



