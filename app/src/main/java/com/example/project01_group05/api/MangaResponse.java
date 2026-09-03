package com.example.project01_group05.api;

import java.util.List;

public class MangaResponse {

    private String result;
    private List<MangaData> data;

    public String getResult() {
        return result;
    }

    public List<MangaData> getData() {
        return data;
    }
}
