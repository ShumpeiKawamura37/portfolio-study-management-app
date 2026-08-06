"use client"

import { createCategory, deleteCategory, updateCategory } from "@/service/category/CategoryService";
import { useEffect, useRef, useState } from "react"
import EditCategoryName from "./EditCategoryName";
import { onKeyDown } from "@/utils/inputAction/onKeyDown";
import CategoryItemFlame from "./CategoryItemFlame";
import CateogryMenu from "./CtegoryMenu";
import CategoryTriangleMenu from "./CategoryTriangleMenu";
import { useCategory } from "@/hooks/category/useCategory";
import { useRecord } from "@/hooks/record/useRecord";
import { useActionForCategory } from "@/hooks/category/useActionForCategory";

type CategoryItemProps = {
  categoryId: number,
  categoryName: string
}

export default function CategoryItem({
categoryId,
categoryName,
}: CategoryItemProps) {
  const category = useCategory();
  const actionForCategory = useActionForCategory();
  const [isOpen, setIsOpen] = useState(false);
  const [newCategoryName, setNewCategoryName] = useState("");
  const triangleRef = useRef<HTMLSpanElement>(null);
  const { targetCategoryId, setTargetCategoryId } = useRecord();

  // actionによって処理を分岐する
  const handleKeyDown = async (e: React.KeyboardEvent<HTMLInputElement>) => {
    try{
      if (e.key !== "Enter" || e.nativeEvent.isComposing) {
        return
      }
      onKeyDown(e);

      switch(actionForCategory?.action) {
        case "create":
          const resForCreate = await createCategory(newCategoryName, categoryId);
          category?.addCategory(categoryId, resForCreate.data);
          break;
        case "update":
          const resForUpdate = await updateCategory(categoryId, newCategoryName);
          category?.putCategory(resForUpdate.data);
          break;
      }

      setNewCategoryName("");
      setIsOpen(false);
      actionForCategory?.setAction(null);
      setTargetCategoryId(null);
    } catch(error: Error | any) {
      alert(error);
    }
  } 

  // カテゴリ削除
  const handleDelete = async() => {
    try {
      await deleteCategory(categoryId);
      category?.removeCategory(categoryId);
      setIsOpen(false);
    } catch(error: Error | any) {
      alert(error);
    }
  }

  const handleClick = (e: React.MouseEvent) => {
    
    e.stopPropagation();

    setTargetCategoryId(prev =>
      prev === categoryId ? null : categoryId
    );
  };

  return (
    <>
      <CategoryItemFlame categoryId={categoryId} onClick={handleClick}>
        {actionForCategory?.action !== "update" ? (
          <>
            <div 
              className="px-[3px] py-[3px]"
            >
                {categoryName}
            </div>

            {/* 逆三角形 */}
            <CategoryTriangleMenu 
              setIsOpen={setIsOpen}
              triangleRef={triangleRef}
            />

            {/* トライアングルメニュー */}
            {isOpen ? ( 
              <CateogryMenu 
                setIsOpen={setIsOpen} 
                handleDelete={handleDelete} 
                triangleRef={triangleRef} 
                categoryId={categoryId}
              />
            ) : null}
          </>
        ) : (
          <>
            <EditCategoryName  
              newCategoryName={newCategoryName}
              setNewCategoryName={setNewCategoryName}
              handleKeyDown={handleKeyDown}
            />
          </>
          )}
      </CategoryItemFlame>
    </>
  )
}