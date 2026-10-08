"use client"

import { login, register } from "@/service/auth/authService";
import Button from "../ui/Button"
import InputForm from "./InputForm"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation";
import { showError } from "@/utils/error";
import { useAuth } from "@/hooks/auth/useAuth";

export default function LoginForm() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [action, setAction] = useState<"login" | "register">("login");
  const { setIsLogin } = useAuth();
  const [isLoading, setIsLoading] = useState(false);

  // ログイン画面に戻ってきたらログアウト状態にする
  useEffect(() => {
    setIsLogin(false);
    localStorage.removeItem("token");
  }, [setIsLogin]);

  const onChangeEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const onChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
      
      e.preventDefault();;

      if (action === "login") {
        setIsLoading(true);
        try {
          const startTime = Date.now();
          const res = await login({ email, password });
          const elapsedTime = Date.now() - startTime;
          const remainingTime = Math.max(0, 3000 - elapsedTime);

          await new Promise((resolve) => setTimeout(resolve, remainingTime));

          if(res.status === "SUCCESS") {
            //メニュー画面へ遷移
            localStorage.setItem("token", res.data.token);
            setIsLogin(true);
            router.push("menu");
          }
        } catch (error: Error | any) {
          showError(error);
        } finally {
          setIsLoading(false);
        }
      } else if (action === "register") {
        try {
          const res = await register({ email, password });
          if(res.status === "SUCCESS") {
            alert("ユーザーを登録しました。ログインしてください。");
          }
        } catch (error: Error | any) {
          showError(error);
        }
      } else {
        console.error("Invalid action:", action);
      }
      
      setEmail("");
      setPassword("");
    };

  return (
    <div className="w-64 flex items-center mx-auto mt-[50px] pt-[70px]">
      <form onSubmit={handleSubmit} className="flex flex-col space-y-10 justify-center items-center mx-auto">
        <InputForm 
          email={email} 
          password={password} 
          onChangeEmail={onChangeEmail} 
          onChangePassword={onChangePassword}
        />
        <Button 
          type="submit"
          onClick={() => setAction("login")} 
          variant="primary"
        >
          ログイン
        </Button>
        <Button 
        type="submit"
        onClick={() => setAction("register")} 
        variant="secondary">
          新規登録
        </Button>
      </form>

      {isLoading && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-white/80">
          <div className="flex flex-col items-center gap-3">
            <div className="h-10 w-10 animate-spin rounded-full border-4 border-gray-300 border-t-[#53DEB7]" />
            <p>ログイン中...</p>
          </div>
        </div>
      )}
    </div> 
  )
}
