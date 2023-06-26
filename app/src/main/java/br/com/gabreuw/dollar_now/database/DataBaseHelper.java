package br.com.gabreuw.dollar_now.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.gabreuw.dollar_now.entities.DollarQuotation;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dollar_quotations.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "dollar_quotations";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_VARIATION = "variation";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_DATE + " TEXT PRIMARY KEY,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_VARIATION + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    @SuppressLint("NewApi")
    public List<DollarQuotation> getAllDollarQuotations() {
        SQLiteDatabase db = getReadableDatabase();
        List<DollarQuotation> quotations = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String dateStr = cursor.getString(0);
                LocalDate date = LocalDate.parse(dateStr);
                float price = cursor.getFloat(1);
                float variation = cursor.getFloat(2);

                DollarQuotation quotation = new DollarQuotation(date, price, variation);

                quotations.add(quotation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return quotations;
    }

    public void insertOrUpdateDollarQuotation(DollarQuotation quotation) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, quotation.getDate().toString());
        values.put(COLUMN_PRICE, quotation.getPrice());
        values.put(COLUMN_VARIATION, quotation.getVariation());

        String whereClause = COLUMN_DATE + " = ?";
        String[] whereArgs = {quotation.getDate().toString()};
        Cursor cursor = db.query(TABLE_NAME, null, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            db.update(TABLE_NAME, values, whereClause, whereArgs);
        } else {
            db.insert(TABLE_NAME, null, values);
        }

        cursor.close();
    }
}
