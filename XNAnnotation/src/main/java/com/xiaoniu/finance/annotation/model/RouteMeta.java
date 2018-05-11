package com.xiaoniu.finance.annotation.model;

/**
 * Created by wenzhonghu on 2018/5/9.
 */

public class RouteMeta {
    private String url;
    private boolean enable;
    private int permissionType;
    private boolean crossable;
    private boolean match;

    /**
     * @param url
     * @param enable
     * @param permissionType
     * @param crossable
     * @param match
     */
    public static RouteMeta build(String url, boolean enable, int permissionType, boolean crossable, boolean match) {
        return new RouteMeta(url, enable, permissionType, crossable, match);
    }

    /**
     * @param url
     * @param enable
     * @param permissionType
     * @param crossable
     * @param match
     */
    public RouteMeta(String url, boolean enable, int permissionType, boolean crossable, boolean match) {
        this.url = url;
        this.enable = enable;
        this.permissionType = permissionType;
        this.crossable = crossable;
        this.match = match;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
    }

    public boolean isCrossable() {
        return crossable;
    }

    public void setCrossable(boolean crossable) {
        this.crossable = crossable;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }

    @Override
    public String toString() {
        return "RouteMeta{" +
                "url='" + url + '\'' +
                ", enable=" + enable +
                ", permissionType=" + permissionType +
                ", crossable=" + crossable +
                ", match=" + match +
                '}';
    }
}
