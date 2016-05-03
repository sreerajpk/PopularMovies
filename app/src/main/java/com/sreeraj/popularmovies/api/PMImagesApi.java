package com.sreeraj.popularmovies.api;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.api.response.ImagesResponseBean;
import com.sreeraj.popularmovies.events.FailureEvent;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sreeraj on 3/2/16.
 */
public class PMImagesApi extends PMBaseApi {

    public void getImages(int id, String apiKey) {
        Call<ImagesResponseBean> call = service.getImages(id, apiKey);
        call.enqueue(new Callback<ImagesResponseBean>() {
            @Override
            public void onResponse(Call<ImagesResponseBean> call, Response<ImagesResponseBean> response) {
                EventBus.getDefault().postSticky(response.body());
            }

            @Override
            public void onFailure(Call<ImagesResponseBean> call, Throwable t) {
                EventBus.getDefault().postSticky(new FailureEvent(R.string.some_unknown_error_has_occurred));
            }
        });
    }
}
