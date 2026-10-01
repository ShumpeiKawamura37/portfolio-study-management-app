"use client"

import { usePathname, useRouter } from "next/navigation"
import Button from "../ui/Button"
import { useRecord } from "@/hooks/record/useRecord";

export default function RecordBackButton() {
  const pathname = usePathname();
  const router = useRouter();
  const { totalStudySeconds, reset } = useRecord();

  const handleBack = () => {
    if(pathname === "/record") {
      if(totalStudySeconds != null && totalStudySeconds != 0) {
        reset();
      }
    }
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
