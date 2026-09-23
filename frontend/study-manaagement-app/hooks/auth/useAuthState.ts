"use client";

import { useEffect, useState } from "react";

export const useAuthState = () => {
  const [isLogin, setIsLogin] = useState<boolean>(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if(token) {
      setIsLogin(true);
    }
  }, []);
  return {
    isLogin,
    setIsLogin
  }
}