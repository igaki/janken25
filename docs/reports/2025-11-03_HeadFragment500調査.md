# 2025-11-03 Head fragment による 500 エラー調査報告

作成日: 2025-11-03
作成者: GitHub Copilot

## 概要
/janken のプレイ後に結果画面から「もう一度勝負する」をクリックすると 500 エラーが発生する問題を調査しました。原因はテンプレートの head フラグメント `fragments/head.html` の定義不備により、レンダリング時にドキュメント構造が重複・不整合になって Thymeleaf 側で例外が発生していると推定されます。

## 調査手順（実行済 / 推奨）
1. 変更履歴とテンプレート内容を確認（`janken/src/main/resources/templates/fragments/head.html`、各テンプレートの `th:replace`）。
2. 問題の再現手順を確認：
   - サーバ起動: `./gradlew bootRun`
   - ブラウザで `/janken` を開き、勝負を実行して結果ページへ遷移
   - 結果画面の「もう一度勝負する」（`/janken` へのリンク）をクリックして 500 エラー発生
3. サーバのコンソールログ（スタックトレース）を確認することを推奨（本レポート作成時点で実ログ未送付）。

## 発見した事実
- 既存の `fragments/head.html` が HTML ドキュメント全体（`<!DOCTYPE html>`、`<html>...</html>` を含む）として作成されていました。
- 各テンプレート（例: `janken.html`）側では `<!DOCTYPE html><html lang="ja"><head th:replace="fragments/head :: head">` として head を置換しているため、fragment 側の `<!DOCTYPE html>` / `<html>` と衝突し、結果としてテンプレートの構造が不整合になります。
- Thymeleaf のテンプレート処理は文書構造の整合を期待するため、不整合が原因で TemplateProcessingException や類似の例外（500 エラー）が発生する可能性が高い。

## 根本原因（推定）
fragments/head.html に `<!DOCTYPE html>` と `<html>` を含めた写し全体を置いてしまったため、`th:replace` による head 部差し替え時に HTML 構造が二重になりテンプレート処理が失敗している。

## 迅速な対処案（推奨・最優先）
fragments/head.html を head 要素だけを定義する fragment に修正する。具体例は以下。

- 修正内容（fragments/head.html の例）
```html
<!-- fragments/head.html -->
<head th:fragment="head">
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet">
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      theme: {
        extend: {
          colors: { primary: '#06b6d4', accent: '#3b82f6' },
          fontFamily: { sans: ['Inter','ui-sans-serif','system-ui'] }
        }
      }
    }
  </script>
  <style>body{font-family:Inter, system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial;}</style>
  <title th:text="${pageTitle ?: 'Janken'}">Janken</title>
</head>
```
- 各テンプレートは既に `<!DOCTYPE html><html lang="ja"><head th:replace="fragments/head :: head">` 形式になっているため、fragment を上記のように修正することで二重構造は解消されます。

## 検証手順（修正後）
1. `fragments/head.html` を上記の通り修正・保存
2. アプリを起動: `./gradlew bootRun`
3. ブラウザで `/janken` にアクセスしプレイ → 結果画面へ
4. 「もう一度勝負する」をクリックして `/janken` に戻れることを確認
5. サーバログに例外が出ていないことを確認

## 追加で確認すべきログ/情報（重要）
- 実際の 500 エラーのサーバ側スタックトレース（log 出力）を提供いただければ、より確実に原因特定できます。出力例: TemplateProcessingException, IllegalStateException 等。
- ブラウザでの HTML レスポンス（エラーページ）に表示される例外メッセージのスクリーンショットあるいは本文。

## 影響範囲（ファイル一覧）
- 変更対象（修正が必要な可能性が高い）:
  - `janken/src/main/resources/templates/fragments/head.html`
  - `janken/src/main/resources/templates/janken.html`
  - `janken/src/main/resources/templates/result.html`
  - `janken/src/main/resources/templates/history.html`

## 推奨作業フロー（短期）
1. fragments/head.html を head のみの fragment に修正（上記参照）
2. `./gradlew bootRun` で再起動して再現手順を実行
3. 問題解消を確認できたら修正をコミット（branch: `fix/tailwind-head-fix`）しプッシュ、PR を作成

## 次のアクション（選択）
- 私が fragments/head.html を修正して動作確認まで行う（実装します）: 指示「実装してください」を送ってください。
- あなたの環境で先にログ（サーバのスタックトレース）を取得して提供してください。ログを解析して追加の原因を特定します。

---

作業報告は以上です。必要なら今すぐ修正を適用します。
