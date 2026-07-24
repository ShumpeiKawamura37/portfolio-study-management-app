"use client"
import { getCategories } from "@/service/category/CategoryService";
import { createContext, ReactNode, SetStateAction, useEffect, useState } from "react";


type Category = {
  categoryId: number;
  categoryName: string;
  children: Category[];
}

type CategoryContextType = {
  categories: Category[];
  addCategory: (parentId: number | null, category: Category) => void;
  putCategory: (updatedCategory: Category) => void;
  removeCategory: (categoryId: number) => void;
}

export const CategoryContext = createContext<CategoryContextType | null>(null);

export function CategoryProvider({children} : {children: ReactNode}) {
  const [categories, setCategories] = useState<Category[]>([]);

  // カテゴリを配列に追加
  const addCategory = (parentId: number | null, category: Category) => {
    if (parentId === null) {
      setCategories(prev => [...prev, category]);
    } else {
      setCategories(prev => addChild(prev, parentId,category));
    }
  }
  
  // 親カテゴリに子要素を追加
  const addChild = ( 
    categories: Category[], 
    parentCategoryId: number, 
    child: Category
  ): Category[] => {
    return categories.map(category => {
      if (category.categoryId === parentCategoryId) {
        return {
          ...category,
          children: [...category.children, child],
        };
      }

      return {
        ...category,
        children: addChild(category.children, parentCategoryId, child),
      };
    });
  }

  // カテゴリを更新 updateTree呼び出し
  const putCategory = (updatedCategory: Category) => {
    setCategories(prev => updateTree(prev, updatedCategory));
  };

  // 更新を子要素まで届けるため別記
  const updateTree = (
    categories: Category[],
    updatedCategory: Category
  ): Category[] => {
    console.log(updatedCategory);
    return categories.map(category => {
      if (category.categoryId === updatedCategory.categoryId) {
        return {
          ...category,
          categoryName: updatedCategory.categoryName,
        };
      }

      return {
        ...category,
        children: updateTree(category.children, updatedCategory),
      };
    });
  };

  // カテゴリを削除
  const removeCategory = (categoryId: number) => {
    setCategories((prev) => prev.filter((category)=> category.categoryId !== categoryId))
  }

  // 初回描画
  useEffect(() => {
    const fetchCategories = async() => {
      try {
        const res = await getCategories();
        setCategories(res.data);
      } catch(error: Error | any) {
        alert(error);
      }     
    }
    fetchCategories();  
  }, []);

  return (
    <CategoryContext.Provider
      value={{
        categories,
        addCategory,
        putCategory,
        removeCategory
      }} 
    >
      {children}
    </CategoryContext.Provider>
  )
}