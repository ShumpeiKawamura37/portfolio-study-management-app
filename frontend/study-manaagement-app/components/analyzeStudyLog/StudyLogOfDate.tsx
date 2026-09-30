"use client";

import { useState } from "react";
import Calender from "../ui/Calender";
import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import Button from "../ui/Button";
import StudyLogAnalytics from "./StudyLogAnalytics";

export default function StudyLogOfDate() {
  const { targetDate, setTargetDate } = useAnalytics();
  const handleClickDayButton = (action: "prev" | "next") => {
    switch(action) {
      case "prev":
        console.log("変更前:", targetDate);
        setTargetDate((prev) => {
          const date = new Date(prev);
          date.setDate(date.getDate() - 1)
          console.log("変更後:", date);
          return date;
        });
        break;
      case "next":
        console.log("変更前:", targetDate);
        setTargetDate((prev) => {
          const date = new Date(prev);
          date.setDate(date.getDate() + 1)
          console.log("変更後:", date);
          return date;
        });
        break
      default:
        return;
    }
  }
  return (
    <div className=" w-[430px] h-[300px] border border-[#B7B7B7] overflow-scroll scrollbar-hide">
      <div className="w-[200px] p-4 mx-auto flex gap-3 justify-between items-center">
        <Button 
          onClick={() => {
            handleClickDayButton("prev")
          }}
          variant="turnDate"
        >
          前日
        </Button>
        <Calender />

        <Button 
          onClick={()=>{
            handleClickDayButton("next")
          }}
          variant="turnDate"
        >
          翌日
        </Button>
      </div>

      <StudyLogAnalytics/>
      
    </div>
  )
}

