"use client"

import { useTimer } from "@/hooks/timer/UseTimer";
import React, { createContext, ReactNode, SetStateAction, useState } from "react"


type RecordContextType = ReturnType<typeof useTimer> & {
  targetCategoryId: number | null,
  setTargetCategoryId: React.Dispatch<SetStateAction<number | null>>,
  memo: string,
  setMemo: React.Dispatch<SetStateAction<string>>,
  studyLogVersion: number,
  setStudyLogVersion: React.Dispatch<SetStateAction<number>>
}

export const RecordContext = createContext<RecordContextType | null>(null);

export function RecordProvider({children}: {children: ReactNode}) {
  const timer = useTimer();
  const [targetCategoryId, setTargetCategoryId] = useState<number | null>(null);
  const [memo, setMemo] = useState<string>("");
  const [studyLogVersion, setStudyLogVersion] = useState(0);
  return (
      <RecordContext.Provider
        value={{
          ...timer,
          targetCategoryId,
          setTargetCategoryId,
          memo,
          setMemo,
          studyLogVersion,
          setStudyLogVersion
        }} 
      >
        {children}
      </RecordContext.Provider>
    )
}