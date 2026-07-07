"use client"

type InputBoxProps = {
  type: "email" | "password" | "text"
  value: string,
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export default function InputBox({
  type,
  value,
  onChange
} : InputBoxProps) {
  return (
    <>
      <input 
        type={type} 
        id={type}
        name={type}
        value={value}
        onChange={onChange}
        className="border border-black w-[250px] px-1 py-1" 
        required
      />
    </>
  )
}