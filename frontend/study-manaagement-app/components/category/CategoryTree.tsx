"use client"

import CreateCategory from "./CreateCategory";
import CategoryNode from "./CategoryNode";
import CategoryItemFlame from "./CategoryItemFlame";
import { useRef } from "react";
import { useRecord } from "@/hooks/record/useRecord";
import { useCategory } from "@/hooks/category/UseCategory";


export default function CategoryTree(){

  const category = useCategory();
  const { setTargetCategoryId } = useRecord();
  const treeRef = useRef<HTMLDivElement>(null);

  const handleTreeClick = (e: React.MouseEvent<HTMLDivElement>) => {
    const target = e.target as HTMLElement;

    if (target.closest("[data-category-node]")) {
      return;
    }
    setTargetCategoryId(null);
} ;
  
  return (
    <>
        カテゴリ選択
        <div 
          ref={treeRef}
          className="w-[250px] h-[100px] border border-[#B7B7B7] overflow-scroll scrollbar-hide px-2 py-2"
          onClick={handleTreeClick}

        >
        {/* categoriesの数だけ繰り返しCategoryItemを呼ぶ */}
        {category?.categories.map((cat) => (
          <CategoryNode key={cat.categoryId} category={cat} isLast={false} isRoot={true}
          ancestorHasLine={[]}/>
        ))}

        {/* 配列の最後に「カテゴリの作成」 */}
        <CategoryItemFlame>
          <CreateCategory />
        </CategoryItemFlame>
      </div>
    </> 
  );
}

