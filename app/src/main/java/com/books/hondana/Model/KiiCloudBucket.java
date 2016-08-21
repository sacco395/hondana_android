package com.books.hondana.Model;

/**
 * Created by Administrator on 2016/08/02.
 */
public enum KiiCloudBucket {

    /** 列挙定数の定義 */
    BOOKS("appbooks", 1, ""),
    MEMBERS("members",2,""),
    USERBOOKS("userbooks",3,""),
    EVALUATIONS("evaluations",4,""),
    LINKS("links",5,""),
    GENRES("genres",6,"");

    /** フィールド変数 */
    private String name;
    private int intValue;
    private String  strValue;

    /** コンストラクタ */
    KiiCloudBucket(String name, int intValue, String strValue) {
        this.name = name;
        this.intValue = intValue;
        this.strValue  = strValue;
    }

    /** 名称取得メソッド */
    public String getName() {
        return this.name;
    }

    /** 値取得メソッド */
    public int getValue() {
        return this.intValue;
    }
    }
