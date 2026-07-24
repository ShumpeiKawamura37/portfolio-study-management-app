type TimerDisplayProps = {
  time: string,
}

export default function TimerDisplay({time} : TimerDisplayProps) {
  return (
    <span className="block text-[96px] mb-[50px]">{time}</span>
  )
}