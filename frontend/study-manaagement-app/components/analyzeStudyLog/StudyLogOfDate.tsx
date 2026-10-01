"use client";

import Calender from "../ui/Calender";
import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import Button from "../ui/Button";
import StudyLogAnalytics from "./StudyLogAnalytics";

export default function StudyLogOfDate() {
  const { setTargetDate } = useAnalytics();
  const handleClickDayButton = (action: "prev" | "next") => {
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

