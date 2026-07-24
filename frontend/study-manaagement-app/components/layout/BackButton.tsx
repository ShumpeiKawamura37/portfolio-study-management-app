"use client"

import { useRouter } from "next/navigation"
import Button from "../ui/Button"

export default function BackButton() {
  const router = useRouter();
  return (
    <div className="absolute top-[140px] left-[30px]">
      <Button onClick={() => router.back()} variant="back">
        戻る
      </Button>
    </div>
  )
}