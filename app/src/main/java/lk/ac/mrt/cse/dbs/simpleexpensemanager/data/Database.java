package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 11/18/2017.
 */

public class Database extends SQLiteOpenHelper {


    public Database(Context context) {
        super(context, "150106D", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1="CREATE TABLE Account (accountNo TEXT PRIMARY KEY, bankName TEXT,accountHolderName TEXT,balance REAL)";
        sqLiteDatabase.execSQL(sql1);
        String sql2="CREATE TABLE AccTransaction (transDate DATE,accountNo TEXT, expenseType TEXT,amount REAL)";
        sqLiteDatabase.execSQL(sql2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Account");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Transaction");
        onCreate(sqLiteDatabase);
    }

}
