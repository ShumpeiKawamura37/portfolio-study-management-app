"use client";

import { RecordContext } from "@/context/RecordContext";
import { useContext } from "react";

export const useRecord = () => {
  const context = useContext(RecordContext);
  if (context === null) {
    throw new Error("useRecord must be used within RecordProvider");
  }

  return context;
}