package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by 150106D on 11/16/2017.
 */

public class PersistentAccountDAO implements AccountDAO {
    private Database  db;

    public PersistentAccountDAO(Context context){
        this.db=new Database(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List <String> accountNos=new ArrayList<>();
        String selectQuery = "SELECT accountNo FROM Account";

        SQLiteDatabase sdb = db.getWritableDatabase();
        Cursor cursor = sdb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                accountNos.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return accountNos;
    }

    @Override
    public List<Account> getAccountsList() {
        List <Account> accounts=new ArrayList<>();
        String selectQuery = "SELECT * FROM Account";

        SQLiteDatabase sdb = db.getWritableDatabase();
        Cursor cursor = sdb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account a=new Account();
                a.setAccountNo(cursor.getString(0));
                a.setBankName(cursor.getString(1));
                a.setAccountHolderName(cursor.getString(2));
                a.setBalance(cursor.getDouble(3));
                accounts.add(a);
            } while (cursor.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String selectQuery = "SELECT * FROM Account where accountNo='"+accountNo+"'";

        SQLiteDatabase sdb = db.getWritableDatabase();
        Cursor cursor = sdb.rawQuery(selectQuery, null);

        if ((cursor.moveToFirst()) || cursor.getCount() !=0){
            Account acc=new Account();
            acc.setAccountNo(cursor.getString(0));
            acc.setBankName(cursor.getString(1));
            acc.setAccountHolderName(cursor.getString(2));
            acc.setBalance(cursor.getDouble(3));
            return acc;

        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sdb = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("accountNo", account.getAccountNo());
        values.put("bankName", account.getBankName());
        values.put("accountHolderName", account.getAccountHolderName());
        values.put("balance", account.getBalance());

        // Inserting Row
        sdb.insert("Account", null, values);
        sdb.close();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if(getAccount(accountNo)!=null){
            String deleteQuery = "SELECT * FROM Account where accountNo='"+accountNo+"'";
            SQLiteDatabase sdb = db.getWritableDatabase();
            sdb.execSQL(deleteQuery);
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(getAccount(accountNo)!=null) {
            SQLiteDatabase sdb = db.getWritableDatabase();
            if(expenseType.equals("EXPENSE")){

                String selectQuery = "SELECT balance FROM Account where accountNo='"+accountNo+"'";
                Cursor cursor = sdb.rawQuery(selectQuery, null);
                cursor.moveToFirst();
                if (amount>=cursor.getDouble(0)){
                    String sql = "update Account set balance=balance -" + amount + " where accountNo='" + accountNo + "'";
                    sdb.execSQL(sql);
                }else{
                    String msg = "Maximum amount overflow ";
                    throw new InvalidAccountException(msg);
                }
            }else{
                String sql = "update Account set balance=balance +" + amount + " where accountNo='" + accountNo + "'";
                sdb.execSQL(sql);
            }
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}
