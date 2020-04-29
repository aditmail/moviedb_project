package com.example.movieproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.movieproject.R;
import com.example.movieproject.models.movies_video.MovieVideos;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.ListViewHolder> {

    private MovieVideos movieVideos;
    private Context context;

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.webView_Video)
        WebView webViewVideo;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MovieVideosAdapter(MovieVideos movieVideos, Context context) {
        this.context = context;
        this.movieVideos = movieVideos;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_movie_video_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.webViewVideo.setWebChromeClient(new WebChromeClient());
        holder.webViewVideo.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        holder.webViewVideo.getSettings().setJavaScriptEnabled(true);
        holder.webViewVideo.getSettings().setDefaultFontSize(18);

        String site = movieVideos.getResults().get(position).getSite();
        if (site != null && !site.equalsIgnoreCase("null")) {
            if (site.equalsIgnoreCase("Youtube")) {
                String embedCode = movieVideos.getResults().get(position).getKey();
                String htmlCode =
                        "<iframe width=\"100%\" height=\"100%\" " +
                                "src=\"https://www.youtube.com/embed/" +
                                embedCode + "\"" +
                                "frameborder=\"0\"" +
                                "allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" " +
                                "allowfullscreen></iframe>";

                holder.webViewVideo.loadData(htmlCode, "text/html; charset=utf-8", null);
            }
        }

    }

    @Override
    public int getItemCount() {
        return movieVideos.getResults().size();
    }
}
