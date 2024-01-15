package com.example.finalproject;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a new customer into the CUSTOMER_TABLE
    public long insertCustomer(String email, String firstName, String lastName, String passwordHashed,
                               String phoneNumber, String country, String city) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CUSTOMER_EMAIL, email);
        values.put(DatabaseHelper.CUSTOMER_FIRST_NAME, firstName);
        values.put(DatabaseHelper.CUSTOMER_LAST_NAME, lastName);
        values.put(DatabaseHelper.CUSTOMER_PASSWORD_HASHED, passwordHashed);
        values.put(DatabaseHelper.CUSTOMER_PHONE_NUMBER, phoneNumber);
        values.put(DatabaseHelper.CUSTOMER_COUNTRY, country);
        values.put(DatabaseHelper.CUSTOMER_CITY, city);

        return database.insert(DatabaseHelper.CUSTOMER_TABLE, null, values);
    }

    // Insert a new car into the CAR_TABLE
    public long insertCar(String carName, String carFactory, String carModel, int carYear,
                          int carPrice, String carImage) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CAR_NAME, carName);
        values.put(DatabaseHelper.CAR_FACTORY, carFactory);
        values.put(DatabaseHelper.CAR_MODEL, carModel);
        values.put(DatabaseHelper.CAR_YEAR, carYear);
        values.put(DatabaseHelper.CAR_PRICE, carPrice);
        values.put(DatabaseHelper.CAR_IMAGE, carImage);

        return database.insert(DatabaseHelper.CAR_TABLE, null, values);
    }

    // Insert a new reservation into the RESERVATION_TABLE
    public long insertReservation(int carId, String email, String reservationDate) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.RESERVATION_CAR_ID, carId);
        values.put(DatabaseHelper.RESERVATION_EMAIL, email);
        values.put(DatabaseHelper.RESERVATION_DATE, reservationDate);

        return database.insert(DatabaseHelper.RESERVATION_TABLE, null, values);
    }

    // Insert a new favorite into the FAVORITE_TABLE
    public long insertFavorite(int carId, String email) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FAVORITE_CAR_ID, carId);
        values.put(DatabaseHelper.FAVORITE_EMAIL, email);

        return database.insert(DatabaseHelper.FAVORITE_TABLE, null, values);
    }

    // Example query: Get all customers
    public Cursor getAllCustomers() {
        return database.query(DatabaseHelper.CUSTOMER_TABLE, null, null, null, null, null, null);
    }

    // Example query: Get all cars
    public Cursor getAllCars() {
        return database.query(DatabaseHelper.CAR_TABLE, null, null, null, null, null, null);
    }

    public Cursor getCustomerReservations(String email) {
        return database.query(DatabaseHelper.RESERVATION_TABLE, null, DatabaseHelper.RESERVATION_EMAIL + " = ?", new String[]{email}, null, null, null);
    }

    public Cursor getCustomerFavorites(String email) {
        return database.query(DatabaseHelper.FAVORITE_TABLE, null, DatabaseHelper.FAVORITE_EMAIL + " = ?", new String[]{email}, null, null, null);
    }

    public boolean isEmailUsed(String email) {
        Cursor cursor = null;

        try {
            cursor = database.query(
                    DatabaseHelper.CUSTOMER_TABLE,
                    null,
                    DatabaseHelper.CUSTOMER_EMAIL + " = ?",
                    new String[]{email},
                    null,
                    null,
                    null
            );

            // Check if the cursor has rows
            return cursor != null && cursor.moveToFirst();

        } finally {
            // Close the cursor in a finally block to ensure it gets closed even if an exception occurs
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean isLoginCredentialsValid(String email, String password) {
        try (Cursor cursor = database.query(
                DatabaseHelper.CUSTOMER_TABLE,
                null,
                DatabaseHelper.CUSTOMER_EMAIL + " = ? AND " + DatabaseHelper.CUSTOMER_PASSWORD_HASHED + " = ?",
                new String[]{email, password},
                null,
                null,
                null
        )) {

            // Check if the cursor has any rows, indicating a match
            return cursor != null && cursor.getCount() > 0;
        }
        // Close the cursor in a finally block to ensure it gets closed even if an exception occurs
    }
}