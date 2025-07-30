package com.rhix.fragmentnav;

public class TimelineItem {
    public String title;
    public String description;
    public String timestamp;
    public String icon; // Drawable name

    public TimelineItem(String title, String description, String timestamp, String icon) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.icon = icon;
    }
}
