"use client";

import { CategoryContext } from "@/context/CategoryContext";
import { useContext } from "react";

export const useCategory = () => {
  return useContext(CategoryContext);
}