# システム仕様書

## プロジェクト概要

**プロジェクト名**: janken25
**バージョン**: 0.0.1-SNAPSHOT
**説明**: Spring Boot を使用したじゃんけんゲームアプリケーション

## 技術スタック

### バックエンド
- **フレームワーク**: Spring Boot 3.5.6
- **言語**: Java 21
- **ビルドツール**: Gradle 8.14.3
- **データベース**: H2 Database（インメモリ）
- **ORM**: MyBatis 3.0.5

### フロントエンド
- **テンプレートエンジン**: Thymeleaf
- **静的リソース**: HTML, CSS, JavaScript

### 開発環境
- **パッケージ構造**: `oit.is.z9999.kaizi.janken`
- **テストフレームワーク**: JUnit 5, Spring Boot Test

## 現在の実装状況

### 実装済み機能

#### 1. Spring Boot アプリケーション起動
- **クラス**: `JankenApplication.java`
- **場所**: `janken/src/main/java/oit/is/z9999/kaizi/janken/`
- **説明**: Spring Boot アプリケーションのエントリーポイント

#### 2. 静的コンテンツ配信
- **ファイル**: `index.html`
- **場所**: `janken/src/main/resources/static/`
- **内容**: "Hello GitHub Flow!" を表示するシンプルなHTMLページ
- **URL**: `http://localhost:8080/index.html` または `http://localhost:8080/`

#### 3. テスト環境
- **テストクラス**: `JankenApplicationTests.java`
- **場所**: `janken/src/test/java/oit/is/z9999/kaizi/janken/`
- **説明**: Spring Boot コンテキストが正常にロードされることを確認する基本的なテスト

### 3. じゃんけん（CPU対戦）機能
- **モデルクラス**: JankenResult
  - ユーザの手、CPUの手、勝敗結果を保持
- **サービスクラス**: JankenService
  - CPUの手を乱数で生成
  - 勝敗判定ロジック（グー・チョキ・パーの組み合わせで「勝ち」「負け」「あいこ」を判定）
  - playメソッドで結果生成
- **コントローラクラス**: JankenController
  - ユーザの手を受け取り、サービス層に処理を委譲
  - 結果をテンプレートに渡して表示
- **画面テンプレート**: janken.html, result.html
  - janken.html: ユーザが手を選択できるフォーム
  - result.html: 勝敗結果を表示
- **テストコード**: JankenServiceTests, JankenControllerTests
  - サービスの勝敗判定ロジックの単体テスト
  - コントローラの画面遷移・レスポンス内容のテスト
- **ブランチ運用**: feat/janken-cpu-game
  - semantic commit・プッシュ・プルリクエスト

## フロントエンド（追記）

### Tailwind CDN プロトタイプ適用（2025-10-31）
- 概要: 開発プロトタイプとして Tailwind CSS の CDN を導入し、既存のテンプレートにクールなダークテーマを適用した。
- 変更対象:
  - `janken/src/main/resources/templates/janken.html` — Tailwind CDN スニペット追加、ダークテーマのベースクラスとカード・ボタン・テーブルのユーティリティを適用
  - `janken/src/main/resources/templates/result.html` — Tailwind CDN スニペット追加、結果表示のスタイル適用
  - `janken/src/main/resources/templates/history.html` — Tailwind CDN スニペット追加、履歴一覧のスタイル適用
- 目的: デザイン検証を迅速に行い、本番導入時のスタイル方針を確定する。
- 注意点:
  - 本実装は CDN を利用したプロトタイプであり、本番では Tailwind をビルドし静的 CSS として配備することを推奨する。

## プロジェクト構成

```
janken25/
├── .github/
│   └── copilot-instructions.md    # GitHub Copilot 指示書
├── docs/
│   ├── reports/                   # 調査レポート格納ディレクトリ
│   ├── schema.md                  # データベーススキーマ定義
│   ├── specs.md                   # システム仕様書（本ファイル）
│   ├── requirements.md            # 要件定義書
│   ├── tasks.md                   # 現在の作業計画
│   └── tasks_done.md              # 完了した作業履歴
└── janken/
    ├── build.gradle               # Gradle ビルド設定
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── oit/is/z9999/kaizi/janken/
    │   │   │       └── JankenApplication.java
    │   │   └── resources/
    │   │       └── static/
    │   │           └── index.html
    │   └── test/
    │       └── java/
    │           └── oit/is/z9999/kaizi/janken/
    │               └── JankenApplicationTests.java
    └── ...
```

## ビルドとデプロイ

### ビルド方法
```bash
cd janken
./gradlew build
```

### アプリケーション起動
```bash
cd janken
./gradlew bootRun
```

### テスト実行
```bash
cd janken
./gradlew test
```

## 今後の拡張予定

現在は基本的なSpring Bootアプリケーションの骨組みのみが実装されています。
今後、以下のような機能を実装予定です：

- じゃんけんゲームのロジック実装
- ユーザー管理機能
- ゲーム履歴の保存と表示
- Webインターフェースの充実
- データベースを使用した永続化

## 更新履歴

| 日付 | バージョン | 更新内容 |
|------|-----------|---------|
| 2025-xx-xx | 0.0.1-SNAPSHOT | 初版作成。Spring Boot基本構成を記載 |
| 2025-10-31 | 0.0.1-SNAPSHOT | Tailwind CDN を用いたテンプレートのプロトタイプ適用（janken/result/history にスタイル追加、ドキュメント更新） |
