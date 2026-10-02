"use client"

import Button from "../ui/Button"

type TimerButtonsProps = {
  handleClick: () => void,
  isRunning: boolean,
  reset: () => void
}

export default function TimerButtons({
  handleClick,
  isRunning,
  reset
}: TimerButtonsProps) {
  return (
    <div className="w-full mx-auto flex flex-row justify-center items-center gap-[200px] mb-[50px]">
      <div>          
        <Button 
          onClick={handleClick} 
          variant="startOrStop"
        >
          {!isRunning? "START" : "STOP"}
        </Button>
      </div>
      <div>
        <Button 
          onClick={reset} 
          variant="reset"
          disabled={!isRunning? false : true}
        > 
          RESET
        </Button>
      </div>
    </div>
  )
}