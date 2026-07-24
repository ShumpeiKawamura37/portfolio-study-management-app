"use client"

import React, { ReactNode } from "react"

type CategoryItemFlameProps = {
  children: ReactNode
}

export default function CategoryItemFlame({
  children,
}: CategoryItemFlameProps) {
  return (
    <div 
      className="relative flex items-center relative w-[160px] h-[25px] border border-[#B7B7B7] rounded-sm scrollbar-hide"
    >
      {children}
    </div>
  )
}