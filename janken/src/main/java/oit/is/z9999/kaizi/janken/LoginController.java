package oit.is.z9999.kaizi.janken;

import oit.is.z9999.kaizi.janken.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

/**
 * ログイン画面・認証処理コントローラ
 */
@Controller
public class LoginController {

  @Autowired
  private UserService userService;

  /**
   * ログイン画面表示
   */
  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  /**
   * ログイン認証処理
   */
  @PostMapping("/login")
  public String login(@RequestParam("username") String username,
      @RequestParam("password") String password,
      Model model,
      HttpSession session) {
    User user = userService.authenticate(username, password);
    if (user != null) {
      session.setAttribute("loginUser", user);
      return "redirect:/home";
    } else {
      model.addAttribute("loginError", "ユーザIDまたはパスワードが違います");
      return "login";
    }
  }

  /**
   * ホーム画面表示（認証後）
   */
  @GetMapping("/home")
  public String showHome(Model model, HttpSession session) {
    User user = (User) session.getAttribute("loginUser");
    model.addAttribute("user", user);
    return "home";
  }

  /**
   * ログアウト処理
   */
  @PostMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
  }
}
