package com.books.hondana.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.R;
import com.kii.cloud.storage.KiiObject;

/**
 * Created by Administrator on 2016/07/12.
 */
public class KiiBook extends KiiDataObj implements Parcelable {

    public static final String BOOK_ID = "book_id";
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String AUTHOR = "author";
    public static final String ISBN = "isbn";
    public static final String LANGUAGE = "language";
    public static final String ISSUE_DATE = "issue_date";  // Date
    public static final String IMAGE = "image";  // Image
    public static final String IMAGE_URL = "image_url";
    public static final String HEIGHT = "height"; // double
    public static final String WIDTH = "width";   // double
    public static final String DEPTH = "depth";   // double
    public static final String WEIGHT = "weight"; // double
    public static final String GENRE_1 = "genre_1";
    public static final String GENRE_2 = "genre_2";
    public static final String GENRE_3 = "genre_3";
    public static final String GENRE_4 = "genre_4";
    public static final String GENRE_5 = "genre_5";
    public static final String USER_ID = "user_id";
    public static final String CONDITION = "condition";
    public static final String NOTES = "notes";
    public static final String BAND = "band";
    public static final String SUNBURNED = "sunburned";
    public static final String SCRATCHED = "scratched";
    public static final String CIGAR_SMELL = "cigar_smell";
    public static final String PET_SMELL = "pet_smell";
    public static final String MOLD_SMELL = "mold_smell";
    public static final String DESCRIPTION = "description";

    // Constructor
    public KiiBook() {
        super();
        // application scope の kiiBucket の Objectとして設定
        kiiDataInitialize(KiiCloudBucket.BOOKS);
    }

    public KiiBook( KiiObject kiiObject){
        super(kiiObject);
    }

    protected KiiBook(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KiiBook> CREATOR = new Creator<KiiBook>() {
        @Override
        public KiiBook createFromParcel(Parcel in) {
            return new KiiBook(in);
        }

        @Override
        public KiiBook[] newArray(int size) {
            return new KiiBook[size];
        }
    };

    public int getConditionDrawableResId() {
        String condition = get (CONDITION);
        if (condition == null) {
            return 0;
        }
        switch (condition) {
            case "良い":
                return R.drawable.book_icon_excellent;
            case "普通":
                return R.drawable.book_icon_good;
            case "汚れあり":
                return R.drawable.book_icon_bad;
            default:
                return 0;
        }
    }
}



//    //保存されているデータを読み込み
//    public void loadKiiData( int id ) {
//        KiiQuery query = new KiiQuery(KiiClause.equals("book_id", id)); //IDが一致するオブジェクトを検索
//
//        appBucket.query(new KiiQueryCallBack<KiiObject>() {
//            @Override
//            public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception exception) {
//                if (exception != null) {
//                    // Error handling
//                    return;
//                }
//                List<KiiObject> objLists = result.getResult();
////////////////////////////////////////////////////////////////////////////////////
////                        for (KiiObject obj : objLists) {
//// Do something with objects in the result
////                            Books_Kii book = new Books_Kii();
////                            book.setBook_id(obj.getInt(Books_Kii.BOOK_ID));
////                            book.setTitle(obj.getString(Books_Kii.TITLE));
////                            book.setAuthor(obj.getString(Books_Kii.AUTHOR));
////                            book.setPublisher(obj.getString(Books_Kii.PUBLISHER));
////                        }
//////////////////////////////////////////////////////////////////////////////////////////
//            }
//        }, query);
//
//    }
//
//    //    //現在のデータの保存
//    public void update(int id) {
//        String userId = KiiUser.getCurrentUser().getID();//ログイン中のユーザーIDを取得
//
//        KiiQuery query = new KiiQuery(KiiClause.equals("book_id", id)); //IDが一致するオブジェクトを検索
//        appBucket.query(new KiiQueryCallBack<KiiObject>() {
//            @Override
//            public void onQueryCompleted(int token, KiiQueryResult<KiiObject> result, Exception exception) {
//                if (exception != null) {
//                    // Error handling
//                    return;
//                }
//                List<KiiObject> objLists = result.getResult();
//                for (KiiObject kObj : objLists) {
//                    kObj.set(KiiBook.BOOK_ID, book_id);
//                    kObj.set(KiiBook.TITLE, title);
//                    kObj.set(KiiBook.AUTHOR, author);
//                    kObj.set(KiiBook.PUBLISHER, publisher);
//                    try {
//                        kObj.save();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (BadRequestException e) {
//                        e.printStackTrace();
//                    } catch (ConflictException e) {
//                        e.printStackTrace();
//                    } catch (ForbiddenException e) {
//                        e.printStackTrace();
//                    } catch (NotFoundException e) {
//                        e.printStackTrace();
//                    } catch (UnauthorizedException e) {
//                        e.printStackTrace();
//                    } catch (UndefinedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }, query);
//    }

