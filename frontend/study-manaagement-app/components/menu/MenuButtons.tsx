"use client"

import React, { useState } from "react"
import Button from "../ui/Button"
import { useRouter } from "next/navigation";

export default function MenuButtons() {
  const router = useRouter();
  const [action, setAction] = useState<"record" | "data" | null>(null);
  const handleClick = (selectedAction: "record" | "data") => {
    setAction(selectedAction);
  };
  const handleSubmit = (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();

    switch (action) {
      case "record":
        router.push("/record")
        break;
      case "data":
        router.push("/analyzeStudyLog")
        break;
      default:
        return null;
    }
  }

  return (
    <form className="w-64 flex flex-col items-center mx-auto mt-[170px]" onSubmit={(e) => handleSubmit(e as React.SubmitEvent<HTMLFormElement>)}>
      <div className="my-[50px]">
        <Button
          type="submit"
          onClick={() => handleClick("record")} 
          variant="primary"
        >
          学習を記録する
        </Button>
      </div>

      <div className="my-[50px]">
        <Button 
          type="submit"
          onClick={() => handleClick("data")} 
          variant="primary"
        >
          学習データ
        </Button>
      </div>
    </form>
  )
}