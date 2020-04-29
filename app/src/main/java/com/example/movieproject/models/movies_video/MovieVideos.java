package com.example.movieproject.models.movies_video;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MovieVideos {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<ResultsItem> results;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setResults(List<ResultsItem> results) {
        this.results = results;
    }

    public List<ResultsItem> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return
                "MovieVideos{" +
                        "id = '" + id + '\'' +
                        ",results = '" + results + '\'' +
                        "}";
    }
}