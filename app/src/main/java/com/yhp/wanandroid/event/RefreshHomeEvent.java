package com.yhp.wanandroid.event;

public class RefreshHomeEvent {
    private boolean isRefresh;

    public RefreshHomeEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
