"use client";

import { useEffect, useRef, useState } from "react";
import { DayPicker } from "react-day-picker";
import Button from "./Button";
import { useAnalytics } from "@/hooks/analytics/useAnalytics";
import "react-day-picker/style.css";
import { ja } from "date-fns/locale";

export default function Calender() {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const {targetDate, setTargetDate} = useAnalytics();
  const calenderRef = useRef<HTMLDivElement>(null);

  const formatDate = (date: Date) => {
    if(!date) {
      return "";
    }
    return new Date(date).toLocaleString("ja-JP", {
      year: "numeric",
      month: "numeric",
      day: "numeric",
    });
  };

  useEffect(() => {
    const handleClickOutSide = (e: MouseEvent) => {
      if(calenderRef.current && 
        !calenderRef.current?.contains(e.target as Node)
      ) {
        setIsOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutSide);

    return () => {
      document.removeEventListener("mousedown", handleClickOutSide);
    }
  }, []);

  return (
    <div className="flex flex-col items-center relative">
      <div>
        <Button
        onClick={() => setIsOpen(prev => !prev)}
        variant="calender"
        type="button"
        >
          {formatDate(targetDate)}
        </Button>
      </div>
      {isOpen && (
        <div 
        className="absolute -top-10 -right-10 w-[200px] h-[200px]"
        ref={calenderRef}
        >
          <DayPicker
            selected={targetDate}
            onSelect={(date)=> {
              if(date) {
                setTargetDate(date);
              }
            }}
            mode="single"
            locale={ja}
            className="w-full h-full bg-white shadow-lg border"
            classNames={{

              root: "w-full h-full",

              month: "w-full h-full",
              month_grid: "w-[200px]",

              month_caption: "flex items-center justify-center h-7",
              caption_label: "text-sm",

              nav: "absolute inset-x-0 top-0 h-8",
              button_previous:
                "absolute left-2 top-1/2 -translate-y-1/2",
              button_next:
                "absolute right-2 top-1/2 -translate-y-1/2",
              day_button:
                "text-xs",
            }}
            modifiers={{
              sunday: { dayOfWeek: [0] },
              saturday: { dayOfWeek: [6] },
            }}
            modifiersClassNames={{
              sunday: "text-red-500",
              saturday: "text-blue-500",
            }}
          />
        </div>
      )}
    </div>
  )
}
