"use client"

import { getUser } from "@/service/user/userService";
import { useEffect, useState } from "react";
import { useCategoryState } from "../category/useCategoryState";
import { CategoryResponse } from "@/types/category/category";

export const useAnalyticsState = () => {
  const [username, setUsername] = useState<string>("");
  const categortyState = useCategoryState();
  const studyLogs = 


  useEffect(() => {
    const fetchUser = async () => {
      const res = await getUser();
      setUsername(res.data.username);
    };

    fetchUser();
  }, []);

  return {
    username,
    categories
  }
}

