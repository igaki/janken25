package oit.is.z9999.kaizi.janken.model;

/**
 * ユーザ情報を保持するモデルクラス
 */
public class User {
  /** ユーザID */
  private String username;
  /** パスワード */
  private String password;

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
