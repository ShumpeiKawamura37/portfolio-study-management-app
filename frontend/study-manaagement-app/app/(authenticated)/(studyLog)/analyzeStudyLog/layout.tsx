"use client";

import { AnalyticsProvider } from "@/context/AnalyticsContext";

export default function analyticsLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <AnalyticsProvider>
    {children}
    </AnalyticsProvider>
  );
}