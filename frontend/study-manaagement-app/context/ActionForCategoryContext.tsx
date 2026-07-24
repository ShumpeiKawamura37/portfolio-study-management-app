"use client"

import { createContext, ReactNode, SetStateAction, useState } from "react";

type ActionForCategory = "create" | "update" | null;

type ActionForCategoryContextType = {
  action: ActionForCategory
  setAction: React.Dispatch<SetStateAction<ActionForCategory>>
}

export const ActionForCategoryContext = createContext<ActionForCategoryContextType | null>(null);

export function ActionForCategoryProvider({children}: {children: ReactNode}) {
  const [action, setAction] = useState<ActionForCategory>(null);
  return (
    <ActionForCategoryContext.Provider
      value={{
        action,
        setAction
      }}
    >
      {children}
    </ActionForCategoryContext.Provider>
  )
}