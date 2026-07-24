"use client"

import { useState } from "react";
import Toggle from "../ui/Toggle";
import TimerDisplay from "./TimerDisplay";
import TimerSet from "./TimerSet";
import { useTimer } from "@/hooks/timer/UseTimer";
import { formatTime } from "@/utils/timer/formatTime";
import TimerButtons from "./TimerButtons";
import CategoryTree from "../ui/category/CategoriyTree";
import { CategoryProvider } from "@/context/CategoryContext";

export default function Record() {

  const [isTimer, setIsTimer] = useState(true);
  const { seconds, setSeconds, isRunning, start, stop, reset } = useTimer();
  const toggleTimer = () => {
    setIsTimer(!isTimer)
    setSeconds(0);
  }
  const handleClick = () => {
    if(isRunning) {
      stop();  
    } else {
      start(isTimer);
    }
  }
  return (
    <div>
      
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

        <CategoryProvider>
          <CategoryTree />
        </CategoryProvider>
      </div>
    </div>
  )
}
