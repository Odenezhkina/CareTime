package com.example.testingappproject.model;

// in Tracker class we do not have points value we have only max points value, but in RecyclerView we
// have to known current tracker points (I mean current because we display ONLY last points linked to the CURRENT date),
// we have to divide Tracker and its points into different classes, BUT gather them together in RecyclerView
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
