package com.example.testingappproject.model;

public class TrackerDatePoint {
    public String headline;
    public int img_res;
    public int max_points;
    public int points;
    public long tracker_id;
    public long date_id;

    @Override
    public String toString() {
        return "TrackerDatePoint{" +
                "headline='" + headline + '\'' +
                ", img_res=" + img_res +
                ", max_points=" + max_points +
                ", points=" + points +
                ", tracker_id=" + tracker_id +
                ", date_id=" + date_id +
                '}';
    }
}
