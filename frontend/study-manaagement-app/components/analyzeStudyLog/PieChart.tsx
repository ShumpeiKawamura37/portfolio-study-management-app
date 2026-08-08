"use client";

import { useAnalytics } from "@/hooks/analytics/useAnalytics";

export default function PieChart() {
  const analytics = useAnalytics();
  
  // カテゴリごとの合計学習時間が必要。　そもそもバックエンドのアナリティクスってカテゴリ単体のデータ？全体のデータ？
  return (
    <></>
  )
}