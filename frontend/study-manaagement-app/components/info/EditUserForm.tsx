"use client"
import { useEffect, useState } from "react";
import Button from "../ui/Button";
import { deleteUser, getUser, updateUser } from "@/service/user/userService";
import { showError } from "@/utils/error";
import InputItem from "./InputItem";
import { useRouter } from "next/navigation";


export default function EditUserForm() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("********");
  const [initialUsername, setInitialUsername] = useState("");
  const [initialEmail, setInitialEmail] = useState("");
  const [initialPassword, setInitialPassword] = useState("********");

  // 各値の更新用関数
  const onChangeUsername = (value: string) => {
    setUsername(value);
  }

  const onChangeEmail = (value: string) => {
    setEmail(value);
  }

  const onChangePassword = (value: string) => {
    setPassword(value);
  }

  //変更内容を保存
  const handleSave = async () => {
    try {
      const requestPassword = password === initialPassword ? "" : password;
      const res = await updateUser({ username, email, password: requestPassword });

      setUsername(res.data.username);
      setInitialUsername(res.data.username);
      setEmail(res.data.email);
      setInitialEmail(res.data.email);
      setPassword("********");
      setInitialPassword("********");
      alert("ユーザー情報を更新しました。")
    } catch (error: Error | any) {
        showError(error);
    }
  }

  // ユーザーを削除しログアウト
  const handleDelete = async () => {
    try {
      const isConfirmed = window.confirm("本当に削除しますか？\n\nこの操作は取り消せません。\nこれまでの記録はすべて削除され、復元できません。");
      if(!isConfirmed) {
        return;
      }

      await deleteUser();
      localStorage.removeItem("token");
      router.replace("/auth");

    } catch(error: Error | any) {
      showError(error);
    }
  }

  // レンダリング後にユーザー情報を取得してフォームに表示する。パスワードはセキュリティ上の理由で取得せず、仮の値を表示する。
  useEffect(() => {
    try {
      const fetchUser = async () => {
        const res = await getUser();
        if(res.status === "SUCCESS") {
          setUsername(res.data.username);
          setInitialUsername(res.data.username);

          setEmail(res.data.email);
          setInitialEmail(res.data.email);

          setPassword("********");
          setInitialPassword("********");
        }
      }
      fetchUser();
    } catch (error: Error | any) {
      showError(error);
    }
  }, []);

    return (
    <div className="mt-[70px] flex flex-col items-center space-y-10 justify-center">
      <div className="w-[680px] h-[450px] py-[60px] mx-auto flex justify-center border border-black">
        
        <div className="w-[625px] flex flex-col items-center">
          <ul className="mb-[50px]">
            <li>
              <InputItem type="text" value={username} initialValue={initialUsername} onChange={onChangeUsername}/>
            </li>
            <li>
              <InputItem type="email" value={email} initialValue={initialEmail} onChange={onChangeEmail}/>
            </li>
            <li>
              <InputItem type="password" value={password} initialValue={initialPassword} onChange={onChangePassword}/>
            </li>
          </ul>
          <Button onClick={handleSave} variant="primary">
            変更内容を保存する
          </Button>
        </div>
      </div>
      <Button onClick={handleDelete} variant="delete">
        ユーザーを削除
      </Button>
    </div>
  );
}

