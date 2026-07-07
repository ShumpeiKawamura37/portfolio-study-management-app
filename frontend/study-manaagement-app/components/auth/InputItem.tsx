"use client"

import InputBox from "../ui/InputBox"

type InputItemProps = {
  type: "email" | "password" | "text"
  value: string,
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function InputItem({
  type,
  value,
  onChange
}: InputItemProps) {
  const label = {
    email: "メールアドレス",
    password: "パスワード",
    text: "ユーザーネーム" 
  }
  return (
    <>
      <label htmlFor={type} className="text-center text-2xl mb-4">
        {label[type]}
      </label>
      <InputBox 
        type={type}
        value={value}
        onChange={onChange}
      />
    </>
  )
}