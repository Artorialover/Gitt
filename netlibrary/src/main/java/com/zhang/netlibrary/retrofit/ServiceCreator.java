package com.zhang.netlibrary.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator {
    private static ServiceCreator serviceCreator;

    private String BASE_URI="http://10.0.2.2/";
    private OkHttpClient client;
    private Retrofit retrofit;

    public static ServiceCreator getInstance(){
        if(serviceCreator==null){
            synchronized (ServiceCreator.class){
                serviceCreator=new ServiceCreator();
            }
        }
        return serviceCreator;
    }

    private ServiceCreator() {
        if(client==null){
            OkHttpClient.Builder builder=new OkHttpClient.Builder();
            client=builder.connectTimeout(15, TimeUnit.SECONDS)
                          .readTimeout(15,TimeUnit.SECONDS)
                          .writeTimeout(15,TimeUnit.SECONDS)
                          .build();
        }
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
    }

    public <T> T create(Class<T> tClass){
        return retrofit.create(tClass);
    }


    
}
