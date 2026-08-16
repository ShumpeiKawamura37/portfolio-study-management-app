"use client";

import { useAnalytics } from "@/hooks/analytics/useAnalytics";

export default function CategoryAnalytics() {
  const analytics = useAnalytics();
  return (
    <div className=" border border-[#B7B7B7] w-[250px] h-[100px] px-2 py-2 mt-[20px]">
      <ul>
        <li></li>
        <li></li>
      </ul>
    </div>
  );
}