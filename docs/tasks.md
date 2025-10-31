# じゃんけん認証・ログイン機能追加 実装計画（2025-10-14）

## 概要
- ルートパス（http://localhost:8080/）にアクセスした際にログインページを表示し、ログイン後にじゃんけんページへ遷移させる
- ユーザ名「yamada」、パスワード「tarou」でログインできるようにする
- Spring Securityを利用し、認証・認可を実装する

## 必要なファイル・関連ファイル
- janken/build.gradle（依存追加）
- janken/src/main/java/oit/is/z9999/kaizi/janken/security/SecurityConfig.java（新規作成）
- janken/src/main/java/oit/is/z9999/kaizi/janken/JankenController.java（既存）
- janken/src/main/resources/templates/janken.html（既存）
- janken/src/main/resources/static/index.html（不要なら削除）

## 実装タスク
1. `build.gradle` に `spring-boot-starter-security` を追加する
2. `security` ディレクトリを作成し、`SecurityConfig.java` を新規作成
   - インメモリユーザ（yamada/tarou）を設定
   - `/janken` へのアクセスは認証必須
   - ログイン画面は自動生成
3. コントローラやテンプレートは既存のままでOK
4. index.html の役割を確認し、不要なら削除
5. 実装後、ブラウザで http://localhost:8080/ にアクセスし、ログイン画面→認証後じゃんけん画面が表示されることを確認

## テスト・確認方法
- ログイン画面が表示されること
- ユーザ名「yamada」、パスワード「tarou」でログインできること
- ログイン後、じゃんけん画面（janken.html）が表示されること
- 認証なしで `/janken` にアクセスするとログイン画面にリダイレクトされること

## 優先順位
1. 依存追加・SecurityConfig作成
2. index.html の整理
3. 動作確認

## 参考ファイルパス
- janken/build.gradle
- janken/src/main/java/oit/is/z9999/kaizi/janken/security/SecurityConfig.java
- janken/src/main/java/oit/is/z9999/kaizi/janken/JankenController.java
- janken/src/main/resources/templates/janken.html
- janken/src/main/resources/static/index.html

---

# 対戦履歴の永続化（H2 + MyBatis）実装計画（2025-10-28）

## 概要
- 要求: 対戦履歴を組み込みH2に永続化し、認証ユーザごとに
  - 対戦ページ（`/janken`）では直近5戦分を表示する
  - 履歴ページ（`/janken/history`）では当該ユーザの過去すべての対戦履歴を表示する
- 実装方式: `schema.sql` による簡易マイグレーション + MyBatis Mapper を使用する。
- 目的: 開発環境で永続化を行い、将来的にRDBへ移行可能な設計にする。

## 目標（DoD）
1. `./gradlew test` がすべて成功すること
2. `./gradlew bootRun` でアプリ起動後、認証ユーザでログインして `/janken` に「直近5戦分の履歴」が表示されること（ページ内にリンクも表示）
3. `/janken/history` を開くと、当該ユーザの過去のすべての対戦履歴が日時降順で表示されること
4. Controller のテストで recent5 件取得と全件取得の両方が検証されていること

## 必要なファイル（追加/変更）
- 追加:
  - `janken/src/main/resources/schema.sql`  -- テーブル定義を追加
  - `janken/src/main/java/oit/is/z9999/kaizi/janken/model/JankenHistory.java`  -- 履歴モデル
  - `janken/src/main/java/oit/is/z9999/kaizi/janken/mapper/JankenHistoryMapper.java`  -- MyBatisインタフェース（`insert`、`selectRecentByUsername`、`selectByUsername` を定義）
  - `janken/src/main/resources/mapper/JankenHistoryMapper.xml`  -- Mapper XML（直近N件取得と全件取得の select を実装）
  - `janken/src/main/java/oit/is/z9999/kaizi/janken/service/HistoryService.java`  -- インタフェース（`add`, `getLast(String username, int n)`, `getAll(String username)` を定義）
  - `janken/src/main/java/oit/is/z9999/kaizi/janken/service/MybatisHistoryService.java`  -- 実装
  - `janken/src/main/resources/templates/history.html`  -- 履歴表示テンプレート（全件表示）
  - `janken/src/test/java/oit/is/z9999/kaizi/janken/JankenHistoryMapperTests.java`  -- Mapper 用テスト
  - `janken/src/test/java/oit/is/z9999/kaizi/janken/MybatisHistoryServiceTests.java`  -- サービス単体テスト
- 変更:
  - `janken/src/main/java/oit/is/z9999/kaizi/janken/JankenController.java`  -- POST結果保存呼び出し、GET `/janken/history`（全件取得）ハンドラ追加、GET `/janken` に recent5 件を model に追加
  - `janken/src/main/resources/templates/janken.html`  -- 直近5戦分表示領域と履歴ページへのリンクを追加
  - `janken/build.gradle`  -- MyBatis 依存追加（`org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.2` 等）

## タスク分割（小さい単位で実行可能）
1. 依存追加と設定（約0.5日）
   - `build.gradle` に MyBatis 依存を追加
   - `application.properties` に H2 接続設定と MyBatis 設定を追記
   - 確認: `./gradlew test` が起動時に依存解決できること

2. スキーマ追加（約0.25日）
   - `janken/src/main/resources/schema.sql` を作成（テーブル定義・インデックス）
   - 確認: アプリ起動時にテーブルが作成されること（H2コンソールまたは JDBC で確認）

3. モデルと Mapper インタフェース作成（約0.25日）
   - `JankenHistory` モデル作成（`LocalDateTime playedAt` 等）
   - `JankenHistoryMapper` インタフェースで `insert`, `selectRecentByUsername`（limit パラメータ付き）, `selectByUsername`（全件取得）を定義

4. Mapper XML 実装と動作確認（約0.5日）
   - `JankenHistoryMapper.xml` に `insert`、`selectRecentByUsername`（ORDER BY played_at DESC LIMIT #{limit}）および `selectByUsername`（ORDER BY played_at DESC）を実装
   - 簡易テスト（@MybatisTest）で insert → selectRecent / selectByUsername を確認

5. サービス層実装（約0.5日）
   - `HistoryService` インタフェース作成
   - `MybatisHistoryService` を実装し Mapper を使用して DB 操作を行う
   - `add(String username, JankenResult result)` では JankenResult から JankenHistory を生成して insert
   - `getLast(String username, int n)` では Mapper の `selectRecentByUsername` を呼び、`getAll(String username)` では `selectByUsername` を呼ぶ

6. コントローラとテンプレート変更（約0.5日）
   - `JankenController` の POST `/janken/result` に Principal を受け取り `historyService.add(...)` を呼ぶ
   - GET `/janken/history` ハンドラを実装し `history` をモデルに追加して `history.html` を返す（全件表示）
   - GET `/janken` ハンドラ（既存）で `historyService.getLast(principal.getName(), 5)` を呼び `recentHistory` をモデルに追加して janken.html を返す
   - `janken.html` に直近5件の表示領域と `<a href="/janken/history">対戦履歴（すべて）</a>` を追加
   - `history.html` を作成（全件表示、履歴なしメッセージ）

7. テスト実装（約0.5日）
   - Mapper の @MybatisTest を作成
   - `MybatisHistoryService` のユニットテスト（Mapper をモック）を作成
   - `JankenControllerTests` に統合テストを追加:
     - 認証ユーザで GET `/janken` を実行し model に `recentHistory` があることを確認（直近5件が取得されることを検証）
     - 認証ユーザで GET `/janken/history` を実行し model に `history` があることを確認（全件取得）

8. 最終確認とドキュメント更新（約0.25日）
   - `./gradlew test` と `./gradlew bootRun` の確認
   - `docs/reports/done_YYYY-MM-DD_実装内容.md` と `docs/tasks_done.md` を更新（実装時）

## テスト入力・手順（手動確認）
1. アプリを起動（`./gradlew bootRun`）
2. ブラウザで `http://localhost:8080/` にアクセスしログイン（`yamada` / `tarou`）
3. `/janken` で複数回対戦（同一ユーザ）
4. `/janken` を開き、直近5件が表示されることを確認
5. `/janken/history` を開き、当該ユーザの過去すべての対戦履歴が日時降順で表示されることを確認

## セキュリティとバリデーション
- Controller では `Principal.getName()` を使い取得したユーザ名で DB を問い合わせる。クエリパラメータでユーザ指定を受けない。
- Mapper の SQL はパラメタライズして SQL インジェクションを防ぐ（MyBatis のパラメタバインディングを使用）

## マイグレーション運用（将来的な対応）
- 当面は `schema.sql` による初期化で運用。将来的に Flyway へ移行する場合は migration スクリプトを追加する。

## 依存関係・留意点
- 既に MyBatis がプロジェクトに無ければ `build.gradle` に追加する（バージョン互換を確認）
- H2 の自動初期化設定は `spring.datasource.initialization-mode` と `spring.sql.init.mode` を確認

---

# Tailwind CDN プロトタイプ適用 計画（2025-10-31）

## 概要
- 目的: Tailwind の CDN を用いて `index.html`, `janken.html`, `result.html`, `history.html` にクールなダークテーマ UI を素早く適用し、デザインを検証するプロトタイプを作成する。
- 対象ファイル（編集対象）:
  - `src/main/resources/templates/index.html`
  - `src/main/resources/templates/janken.html`
  - `src/main/resources/templates/result.html`
  - `src/main/resources/templates/history.html`
  - （必要に応じて）`src/main/resources/templates/fragments/head.html`

## 前提条件
- ローカルで Gradle / Spring Boot が起動できること
- 本作業はプロトタイプ目的。CSP 等の運用制約がある場合は事前に確認すること

## ブランチ戦略
- 新規ブランチ名: `feat/tailwind-cdn-prototype`
- すべての作業は上記ブランチで行い、タスクごとに semantic commit を実施する（例: `feat: add tailwind CDN snippet to head`）

## タスク分割（小タスク・順序）
1. ブランチ作成
   - コマンド例: `git switch -c feat/tailwind-cdn-prototype`
   - コミット: `feat: create branch for tailwind cdn prototype`

2. head スニペット追加（共通化可能なら fragments/head.html を更新）
   - 追加内容: Google Fonts (Inter)、`https://cdn.tailwindcss.com`、`tailwind.config` の簡易拡張、基本フォント style
   - 編集ファイル: 各テンプレートの `<head>` または共通フラグメント
   - コミット例: `feat: add tailwind CDN snippet to template head`

3. ベースレイアウト適用（共通スタイル）
   - 変更: 各テンプレートの body に `min-h-screen bg-slate-900 text-slate-100` を適用。主要コンテナに `max-w-4xl mx-auto p-8 bg-white/5 backdrop-blur-md rounded-2xl shadow-lg` 等を追加
   - コミット例: `feat: apply base layout classes to templates`

4. ページ別スタイル適用（ページ単位でコミット）
   - `index.html`: ヒーロー、CTA ボタン
   - `janken.html`: ステータスバー、手カードグリッド
   - `result.html`: 勝敗表示カード、ボタン群
   - `history.html`: 検索バー、履歴リスト
   - 各ファイルごとに小さな変更でコミット（例: `feat: style index hero and CTA`）

5. 起動・動作確認と微修正
   - コマンド: `./gradlew bootRun`
   - 確認ページ: `/`, `/janken`, `/janken/history`（結果ページも確認）
   - 必要に応じて修正し `fix:` コミットを行う

6. ドキュメント更新・レポート作成
   - `docs/reports/done_YYYY-MM-DD_tailwind-cdn-prototype.md` を作成し、実施内容・確認手順・ブランチ名を記載
   - `docs/tasks_done.md` に要約を先頭行として追記
   - コミット例: `docs: add done report for tailwind CDN prototype`

## 具体的な挿入スニペット（head）
- 各テンプレートの `<head>` に以下を追加（コメントで囲むと差分確認しやすい）:

```html
<!-- tailwind-cdn-start -->
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet">
<script src="https://cdn.tailwindcss.com"></script>
<script>
  tailwind.config = {
    theme: {
      extend: {
        colors: { primary: '#06b6d4', accent: '#3b82f6' },
        fontFamily: { sans: ['Inter', 'ui-sans-serif', 'system-ui'] }
      }
    }
  }
</script>
<style>body{font-family:Inter, system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial;}</style>
<!-- tailwind-cdn-end -->
```

## ベースクラス例（適用候補）
- body: `min-h-screen bg-slate-900 text-slate-100`
- コンテナ: `max-w-4xl mx-auto p-8 bg-white/5 backdrop-blur-md rounded-2xl shadow-lg`
- グリッド: `grid grid-cols-1 sm:grid-cols-3 gap-6`
- ボタン: `px-6 py-3 rounded-lg text-white font-semibold bg-gradient-to-r from-primary to-accent shadow-md hover:scale-105 transition-transform`

## Definition of Done (DoD)
- 4 テンプレートに head スニペットが追加されていること（共通フラグメントに追加した場合はその旨コメント）
- 各ページにベースの Tailwind クラスが適用され、ブラウザで「ダーク・クール」な見た目になっていること
- `./gradlew bootRun` で起動し、主要ページが表示されること（UI 崩れなし）
- レスポンシブ確認を行い大きな崩れがないこと
- docs に完了レポートを作成し `docs/tasks_done.md` を更新すること
- すべてのコミットは semantic commit を使用すること

## 検証手順（簡易）
1. `git switch -c feat/tailwind-cdn-prototype`
2. 編集を段階的に実施しコミット
3. `./gradlew bootRun` を実行
4. ブラウザで `/`, `/janken`, `/janken/history` を確認
5. devtools でレスポンシブ表示を確認
6. 問題なければプッシュして PR を作成

---

実装を開始する場合は「実装」と指示してください。実装前にこの計画に基づき作業ブランチを作成し、タスクごとにコミットします（semantic commit を使用）。
