import "./globals.css";
import Header from "@/components/layout/Header";
import { AuthProvider } from "@/context/AuthContext";
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="ja"
    >
      <body className="mb-[30px]">
        <AuthProvider>
          <Header />
          {children}
        </AuthProvider>
      </body>
    </html>
  );
}
