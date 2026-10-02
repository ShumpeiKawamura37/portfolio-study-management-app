import { getCategories } from "@/service/category/CategoryService";
import { useEffect, useState } from "react";

type Category = {
  categoryId: number;
  categoryName: string;
  children: Category[];
}

export function useCategoryState() {
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
      setCategories(prev => removeTree(prev, categoryId));
    };
  
    // 削除によるツリー再描画
    const removeTree = (
      categories: Category[],
      targetId: number
    ): Category[] => {
      return categories
        .filter(category => category.categoryId !== targetId)
        .map(category => ({
          ...category,
          children: removeTree(category.children, targetId),
        }));
    };
  
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
  
  return {
    categories,
    addCategory,
    putCategory,
    removeCategory
  }
}