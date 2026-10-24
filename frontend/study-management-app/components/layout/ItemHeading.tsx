"use client";

import React, { ReactNode } from "react";

export default function ItemHeading({title}: {title: React.ReactNode}) {
  return (
    <div className="inline-block bg-[#53DEB7] rounded-lg text-white p-[10px] mr-10">
      {title}
    </div> 
  )
}
