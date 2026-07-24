"use client"
export const formatTime = (totalSeconds: number): string => {
  const hours = Math.floor(totalSeconds / 60 / 60);
  const minutes = Math.floor((totalSeconds / 60) % 60);
  const seconds = Math.floor(totalSeconds % 60);
  
  const pad = (num: number) => String(num).padStart(2, "0");

  return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
};