"use client";

import { useAnalytics } from "@/hooks/analytics/useAnalytics";

export default function DisplayUsername() {
  const analyrtics = useAnalytics();

  return (
    <div className="w-[250px] h-[45px] text-[36px]">
      {analyrtics.username}
    </div>
  )
} 