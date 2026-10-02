"use client"

import InputItem from "./InputItem"

type InputFormProps = {
  email: string,
  password: string,
  onChangeEmail: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onChangePassword: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function InputForm({
  email,
  password,
  onChangeEmail,
  onChangePassword

}: InputFormProps) {
  return (
    <div className="flex flex-col mx-auto">
      <InputItem 
        type="email" 
        value={email}
        onChange={onChangeEmail}
      />
      <InputItem 
        type="password" 
        value={password}
        onChange={onChangePassword}
      />
    </div>
  )
}
