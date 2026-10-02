"use client"
export const onKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      // 日本語入力に対応
      if (e.nativeEvent.isComposing) return;
      e.currentTarget.blur();
    }
}