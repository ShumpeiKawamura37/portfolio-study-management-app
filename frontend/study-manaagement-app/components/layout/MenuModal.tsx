"use client"
import { useRouter } from "next/navigation";
import Button from "../ui/Button";
import { useState } from "react";

export default function MenuModal({isOpen, onClose}: {isOpen: boolean, onClose: () => void}) {
  const router = useRouter();
  const handleClick = (action: "info" | "logout") => {
    switch(action) {
      case "info":
        onClose();
        router.push("/info");
        break;
      case "logout":
        localStorage.removeItem("token");
        onClose();
        router.push("/auth");
    }
  }
  return (
    <>
      <div className={`
          fixed top-[120px] left-0 right-0 bottom-0 
          flex items-center justify-center 
          bg-black/50 z-50
          ${ isOpen ? "opacity-100" : "opacity-0 pointer-events-none"} transition-opacity duration-300
        `}
        onClick={onClose}
      >
        <div 
          className="bg-white p-10 w-[500px] flex flex-col items-center justify-center  mx-auto z-100" 
          onClick={(e) => e.stopPropagation()}>
          <div className="my-[50px]">
            <Button 
            onClick={() => handleClick("info")} variant="primary"
            >
              ユーザー情報
            </Button>
          </div>
          <div className="my-[50px]">
            <Button 
              onClick={() => handleClick("logout")} variant="secondary"
            >
              ログアウト
            </Button>
          </div>
        </div>
      </div> 
    </>
  )
}