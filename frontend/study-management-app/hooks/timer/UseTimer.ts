"use client";

import { current } from "@reduxjs/toolkit";
import { useRef, useState } from "react";
export const useTimer = () => {
  const [seconds, setSeconds] = useState(0); //学習時間
  const [startTime, setStartTime] = useState<Date | null>(null); //開始時刻
  const [endTime, setEndTime] = useState<Date | null>(null); //終了時刻
  const [totalStudySeconds, setTotalStudySeconds] = useState(0); //合計学習時間
  const [isRunning, setIsRunning] = useState(false); //タイマー作動状態
  const intervalRef = useRef<number | null>(null); //
  const [isTimer, setIsTimer] = useState(false); //タイマーかストップウォッチか

  // 値の保持の為userRef
  const firstStartRef = useRef<number | null>(null); //学習開始時刻
  const pausedAtRef = useRef<number | null>(null); // 停止時刻
  const pausedSecondsRef = useRef(0); //停止時間(m秒)
  const countDownSecondsRef = useRef(0); //タイマー設定時間

  // 有効な学習時間 現在時刻 - 開始時刻 -  停止時間。 ミリ秒→秒に変換
  const getElapsedSeconds = (now: number) => {
    if(firstStartRef.current == null) {
      return 0;
    }
    return Math.floor((now - firstStartRef.current - pausedSecondsRef.current) / 1000);
  }

  const start = (isTimer: boolean) => {
    // 動いている時は処理しない
    if(intervalRef.current !== null) {
        return; 
    }
    const now = Date.now();
    
    if(firstStartRef.current == null) {
      firstStartRef.current = now;
      setStartTime(new Date(now));
      countDownSecondsRef.current = seconds;
    } else if (pausedAtRef.current != null) {
      pausedSecondsRef.current += now - pausedAtRef.current;
      pausedAtRef.current = null;
    }

    setIsRunning(true);

    intervalRef.current = window.setInterval(() => {
      const elapsed = getElapsedSeconds(Date.now());
      setTotalStudySeconds(elapsed);

      if(isTimer) {
        setSeconds(elapsed);
        return;
      }

      const remaining = countDownSecondsRef.current - elapsed;
      if(remaining <= 0) {
        setSeconds(0);
        stop();
        return;
      }
      setSeconds(remaining);
    }, 1000);
  }

  const stop = () => {
    if(intervalRef.current == null) {
      return;
    }
    window.clearInterval(intervalRef.current);
    intervalRef.current = null;

      const now = Date.now();
      pausedAtRef.current = now;
      setTotalStudySeconds(getElapsedSeconds(now));
      setIsRunning(false);
      setEndTime(new Date(now));
  }

  const clear = () => {
    if(intervalRef.current !== null) {
        window.clearInterval(intervalRef.current);
        intervalRef.current = null;
      }
    firstStartRef.current = null;
    pausedAtRef.current = null;
    pausedSecondsRef.current = 0;
    setIsRunning(false);
    setSeconds(0);
    setTotalStudySeconds(0);
    setStartTime(null);
    setEndTime(null);
  }

  const reset = () => {
    const isConfirmed = confirm("学習計測をリセットしますか？");
    if(isConfirmed) {
      clear();
    }
  }

  const toggleTimer = () => {
    reset();
    setIsTimer(prev => !prev);
  }

  return {
    seconds,
    startTime,
    setSeconds,
    endTime,
    totalStudySeconds,
    isRunning,
    start,
    stop,
    clear,
    reset,
    isTimer,
    toggleTimer
  }
}