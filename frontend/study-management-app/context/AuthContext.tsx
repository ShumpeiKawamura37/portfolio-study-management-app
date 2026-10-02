"use client";

import { useAuthState } from "@/hooks/auth/useAuthState";
import { createContext, ReactNode } from "react";


type AuthContextType = ReturnType<typeof useAuthState>;

export const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({children}: {children: ReactNode}) {
  const { isLogin, setIsLogin } = useAuthState();
  return (
    <AuthContext.Provider
      value={{
        isLogin,
        setIsLogin,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}