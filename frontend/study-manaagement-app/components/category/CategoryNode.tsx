"use client"

import { CategoryResponse } from "@/types/category/category"
import CategoryLine from "./CategoryFlameLine"
import CategoryItem from "./CategoryItem"
import CreateChildCategory from "./CreateChildCategory"
import { useActionForCategory } from "@/hooks/category/UseActionForCategory"

type CategoryNodeProps = {
  category: CategoryResponse,
  isLast: boolean,
  isRoot: boolean,
  ancestorHasLine: boolean[]
}

export default function CategoryNode({
  category,
  isLast,
  isRoot,
  ancestorHasLine,
}: CategoryNodeProps) {
  const actionForCategory = useActionForCategory();
  const isCreateChild = actionForCategory?.action === "create" && actionForCategory?.parentCategoryId === category.categoryId;
  return (
    <>
      <div className="flex items-center">
        {!isRoot && (
          <CategoryLine
            ancestorHasLine={ancestorHasLine}
            isLast={isLast}
          />
        )}
        <CategoryItem categoryId={category.categoryId} categoryName={category.categoryName} />
      </div>

      {(category.children.length > 0 || isCreateChild) && (
        <div>
          {category.children.map((child, index) => {
            const isLastChild = index === category.children.length -1;
            return (
              <CategoryNode
                key={child.categoryId}
                category={child}
                isRoot={false}
                isLast={isLastChild}
                ancestorHasLine={[...ancestorHasLine, !isLast]}
              />
            );
          })}

          {isCreateChild && (
            <div className="flex items-center">
              <CategoryLine
                ancestorHasLine={[...ancestorHasLine, !isLast]}
                isLast={true}
              />
              <CreateChildCategory parentCategoryId={category.categoryId}/>
            </div>
          )}
        </div>
      )}
    </>
  )
}