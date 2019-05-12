package com.example.mcproject;

public class UserDetails {
    public String uuid;
    public String email;
    public String gameId;
    public String totalTimeRequired;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTotalTimeRequired() {
        return totalTimeRequired;
    }

    public void setTotalTimeRequired(String totalTimeRequired) {
        this.totalTimeRequired = totalTimeRequired;
    }
}
