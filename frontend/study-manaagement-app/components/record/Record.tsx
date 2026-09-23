"use client"

import { useEffect, useState } from "react";
import Toggle from "../ui/Toggle";
import TimerDisplay from "./TimerDisplay";
import TimerSet from "./TimerSet";
import { formatTime } from "@/utils/timer/formatTime";
import TimerButtons from "./TimerButtons";
import CategoryTree from "../category/CategoryTree";
import Button from "../ui/Button";
import InputMemo from "./InputMemo";
import { createStudyLog } from "@/service/studyLog/StudyLogService";
import { useRecord } from "@/hooks/record/useRecord";
import { useRouter } from "next/navigation";

export default function Record() {
  const router = useRouter();
  const {
    isTimer, setStudyLogVersion, startTime, endTime, targetCategoryId, setSeconds , isRunning, stop, start, reset , totalStudySeconds, memo, seconds, toggleTimer
  } = useRecord();

  const submittable: boolean = startTime !== null && endTime !== null && targetCategoryId !== null;

  const handleClick = () => {
    if(isRunning) {
      stop();  
    } else {
      start(isTimer);
    }
  }

  const onSubmit = async(e: React.SubmitEvent<HTMLElement>) => {
    e.preventDefault();
    try {
      if(startTime === null || endTime === null || targetCategoryId === null) {
        return;
      }
      await createStudyLog(startTime, endTime, totalStudySeconds, memo, targetCategoryId);
      setStudyLogVersion(prev => prev + 1);
      router.push("/analyzeStudyLog");
      setSeconds(0);
    } catch(error: Error | any) {
      alert(error);
    }
  }

  useEffect(() => {
    return () => {
    stop();
  };
  },[])

  return (
    <>
      <form onSubmit={onSubmit}>
        <div className="max-w-[635px] mx-auto flex flex-col items-center justify-center py-[50px] mt-[50px]">
          <div className="mb-[50px]">
            <Toggle isOn={isTimer} onToggle={toggleTimer} leftLabel="タイマー" rightLabel="ストップウォッチ" />
          </div>

          <TimerDisplay time={formatTime(seconds)}/>

          <TimerButtons handleClick={handleClick} isRunning={isRunning} reset={reset} />

          {!isTimer? (
            <TimerSet setTotalSeconds={setSeconds}/>
          ): null
          }

          <>
            <CategoryTree />
            
            <InputMemo />

            <Button 
              onClick={()=>console.log()}
              type="submit" 
              variant={`${submittable? "primary" : "disabled"}`}
              disabled={!submittable}
            >
              保存する
            </Button>
            <div className="h-[20px]">
              {totalStudySeconds !== 0 && isRunning === false ? (
                null
              ): (
                <p className="text-[14px] text-red-500">
                  ・学習時間を計測していません
                </p>
              )}
              {!targetCategoryId && (
                <p className="text-[14px] text-red-500">
                  ・カテゴリが選択されていません
                </p>
              )}
            </div>
          </>
        </div>
      </form>
    </>
  )
}

