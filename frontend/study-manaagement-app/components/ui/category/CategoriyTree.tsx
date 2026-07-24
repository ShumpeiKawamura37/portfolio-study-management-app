"use client"

import CreateCategory from "./CreateCategory";
import CategoryNode from "./CategoryNode";
import { useCategory } from "@/hooks/category/UseCategory";


export default function CategoryTree(){
  const category = useCategory();
  return (
    <div className="w-[250px] h-[100px] border overflow-schroll">
      <div className="px-2 py-2">
        {/* categoriesの数だけ繰り返しCategoryItemを呼ぶ */}
        {category?.categories.map((category) => (
          <CategoryNode category={category} />
        ))}

        {/* 配列の最後に「カテゴリの作成」 */}
        <CreateCategory />
      </div>
    </div>
  );
}
