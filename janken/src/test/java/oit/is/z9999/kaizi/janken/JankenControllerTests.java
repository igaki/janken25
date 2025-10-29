package oit.is.z9999.kaizi.janken;

import oit.is.z9999.kaizi.janken.model.JankenResult;
import oit.is.z9999.kaizi.janken.service.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(JankenController.class)
class JankenControllerTests {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private JankenService jankenService;

  @MockBean
  private HistoryService historyService;

  @Test
  void testShowJankenForm() throws Exception {
    // 認証ユーザでログインしてアクセス
    mockMvc.perform(MockMvcRequestBuilders.get("/janken")
        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("yamada")
            .password("tarou")))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("janken"));
  }

  @Test
  void testPlayJanken() throws Exception {
    JankenResult mockResult = new JankenResult("グー", "チョキ", "勝ち");
    when(jankenService.play("グー")).thenReturn(mockResult);

    mockMvc.perform(MockMvcRequestBuilders.post("/janken/result")
        .param("userHand", "グー")
        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("yamada")
            .password("tarou"))
        .with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("result"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("result"));
  }
}
