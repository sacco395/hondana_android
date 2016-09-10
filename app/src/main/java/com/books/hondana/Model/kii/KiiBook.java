package com.books.hondana.Model.kii;

import android.os.Parcel;
import android.os.Parcelable;

import com.books.hondana.Model.Convertible;
import com.books.hondana.Model.book.Book;
import com.books.hondana.Model.book.BookCondition;
import com.books.hondana.Model.book.BookInfo;
import com.kii.cloud.storage.KiiObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/07/12.
 */
public class KiiBook extends KiiDataObj implements Parcelable
//        , Convertible<Book>
{

    public static final String BOOK_ID = "book_id";
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String AUTHOR = "author";
    public static final String ISBN = "isbn";
    public static final String LANGUAGE = "language";
    public static final String ISSUE_DATE = "issue_date";  // Date
    public static final String IMAGE = "image";  // Image
    public static final String IMAGE_URL = "image_url";
    public static final String HEIGHT = "size_height"; // double
    public static final String WIDE = "size_wide";   // double
    public static final String DEPTH = "size_depth";   // double
    public static final String WEIGHT = "size_weight"; // double
    public static final String GENRE_1 = "genre_1";
    public static final String GENRE_2 = "genre_2";
    public static final String GENRE_3 = "genre_3";
    public static final String GENRE_4 = "genre_4";
    public static final String GENRE_5 = "genre_5";
    public static final String USER_ID = "user_id";
    public static final String CONDITION = "condition";
    public static final String LINE = "line";
    public static final String BROKEN = "broken";
    public static final String NOTES = "notes";
    public static final String BAND = "band";
    public static final String SUNBURNED = "sunburned";
    public static final String SCRATCHED = "scratched";
    public static final String CIGAR_SMELL = "cigar_smell";
    public static final String PET_SMELL = "pet_smell";
    public static final String MOLD_SMELL = "mold_smell";
    public static final String DESCRIPTION = "description";

    public long createdAt = -1;
    public long updatedAt = -1;

//    public static KiiBook create(Book book) {
//        KiiBook kiiBook = new KiiBook();
//
//        kiiBook.set(BOOK_ID, book.getId());
//
//        BookInfo info = book.getInfo();
//        kiiBook.set(TITLE, info.getTitle());
//        kiiBook.set(PUBLISHER, info.getPublisher());
//        kiiBook.set(AUTHOR, info.getAuthor());
//        kiiBook.set(ISBN, info.getIsbn());
//        kiiBook.set(LANGUAGE, info.getLanguage());
//        kiiBook.set(ISSUE_DATE, info.getIssueDate());
//        kiiBook.set(IMAGE_URL, info.getImageUrl());
//
//        BookInfo.Size size = book.getSize();
//        kiiBook.set(HEIGHT, Double.toString(size.getHeight()));
//        kiiBook.set(WIDE, Double.toString(size.getWidth()));
//        kiiBook.set(DEPTH, Double.toString(size.getDepth()));
//        kiiBook.set(WEIGHT, Double.toString(size.getWeight()));
//
//        List<String> genres = book.getGenres();
//        kiiBook.set(GENRE_1, genres.get(0));
//        kiiBook.set(GENRE_2, genres.get(1));
//        kiiBook.set(GENRE_3, genres.get(2));
//        kiiBook.set(GENRE_4, genres.get(3));
//        kiiBook.set(GENRE_5, genres.get(4));
//
//        kiiBook.set(USER_ID, book.getOwner());
//
//        BookCondition condition = book.getCondition();
//        kiiBook.set(CONDITION, condition.getEvaluation());
//        kiiBook.set(LINE, condition.getLined());
//        kiiBook.set(BROKEN, condition.getFolded());
//        kiiBook.set(NOTES, condition.getNoted());
//        kiiBook.set(BAND, condition.getBand());
//        kiiBook.set(SUNBURNED, condition.getSunburned());
//        kiiBook.set(SCRATCHED, condition.getScratched());
//
//        BookCondition.Smell smell = book.getSmell();
//        kiiBook.set(CIGAR_SMELL, smell.getCigar());
//        kiiBook.set(PET_SMELL, smell.getPet());
//        kiiBook.set(MOLD_SMELL, smell.getMold());
//
//        kiiBook.set(DESCRIPTION, book.getDescription());
//
//        return kiiBook;
//    }
//
//    @Override
//    public Book convert() {
//        Book book = new Book();
//
//        book.setId(get(BOOK_ID));
//
//        BookInfo info = new BookInfo();
//        info.setTitle(get(TITLE));
//        info.setPublisher(get(PUBLISHER));
//        info.setAuthor(get(AUTHOR));
//        info.setIsbn(get(ISBN));
//        info.setLanguage(get(LANGUAGE));
//        info.setIssueDate(get(ISSUE_DATE));
//        info.setImageUrl(get(IMAGE_URL));
//        book.setInfo(info);
//
//        BookInfo.Size size = new BookInfo.Size();
//        String height = get(HEIGHT);
//        if (height != null && !height.equals("")) {
//            size.setHeight(Double.valueOf(height));
//        }
//        String width = get(WIDE);
//        if (width != null && !width.equals("")) {
//            size.setWidth(Double.valueOf(width));
//        }
//        String depth = get(DEPTH);
//        if (depth != null && !depth.equals("")) {
//            size.setDepth(Double.valueOf(depth));
//        }
//        String weight = get(WEIGHT);
//        if (weight != null && !weight.equals("")) {
//            size.setWeight(Double.valueOf(weight));
//        }
//        book.setSize(size);
//
//        List<String> genres = new ArrayList<>();
//        genres.add(get(GENRE_1));
//        genres.add(get(GENRE_2));
//        genres.add(get(GENRE_3));
//        genres.add(get(GENRE_4));
//        genres.add(get(GENRE_5));
//        book.setGenres(genres);
//
//        book.setOwner(get(USER_ID));
//
//        BookCondition condition = new BookCondition();
//        condition.setEvaluation(get(CONDITION));
//        condition.setLined(get(LINE));
//        condition.setFolded(get(BROKEN));
//        condition.setNoted(get(NOTES));
//        condition.setBand(get(BAND));
//        condition.setSunburned(get(SUNBURNED));
//        condition.setScratched(get(SCRATCHED));
//        book.setCondition(condition);
//
//        BookCondition.Smell smell = new BookCondition.Smell();
//        smell.setCigar(get(CIGAR_SMELL));
//        smell.setPet(get(PET_SMELL));
//        smell.setMold(get(MOLD_SMELL));
//        book.setSmell(smell);
//
//        book.setDescription(get(DESCRIPTION));
//        book.setCreatedAt(createdAt);
//        book.setUpdatedAt(updatedAt);
//
//        return book;
//    }

    /**
     * Kii サーバとやりとりするときのみ使う
     */
    public static KiiBook create() {
        return new KiiBook();
    }

    private KiiBook() {
        super();
        kiiDataInitialize(KiiCloudBucket.BOOKS);
    }

    /**
     * アプリ内で KiiBook は基本的に、このコンストラクタを使って生成する。
     * KiiObject から生成しないと、createdAt などが取得できないため。
     * @param kiiObject
     */
    public KiiBook(KiiObject kiiObject){
        super(kiiObject);
        createdAt = kiiObject.getCreatedTime();
        updatedAt = kiiObject.getModifedTime();
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

