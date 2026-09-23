"use client";

export default function ItemHeading({title}: {title: string}) {
  return (
    <div className="inline-block bg-[#53DEB7] rounded-lg text-white p-[10px] mb-7 mr-10">
      {title}
    </div> 
  )
}
