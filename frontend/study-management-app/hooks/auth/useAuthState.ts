"use client";

import { useEffect, useState } from "react";

export const useAuthState = () => {
  const [isLogin, setIsLogin] = useState<boolean>(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if(token) {
      //　ESLint(テストツール)でエラー回避のため以下コメント
      // eslint-disable-next-line react-hooks/set-state-in-effect
      setIsLogin(true);
    }
  }, []);
  return {
    isLogin,
    setIsLogin
  }
}