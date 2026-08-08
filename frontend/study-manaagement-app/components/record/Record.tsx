"use client"

import { useState } from "react";
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
  const [isTimer, setIsTimer] = useState(true);
  const record = useRecord();
  const submittable: boolean = record.startTime !== null && record.endTime !== null && record.targetCategoryId !== null;
  const toggleTimer = () => {
    setIsTimer(!isTimer)
    record.setSeconds(0);
  }
  const handleClick = () => {
    if(record.isRunning) {
      record.stop();  
    } else {
      record.start(isTimer);
    }
  }

  const onSubmit = async(e: React.SubmitEvent<HTMLElement>) => {
    e.preventDefault();
    try {
      if(record.startTime === null || record.endTime === null || record.targetCategoryId === null) {
        return;
      }
      await createStudyLog(record.startTime, record.endTime, record.seconds, record.memo, record.targetCategoryId);
      router.push("/analyzeStudyLog");
    } catch(error: Error | any) {
      alert(error);
    }
  }

  return (
    <>
      <form onSubmit={onSubmit}>
        <div className="max-w-[635px] mx-auto flex flex-col items-center justify-center py-[50px] mt-[50px]">
          <div className="mb-[50px]">
            <Toggle isOn={isTimer} onToggle={toggleTimer} leftLabel="タイマー" rightLabel="ストップウォッチ" />
          </div>

          <TimerDisplay time={formatTime(record.seconds)}/>

          <TimerButtons handleClick={handleClick} isRunning={record.isRunning} reset={record.reset} />

          {!isTimer? (
            <TimerSet setTotalSeconds={record.setSeconds}/>
          ): null
          }

          <>
            <div className="mb-2">
              <CategoryTree />
            </div>
            <div className="mb-2">
              <InputMemo />
            </div>
            

            <Button 
              onClick={() => console.log()} 
              type="submit" 
              variant={`${submittable? "primary" : "disabled"}`}
              disabled={!submittable}
            >
              保存する
            </Button>
            <div className="h-[20px]">
              {record.seconds !== 0 && record.isRunning === false ? (
                null
              ): (
                <p className="text-[14px] text-red-500">
                  ・学習時間を計測していません
                </p>
              )}
              {!record.targetCategoryId && (
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