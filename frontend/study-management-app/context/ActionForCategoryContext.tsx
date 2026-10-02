"use client"

import { createContext, ReactNode, SetStateAction, useState } from "react";

type ActionForCategory = "create" | "update" | null;

type ActionForCategoryContextType = {
  action: ActionForCategory
  setAction: React.Dispatch<SetStateAction<ActionForCategory>>
  parentCategoryId: number | null
  setParentCategoryId: React.Dispatch<SetStateAction<number | null>>
}

export const ActionForCategoryContext = createContext<ActionForCategoryContextType | null>(null);

export function ActionForCategoryProvider({children}: {children: ReactNode}) {
  const [action, setAction] = useState<ActionForCategory>(null);
  const [parentCategoryId, setParentCategoryId] = useState<number | null>(null);
  return (
    <ActionForCategoryContext.Provider
      value={{
        action,
        setAction,
        parentCategoryId,
        setParentCategoryId
      }}
    >
      {children}
    </ActionForCategoryContext.Provider>
  )
}