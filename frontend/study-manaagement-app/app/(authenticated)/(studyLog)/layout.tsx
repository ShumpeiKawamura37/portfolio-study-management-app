import Providers from "@/app/(authenticated)/(studyLog)/Providers";
import RecordBackButton from "@/components/layout/RecordBackButton";
export default function studyLogLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <Providers>
      <RecordBackButton/>
      {children}
    </Providers>
  );
}
