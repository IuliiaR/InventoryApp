package com.example.julia.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julia.inventoryapp.data.BookContract.BookEntry;

import java.text.NumberFormat;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.title);
        TextView tvPrice = view.findViewById(R.id.price);
        TextView tvQuantity = (TextView) view.findViewById(R.id.quantity);
        Button btnSale = view.findViewById(R.id.sale_btn);

        // Extract properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_TITLE));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_PRICE));
        final Integer quantity = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_QUANTITY));

        // Populate fields with extracted properties
        tvName.setText(title);

        tvPrice.setText(NumberFormat.getCurrencyInstance().format(price));

        if (quantity == 0) {
            tvQuantity.setVisibility(View.GONE);
            view.findViewById(R.id.instock_label).setVisibility(View.GONE);
            view.findViewById(R.id.outofstock_label).setVisibility(View.VISIBLE);
        } else {
            tvQuantity.setText(String.format("%d", quantity));
            tvQuantity.setVisibility(View.VISIBLE);
            view.findViewById(R.id.instock_label).setVisibility(View.VISIBLE);
            view.findViewById(R.id.outofstock_label).setVisibility(View.GONE);
        }

        String currentId = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry._ID));
        final Uri currentUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, Long.parseLong(currentId));

        btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity > 0) {
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_BOOK_QUANTITY, (quantity - 1));

                    int rowsUpdated = context.getContentResolver().update(currentUri, values, null, null);
                    if (rowsUpdated == 0)
                        Toast.makeText(context, R.string.editor_update_book_failed, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, R.string.item_out_of_stock, Toast.LENGTH_SHORT).show();
            }
        });

    }
}

