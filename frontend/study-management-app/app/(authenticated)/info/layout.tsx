"use client";

import BackButton from "@/components/layout/BackButton";

export default function infoLayout({
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
