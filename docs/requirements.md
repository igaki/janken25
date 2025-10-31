# 要求仕様（Tailwind CDN プロトタイプ適用）

## 目的
- 開発プロトタイプとして Tailwind CDN を導入し、画面のデザイン検証を行う。

## 対象
- `janken/src/main/resources/templates/index.html`（必要に応じてテンプレート化）
- `janken/src/main/resources/templates/janken.html`
- `janken/src/main/resources/templates/result.html`
- `janken/src/main/resources/templates/history.html`

## 要求事項
1. 各テンプレートの `<head>` に Tailwind CDN スニペット（Google Fonts、`https://cdn.tailwindcss.com`、`tailwind.config` の簡易拡張）を追加すること。
2. ページ全体にダークテーマ（`bg-slate-900` / `text-slate-100`）を適用すること。
3. 主要な UI 要素（ボタン、カード、テーブル、リンク）に Tailwind のユーティリティクラスを適用し、視認性の高いクールなデザインにすること。
4. Thymeleaf の `th:*` 属性は破壊せずに維持すること。
5. アプリ起動 (`./gradlew bootRun`) 後、主要ページで見た目が適用され表示されること。

## 検証基準 (DoD)
- ブラウザで `/janken`, `/janken/history`, 結果ページが表示され、デザインが適用されていること。
- レスポンシブにおいて重大な崩れがないこと。
- ブラウザコンソールに重大なエラーが出ていないこと。

## 制約
- プロトタイプのため CDN を使用する。企業ポリシーで CDN 禁止の場合は別対応。
- 本番導入は Tailwind をローカルでビルドして静的 CSS を配備することを想定する。
