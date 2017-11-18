package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by 150106D on 11/16/2017.
 */

public class PersistentTransactionDAO implements TransactionDAO{
    private Database db;

    public PersistentTransactionDAO(Context context){
        this.db=new Database(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String fdate = sdf.format(date);
        ContentValues values = new ContentValues();
        values.put("transDate",fdate );
        values.put("accountNo", accountNo);
        values.put("expenseType", expenseType.toString());
        values.put("amount", amount);

        // Inserting Row
        sdb.insert("AccTransaction", null, values);
        sdb.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List <Transaction> logs=new ArrayList<>();
        String selectQuery = "SELECT * FROM AccTransaction";

        SQLiteDatabase sdb = db.getWritableDatabase();
        Cursor cursor = sdb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction t=new Transaction();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    t.setDate(sdf.parse(cursor.getString(0)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                t.setAccountNo(cursor.getString(1));
                t.setExpenseType(ExpenseType.valueOf(cursor.getString(2)));
                t.setAmount(cursor.getDouble(3));
                logs.add(t);
            } while (cursor.moveToNext());
        }

        return logs;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List <Transaction> logs=new ArrayList<>();
        String selectQuery = "SELECT * FROM AccTransaction limit 10";

        SQLiteDatabase sdb = db.getWritableDatabase();
        Cursor cursor = sdb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction t=new Transaction();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    t.setDate(sdf.parse(cursor.getString(0)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                t.setAccountNo(cursor.getString(1));
                t.setExpenseType(ExpenseType.valueOf(cursor.getString(2)));
                t.setAmount(cursor.getDouble(3));
                logs.add(t);
            } while (cursor.moveToNext());
        }

        return logs;
    }
}
