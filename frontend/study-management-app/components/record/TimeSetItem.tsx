"use client"

type TimeSetItemProps = {
  time: string,
  setTime: (value: string) => void,
  maxNum: number
}

export default function TimeSetItem({ time, setTime, maxNum } : TimeSetItemProps) {
  
  return (
    <>
      <select 
        value={time} 
        onChange={(e) => setTime(e.target.value)}
        className="w-[64px] px-1 py-1"
      >
        {
          Array.from({length: maxNum},(num, i) => {
            const value = String(i).padStart(2, "0");
            return (
              <option key={i} value={value}>
                {value}
              </option>
            )
          })
        }
      </select>
    </>
  )
}