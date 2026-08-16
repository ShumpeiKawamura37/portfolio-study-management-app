"use client";

import { ActionForCategoryProvider } from "@/context/ActionForCategoryContext";
import { AnalyticsProvider } from "@/context/AnalyticsContext";
import { CategoryProvider } from "@/context/CategoryContext";
import { RecordProvider } from "@/context/RecordContext";

export default function Providers({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <RecordProvider>
      <CategoryProvider>
        <ActionForCategoryProvider>
          <AnalyticsProvider>
            {children}
          </AnalyticsProvider>      
        </ActionForCategoryProvider>
      </CategoryProvider>
    </RecordProvider>
  );
}