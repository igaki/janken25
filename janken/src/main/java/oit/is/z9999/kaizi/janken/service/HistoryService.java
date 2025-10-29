package oit.is.z9999.kaizi.janken.service;

import java.util.List;
import oit.is.z9999.kaizi.janken.model.JankenHistory;
import oit.is.z9999.kaizi.janken.model.JankenResult;

public interface HistoryService {
  void add(String username, JankenResult result);

  List<JankenHistory> getLast(String username, int n);

  List<JankenHistory> getAll(String username);
}
