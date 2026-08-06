"use client"
import { useCategoryState } from "@/hooks/category/useCategoryState";
import { createContext, ReactNode } from "react";

type CategoryContextType = ReturnType<typeof useCategoryState>;


export const CategoryContext = createContext<CategoryContextType | null>(null);

export function CategoryProvider({children} : {children: ReactNode}) {

  const category = useCategoryState();

  return (
    <CategoryContext.Provider
      value={category} 
    >
      {children}
    </CategoryContext.Provider>
  )
}