"use client"

import { useActionForCategory } from "@/hooks/category/UseActionForCategory";
import { useEffect, useRef } from "react";


type EditCategoryNameProps = {
  newCategoryName: string,
  setNewCategoryName: React.Dispatch<React.SetStateAction<string>>,
  handleKeyDown: (e: React.KeyboardEvent<HTMLInputElement>) => void,
}

export default function EditCategoryName({
  newCategoryName,
  setNewCategoryName,
  handleKeyDown
}: EditCategoryNameProps) {
  const actionForCategory = useActionForCategory();

  // onDoubleClick用にinput要素取得
  const inputRef = useRef<HTMLInputElement>(null);
  const onBlur = () => {
    actionForCategory?.setAction(null)
    setNewCategoryName("");
  }
  // ダブルクリック後、そのままinputにフォーカスを当てる
  useEffect(() => {
    if (actionForCategory?.action !== null) {
      inputRef.current?.focus();
    }
  }, [actionForCategory?.action]);
  
  return (
      <input 
          type="text"
          ref={inputRef}
          className="w-full h-full px-1 py-1"
          value={newCategoryName}
          onChange={(e: React.ChangeEvent<HTMLInputElement>)=> setNewCategoryName(e.target.value)} 
          onBlur={onBlur}
          onKeyDown={handleKeyDown}
        />
  )
}