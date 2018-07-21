package com.example.julia.inventoryapp.data;

import android.provider.BaseColumns;

public class BookContract {

    private BookContract() {}

    public static final class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "books";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_BOOK_TITLE = "title";
        public final static String COLUMN_BOOK_PRICE = "price";
        public final static String COLUMN_BOOK_CURRENCY = "currency";
        public final static String COLUMN_BOOK_QUANTITY = "quantity";
        public final static String COLUMN_BOOK_SUPPLIER_ID = "supplierId";
    }

    public static final class SupplierEntry implements BaseColumns {

        public static final String TABLE_NAME = "suppliers";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_SUPPLIER_NAME = "name";
        public final static String COLUMN_SUPPLIER_PHONE = "phone";
    }
}
