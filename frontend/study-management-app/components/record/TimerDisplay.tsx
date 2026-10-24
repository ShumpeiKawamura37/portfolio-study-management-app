type TimerDisplayProps = {
  time: string,
}

export default function TimerDisplay({time} : TimerDisplayProps) {
  return (
    <span className="mx-auto block mb-[50px] text-[60px] md:text-[96px]">{time}</span>
  )
}