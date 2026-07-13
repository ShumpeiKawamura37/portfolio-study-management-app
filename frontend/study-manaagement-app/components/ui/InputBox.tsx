"use client"

type InputBoxProps = {
  type: "email" | "password" | "text"
  value: string,
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onKeyDown?: (e: React.KeyboardEvent<HTMLInputElement>) => void;
}

export default function InputBox({
  type,
  value,
  onChange,
  onKeyDown
} : InputBoxProps) {
  return (
    <>
      <input 
        type={type} 
        id={type}
        name={type}
        value={value}
        onChange={onChange}
        onKeyDown={onKeyDown}
        className="border border-black w-[250px] px-1 py-1" 
      />
    </>
  )
}