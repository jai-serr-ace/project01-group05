package com.example.project01_group05.api;

import com.google.gson.annotations.SerializedName;

public class MangaTitle {

    @SerializedName("en")
    private String english;

    public String getEnglish() {
        return english;
    }
}
