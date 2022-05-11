package com.tallymarks.engroffm.utils.network;

import com.tallymarks.engroffm.models.farmerMeeting.post.CreateFarmerMeetingRequest;
import com.tallymarks.engroffm.models.farmerMeeting.post.CreateFarmerMeetingResponse;
import com.tallymarks.engroffm.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CreateFarmerMeetingInterface {

    @POST(Constants.FFM_CREATE_FARMER_MEETING)
    Call<CreateFarmerMeetingResponse> createMeeting(@Body ArrayList<CreateFarmerMeetingRequest> createFarmerMeetingRequest, @Header(Constants.AUTHORIZATION) String authorization);
}
