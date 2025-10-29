package oit.is.z9999.kaizi.janken.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oit.is.z9999.kaizi.janken.mapper.JankenHistoryMapper;
import oit.is.z9999.kaizi.janken.model.JankenHistory;
import oit.is.z9999.kaizi.janken.model.JankenResult;

@Service
public class MybatisHistoryService implements HistoryService {

  @Autowired
  private JankenHistoryMapper mapper;

  @Override
  public void add(String username, JankenResult result) {
    JankenHistory h = new JankenHistory();
    h.setUsername(username);
    h.setUserHand(result.getUserHand());
    h.setCpuHand(result.getCpuHand());
    h.setResult(result.getResult());
    mapper.insert(h);
  }

  @Override
  public List<JankenHistory> getLast(String username, int n) {
    return mapper.selectRecentByUsername(username, n);
  }

  @Override
  public List<JankenHistory> getAll(String username) {
    return mapper.selectByUsername(username);
  }
}
