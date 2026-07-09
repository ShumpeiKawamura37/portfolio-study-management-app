import BackButton from "@/components/layout/BackButton";

export default function InfoLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <BackButton />
      {children}
    </>
  )
}