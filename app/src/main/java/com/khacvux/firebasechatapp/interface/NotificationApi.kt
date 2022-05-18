package com.khacvux.firebasechatapp.`interface`

import com.khacvux.firebasechatapp.Constants.Constants.Companion.CONTENT_TYPE
import com.khacvux.firebasechatapp.Constants.Constants.Companion.SERVER_KEY
import com.khacvux.firebasechatapp.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers("AuthorZation key: $SERVER_KEY", "Content-type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification (
        @Body notification: PushNotification
    ):Response<ResponseBody>
}