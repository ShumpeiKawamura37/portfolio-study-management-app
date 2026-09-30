"use client";

import { CategoryContext } from "@/context/CategoryContext";
import { useContext } from "react";

export const useCategory = () => {
  const context = useContext(CategoryContext);
  if (context === null) {
    throw new Error("useCategory must be used within RecordProvider");
  }

  return context;
}