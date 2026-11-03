export const showError = (error: unknown) => {
  const message = error instanceof Error ? error.message : "予期しないエラーが発生しました。";
  alert(`エラー: ${message}`);
};