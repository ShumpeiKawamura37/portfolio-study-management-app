"use client"

import { useEffect, useRef, useState } from "react";

type TimeSetItemProps = {
  time: string,
  setTime: (value: string) => void,
  maxNum: number
}

export default function TimeSetItem({ time, setTime, maxNum } : TimeSetItemProps) {
  const [isOpen, setIsOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);


  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const handleChange = (value: string) => {
    if(!/^\d(0,2)$/.test(value)) return;

    if(value == "") {
      setTime("");
      return;
    }

    if(Number(value) >= maxNum) return;
    
    setTime(value);
  } 

  const handleBlur = () => {
    if (time === "") {
      setTime("00");
    } else {
      setTime(time.padStart(2, "0"));
    }
  };
  
  const handleSelect = (value: string) => {
    setTime(value);
    setIsOpen(false);
  };

  
  return (
    <div ref={containerRef} className="relative flex items-center">
      <input 
        type="text" 
        inputMode="numeric"
        value={time}
        onChange={(e) => handleChange(e.target.value)}
        onBlur={handleBlur}
        maxLength={2}
        className="w-[40px] px-1 py-1 items-center"
        />
        <button
          type="button"
          onClick={()=> setIsOpen((prev) => !prev)}
        >
          <span
            className="block h-2 w-2 border-b-2 border-r-2 border-gray-600 transition-transform"
            style={{
              transform: isOpen ? "rotate(225deg)" : "rotate(45deg)",
            }}
          />
        </button>
      {isOpen && (
        <ul 
          className="absolute left-0 top-full z-50 mt-1 py-1 max-h-40 w-[80px] overflow-y-auto rounded border border-gray-300 bg-white shadow-lg"
        >
          {Array.from({length: maxNum}, (_, i) => {
            const value = String(i).padStart(2, "0");
            return (
              <li key={value}>
                <button
                  type="button"
                  onClick={() => handleSelect(value)}
                  className="w-full"
                >
                  {i}
                </button>
              </li>
            )
          })}
        </ul>
      )}
    </div>
  )
}