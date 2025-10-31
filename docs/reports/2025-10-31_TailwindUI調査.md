# 2025-10-31 Tailwind CSS を用いた画面リデザイン調査

## 目的
`index.html`, `janken.html`, `result.html`, `history.html` を Tailwind CSS を活用して「クール」なイメージに変更するための方法を調査・提案する。

## 対象ファイル（ソース）
- `src/main/resources/templates/index.html`
- `src/main/resources/templates/janken.html`
- `src/main/resources/templates/result.html`
- `src/main/resources/templates/history.html`

※ ビルド出力にも同様のファイルが存在しますが（`build/resources/main/...`）、編集対象は上記ソースパスです。

## 検討した選択肢

### A. CDN（開発プロトタイプ向け、素早い検証）
- 手順: 各 HTML の head 内に `<script src="https://cdn.tailwindcss.com"></script>` を追加
- メリット: すぐに使える。ビルド環境を追加する必要なし。プロトタイプや試作に最適。
- デメリット: 本番向けに未最適化（未圧縮のスタイルが多い）、カスタム設定（色や breakpoints 等）の反映が限定的、パフォーマンス面で不利。


## 推奨実装フロー（段階的）

1. まずプロトタイプ（短期）
   - 各テンプレートに CDN を挿入してレイアウト・見た目を検証。
   - 変更点・好みのデザインを確定する。

2. 本番準備（推奨）
   - プロジェクトルートに `package.json` を追加し、`tailwindcss`, `postcss`, `autoprefixer` をインストールする。
   - `src/main/resources/tailwind/input.css` を作成し、以下を記載：
     @tailwind base;
     @tailwind components;
     @tailwind utilities;
   - `tailwind.config.js` を作成し、`content` にテンプレートパス（`src/main/resources/templates/**/*.html` など）を指定。
   - ビルドコマンド例（npm script）:
     `npx tailwindcss -i ./src/main/resources/tailwind/input.css -o ./src/main/resources/static/css/tailwind.css --minify`
   - Gradle に組み込む場合は `com.github.node-gradle.node` プラグインや Gradle の Exec タスクで `npm run build:css` を実行する設定を検討。

## HTML の具体的なデザイン方針（ページ別）

共通デザイン要素（クールなイメージ）
- カラーパレット: ダーク背景（例: `bg-slate-900`）＋アクセントにシアン/青系（`text-cyan-400`, `bg-cyan-600/80`）
- フォント: セリフではなくサンセリフ。Google Fonts の `Inter` 等を利用すると良い。
- レイアウト: 中央にカード、半透明グラスモーフィック（`backdrop-blur` + `bg-white/5`）
- アニメーション: ボタンの hover/active、やわらかいトランジション（`transition`, `transform`, `scale-105`）
- アイコン: Heroicons（SVG）を利用

ページ別提案
- `index.html` (イントロ/トップ)
  - フルスクリーンのヒーローセクション、左にキャッチコピー、右にアプリへのリンク（カード風ボタン）
  - 大きなボタンにグラデーション（`bg-gradient-to-r from-cyan-500 to-blue-500`）

- `janken.html` (じゃんけん選択画面)
  - 中央に手のカードを横並びに配置（`grid grid-cols-3 gap-6`）、各カードはクリックで影とスケール変化
  - 上部にプレイヤー名・現在のスコアを表示するステータスバー（半透明）

- `result.html` (結果表示)
  - 勝敗によって背景色やカードのエフェクトを変える（勝ち: `ring-4 ring-cyan-400/50`、負け: `opacity-80`）
  - 再戦ボタンと履歴へ遷移するボタンをわかりやすく配置

- `history.html` (対戦履歴)
  - タイムライン形式またはカードリスト（`divide-y`）で一覧表示
  - フィルタ（勝敗別）や検索バー（`input` にアイコンとプレースホルダ）を上部に配置

## 具体的な Tailwind ユーティリティ例（参考）
- ヘッダー: `class="w-full py-6 px-8 flex items-center justify-between bg-transparent"`
- カード: `class="bg-white/5 backdrop-blur-md rounded-xl p-6 shadow-lg hover:scale-105 transition-transform"`
- ボタン: `class="px-6 py-3 rounded-md text-white font-semibold bg-gradient-to-r from-cyan-500 to-blue-500 hover:from-cyan-400 hover:to-blue-600 shadow-md"`

※ 上記はあくまで例。実際の HTML に Tailwind クラスを適用して調整する。

## 導入時の注意点
- Thymeleaf テンプレートで CDN スクリプトを読み込む場合、Content Security Policy(CSP) が有効な環境では許可設定が必要。
- Tailwind の `content` 設定にテンプレートパスを正しく含めないと、使用したクラスが purge（削除）される可能性がある。
- ビルド環境を追加する場合は、開発者全員に Node.js のバージョンを共有すること。

## 推奨（結論）
- まずは CDN でプロトタイプを作成してデザインを固める（短期）
- デザインが確定したら、Tailwind をローカルでビルドするワークフローを導入して本番用に最適化する（長期）

## 参考リンク
- Tailwind CSS 公式: https://tailwindcss.com/
- CDN（Play）: https://cdn.tailwindcss.com
- Tailwind 入門記事（日本語）: https://ja.tailwindcss.com/docs/installation
- Heroicons: https://heroicons.com/

---

作業報告:
- 本調査を `docs/reports/2025-10-31_TailwindUI調査.md` に作成しました。

次のアクション案:
1. すぐに見た目を確認したい場合: CDN を使って `src/main/resources/templates/*.html` にスクリプトを差し込み、クラスを追加するプロトタイプ実装を要求してください（実装フェーズ）。
2. 本番対応を希望する場合: Node.js 環境で Tailwind を導入する実装を行います。事前に Node.js の有無を教えてください。

## CDN 実装案（詳細）

目的: すばやく画面デザインを検証するために Tailwind CDN を用いたプロトタイプを作成する手順を示す。

### 1) head に挿入するスニペット（各テンプレートの <head> 内に追加）
```html
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet">
<script src="https://cdn.tailwindcss.com"></script>
<script>
  tailwind.config = {
    theme: {
      extend: {
        colors: {
          primary: '#06b6d4', /* cyan-500 */
          accent: '#3b82f6'   /* blue-500 */
        },
        fontFamily: {
          sans: ['Inter', 'ui-sans-serif', 'system-ui']
        }
      }
    }
  }
</script>
<style>body{font-family:Inter, system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial;}</style>
```

- 注意: 既存で共通ヘッドフラグメント（例: `fragments/head.html`）を使用している場合はそのフラグメントに追加してください。

### 2) ページ別の初期クラス配布（すばやく見た目を確認するためのテンプレート）
- 共通レイアウト
  - body: `class="min-h-screen bg-slate-900 text-slate-100"`
  - コンテナカード: `class="max-w-4xl mx-auto p-8 bg-white/5 backdrop-blur-md rounded-2xl shadow-lg"`
  - ボタン（グラデ）: `class="px-6 py-3 rounded-lg text-white font-semibold bg-gradient-to-r from-primary to-accent shadow-md hover:scale-105 transition-transform"`

- `index.html`（ヒーロー）
  - ヒーローコンテナ: `class="min-h-screen flex items-center"`
  - キャッチ: `class="text-4xl md:text-6xl font-extrabold leading-tight text-white/95"`
  - CTA: `class="mt-6 inline-block px-8 py-4 rounded-xl bg-gradient-to-r from-primary to-accent text-white text-lg"`

- `janken.html`（手選択）
  - ステータスバー: `class="w-full flex items-center justify-between bg-white/3 p-3 rounded-lg mb-6"`
  - グリッド: `class="grid grid-cols-1 sm:grid-cols-3 gap-6"`
  - 手カード: `class="bg-white/4 backdrop-blur rounded-xl p-6 flex flex-col items-center gap-4 hover:scale-105 transform transition-shadow shadow-md cursor-pointer"`

- `result.html`（結果）
  - 勝ち表示: `class="ring-4 ring-primary/40 bg-white/6 rounded-xl p-8 shadow-xl"`
  - neutral: `class="bg-white/5 rounded-xl p-8"`
  - ボタン群: `class="flex gap-4 mt-6"`

- `history.html`（履歴）
  - 検索バー: `class="flex items-center gap-3 mb-4"`
  - リスト: `class="divide-y divide-white/6"`
  - 履歴アイテム: `class="py-4 flex items-center justify-between bg-white/3 rounded-lg p-4 mb-3"`

### 3) 実装手順（プロトタイプ）
1. ワークツリーが main ブランチであることを確認し、新しいブランチを作成する（例: `feat/tailwind-cdn-prototype`）。
2. 各テンプレート（`src/main/resources/templates/*.html`）の `<head>` に上記スニペットを追加する。
3. 主要コンテナ（ヘッダ、メイン、ボタン、カード）に上記クラスを仮適用する。まずは大枠のみで可。
4. `./gradlew bootRun` でアプリを起動し、ブラウザで `/`, `/janken`, `/history` および結果ページを確認する。
5. デザインをレビューし、必要に応じてクラスを調整する。

### 4) 検証項目
- レスポンシブ（スマホ・タブレット・デスクトップ）でレイアウト崩れがないか。
- ボタンの hover/active エフェクトが期待通りに動作するか。
- Thymeleaf の動的部分（ユーザ名、スコア、履歴）が崩れていないか。
- ブラウザの開発者ツールで不要な CSS の問題やコンソールエラーがないか。

### 5) 留意点（CDN 利用の制約）
- 開発・検証目的では有効だが、本番ではビルド導入を推奨する。
- CSP が有効な環境では CDN の許可設定が必要。
- Play CDN はページで利用するクラスを動的に解釈するが、複雑なサーバサイドテンプレートの組み合わせによっては想定外のクラスが生成されることがあるため、動作確認を入念に行うこと。

---

## 次のアクション（提案）
1. すべてのテンプレートに CDN を適用してプロトタイプ実装を行う（推奨）。私が実装を行う場合はブランチ `feat/tailwind-cdn-prototype` を作成して変更を適用します。実行する場合は「実装してください」と指示してください。
2. まずは `index.html` のみ適用して見た目を確認する。段階的に進めたい場合に選択してください。

作業報告追記:
- CDN 実装案（head スニペット、クラス一覧、手順、検証項目）を本レポートに追記しました。
