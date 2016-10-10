package com.books.hondana;

import android.app.Application;

import com.kii.cloud.storage.Kii;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class HondanaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the Kii SDK!
        Kii.initialize(getString(R.string.kii_app_id), getString(R.string.kii_app_key), Kii.Site.JP);
        // 画像ダウンロード用ライブラリの初期化
        initImageLoder();
    }

    // 画像ダウンロード用ライブラリの初期化
    private void initImageLoder(){
        // Create global configuration and initialize ImageLoader with this config
        File cacheDir = StorageUtils.getCacheDirectory(this);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);
    }
}
