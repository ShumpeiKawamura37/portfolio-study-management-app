"use client";

import { useState } from "react";
import Calender from "../ui/Calender";
import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import Button from "../ui/Button";
import StudyLogAnalytics from "./StudyLogAnalytics";

export default function StudyLogOfDate() {
  const { setTargetDate } = useAnalytics();
  const [action, setAction] = useState<"prev" | "next">("prev");
  const handleClickDayButton = () => {
    switch(action) {
      case "prev":
        setTargetDate((prev) => {
          const date = new Date(prev);
          date.setDate(date.getDate() - 1)
          return date;
        });
        break;
      case "next":
        setTargetDate((prev) => {
          const date = new Date(prev);
          date.setDate(date.getDate() + 1)
          return date;
        });
        break
      default:
        return;
    }
  }
  return (
    <div className=" w-[400px] border border-[#B7B7B7]">
      <div className="flex justify-between px-20 py-4 items-center">
        <Button 
          onClick={() => {
            setAction("prev");
            handleClickDayButton()
          }}
          variant="turnDate"
        >
          前日
        </Button>
        <Calender />

        <Button 
          onClick={()=>{
            setAction("next");
            handleClickDayButton()
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