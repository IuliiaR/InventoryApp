package com.example.julia.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.julia.inventoryapp.data.BookContract.BookEntry;
import com.example.julia.inventoryapp.data.BookContract.SupplierEntry;
import com.example.julia.inventoryapp.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity {

    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertFakeData();
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new BookDbHelper(this);
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        TextView displayView = (TextView) findViewById(R.id.text_view_book);

        String rawQuery = "SELECT * FROM " + BookEntry.TABLE_NAME + " INNER JOIN " + SupplierEntry.TABLE_NAME +
                " ON " + BookEntry.COLUMN_BOOK_SUPPLIER_ID + " = " + SupplierEntry.TABLE_NAME + "." + SupplierEntry._ID + ";";

        Cursor cursor = db.rawQuery(rawQuery, null);

        try {
            displayView.setText(String.format(getString(R.string.books_table_entries) + "\n\n", cursor.getCount()));
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_TITLE + " - " +
                    BookEntry.COLUMN_BOOK_PRICE + " - " +
                    BookEntry.COLUMN_BOOK_CURRENCY + " - " +
                    BookEntry.COLUMN_BOOK_QUANTITY + " - " +
                    SupplierEntry.COLUMN_SUPPLIER_NAME + " - " +
                    SupplierEntry.COLUMN_SUPPLIER_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int currencyColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_CURRENCY);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                Double currentPrice = cursor.getDouble(priceColumnIndex);
                String currentCurrency = cursor.getString(currencyColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentTitle + " - " +
                        currentPrice + " - " +
                        currentCurrency + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " +
                        currentSupplierPhone));
            }       } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertFakeData(){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues supplierValues = new ContentValues();
        supplierValues.put(SupplierEntry.COLUMN_SUPPLIER_NAME, "Supplier Name");
        supplierValues.put(SupplierEntry.COLUMN_SUPPLIER_PHONE, "123-456-789");

        long newSupplierRowId = db.insert(SupplierEntry.TABLE_NAME, null, supplierValues);

        // Create a new map of values, where column names are the keys
        ContentValues bookValues = new ContentValues();
        bookValues.put(BookEntry.COLUMN_BOOK_TITLE, "Alice in Wonderland");
        bookValues.put(BookEntry.COLUMN_BOOK_PRICE, 20.50);
        bookValues.put(BookEntry.COLUMN_BOOK_CURRENCY, "USD");
        bookValues.put(BookEntry.COLUMN_BOOK_QUANTITY, 31);
        bookValues.put(BookEntry.COLUMN_BOOK_SUPPLIER_ID, newSupplierRowId);

        // Insert the new row, returning the primary key value of the new row
        long newBookRowId = db.insert(BookEntry.TABLE_NAME, null, bookValues);
        displayDatabaseInfo();
    }
}
