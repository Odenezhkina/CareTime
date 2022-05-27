package com.example.testingappproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "trackers", indices = {@Index(value = "headline", unique = true)})
public class Tracker {
    @PrimaryKey(autoGenerate = true)
    public long id;

    private final String headline;

    @ColumnInfo(name = "img_res")
    private final int imgResource;

    @ColumnInfo(name = "max_points")
    private final int maxPoints;


    public Tracker(String headline, int imgResource, int maxPoints) {
        this.headline = headline;
        this.imgResource = imgResource;
        this.maxPoints = maxPoints;
    }

    public String getHeadline() {
        return headline;
    }

    public int getImgResource() {
        return imgResource;
    }

    public int getMaxPoints() {
        return maxPoints;
    }


    @Override
    public String toString() {
        return "Tracker{" +
                "id=" + id +
                ", headline='" + headline + '\'' +
                ", imgResource=" + imgResource +
                ", maxPoints=" + maxPoints +
                '}';
    }
}


