"use client";
import { PieChart, Pie, Sector, Tooltip } from "recharts";
import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import { useState } from "react";
import { Period } from "@/hooks/analytics/useAnalyticsState";


const colors = [
  "#3b82f6",
  "#10b981",
  "#f59e0b",
  "#ef4444",
  "#8b5cf6",
  "#06b6d4",
  "#9ca3af",
];


export default function CategoryPieChart() {
  const [activeIndex, setActiveIndex] =
    useState<number>(-1);
  const analytics = useAnalytics();

  const categories = analytics.studySecondsByCategory;

  const allStudySeconds = analytics.analytics?.totalStudySeconds;

  // 学習ログがなければグラフを表示しない
  if (allStudySeconds === undefined || allStudySeconds === 0) {
    return null;
  }

  // 割合の分母
  const totalStudySeconds = categories.reduce(
    (sum, category) => sum + category.studySeconds,
    0
  );
  // 各カテゴリが全体に占める学習時間の割合を算出　[カテゴリ名・学習時間・割合]を持つオブジェクトを生成してchartData配列に格納する
  const chartData = categories.reduce(
  (result, category) => {
    const percentage =
      (category.studySeconds / totalStudySeconds) * 100;

    // 割合が5%未満のカテゴリは"その他"に合算する　
    if (percentage < 5) {

      // すでに"その他"があれば合算　なければ"その他"カテゴリを作成する
      const other = result.find(
        (item) => item.categoryName === "その他"
      );

      if (other) {
        other.studySeconds += category.studySeconds;
        other.percentage = (other.studySeconds / totalStudySeconds) * 100;
      } else {
        result.push({
          categoryName: "その他",
          studySeconds: category.studySeconds,
          percentage: percentage,
        });
      }
    } else {
      result.push({
        categoryName: category.categoryName,
        studySeconds: category.studySeconds,
        percentage: percentage,
      });
    }

    return result;
  },
  [] as {
    categoryName: string;
    studySeconds: number;
    percentage: number;
  }[]
);

  return (
    <div className="flex flex-col justify-center items-center mt-5 mb-7">
      {/* 集計期間 */}
      <select
      value={analytics.period}
      onChange={(e) => {
        analytics.setPeriod(e.target.value as Period)
      }}
      className="border border-[#B7B7B7] rounded-sm px-4 py-1"
      >
        <option value="total" key={"total"}>総合</option>
        <option value="week" key={"week"}>週間</option>
        <option value="month"key={"month"}>月間</option>
        <option value="year" key={"year"}>年間</option>
      </select>
      <PieChart width={260} height={260}>
        <Pie
        data={chartData}
        dataKey="studySeconds"
        nameKey="categoryName"
        startAngle={90}
        endAngle={-270}
        outerRadius={100}
        onMouseEnter={(_, index) => {
          setActiveIndex(index);
        }}
        onMouseLeave={() => setActiveIndex(-1)}
        shape={(props) => {
          const index = props.index;
          const isActive = activeIndex === index;
          return (
            <Sector
              {...props}
              outerRadius={
                isActive? props.outerRadius + 10 : props.outerRadius
              }
              fill={colors[index % colors.length]} /*色を順にを付与 */
            />
          )
        }}
      />
      {/* 選択したカテゴリの情報を表示 */}
      <Tooltip 
        isAnimationActive={false}
        content={({ active, payload }) => {
          if (!active || !payload?.length) {
            return null;
          }

          const data = payload[0].payload;

          const hours = Math.floor(data.studySeconds / 3600);
          const minutes = Math.floor((data.studySeconds % 3600) / 60);

          return (
            <div className="rounded border bg-white p-3 text-center">
              <p>{data.categoryName}</p>

              <p>
                {hours}時間{minutes}分
              </p>
            </div>
          );
        }}
      />
      </PieChart>
    </div>
  );
}



