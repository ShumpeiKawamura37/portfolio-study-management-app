"use client"

import { useActionForCategory } from "@/hooks/category/useActionForCategory";
import { useRecord } from "@/hooks/record/useRecord";
import React, { SetStateAction, useEffect, useRef } from "react";

type CategoryMenuProps = {
  setIsOpen: React.Dispatch<SetStateAction<boolean>>,
  handleDelete: () => void,
  triangleRef: React.RefObject<HTMLSpanElement | null>,
  categoryId: number | null
}

export default function CateogryMenu({
  setIsOpen,
  handleDelete,
  triangleRef,
  categoryId,
}: CategoryMenuProps) {

  const actionForCategory = useActionForCategory();
  const menuRef = useRef<HTMLDivElement>(null);
  const record = useRecord();

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (
        menuRef.current &&
        !menuRef.current.contains(e.target as Node) &&
        !triangleRef.current?.contains(e.target as Node)
      ) {
        setIsOpen(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);
  return (
    <div
      className="
        absolute right-0 top-[25px] w-[160px] bg-white border border-gray-300 rounded-sm z-50"
      ref={menuRef}
    >
      <ul>
        <li 
          className="px-3 py-2 border-b border-gray-300 hover:bg-gray-100 cursor-pointer"
          onClick={(e) => {
            e.stopPropagation();
            actionForCategory?.setAction("create");
            actionForCategory?.setParentCategoryId(categoryId);
            record.setTargetCategoryId(null);
            setIsOpen(false);
          }}
        >
          カテゴリを追加
        </li>
        <li 
          className="px-3 py-2 border-b border-gray-300 hover:bg-gray-100 cursor-pointer"
          onClick={() => {
            setIsOpen(false);
            actionForCategory?.setAction("update");
          }}
          onBlur={()=>actionForCategory?.setAction(null)}
        >
          カテゴリ名を編集
        </li>
        <li 
          className="px-3 py-2 hover:bg-gray-100 cursor-pointer"
          onClick={handleDelete}
        >
          カテゴリを削除
        </li>
      </ul>
    </div>
  )
}