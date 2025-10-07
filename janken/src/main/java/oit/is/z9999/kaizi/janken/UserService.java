package oit.is.z9999.kaizi.janken;

import oit.is.z9999.kaizi.janken.model.User;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ユーザ認証・管理サービス
 */
@Service
public class UserService {
  // メモリ上でユーザ情報を保持（サンプル実装）
  private final Map<String, User> userMap = new HashMap<>();

  public UserService() {
    // サンプルユーザ追加（本番はDB等で管理）
    addUser("user1", "pass1");
    addUser("user2", "pass2");
  }

  /**
   * ユーザ認証
   * 
   * @param username ユーザID
   * @param password パスワード（平文）
   * @return 認証成功ならUser、失敗ならnull
   */
  public User authenticate(String username, String password) {
    User user = userMap.get(username);
    String hashed = hashPassword(password);
    if (user != null && user.getPassword().equals(hashed)) {
      return user;
    }
    return null;
  }

  /**
   * ユーザ追加（パスワードはハッシュ化して保存）
   */
  public void addUser(String username, String password) {
    String hashed = hashPassword(password);
    userMap.put(username, new User(username, hashed));
  }

  /**
   * パスワードをSHA-256でハッシュ化
   */
  private String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hash = md.digest(password.getBytes());
      StringBuilder sb = new StringBuilder();
      for (byte b : hash) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Hashアルゴリズムエラー", e);
    }
  }
}
