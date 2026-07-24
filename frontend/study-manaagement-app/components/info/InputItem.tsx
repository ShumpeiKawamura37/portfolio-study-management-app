"use client"

import { useState } from "react";
import Button from "../ui/Button";
import InputBox from "../ui/InputBox";
import TextFlame from "./TextFlame";
import { onKeyDown } from "@/utils/inputAction/onKeyDown";


type InputItemProps = {
  type: "email" | "password" | "text"
  value: string,
  initialValue: string,
  onChange: (value: string) => void;
}

export default function InputItem({
  type,
  value,
  initialValue,
  onChange
}: InputItemProps) {
  const [isEditable, setIsEditable] = useState(false);
  const label = {
    email: "メールアドレス",
    password: "パスワード",
    text: "ユーザーネーム" 
  }

  const onChangeEditValue = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange(e.target.value);
  }

  const handleCancel = () => {
    onChange(initialValue);
    setIsEditable(false);
  }
  return (
    <div className="w-[625px] h-[55px] flex align-center justify-center items-center mb-[30px] grid-cols-3 gap-4">
      <label htmlFor={type} className="text-center px-1 py-1 text-2xl">
        {label[type]}
      </label>
      {
        isEditable ? (
          <InputBox 
            type={type}
            value={value}
            onChange={onChangeEditValue}
            onKeyDown={onKeyDown}
          />
        ) : (
          <TextFlame text={value} />
        )
      }
      {
        !isEditable ? (
          <Button onClick={() => setIsEditable(true)} variant="edit">
            編集
          </Button>
        ) : (
          <Button onClick={handleCancel} variant="edit">
            キャンセル
          </Button>
        )
      }
    </div>
  )
}
