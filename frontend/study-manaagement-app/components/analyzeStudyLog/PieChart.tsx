"use client";
import { PieChart, Pie, Cell, Sector, Tooltip } from "recharts";
import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import { useState } from "react";

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

  const studyLogs = analytics.studySecondsByCategory;
  const totalStudySeconds = analytics.analytics?.totalStudySeconds;
  if (totalStudySeconds === undefined || totalStudySeconds === 0) {
    return null;
  }

  const chartData = studyLogs.reduce(
  (result, category) => {
    const percentage =
      (category.studySeconds / totalStudySeconds) * 100;

    if (percentage < 5) {
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

  // 割り出したpercentageをPieの角度とする。
  return (
    <>
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
              fill={colors[index % colors.length]}
            />
          )
        }}
      />
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
    </>
  );
}



