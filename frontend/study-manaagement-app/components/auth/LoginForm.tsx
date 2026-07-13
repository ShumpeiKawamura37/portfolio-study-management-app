"use client"

import { login, register } from "@/service/auth/authService";
import Button from "../ui/Button"
import InputForm from "./InputForm"
import { useState } from "react"
import { useRouter } from "next/navigation";
import { showError } from "@/utils/error";

export default function LoginForm() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [action, setAction] = useState<"login" | "register">("login");

  const onChangeEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const onChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    
    e.preventDefault();;

    if (action === "login") {
      try {
        const res = await login({ email, password });
        if(res.status === "SUCCESS") {
          //メニュー画面へ遷移
          localStorage.setItem("token", res.data.token);
          router.push("menu");
        }
      } catch (error: Error | any) {
        showError(error);
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
    <div className="w-64 flex items-center mx-auto">
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
    </div> 
  )
}
