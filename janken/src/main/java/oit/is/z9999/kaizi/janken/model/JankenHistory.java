package oit.is.z9999.kaizi.janken.model;

import java.time.LocalDateTime;

public class JankenHistory {
  private Long id;
  private String username;
  private String userHand;
  private String cpuHand;
  private String result;
  private LocalDateTime playedAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUserHand() {
    return userHand;
  }

  public void setUserHand(String userHand) {
    this.userHand = userHand;
  }

  public String getCpuHand() {
    return cpuHand;
  }

  public void setCpuHand(String cpuHand) {
    this.cpuHand = cpuHand;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public LocalDateTime getPlayedAt() {
    return playedAt;
  }

  public void setPlayedAt(LocalDateTime playedAt) {
    this.playedAt = playedAt;
  }
}
