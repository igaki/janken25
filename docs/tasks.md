# 複数ユーザ対戦（シンプル実装：ポーリング + DB永続化）実装計画（2025-11-10）

## 概要
- 目的: 異なる2ユーザ（例: `koudai` / `hanako` を追加）で対戦できる最もシンプルな実装を行う。
- 方針: HTTP API とデータベース永続化（H2）を使い、クライアントは短い間隔でポーリングして対戦状態を確認する方式を採用する。ユーザは開発段階でインメモリ認証で追加する（簡易化）。

## 採用する設計（要点）
- ユーザ追加: Spring Security のインメモリユーザへ `koudai` / `hanako` を追加（プロトタイプ最速実装）。
- DB: 既存の H2 を利用し、`matches` と `match_moves` テーブルを `src/main/resources/schema.sql` に追加して自動初期化する。
- API: シンプルな REST エンドポイントを用意（作成・参加・手の送信・状態取得・履歴取得）。
- フロント: `janken.html` を改修して対戦相手選択とポーリングロジックを追加する。

## 目的と DoD
- 目的: ログイン済みの2つの別ブラウザ（または別タブ）ユーザが対戦を行い、勝敗がDBに保存されることを確認する。

Definition of Done (DoD):
1. `./gradlew bootRun` でアプリ起動すること。
2. ブラウザ2つでそれぞれ `koudai/hanako` と別のユーザ（例: 既存ユーザ）でログインできること。
3. 一方が対戦を作成し、もう一方が参加できること。
4. 両者が手を送信して勝敗が判定・表示されること。
5. 対戦結果が DB に永続化され、`/janken/history` 等で確認できること。

## 変更予定ファイル（相対パス）
- `src/main/resources/schema.sql`（matches, match_moves のテーブル追加）
- `src/main/resources/mapper/`（MyBatis マッパー XML を追加する場合）
- `src/main/java/oit/is/z9999/kaizi/janken/mapper/`（MatchMapper インタフェース）
- `src/main/java/oit/is/z9999/kaizi/janken/model/`（Match, MatchMove 等のモデル）
- `src/main/java/oit/is/z9999/kaizi/janken/service/`（MatchService）
- `src/main/java/oit/is/z9999/kaizi/janken/controller/`（MatchController）
- `src/main/resources/templates/janken.html`（対戦相手選択・ポーリング追加）
- `src/main/java/oit/is/z9999/kaizi/janken/security/SecurityConfig.java`（インメモリユーザの追加、既にあれば追記）
- `src/test/java/...`（Mapper/Service/Controller のテスト追加）

## タスク分割（小さい単位で実行可能）
前提: 実装は main ブランチから新ブランチを作成して行う。実装開始前に `git switch main` を行い、以下のブランチ名で作業する: `feat/multi-match-polling`。

1. 作業準備（約0.25日）
   - main ブランチにいることを確認（ユーザが実行）。
   - `git switch -c feat/multi-match-polling` を作成する（実装フェーズで実行）。

2. DBスキーマ準備（約0.25日）
   - `src/main/resources/schema.sql` に `matches` と `match_moves` のテーブル定義を追加。
   - 確認: アプリ起動時にテーブルが作成されることを確認する。

3. モデルと Mapper インタフェース（約0.25日）
   - `Match` と `MatchMove` のモデルクラスを作成。
   - `MatchMapper` インタフェースに `insertMatch`, `updateMatch`, `insertMove`, `selectMatchById`, `selectActiveMatches` 等を定義。

4. マッパー XML と MyBatis 結合テスト（約0.5日）
   - Mapper XML を `src/main/resources/mapper/` に追加（select/insert/update を実装）。
   - @MybatisTest で基本 CRUD を確認するテストを作成。

5. サービス層実装（約0.5日）
   - `MatchService` を作成し、対戦作成/参加/移動/判定ロジック（簡潔に）を実装。判定はサーバ側で行い、勝敗を matches テーブルへ保存する。

6. コントローラ実装（約0.5日）
   - `MatchController` に以下エンドポイントを実装:
     - POST `/matches` （対戦作成）
     - POST `/matches/{id}/accept` （対戦参加）
     - POST `/matches/{id}/move` （手を送信）
     - GET  `/matches/{id}/state` （状態取得：ポーリング用）
     - GET  `/matches/history` （履歴取得）

7. テンプレートとフロント実装（約0.5日）
   - `janken.html` にログイン済みユーザ一覧（自分以外）を表示する領域を追加。
   - 対戦作成/参加ボタンを追加し、対戦中は1〜2秒間隔で `/matches/{id}/state` をポーリングして状態を更新する簡易JSを追加。

8. 認証ユーザの追加（約0.1日）
   - 開発中は `SecurityConfig` にインメモリユーザ `koudai` / `hanako` を追加する（後で data.sql に切替可能）。

9. テスト作成・実行（約0.5日）
   - Controller の統合テストで対戦作成→参加→両者move→結果永続化のフローを検証。
   - サービス層のユニットテスト、Mapper の @MybatisTest を実行する。

10. ドキュメント・完了報告（約0.25日）
   - `docs/reports/done/done_YYYY-MM-DD_複数ユーザ対戦実装.md` を作成し、実装内容・確認手順・ブランチ名を記載。
   - `docs/specs.md` を必要に応じて更新。

## テスト手順（手動確認）
1. `./gradlew bootRun` を実行する。
2. ブラウザAで `koudai` / `hanako` でログインする（インメモリ設定）。
3. ブラウザBで既存ユーザ（例: user1）でログインする。
4. A が対戦を作成し、B が参加する。
5. 両者が手を送信し、勝敗が表示される。
6. `/janken/history` で当該対戦が記録されていることを確認する。

## セキュリティと考慮点
- 開発段階はインメモリ認証で簡易対応。プロダクション移行時は DB のユーザストアに切替え、パスワードは必ずハッシュ化する。
- Controller では Principal からユーザ名を取得して操作を行い、他ユーザの操作を防ぐバリデーションを必須とする。
- ポーリング間隔はデフォルト1秒〜2秒。必要に応じて WebSocket に切替える（後段の改善タスク）。

## ブランチ戦略とコミット方針
- ブランチ: `feat/multi-match-polling`（全作業をこのブランチで行う）
- コミット: 小さな単位で semantic commit を使う（例: `feat: add matches schema`, `feat: add MatchService`）

## 見積り（合計）
- 合計: 約 3.5 ~ 4.0 日（テスト・ドキュメント含む、熟練度に依存）

---

次のアクション
1. 実装を開始する場合は「実装」と指示してください。私が `feat/multi-match-polling` ブランチで順に実装作業を支援します。
2. インメモリユーザではなく DB 初期データで `koudai/hanako` を投入したい場合はその旨を指示してください（計画を修正します）。

作成者: GitHub Copilot
作成日: 2025-11-10
