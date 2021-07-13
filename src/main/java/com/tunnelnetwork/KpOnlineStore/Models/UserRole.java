package com.tunnelnetwork.KpOnlineStore.Models;

public enum UserRole {
  ADMIN, USER;

  @Override
  public String toString() {
      return  "ROLE_"+this.name();
  }
}
