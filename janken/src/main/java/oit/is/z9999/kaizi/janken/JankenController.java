package oit.is.z9999.kaizi.janken;

import oit.is.z9999.kaizi.janken.model.JankenResult;
import oit.is.z9999.kaizi.janken.model.JankenHistory;
import oit.is.z9999.kaizi.janken.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class JankenController {

  @Autowired
  private JankenService jankenService;

  @Autowired
  private HistoryService historyService;

  /**
   * じゃんけん選択画面
   */
  @GetMapping("/janken")
  public String showJankenForm(Model model, Principal principal) {
    if (principal != null) {
      List<JankenHistory> recent = historyService.getLast(principal.getName(), 5);
      model.addAttribute("recentHistory", recent);
    }
    return "janken";
  }

  /**
   * じゃんけん結果表示
   */
  @PostMapping("/janken/result")
  public String playJanken(@RequestParam("userHand") String userHand, Model model, Principal principal) {
    JankenResult result = jankenService.play(userHand);
    model.addAttribute("result", result);
    if (principal != null) {
      historyService.add(principal.getName(), result);
    }
    return "result";
  }

  /**
   * 履歴表示
   */
  @GetMapping("/janken/history")
  public String showHistory(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/";
    }
    List<JankenHistory> history = historyService.getAll(principal.getName());
    model.addAttribute("history", history);
    return "history";
  }

  /**
   * ルートパス（/）でじゃんけん選択画面を表示
   */
  @GetMapping("/")
  public String showRootJanken() {
    return "janken";
  }
}
