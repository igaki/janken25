# じゃんけん（CPU対戦）実装計画 2025-10-06

## 概要
CPUと対戦するじゃんけんゲームの基本機能をSpring Bootで実装するための計画。

## 関連ファイル
- src/main/java/oit/is/z9999/kaizi/janken/JankenController.java
- src/main/java/oit/is/z9999/kaizi/janken/JankenService.java
- src/main/java/oit/is/z9999/kaizi/janken/model/JankenResult.java
- src/main/resources/templates/janken.html
- src/main/resources/templates/result.html
- src/test/java/oit/is/z9999/kaizi/janken/JankenServiceTests.java

# ユーザログイン機能追加計画 2025-10-07

## 概要
Spring Boot + Spring Security によるユーザログイン機能の追加計画。

## 関連ファイル
- src/main/java/oit/is/z9999/kaizi/janken/LoginController.java
- src/main/java/oit/is/z9999/kaizi/janken/UserService.java
- src/main/java/oit/is/z9999/kaizi/janken/model/User.java
- src/main/java/oit/is/z9999/kaizi/janken/config/SecurityConfig.java
- src/main/resources/templates/login.html
- src/main/resources/templates/home.html
- src/test/java/oit/is/z9999/kaizi/janken/UserServiceTests.java

## タスク一覧
1. Userモデルクラスの作成
   - ユーザID、パスワード等を保持するクラス
2. UserServiceクラスの作成
   - ユーザ認証ロジックの実装
   - ユーザ情報の管理（メモリ）
3. SecurityConfigクラスの作成
   - Spring Securityの認証設定
4. LoginControllerクラスの作成
   - ログイン画面表示、認証処理
5. 画面（login.html, home.html）の作成
   - ログインフォーム、認証後のホーム画面
6. テストコードの作成
   - UserServiceの認証ロジックの単体テスト

## 優先順位
1 → 2 → 3 → 4 → 5 → 6 の順で実施

---

# じゃんけん（CPU対戦）実装計画 2025-10-06

## 概要
CPUと対戦するじゃんけんゲームの基本機能をSpring Bootで実装するための計画。

## 関連ファイル
- src/main/java/oit/is/z9999/kaizi/janken/JankenController.java
- src/main/java/oit/is/z9999/kaizi/janken/JankenService.java
- src/main/java/oit/is/z9999/kaizi/janken/model/JankenResult.java
- src/main/resources/templates/janken.html
- src/main/resources/templates/result.html
- src/test/java/oit/is/z9999/kaizi/janken/JankenServiceTests.java

## タスク一覧
1. JankenResultモデルクラスの作成
   - ユーザの手、CPUの手、勝敗結果を保持するクラス
2. JankenServiceクラスの作成
   - CPUの手を乱数で生成
   - 勝敗判定ロジックを実装
3. JankenControllerクラスの作成
   - ユーザの手を受け取り、サービス層に処理を委譲
   - 結果をテンプレートに渡して表示
4. 画面（janken.html, result.html）の作成
   - ユーザが手を選択できるフォーム
   - 結果表示用画面
5. テストコードの作成
   - JankenServiceの勝敗判定ロジックの単体テスト

## 優先順位
1 → 2 → 3 → 4 → 5 の順で実施

---

この計画に従い、実装フェーズを進めてください。
