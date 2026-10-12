const DANGEROUS_CHARACTERS_PATTERN = /[<>"';&/\\]/;

/*  
  ユーザー名 
    - 必須 
    - 100文字以内 
    - 日本語（ひらがな・カタカナ・漢字）または英字 
    - 単語間の半角スペースは1つのみ許可 
    - 先頭と末尾の空白は許可しない。
    - 危険文字（< > " '  ; & / \）は禁止する。
*/

export const validateUsername = (username: string): string | null => {
  if (!username) {
    return "ユーザー名を入力してください。";
  }
  if (username.length > 100) {
    return "ユーザー名は100文字以内で入力してください。";
  }
  if (username !== username.trim()) {
    return "ユーザー名は日本語または英字で入力してください（単語間の半角スペースは1つまで）。";
  }
  if (DANGEROUS_CHARACTERS_PATTERN.test(username)) {
    return "使用できない文字が含まれています。";
  }
  const usernamePattern =
    /^[ぁ-んァ-ヶ一-龯a-zA-Z]+(?: [ぁ-んァ-ヶ一-龯a-zA-Z]+)*$/;
  if (!usernamePattern.test(username)) {
    return "ユーザー名は日本語または英字で入力してください（単語間の半角スペースは1つまで）。";
  }
  return null;
};

/*
  メールアドレス
    - 必須
    - 255文字以内
    - メールアドレス形式
    - 重複禁止
    - 危険文字（< > " '  ; & / \）は禁止する。
*/

export const validateEmail = (email: string): string | null => {
  if (!email) {
    return "メールアドレスを入力してください。";
  }
  if (email.length > 255) {
    return "メールアドレスは255文字以内で入力してください。";
  }
  if (DANGEROUS_CHARACTERS_PATTERN.test(email)) {
    return "メールアドレスに使用できない文字が含まれています。";
  }
  const emailPattern =
    /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)+$/;
  if (!emailPattern.test(email)) {
    return "正しいメールアドレスを入力してください。";
  }
  return null;
};

/*
  パスワード
    - 必須
    - 8文字以上
    - 72文字以内
    - 英大文字・英小文字・数字がそれぞれ１文字以上含まれる
    - 危険文字（< > " '  ; & / \）は禁止する。
*/
export const validatePassword = (password: string): string | null => {
  if (!password) {
    return "パスワードを入力してください。";
  }
  if (password.length < 8) {
    return "パスワードは8文字以上で入力してください。";
  }
  if (password.length > 72) {
    return "パスワードは72文字以内で入力してください。";
  }
  if (DANGEROUS_CHARACTERS_PATTERN.test(password)) {
    return "使用できない文字が含まれています。";
  }
  const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)\S+$/;
  if (!passwordPattern.test(password)) {
    return "パスワードには英大文字・英小文字・数字をそれぞれ1文字以上含めてください（空白は使用できません）。";
  }
  return null;
};

/*
  学習カテゴリ名
    - 必須
    - 100文字以内
    - 危険文字（< > " '  ; & / \）は禁止する。
*/
export const validateCategoryName = (categoryName: string): string | null => {
  if (!categoryName.trim()) {
    return "カテゴリ名を入力してください。";
  }
  if (categoryName.length > 100) {
    return "カテゴリ名は100文字以内で入力してください。";
  }
  if (DANGEROUS_CHARACTERS_PATTERN.test(categoryName)) {
    return "使用できない文字が含まれています。";
  }
  return null;
};

/*
  学習メモ
    - 任意
    - 1000文字以内
    - 危険文字（< > " '  ; & / \）は禁止する。
*/
export const validateStudyMemo = (memo: string): string | null => {
  if (memo.length > 1000) {
    return "学習メモは1000文字以内で入力してください。";
  }
  if (DANGEROUS_CHARACTERS_PATTERN.test(memo)) {
    return "使用できない文字が含まれています。";
  }
  return null;
};
