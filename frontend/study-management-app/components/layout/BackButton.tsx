"use client"

import { usePathname, useRouter } from "next/navigation"
import Button from "../ui/Button"

export default function BackButton() {
  const router = useRouter();

  const handleBack = () => {
    router.back();
  }
  return (
    <div className="absolute top-[140px] left-[30px] z-10">
      <Button 
        onClick={() => 
          handleBack()
        } 
        variant="back">
        戻る
      </Button>
    </div>
  )
}