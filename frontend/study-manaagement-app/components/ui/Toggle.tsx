"use client"

type ToggleProps = {
  isOn: boolean,
  leftLabel: string,
  rightLabel: string,
  onToggle: () => void,
}

export default function Toggle({isOn, leftLabel, rightLabel, onToggle} : ToggleProps) {
  const baseStyle = "block w-1/2 h-full text-sm text-white flex items-center justify-center z-10 pointer-events-none";
  return (
    <button onClick={onToggle} className="relative bg-[#B9D4CC] w-[290px] h-[45px] rounded-2xl overflow-hidden">
      <div
        className={`
          absolute top-0 left-0 h-full w-1/2 bg-[#53DEB7] rounded-2xl
          transition-transform duration-100 ease-in-out
          ${isOn ? "translate-x-full" : "translate-x-0"}
        `}
        style={{ pointerEvents: "none" }}
      />
      <div className="flex h-full">
        <span className={baseStyle}>{leftLabel}</span>
        <span className={baseStyle}>{rightLabel}</span>
      </div>
    </button>
  )
}