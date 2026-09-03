package com.example.project01_group05.api;

import java.util.List;

public interface MangaCallback {

    void onSuccess(List<MangaData> mangaList);

    void onError(String errorMessage);
}
