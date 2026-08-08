"use context";


import { useAnalyticsState } from "@/hooks/analytics/useAnalyticsState";
import { createContext, ReactNode } from "react";

type AnalyTicsContextType = ReturnType<typeof useAnalyticsState>;

export const AnalyTicsContext = createContext<AnalyTicsContextType | null>(null);

export function AnalyticsProvider({children}: {children: ReactNode}) {
  const analytics = useAnalyticsState();
  return (
    <AnalyTicsContext.Provider
      value={analytics}
    >
      {children}
    </AnalyTicsContext.Provider>
  )
}