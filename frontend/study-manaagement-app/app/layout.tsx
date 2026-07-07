import "./globals.css";
import Header from "@/components/layout/Header";
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const isLogin = true //ログイン状態を常にtrue（開発用）
  return (
    <html
      lang="ja"
    >
      <body>
        <Header isLogin={isLogin}/>
        {children}
      </body>
    </html>
  );
}
