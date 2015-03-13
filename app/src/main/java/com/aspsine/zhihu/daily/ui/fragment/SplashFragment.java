package com.aspsine.zhihu.daily.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.aspsine.zhihu.daily.R;
import com.aspsine.zhihu.daily.network.Http;
import com.aspsine.zhihu.daily.util.NetWorkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sf on 2015/1/13.
 * splash page
 */
public class SplashFragment extends Fragment {
    public static final String TAG = SplashFragment.class.getSimpleName();
    public ImageView ivSplash;
    public Animation mIvSplashAnim;

    @Override
    public void onAttach(Activity activity) {
        mIvSplashAnim = AnimationUtils.loadAnimation(activity, R.anim.splash);
        super.onAttach(activity);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(new GetSplashRunnable()).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivSplash = (ImageView) view.findViewById(R.id.splash);
        ivSplash.startAnimation(mIvSplashAnim);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIvSplashAnim = null;
    }

    private final class GetSplashRunnable implements Runnable {

        @Override
        public void run() {
            File cacheDir = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheDir = getActivity().getExternalCacheDir();
                Log.i(TAG, "external storage");
            } else {
                cacheDir = getActivity().getCacheDir();
                Log.i(TAG, "internal storage");
            }

            File splashCacheDir = new File(cacheDir, "splash");
            if (!splashCacheDir.exists()) {
                splashCacheDir.mkdir();
                handler.obtainMessage(0).sendToTarget();
            } else {
                File file = new File(splashCacheDir, "bg_splash.jpg");
                if (file.exists()) {
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file);
                        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                        fileInputStream.close();
                        handler.obtainMessage(1, bitmap).sendToTarget();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (NetWorkUtil.isNetWorkAvailable(getActivity())) {

                    } else {
                        try {
                            String json = Http.get("http://news-at.zhihu.com/api/3/start-image/480*782");
                            JSONObject jsonObject = new JSONObject(json);
                            String imgUrl = jsonObject.getString("img");
                            URL url = new URL(imgUrl);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("GET");
                            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                InputStream inputStream = httpURLConnection.getInputStream();
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                byte[] b = new byte[10 * 1024];
                                while (inputStream.read(b) != -1) {
                                    fileOutputStream.write(b);
                                }
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                inputStream.close();
                                httpURLConnection.disconnect();

                                FileInputStream fileInputStream = null;
                                try {
                                    fileInputStream = new FileInputStream(file);
                                    Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                                    fileInputStream.close();
                                    handler.obtainMessage(2, bitmap).sendToTarget();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.i(TAG, "set res splash bg");
                    ivSplash.setBackgroundResource(R.drawable.bg_splash);
                    break;
                case 1:
                    Log.i(TAG, "set cache splash bg");
                    ivSplash.setImageBitmap((Bitmap) msg.obj);
                    break;
                case 2:
                    Log.i(TAG, "set download splash bg");
                    ivSplash.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };
}
