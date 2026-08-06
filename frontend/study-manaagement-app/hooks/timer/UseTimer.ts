"use client";

import { useEffect, useRef, useState } from "react";
export const useTimer = () => {
  const [seconds, setSeconds] = useState(0);
  const [startTime, setStartTime] = useState<Date | null>(null);
  const [endTime, setEndTime] = useState<Date | null>(null);
  const [totalStudySeconds, setTotalStudySeconds] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const intervalRef = useRef<number | null>(null);

  const start = (isTimer: boolean) => {
    if(intervalRef.current !== null) {
        return; 
    }
    setIsRunning(true); 
    setStartTime(new Date());
    // タイマーとストップウォッチで分岐
    if(isTimer) {
      intervalRef.current = window.setInterval(() => { 
        setTotalStudySeconds((prev) => prev + 1);
        setSeconds((prev)=> prev + 1); 
      }, 1000); 
    } else { 
      intervalRef.current = window.setInterval(() => { 
        // 0になったら終了
        setTotalStudySeconds((prev) => prev + 1);
        setSeconds((prev) => {
          if (prev <= 1) {
            clearInterval(intervalRef.current!);
            intervalRef.current = null;
            setIsRunning(false);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }
  }

  const stop = () => {
    if(intervalRef.current !== null) {
      window.clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
    setIsRunning(false);
    setEndTime(new Date());
  }

  const reset = () => {
    const isConfirmed = confirm("学習計測をリセットしますか？");
    if(isConfirmed) {
      setSeconds(0);
      setTotalStudySeconds(0);
      setStartTime(null);
      setEndTime(null);
    };
  }

  useEffect(() => {
    return () => stop();
  }, []);

  return {
    seconds,
    setSeconds,
    startTime,
    endTime,
    totalStudySeconds,
    isRunning,
    start,
    stop,
    reset
  }
}
