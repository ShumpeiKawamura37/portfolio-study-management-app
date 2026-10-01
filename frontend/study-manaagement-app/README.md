# 学習管理アプリ

## 概要

学習時間を記録・分析するWebアプリです。
資格取得や副業など、自主的に学習する人を対象とし、日々の学習時間を記録することで、学習状況を把握・分析できます。

## 開発目的

研修や自学習を通じてHTTP/CSS,JS,Javaといった言語やReact,Next.jsといったフレームワークの基礎知識を身につけました。実践経験に乏しいことを課題とし、設計から実装、公開までの一連の開発プロセスを一人で完遂することを目的として、フルスタックのWebアプリケーションを開発いたしました。
学習済みの言語を可能な限り網羅する為、フロントエンドをNext.js、バックエンドをjavaで構成しております。
内容は、ログイン機能やカレンダーなどの実用性の高い実装を要件に含めつつ、平素における自身の学習態度及び学習内容を可視化できるツールとして学習記録アプリといたしました。

## 主な機能

- ユーザー登録・ログイン
- 学習カテゴリ管理（登録・編集・削除）
- 学習時間の計測
  - タイマー
  - ストップウォッチ
- 学習ログの登録
- 学習メモの登録
- 学習時間の集計
- 日別学習ログの取得
- カテゴリ別学習時間の分析
- 期間別の学習分析
- 学習割合のグラフ表示
- ユーザー情報の編集・削除
- ログアウト

## 使用技術
フロントエンド
- Next.js
- React
- TypeScript
- Tailwind CSS

バックエンド 
- Java
- Spring Boot 
- Spring Security 
- Spring Data JPA

データベース
- PostgreSQL

認証
- JWT

インターフェース 
- Docker

バージョン管理 
- Git
- GitHub


## システム構成

```text
┌─────────────────┐
│   Frontend      │
│ Next.js / React │
│ TypeScript      │
└────────┬────────┘
         │ HTTP / REST API
         ↓
┌─────────────────┐
│    Backend      │
│ Java / Spring   │
│      Boot       │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│   PostgreSQL    │
└─────────────────┘

```

## 起動方法
1. リポジトリをクローン
git clone [[https://github.com/ShumpeiKawamura37/portfolio-study-management-app]
cd portfolio-study-management-app
2. データベースを起動
docker compose up -d
3. バックエンドを起動
cd backend/study-management-app
./gradlew bootRun
4. フロントエンドを起動
cd frontend/study-management-app
npm install
npm run dev

ブラウザから以下にアクセスします。

http://localhost:3000
ドキュメント

詳細な設計・仕様については以下を参照してください。
- 要求仕様書
- バックエンド設計UML(クラス図、シーケンス図)
  - (backend/study-management-app/docs/uml)
- Figma
  - (https://www.figma.com/design/0nbDXizZfNG3D2DZoMoyQ2/studymanagement?node-id=0-1&m=dev&t=flScTOx4Y4off6iG-1)

## 開発環境
- Java 21
- Node.js
- PostgreSQL
- Docker
- Git

## Gitブランチ
main
└── develop
    └── feature/*

main：本番用
develop：開発統合用
feature/*：機能開発用