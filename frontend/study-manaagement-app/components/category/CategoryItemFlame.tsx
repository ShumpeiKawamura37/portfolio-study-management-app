"use client"

import { useRecord } from "@/hooks/record/useRecord";
import { ReactNode, useRef } from "react"

type CategoryItemFlameProps = {
  children: ReactNode,
  categoryId?: number,
  onClick?: (e: React.MouseEvent) => void
}

export default function CategoryItemFlame({
  children,
  categoryId,
  onClick
}: CategoryItemFlameProps) {
  const { targetCategoryId } = useRecord();
  const isFocused = targetCategoryId === categoryId;
  const categoryItemRef = useRef<HTMLDivElement>(null); 

  return (
    
    <div 
    className={`relative flex items-center w-[160px] h-[24px] rounded-sm scrollbar-hide border ${!isFocused? "border-[#B7B7B7]": "border-blue-600"}`}
    onClick={onClick}
    ref={categoryItemRef}
    data-category-node
    >
      {children}
    </div>
  )
}