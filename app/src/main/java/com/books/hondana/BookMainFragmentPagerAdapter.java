package com.books.hondana;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.administrator.bookscan.Connection.QueryParamSet;
import com.example.administrator.bookscan.Model.KiiBook;

/**
 * Created by Administrator on 2016/08/02.
 */
public class BookMainFragmentPagerAdapter
        extends FragmentPagerAdapter {
//        extends FragmentStatePagerAdapter {

    // 以下のコードは楽天ブックスのジャンルコードによる
    private static final String[][] queryGenreTbl = {
            // 0: all
            {"001"},
            // 1:小説・エッセイ,  人文・思想・社会
            {"001004","001008" },
            // 2:ビジネス・経済・就職
            {"001006" },
            // 3:パソコン・システム開発,  科学・技術
            {"001005","001012" },
            // 4:ホビー・スポーツ・美術, 写真集・タレント,文具・雑貨
            {"001009","001013","001027" },
            // 5:文庫
            {"001019" },
            // 6:新書
            {"001020" },
            // 7:漫画（コミック）,ライトノベル,ボーイズラブ（BL）,コミックセット
            {"001001","001017","001021","001025" },
            // 8:その他
            //  語学・学習参考書	絵本・児童書・図鑑	旅行・留学・アウトドア
            // 	美容・暮らし・健康・料理  エンタメ・ゲーム	資格・検定	楽譜
            // 	付録付き	バーゲン本	カレンダー・手帳・家計簿
            // 	医学・薬学・看護学・歯科学
            {"001002", "001003", "001007", "001010", "001011", "001016",
             "001018", "001022", "001023", "001026", "001028" },
    };

    private static  final String[] pageTitle = {
            // 0: all
           "すべて",
            // 1:小説・エッセイ,  人文・思想・社会
            "文芸",
            // 2:ビジネス・経済・就職
            "ビジネス",
            // 3:パソコン・システム開発,  科学・技術
            "テクノロジー",
            // 4:ホビー・スポーツ・美術, 写真集・タレント,文具・雑貨
            "アート",
            // 5:文庫
            "文庫",
            // 6:新書
            "新書",
            // 7:漫画（コミック）,ライトノベル,ボーイズラブ（BL）,コミックセット
           "コミック",
            // 8:その他
            //  語学・学習参考書	絵本・児童書・図鑑	旅行・留学・アウトドア
            // 	美容・暮らし・健康・料理  エンタメ・ゲーム	資格・検定	楽譜
            // 	付録付き	バーゲン本	カレンダー・手帳・家計簿
            // 	医学・薬学・看護学・歯科学
            "その他",
    };

    public BookMainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
        //return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {

        HondanaBooksFragment fragment = null;
        Bundle bundle;
        QueryParamSet queryParamSet;


        switch (position){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                fragment = new HondanaBooksFragment();
                bundle = new Bundle();
                // All ジャンル
                queryParamSet = new QueryParamSet();
                queryParamSet.setQueryByGenre(queryGenreTbl[position]);
//                queryParamSet.addQueryParam(KiiBook.GENRE_1,"001");
//                queryParamSet.addQueryParam(KiiBook.GENRE_2,"001");
//                queryParamSet.addQueryParam(KiiBook.GENRE_3,"001");
//                queryParamSet.addQueryParam(KiiBook.GENRE_4,"001");
//                queryParamSet.addQueryParam(KiiBook.GENRE_5,"001");
                bundle.putSerializable(HondanaBooksFragment.ARG_QUERYPARAMS, queryParamSet);
                bundle.putInt(HondanaBooksFragment.ARG_PAGE,0);
                fragment.setArguments(bundle);
                return fragment;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 9;
    }
}
