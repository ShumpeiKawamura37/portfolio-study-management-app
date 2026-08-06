"use client"

import { SetStateAction } from "react"

type CategoryTrianagleMenuProps = {
  setIsOpen: React.Dispatch<SetStateAction<boolean>>
  triangleRef: React.RefObject<HTMLSpanElement | null>
}

export default function CategoryTriangleMenu({
  setIsOpen,
  triangleRef
}: CategoryTrianagleMenuProps) {
  return (
    <span 
      className="
        absolute top-[calc(50%-3px)] right-[10px]
        w-0 h-0 
        border-l-[6px] border-l-transparent
        border-r-[6px] border-r-transparent
        border-t-[8px] border-t-[#B7B7B7]"
      onClick={()=> setIsOpen((prev) => !prev)}
      ref={triangleRef}
    ></span>
  )
}