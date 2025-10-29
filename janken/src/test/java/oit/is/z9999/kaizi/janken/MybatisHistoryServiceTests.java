package oit.is.z9999.kaizi.janken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import oit.is.z9999.kaizi.janken.mapper.JankenHistoryMapper;
import oit.is.z9999.kaizi.janken.model.JankenHistory;
import oit.is.z9999.kaizi.janken.service.MybatisHistoryService;

public class MybatisHistoryServiceTests {

  @Mock
  JankenHistoryMapper mapper;

  @InjectMocks
  MybatisHistoryService service;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testAddAndGetLast() {
    List<JankenHistory> mockList = new ArrayList<>();
    JankenHistory h = new JankenHistory();
    h.setUsername("yamada");
    h.setUserHand("グー");
    h.setCpuHand("チョキ");
    h.setResult("勝ち");
    mockList.add(h);

    when(mapper.selectRecentByUsername("yamada", 5)).thenReturn(mockList);
    List<JankenHistory> res = service.getLast("yamada", 5);
    assertThat(res).isNotEmpty();
    assertThat(res.get(0).getUserHand()).isEqualTo("グー");
  }
}
