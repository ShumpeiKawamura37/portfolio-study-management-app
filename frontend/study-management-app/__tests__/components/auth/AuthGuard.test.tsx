import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";
import { cleanup, render, screen } from "@testing-library/react";
import AuthGuard from "@/components/auth/AuthGuard";

// vi.mock は import より先に実行されるので、使う値も vi.hoisted で先に作る
const { replaceMock } = vi.hoisted(() => ({ replaceMock: vi.fn() }));

vi.mock("next/navigation", () => ({
  useRouter: () => ({ replace: replaceMock }),
}));

describe("AuthGuard", () => {
  beforeEach(() => {
    vi.spyOn(window, "alert").mockImplementation(() => {});
  });

  afterEach(() => {
    cleanup();
    localStorage.clear();
    vi.restoreAllMocks();
    replaceMock.mockReset();
  });

  test("トークンがない場合、アラートを出してログイン画面へ移動し、中身を表示しない", () => {
    render(
      <AuthGuard>
        <p>ログイン後の画面</p>
      </AuthGuard>,
    );

    expect(screen.queryByText("ログイン後の画面")).toBeNull();
    expect(window.alert).toHaveBeenCalledWith("エラー: ログインし直してください。");
    expect(replaceMock).toHaveBeenCalledWith("/auth");
  });

  test("Strict Mode でも、アラートと移動は1回だけ", () => {
    render(
      <AuthGuard>
        <p>ログイン後の画面</p>
      </AuthGuard>,
      { reactStrictMode: true },
    );

    expect(window.alert).toHaveBeenCalledTimes(1);
    expect(replaceMock).toHaveBeenCalledTimes(1);
  });

  test("トークンがある場合、中身を表示し、移動しない", () => {
    localStorage.setItem("token", "dummy-token");

    render(
      <AuthGuard>
        <p>ログイン後の画面</p>
      </AuthGuard>,
    );

    expect(screen.getByText("ログイン後の画面")).toBeDefined();
    expect(replaceMock).not.toHaveBeenCalled();
  });
});
