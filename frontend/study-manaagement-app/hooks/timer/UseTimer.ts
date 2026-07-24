"use client";

import { useEffect, useRef, useState } from "react";
export const useTimer = () => {
  const [seconds, setSeconds] = useState(0);
  const [totalStudySeconds, setTotalStudySeconds] = useState(0);
  const [isRunning, setIsRunning] = useState(false);
  const intervalRef = useRef<number | null>(null);

  const start = (isTimer: boolean) => {
    if(intervalRef.current !== null) {
        return; 
    }
    setIsRunning(true); 
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
  }

  const reset = () => {
    const isConfirmed = confirm("学習計測をリセットしますか？");
    if(isConfirmed) {
      setSeconds(0);
      setTotalStudySeconds(0);
    };
  }

  useEffect(() => {
    return () => stop();
  }, []);

  return {
    seconds,
    setSeconds,
    totalStudySeconds,
    isRunning,
    start,
    stop,
    reset
  }
}
