package oit.is.z9999.kaizi.janken;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import oit.is.z9999.kaizi.janken.mapper.JankenHistoryMapper;
import oit.is.z9999.kaizi.janken.model.JankenHistory;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class JankenHistoryMapperTests {

  @Autowired
  JankenHistoryMapper mapper;

  @Test
  public void testInsertAndSelect() {
    JankenHistory h = new JankenHistory();
    h.setUsername("yamada");
    h.setUserHand("グー");
    h.setCpuHand("チョキ");
    h.setResult("勝ち");
    mapper.insert(h);

    List<JankenHistory> list = mapper.selectByUsername("yamada");
    assertThat(list).isNotEmpty();
    assertThat(list.get(0).getUserHand()).isEqualTo("グー");
  }
}
