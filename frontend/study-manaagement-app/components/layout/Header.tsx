"use client";

import { useState } from "react";
import MenuModal from "./MenuModal";

export default function Header({isLogin} : {isLogin : boolean}) {
  const [isOpen, setIsOpen] = useState(false);
  const onClose = () => setIsOpen(false);
  return (
    <header className="bg-primary h-[120px] w-full mb-[50px] px-2 mb-[120px]">
      <div className=" max-w-screen-xl mx-auto flex justify-end">
        {/* ハンバーガー */}
        { isLogin && (
          <div className=" w-[120px] h-[120px] flex items-center justify-center">
            <button 
              onClick={() => setIsOpen(!isOpen)}
              className="w-10 h-6 relative">
              <span className={`
                  absolute left-0 w-full h-[3px] bg-white transition-all duration-300
                  ${isOpen ? "top-1/2 rotate-45" : "top-0"} 
                `} 
              />
              <span className={`
                  absolute left-0 w-full h-[3px] bg-white transition-all duration-300
                  ${isOpen? "opacity-0" : "top-1/2 -translate-y-1/2"}  
                `} 
              />
              <span className={`
                  absolute left-0 w-full h-[3px] bg-white transition-all duration-300
                  ${isOpen ? "top-1/2 -rotate-45" : "bottom-0"} 
                `} 
              />
            </button>
          </div>
        )}
      </div>
      <MenuModal isOpen={isOpen} onClose={onClose}/>
    </header>
  )
}
