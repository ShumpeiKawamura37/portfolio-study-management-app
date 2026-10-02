"use client"

type TextFlameProps = {
  text: string,
}
export default function TextFlame({
  text
}: TextFlameProps) {
  return (
    <div className="border border-black w-[250px] px-1 py-1" >
      {text}
    </div>
  )
}