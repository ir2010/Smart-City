package com.ir.smartcity.job;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APiInterface {
    @GET("maps/api/directions/json")
    Single<Result> getDirection(@Query("mode")String mode,
                                @Query("transit_routing_preference") String prference,
                                @Query("origin") String origin,
                                @Query("destination") String destination,
                                @Query("key") String key);
}
