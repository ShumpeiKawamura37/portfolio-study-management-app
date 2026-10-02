"use client"
import { ActionForCategoryContext } from "@/context/ActionForCategoryContext";
import { useContext } from "react";

export const useActionForCategory = () => {
  return useContext(ActionForCategoryContext);
}