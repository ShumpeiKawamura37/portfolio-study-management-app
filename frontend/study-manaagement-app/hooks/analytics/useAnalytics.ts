"use client";

import { AnalyTicsContext } from "@/context/AnalyticsContext";
import { useContext } from "react";

export const useAnalytics = () => {
  const context = useContext(AnalyTicsContext);
  if (context === null) {
    throw new Error("useAnalytics must be used within AnalyticsProvider");
  }
  return context;
}