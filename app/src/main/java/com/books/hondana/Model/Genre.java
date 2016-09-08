package com.books.hondana.Model;

/**
 * @author Tetsuro MIKAMI https://github.com/mickamy
 *         Created on 9/8/16.
 */
public enum Genre {

    // 0: all
    ALL,
    // 1:小説・エッセイ,  人文・思想・社会
    LITERATURE,
    // 2:ビジネス・経済・就職
    BUSINESS,
    // 3:パソコン・システム開発,  科学・技術
    TECHNOLOGY,
    // 4:ホビー・スポーツ・美術, 写真集・タレント,文具・雑貨
    ART,
    // 5:文庫
    POCKET_EDITION,
    // 6:新書
    PAPERBACK,
    // 7:漫画（コミック）,ライトノベル,ボーイズラブ（BL）,コミックセット
    COMIC,
    // 8:その他
    //  語学・学習参考書	絵本・児童書・図鑑	旅行・留学・アウトドア
    // 	美容・暮らし・健康・料理  エンタメ・ゲーム	資格・検定	楽譜
    // 	付録付き	バーゲン本	カレンダー・手帳・家計簿
    // 	医学・薬学・看護学・歯科学
    OTHERS;

    public String[] value() {
        switch (this) {
            case ALL: return new String[]{"001"};
            case LITERATURE: return new String[]{"001004","001008"};
            case BUSINESS: return new String[]{"001006"};
            case TECHNOLOGY: return new String[]{"001005","001012"};
            case ART: return new String[]{"001009","001013","001027"};
            case POCKET_EDITION: return new String[]{"001019"};
            case PAPERBACK: return new String[]{"001020"};
            case COMIC: return new String[]{"001001","001017","001021","001025"};
            case OTHERS: return new String[]{"001002", "001003", "001007", "001010", "001011", "001016", "001018", "001022", "001023", "001026", "001028"};
            default: throw new IllegalArgumentException("This should never happen. Something goes wrong.");
        }
    }

    public String title() {
        switch (this) {
            case ALL: return "すべて";
            case LITERATURE: return "文芸";
            case BUSINESS: return "ビジネス";
            case TECHNOLOGY: return "テクノロジー";
            case ART: return "アート";
            case POCKET_EDITION: return "文庫";
            case PAPERBACK: return "新書";
            case COMIC: return "コミック";
            case OTHERS: return "その他";
            default: throw new IllegalArgumentException("This should never happen. Something goes wrong.");
        }
    }
}
