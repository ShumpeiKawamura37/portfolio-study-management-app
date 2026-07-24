"use client"

import { createCategory, deleteCategory, updateCategory } from "@/service/category/CategoryService";
import { useRef, useState } from "react"
import EditCategoryName from "./EditCategoryName";
import { onKeyDown } from "@/utils/inputAction/onKeyDown";
import CategoryItemFlame from "./CategoryItemFlame";
import CateogryMenu from "./CtegoryMenu";
import CategoryTriangleMenu from "./CategoryTriangleMenu";
import CreateChildCategory from "./CreateChildCategory";
import { useCategory } from "@/hooks/category/UseCategory";
import { useActionForCategory } from "@/hooks/category/UseActionForCategory";

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
          category?.addCategory(null, resForCreate.data);
          break;
        case "update":
          const resForUpdate = await updateCategory(categoryId, newCategoryName);
          category?.putCategory(resForUpdate.data);
          break;
      }

      setNewCategoryName("");
      setIsOpen(false);
      actionForCategory?.setAction(null);
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

  return (
    <>
      <CategoryItemFlame>
        {actionForCategory?.action !== "update" ? (
          <>
            <div className="px-[3px] py-[3px]">
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

      {actionForCategory?.action === "create"? (
        <div className="ml-4  border-l border-gray-300 pl-3">
          <CreateChildCategory 
            parentCategoryId={categoryId}
          />
        </div>
      ): null}
    </>
  )
}

// ページをリロードすると順番が変わる
// 子要素を削除した時の挙動がおかしい->削除処理を再帰