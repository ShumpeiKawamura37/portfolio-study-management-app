"use client"

import { useRecord } from "@/hooks/record/useRecord";


export default function InputMemo() {
  const { memo, setMemo } = useRecord();

  const onChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setMemo(e.target.value);
  }

  return (
    <textarea name="memo"
      className="border border-[#B7B7B7] px-1 py-1 w-[250px] h-[100px] overflow-scroll scrollbar-hide"
      value={memo}
      placeholder="学習メモ(任意)"
      onChange={onChange}
    >
    </textarea>
  )
}
