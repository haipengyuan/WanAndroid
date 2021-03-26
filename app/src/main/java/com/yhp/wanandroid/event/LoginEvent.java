package com.yhp.wanandroid.event;

public class LoginEvent {
    private boolean isLogin;
    private String user;

    public LoginEvent(boolean isLogin, String user) {
        this.isLogin = isLogin;
        this.user = user;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
