"use client"
import { useState } from "react";
import Button from "../ui/Button";
import InputItem from "./InputItem";

export default function EditUserForm() {
  // getUserを取得し、名前とメアドを代入
  const [username, setUsername] = useState();
  const [email, setEmail] = useState();
  const [password, setPassword] = useState("********");
  return (


    <div className="mt-[70px] flex flex-col items-center space-y-10 justify-center">
      <div className="w-[680px] h-[450px] mx-auto flex items-center justify-center border border-black">
        
        <form className="flex flex-col items-center">
          <ul className="mb-[50px]">
            <li>
              {/* <InputItem/> */}
            </li>
            <li></li>
            <li></li>
          </ul>
          <Button onClick={() => console.log("Save clicked")} variant="primary">
            変更内容を保存する
          </Button>
        </form>
      </div>
      <Button onClick={() => console.log("clicked")} variant="delete">
        ユーザーを削除
      </Button>
    </div>
  );
}