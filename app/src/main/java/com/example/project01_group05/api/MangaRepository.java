package com.example.project01_group05.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MangaRepository {

    public void fetchManga(MangaCallback callback) {

        MangaDexApi api = MangaDexClient.getApi();

        api.getManga(10).enqueue(new Callback<MangaResponse>() {

            @Override
            public void onResponse(
                    Call<MangaResponse> call,
                    Response<MangaResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<MangaData> mangaList = response.body().getData();

                    callback.onSuccess(mangaList);

                } else {

                    callback.onError(
                            "MangaDex request failed. HTTP code: "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<MangaResponse> call,
                    Throwable throwable) {

                String message = throwable.getMessage();

                if (message == null) {
                    message = "Unable to retrieve manga from MangaDex.";
                }

                callback.onError(message);
            }
        });
    }
}