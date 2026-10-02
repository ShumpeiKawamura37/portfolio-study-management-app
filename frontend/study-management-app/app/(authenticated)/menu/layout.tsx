"use client";

import BackButton from "@/components/layout/BackButton";

export default function menuLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <BackButton />
      {children}
    </>
  );
}
