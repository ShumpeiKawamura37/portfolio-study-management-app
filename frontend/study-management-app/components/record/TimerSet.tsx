"use client"
import { useEffect, useState } from "react"
import TimerSetItem from "./TimeSetItem";

type TimerSetProps = {
  setTotalSeconds: (value: number) => void,
}
export default function TimerSet({ 
  setTotalSeconds 
} : TimerSetProps) {

  const [hour, setHour] = useState("00");
  const [minute, setMinute] = useState("00");
  const [second, setSecond] = useState("00");

  useEffect(() => {
    const total = 
      (Number(hour) * 60 * 60) +
      (Number(minute) * 60) +
      Number(second);
    setTotalSeconds(total);
  }, [ hour, minute, second ]);

  return (
    <div className="my-[50px] flex flex-row border  border-[#B7B7B7] px-5 py-2 rounded-3xl">
      <div className="flex items-center gap-2">
        <TimerSetItem time={hour} setTime={setHour} maxNum={24}/>
        <span>時間</span>
      </div>
      <div className="flex items-center gap-2">
        <TimerSetItem time={minute} setTime={setMinute} maxNum={60}/>
        <span>分</span>
      </div>
      
      <div className="flex items-center gap-2">
        <TimerSetItem time={second} setTime={setSecond} maxNum={60}/>
        <span>秒</span>
      </div>
    </div>
  )
}