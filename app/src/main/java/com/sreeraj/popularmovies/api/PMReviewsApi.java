package com.sreeraj.popularmovies.api;

import com.sreeraj.popularmovies.R;
import com.sreeraj.popularmovies.api.response.ReviewsResponseBean;
import com.sreeraj.popularmovies.events.FailureEvent;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sreeraj on 3/2/16.
 */
public class PMReviewsApi extends PMBaseApi {

    public void getReviews(long id, String apiKey) {
        Call<ReviewsResponseBean> call = service.getReviews(id, apiKey);
        call.enqueue(new Callback<ReviewsResponseBean>() {
            @Override
            public void onResponse(Call<ReviewsResponseBean> call, Response<ReviewsResponseBean> response) {
                EventBus.getDefault().postSticky(response.body());
            }

            @Override
            public void onFailure(Call<ReviewsResponseBean> call, Throwable t) {
                EventBus.getDefault().postSticky(new FailureEvent(R.string.some_unknown_error_has_occurred));
            }
        });
    }
}
