"use client"

import { CategoryResponse } from "@/types/category/category"
import CategoryItem from "./CategoryItem"
import { ActionForCategoryProvider } from "@/context/ActionForCategoryContext"

type CategoryNodeProps = {
  category: CategoryResponse,
}

export default function CategoryNode ({
  category,
}: CategoryNodeProps) {
  return (
    <>
      <ActionForCategoryProvider>
        <CategoryItem 
          key={category.categoryId}
          categoryId={category.categoryId}
          categoryName={category.categoryName}
        />
      </ActionForCategoryProvider>

      {category.children.length > 0 && (
        <div className="ml-4  border-l border-gray-300 pl-3">
          {category.children.map(child => (
            <CategoryNode
              key={child.categoryId}
              category={child}
            />
          ))}
        </div>
      )}
    </>
  )
}
