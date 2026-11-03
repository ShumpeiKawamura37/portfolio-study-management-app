import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";
import { register } from "@/service/user/userService";

describe("register", () => {
  const fetchMock = vi.fn();

  beforeEach(() => {
    vi.stubGlobal("fetch", fetchMock);
  });

  afterEach(() => {
    vi.unstubAllGlobals();
    fetchMock.mockReset();
  });

  test("メールアドレスが不正な場合、エラーになり API に送信しない", async () => {
    await expect(register({ email: "abc", password: "Password1" })).rejects.toThrow(
      "正しいメールアドレスを入力してください。",
    );
    expect(fetchMock).not.toHaveBeenCalled();
  });

  test("パスワードが不正な場合、エラーになり API に送信しない", async () => {
    await expect(register({ email: "test@example.com", password: "short" })).rejects.toThrow(
      "パスワードは8文字以上で入力してください。",
    );
    expect(fetchMock).not.toHaveBeenCalled();
  });

  test("入力が正しい場合、API に POST で送信する", async () => {
    fetchMock.mockResolvedValue(
      new Response(JSON.stringify({ status: "SUCCESS", data: null, message: "" }), { status: 200 }),
    );

    await register({ email: "test@example.com", password: "Password1" });

    expect(fetchMock).toHaveBeenCalledTimes(1);
    const [url, options] = fetchMock.mock.calls[0];
    expect(url).toContain("/api/user");
    expect(options.method).toBe("POST");
    expect(JSON.parse(options.body)).toEqual({ email: "test@example.com", password: "Password1" });
  });
});
