"use client";

import { showError } from "@/utils/error";
import { useRouter } from "next/navigation";
import { ReactNode, useEffect, useRef, useSyncExternalStore } from "react";

// 別タブでのログアウトなど、localStorage の変更を検知する
const subscribe = (onStoreChange: () => void) => {
  window.addEventListener("storage", onStoreChange);
  return () => window.removeEventListener("storage", onStoreChange);
};

const getSnapshot = (): boolean | null => localStorage.getItem("token") !== null;

// サーバー描画時はlocalStorageを取得していないため確認中のnull
const getServerSnapshot = (): boolean | null => null;

export default function AuthGuard({ children }: { children: ReactNode }) {
  const router = useRouter();
  const hasToken = useSyncExternalStore(subscribe, getSnapshot, getServerSnapshot);
  const isRedirectingRef = useRef(false);     

  useEffect(() => {
    if (hasToken === false && !isRedirectingRef.current) {
      isRedirectingRef.current = true;
      showError(new Error("ログインし直してください。"));   
      router.replace("/auth");
    }
  }, [hasToken, router]);

  // 確認中（null）と未ログイン（false）の場合、ログイン後の画面を表示しない
  if (!hasToken) {
    return null;
  }

  return <>{children}</>;
}
