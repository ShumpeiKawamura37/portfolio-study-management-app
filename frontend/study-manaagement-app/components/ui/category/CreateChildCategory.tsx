"use client"

import { CategoryResponse } from "@/types/category/category";
import CategoryItemFlame from "./CategoryItemFlame";
import EditCategoryName from "./EditCategoryName";
import { useState } from "react";
import { onKeyDown } from "@/utils/inputAction/onKeyDown";
import { createCategory } from "@/service/category/CategoryService";
import { useCategory } from "@/hooks/category/UseCategory";
import { useActionForCategory } from "@/hooks/category/UseActionForCategory";

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
    <CategoryItemFlame>
        <EditCategoryName
          newCategoryName={newCategoryName}
          setNewCategoryName={setNewCategoryName}
          handleKeyDown={handleKeyDown}
        />
    </CategoryItemFlame>
  );
}