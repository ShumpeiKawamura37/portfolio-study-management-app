"use client"

import React from "react";

type ButtonProps = {
  onClick: () => void;
  children: React.ReactNode;
  type?: "button" | "submit";
  variant?: "primary" | "secondary" | "back" | "edit" | "delete" | "startOrStop" | "reset" ;
  disabled?: boolean;
}

export default function Button({
  onClick,
  children,
  type = "button",
  variant = "primary",
  disabled = false,
} : ButtonProps) {
  const baseStyle = "w-[250px] py-2 px-4 rounded-2xl bg-[#53DEB7] active:scale-95 transition text-2xl justify-center flex items-center";

  const variantStyle = {
    primary: "hover:bg-[#64ebc5] text-white", 
    secondary:"bg-[#E1E1E1] hover:bg-[#f2f0f0] text-black",
    back: "w-auto px-2 py-3 round-sm text-sm hover:bg-[#64ebc5] text-white",
    edit: "w-auto px-[8px] h-[30px] bg-[#E1E1E1] hover:bg-[#f2f0f0] rounded-md text-black text-sm",
    delete: "bg-[#DE5353] hover:bg-[#ff6b6b] text-white ",
    startOrStop: "w-[85px] h-[85px] hover:bg-[#64ebc5] text-white text-sm rounded-full",
    reset: "w-[85px] h-[85px] bg-[#DE5353] hover:bg-[#ff6b6b] text-white text-sm rounded-full"
  };

  const disabledStyle = disabled 
    ? "opacity-50 cursor-not-allowed pointer-events-none" 
    : "";
  
  return (
      <button
        onClick={onClick}
        type={type}
        className={`${baseStyle} ${variantStyle[variant]} ${disabledStyle}`}
        disabled={disabled}
      >
        {children}
      </button>
  )
}