"use client"

import { createCategory } from "@/service/category/CategoryService";
import { onKeyDown } from "@/utils/inputAction/onKeyDown";
import EditCategoryName from "./EditCategoryName";
import { useState } from "react";
import { useCategory } from "@/hooks/category/useCategory";

type CreateCategoryProps = {
}

export default function CreateCategory({
}: CreateCategoryProps) {
  const category = useCategory();
  const [isEditing, setIsEditing] = useState(false)
  const [newCategoryName, setNewCategoryName] = useState("");


  const handleKeyDown = async(e: React.KeyboardEvent<HTMLInputElement>) => {
    try {
      if (e.key !== "Enter" || e.nativeEvent.isComposing) {
      return
      }
      onKeyDown(e);
      
      const res = await createCategory(newCategoryName, null);
      category?.addCategory(null, res.data);
      setNewCategoryName("");
      setIsEditing(false);
    } catch(error: Error | any) {
      alert(error);
    }
  }

  return (
    <>
      { !isEditing? (
        <div 
          className="flex items-center relative w-full h-full px-[3px] py-[3px]"
          onDoubleClick={()=>setIsEditing(true)}
        >
          カテゴリを作成する
        </div>
      ): (
        <EditCategoryName
          newCategoryName={newCategoryName}
          setNewCategoryName={setNewCategoryName}
          handleKeyDown={handleKeyDown}
        />
      )}
    </>
  );
}