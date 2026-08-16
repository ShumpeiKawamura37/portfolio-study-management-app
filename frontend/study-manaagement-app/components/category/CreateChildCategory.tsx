"use client"

import { CategoryResponse } from "@/types/category/category";
import CategoryItemFlame from "./CategoryItemFlame";
import EditCategoryName from "./EditCategoryName";
import { useState } from "react";
import { onKeyDown } from "@/utils/inputAction/onKeyDown";
import { createCategory } from "@/service/category/CategoryService";
import { useCategory } from "@/hooks/category/UseCategory";
import { useActionForCategory } from "@/hooks/category/UseActionForCategory";

const INDENT_WIDTH = 12;

type CreateCategoryProps = {
  parentCategoryId: number
}

export default function CreateChildCategory({
  parentCategoryId
}: CreateCategoryProps) {
  const [newCategoryName, setNewCategoryName] = useState("");
  const category = useCategory();
  const actionForCategory = useActionForCategory();

  const handleKeyDown = async(e: React.KeyboardEvent<HTMLInputElement>) => {
    try {
      if (e.key !== "Enter" || e.nativeEvent.isComposing) {
      return
      }
      onKeyDown(e);
      
      const res = await createCategory(newCategoryName, parentCategoryId);
      actionForCategory?.setAction(null);
      category?.addCategory(parentCategoryId, res.data)
      setNewCategoryName("");
    } catch(error: Error | any) {
      alert(error);
    }
  }

  return (
    <>
      {/* 縦線 */}
      <div 
        className="absolute -top-2 w-[1px] bg-gray-400 h-5"
        style={{left: -INDENT_WIDTH * 2}}
      />

      {/* 横線 */}
      <div 
        className="absolute top-3 h-[1px] bg-gray-400 w-6" 
        style={{left: -INDENT_WIDTH * 2}}
      />

      <EditCategoryName
      newCategoryName={newCategoryName}
      setNewCategoryName={setNewCategoryName}
      handleKeyDown={handleKeyDown}
      />
    </>
    
  );
}