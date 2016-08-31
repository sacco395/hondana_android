package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Administrator on 2016/07/12.
 */
public class KiiDataObj implements Parcelable {

    /* package */  //KiiBucket kiiBucket = null;
    /* package */  KiiCloudBucket kiiCloudBucket;
    /* package */  KiiObject kiiObject = null;
    /* package */  HashMap<String, String> map = null;
    //protected KiiQueryResult<KiiObject> result=null;

    public static final Creator<KiiDataObj> CREATOR = new Creator<KiiDataObj>() {
        @Override
        public KiiDataObj createFromParcel(Parcel in) {
            return new KiiDataObj(in);
        }

        @Override
        public KiiDataObj[] newArray(int size) {
            return new KiiDataObj[size];
        }
    };

    // KiiCloudの初期化
    public void kiiDataInitialize(KiiCloudBucket kiiCloudBucket) {
        this.kiiCloudBucket = kiiCloudBucket;
        KiiBucket kiiBucket;
        //Application Scopeに所属するBucketを定義
        if (kiiCloudBucket == KiiCloudBucket.BOOKS){
            kiiBucket = Kii.bucket(KiiCloudBucket.BOOKS.getName());
            kiiObject =  kiiBucket.object();

        } else if (kiiCloudBucket == KiiCloudBucket.MEMBERS ){
            kiiBucket = Kii.bucket((KiiCloudBucket.MEMBERS.getName()));
            kiiObject = kiiBucket.object();

        } else if (kiiCloudBucket == KiiCloudBucket.USERBOOKS) {
            kiiBucket = Kii.bucket((KiiCloudBucket.USERBOOKS.getName()));
            kiiObject = kiiBucket.object();

        } else if (kiiCloudBucket == KiiCloudBucket.EVALUATIONS ){
            kiiBucket = Kii.bucket(KiiCloudBucket.EVALUATIONS.getName());
            kiiObject = kiiBucket.object();

        } else if (kiiCloudBucket == KiiCloudBucket.LIKES ){
            kiiBucket = Kii.bucket(KiiCloudBucket.LIKES.getName());
            kiiObject = kiiBucket.object();

        } else if (kiiCloudBucket == KiiCloudBucket.GENRES ){
            kiiBucket = Kii.bucket(KiiCloudBucket.GENRES.getName());
            kiiObject = kiiBucket.object();
        }

        } else if (kiiCloudBucket == KiiCloudBucket.IMAGES ){
        kiiBucket = Kii.bucket(KiiCloudBucket.IMAGES.getName());
        kiiObject = kiiBucket.object();
    }

    public KiiDataObj(){
        map = new HashMap<String, String>();
    }

    public KiiDataObj(KiiObject kiiObject) {
        this.kiiObject = kiiObject;
        this.map = KiiDataObj.makeMapObject(kiiObject);
    }

    public String get(String key){
        // KiiObjectからは取得しない
        return map.get(key);
    }

    public void set(String key, String value){
        // KiiObjectには設定しない
        map.put(key,value);
    }

    // Convert to KiiObject
    public  KiiObject toKiiObject(){

        KiiObject kiiObject = this.kiiObject;
        // Bucketからオブジェクトの生成
        if ( kiiObject == null ) {
            kiiDataInitialize(this.kiiCloudBucket);
            kiiObject = this.kiiObject;
        }
        // Mapから一つずつ取り出してセット
        for ( Map.Entry<String, String> e : map.entrySet() ){
            //保存するデータを定義
            kiiObject.set(e.getKey(), e.getValue());
        }
        return kiiObject;
    }

    public HashMap<String,String> getMap(){
        return map;
    }

    public void setUpMap( HashMap<String, String> srcMap ){
        for(HashMap.Entry<String,String> e : srcMap.entrySet() ) {
            this.map.put(e.getKey(), e.getValue());
        }
    }

    // make HashMap
    public static HashMap<String, String> makeMapObject(KiiObject kiiObject) {

        if (kiiObject == null ) return null;

        // マップにして返す
        HashMap<String, String> map = new HashMap<String, String>();
        // キーの一覧の取得
        HashSet<String> keyset = kiiObject.keySet();

        // キー値から値(文字列)を取得
        for (String key: keyset){
            map.put(key, kiiObject.getString(key));
        }
        return map;
    }

    //現在のデータの保存
    public void save( KiiObjectCallBack callback ){
        kiiObject = this.toKiiObject();
        //オブジェクトを保存
        kiiObject.save(callback);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(kiiObject);
        dest.writeMap(map);
    }

    public KiiDataObj(Parcel in){
        //this.kiiBucket = in.readTypedObject(KiiBucket.CR);
        this.kiiObject = (KiiObject) in.readValue(KiiObject.class.getClassLoader());
        this.map = in.readHashMap(HashMap.class.getClassLoader());
    }

}
