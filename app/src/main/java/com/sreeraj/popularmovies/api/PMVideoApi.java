package com.sreeraj.popularmovies.api;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.api.response.VideoResponseBean;
import com.sreeraj.popularmovies.events.FailureEvent;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * PMVideoApi which handles api call to get videos of a movie.
 */
public class PMVideoApi extends PMBaseApi {

    public void getVideoDetails(int id, String apiKey) {
        Call<VideoResponseBean> call = service.getVideoDetails(id, apiKey);
        call.enqueue(new Callback<VideoResponseBean>() {
            @Override
            public void onResponse(Call<VideoResponseBean> call, Response<VideoResponseBean> response) {
                EventBus.getDefault().postSticky(response.body());
            }

            @Override
            public void onFailure(Call<VideoResponseBean> call, Throwable t) {
                EventBus.getDefault().postSticky(new FailureEvent(R.string.some_unknown_error_has_occurred));
            }
        });
    }
}
