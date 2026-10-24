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
    <div className="mx-auto flex w-full flex-row justify-between mb-[20px]">
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