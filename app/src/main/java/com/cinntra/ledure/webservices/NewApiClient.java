package com.cinntra.ledure.webservices;


import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MyApp;
import com.cinntra.ledure.globals.SessionManagement;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class NewApiClient {
    private static NewApiClient ourInstance = new NewApiClient();
    private final boolean SHOW_LOGS = false;
    private final int TIME_OUT = 1500000;
    private ApiServices apiServices = null;
    private Retrofit.Builder mRetrofitBuilder = null;
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private OkHttpClient.Builder httpClientForCache = new OkHttpClient.Builder();
    private Dispatcher mDispatcher;


    public NewApiClient() {
    }

    public static NewApiClient getInstance() {
        return ourInstance;
    }

    public Retrofit.Builder getBuilder() {
        if (mRetrofitBuilder == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(provideOfflineCacheInterceptor())
                    .addNetworkInterceptor(provideCacheInterceptor()) //FOR PLAY STORE
                    .cache(provideCache()).build();

            mRetrofitBuilder = new Retrofit.Builder()
                    .baseUrl(Globals.NewBaseUrl).client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create());

        }


        return mRetrofitBuilder;
    }


    public ApiServices getApiService(Context context) {
        SessionManagement sessionManagement;
        sessionManagement = new SessionManagement(context);


        if (apiServices == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); //FOR PLAY STORE
            CookieHandler cookieHandler = new CookieManager();
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            httpClient.cookieJar(new JavaNetCookieJar(cookieManager));
          /*  File httpCacheDirectory = new File(MyApp.getInstance().getApplicationContext().getCacheDir(), "offlineCache");
             //10 MB
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
*/

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request originalRequest = chain.request();
                    final Request.Builder builder;
                    if (Globals.APILog.equalsIgnoreCase("APILog")) {
                        builder = originalRequest.newBuilder()
                                .header("content-type", "application/json").cacheControl(CacheControl.FORCE_NETWORK);
                        Globals.APILog = "Not";
                    } else {
                        if (Prefs.getString(Globals.TOKEN, "").equalsIgnoreCase("")){
                            builder = originalRequest.newBuilder()
                                    //.removeHeader("Cache-Control")
                                    .addHeader("Authorization", "Bearer " + sessionManagement.getToken())
                                    // .addHeader("WWW-Authenticate", "Basic " + Prefs.getString(Globals.SessionID, ""))
                                    // .header("User-Agent", "android")
                                    // .addHeader("Cookie", "Bearer " + Prefs.getString(Globals.SessionID, ""))
                                    .header("content-type", "application/json").cacheControl(CacheControl.FORCE_NETWORK);
                        }else {
                            builder = originalRequest.newBuilder()
                                    //.removeHeader("Cache-Control")
                                    .addHeader("Authorization", "Bearer " + Prefs.getString(Globals.TOKEN, ""))
                                    // .addHeader("WWW-Authenticate", "Basic " + Prefs.getString(Globals.SessionID, ""))
                                    // .header("User-Agent", "android")
                                    // .addHeader("Cookie", "Bearer " + Prefs.getString(Globals.SessionID, ""))
                                    .header("content-type", "application/json").cacheControl(CacheControl.FORCE_NETWORK);
                        }


                    }


                    Request newRequest = builder.build();
                    //.header("Authorization", Prefs.getString(Globals.TOKEN,""));
                    return chain.proceed(newRequest);

                }
            });

         //  httpClient.addInterceptor(interceptor); //FOR PLAY STORE
            httpClient.readTimeout(300, TimeUnit.SECONDS);
            httpClient.connectTimeout(300, TimeUnit.SECONDS);
            httpClient.writeTimeout(300, TimeUnit.SECONDS);

            // PLAY STORE
          /*  if (SHOW_LOGS)
                httpClient.addInterceptor(new LoggingInterceptor());*/

            OkHttpClient client = httpClient.build();
            Retrofit retrofit = getBuilder().client(client).build();


            apiServices = retrofit.create(ApiServices.class);
        }
        return apiServices;
    }


    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {


            Request request = chain.request();

            long t1 = System.nanoTime();

            Log.d("AVIS_NW", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d("AVIS_NW", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));


            final String responseString = new String(response.body().bytes());

            Log.d("AVIS_NW", "Response: " + responseString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }

    /************* Offline Work Manager ****************/
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Cinntra";

    private Context mContext;

    private Cache provideCache() {
        Cache cache = null;

        try {
            cache = new Cache(new File(mContext.getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
//            Log.e(TAG, "Could not create Cache!");
        }

        return cache;
    }

    private Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());

            CacheControl cacheControl;

            if (isConnected()) {
                cacheControl = new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build();
            } else {
                cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();
            }

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();

        };
    }

    private Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            if (!isConnected()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    public boolean isConnected() {
        try {
            android.net.ConnectivityManager e = (android.net.ConnectivityManager) mContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = e.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        }

        return false;
    }


}
