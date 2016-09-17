package com.books.hondana.connection;

/**
 * Created by Administrator on 2016/07/29.
 */
public enum SearchAPI
{
    /** 列挙定数の定義 */
    RAKUTEN("楽天 ブックス API", 1, ""),
    GOOGLE("Google Books API", 2,  "");

    /** フィールド変数 */
    private String label;
    private int intValue;
    private String  strValue;

    /** コンストラクタ */
    SearchAPI(String label, int intValue, String strValue) {
        this.label = label;
        this.intValue = intValue;
        this.strValue  = strValue;
    }

    /** 名称取得メソッド */
    public String getLabel() {
        return this.label;
    }

    /** 値取得メソッド */
    public int getValue() {
        return this.intValue;
    }
}
