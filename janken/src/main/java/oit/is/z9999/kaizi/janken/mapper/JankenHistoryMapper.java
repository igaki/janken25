package oit.is.z9999.kaizi.janken.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import oit.is.z9999.kaizi.janken.model.JankenHistory;

@Mapper
public interface JankenHistoryMapper {
  void insert(JankenHistory history);

  List<JankenHistory> selectRecentByUsername(@Param("username") String username, @Param("limit") int limit);

  List<JankenHistory> selectByUsername(@Param("username") String username);
}
