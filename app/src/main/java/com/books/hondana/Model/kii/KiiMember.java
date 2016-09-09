package com.books.hondana.Model.kii;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.Convertible;
import com.books.hondana.Model.Member;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sacco on 2016/08/21.
 */
public class KiiMember extends KiiDataObj implements Parcelable, Convertible<Member> {

    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String BIRTHDAY = "birthday";
    public static final String ADDRESS = "address";
    public static final String PROFILE = "profile";
    public static final String IMAGE_URL = "image_Url";
    public static final String POINT = "point";
    public static final String FAVORITE_AUTHOR1 = "favorite_author1";
    public static final String FAVORITE_AUTHOR2 = "favorite_author2";
    public static final String FAVORITE_AUTHOR3 = "favorite_author3";
    public static final String DELETE_FLG = "delete_flg";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATE_AT = "updated_at";

    public long createdAt = -1;
    public long updatedAt = -1;

    public static KiiMember create(KiiUser user) {
        KiiMember member = new KiiMember();
        member.set(USER_ID, user.getID());
        member.set(NAME, user.getUsername());
        // TODO: 9/9/16 ユーザ登録時に付与されるポイントを設定
        member.set(POINT, "0");
        member.set(DELETE_FLG, "false");
        return member;
    }

    public static KiiMember create(Member member) {
        KiiMember kiiMember = new KiiMember();

        kiiMember.set(USER_ID, member.getId());
        kiiMember.set(NAME, member.getName());
        kiiMember.set(BIRTHDAY, member.getBirthday());
        kiiMember.set(ADDRESS, member.getAddress());
        kiiMember.set(POINT, Integer.toString(member.getPoint()));

        List<String> authors = member.getFavoriteAuthors();
        kiiMember.set(FAVORITE_AUTHOR1, authors.get(0));
        kiiMember.set(FAVORITE_AUTHOR1, authors.get(1));
        kiiMember.set(FAVORITE_AUTHOR1, authors.get(2));

        kiiMember.set(DELETE_FLG, Boolean.toString(member.isDeleted()));
        kiiMember.set(CREATED_AT, Long.toString(member.getCreatedAt()));
        kiiMember.set(UPDATE_AT, Long.toString(member.getUpdatedAt()));

        return kiiMember;
    }

    @Override
    public Member convert() {
        Member member = new Member();

        member.setId(get(USER_ID));
        member.setName(get(NAME));
        member.setBirthday(get(BIRTHDAY));
        member.setAddress(get(ADDRESS));
        member.setProfile(get(PROFILE));
        member.setImageUrl(get(IMAGE_URL));
        member.setPoint(Integer.valueOf(get(POINT)));

        List<String> authors = new ArrayList<>();
        authors.add(get(FAVORITE_AUTHOR1));
        authors.add(get(FAVORITE_AUTHOR2));
        authors.add(get(FAVORITE_AUTHOR3));
        member.setFavoriteAuthors(authors);

        String deleted = get(DELETE_FLG);
        if (deleted == null) {
            member.setDeleted(false);
        } else {
            member.setDeleted(Boolean.valueOf(deleted));
        }
        member.setCreatedAt(createdAt);
        member.setUpdatedAt(updatedAt);

        return member;
    }

    /**
     * Kii サーバとやりとりするときのみ使う
     */
    public static KiiMember create() {
        return new KiiMember();
    }

    private KiiMember() {
        super();
        // application scope の kiiBucket の Objectとして設定
        kiiDataInitialize(KiiCloudBucket.MEMBERS);
    }

    /**
     * アプリ内で KiiBook は基本的に、このコンストラクタを使って生成する。
     * KiiObject から生成しないと、createdAt などが取得できないため。
     * @param kiiObject
     */
    public KiiMember(KiiObject kiiObject){
        super(kiiObject);
        createdAt = kiiObject.getCreatedTime();
        updatedAt = kiiObject.getModifedTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel (dest, flags);
    }

    protected KiiMember(Parcel in) {
        super (in);
    }

    public static final Creator<KiiMember> CREATOR = new Creator<KiiMember> () {
        @Override
        public KiiMember createFromParcel(Parcel source) {
            return new KiiMember(source);
        }

        @Override
        public KiiMember[] newArray(int size) {
            return new KiiMember[size];
        }
    };

    public int getPoint() {
        return 10;
    }
}