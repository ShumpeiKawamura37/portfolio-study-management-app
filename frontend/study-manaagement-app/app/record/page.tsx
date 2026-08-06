"use client"

import Record from "@/components/record/Record"
import { ActionForCategoryProvider } from "@/context/ActionForCategoryContext"
import { CategoryProvider } from "@/context/CategoryContext"
import { RecordProvider } from "@/context/RecordContext"

export default function RecordPage() {
  return (
    <RecordProvider>
      <CategoryProvider>
        <ActionForCategoryProvider>
          <Record />        
        </ActionForCategoryProvider>
      </CategoryProvider>
    </RecordProvider>
    
  )
}