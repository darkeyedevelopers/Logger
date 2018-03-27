package darkeyedevelopers.logger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by onk_r on 26-02-2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Accounts.db";
    private static final String TABLE_ACCOUNTS = "ACCOUNTS";
    private static final String COLUMN_USERNAME = "USERNAME";
    private static final String COLUMN_PASS = "PASS";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_ACCOUNTS + "(" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY ," +
                COLUMN_PASS + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS + ";");
    }

    //add data
    public long addAccount(Accounts New) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, New.getUsername());
        values.put(COLUMN_PASS, New.getPass());
        SQLiteDatabase db = this.getWritableDatabase();
        long status = db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
        return status;
    }

    //remove data
    public void removeAccount(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_USERNAME + " = \"" + username + "\";");
    }

    //save gatway and port
    public void savePort(String gatway, String portno) {
            addAccount(new Accounts("gatway", gatway + ":" + portno));
    }

    //save gatway and port
    public void updateAccount(String username,String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME,username);
        values.put(COLUMN_PASS,password);
        SQLiteDatabase db = this.getWritableDatabase();
        long status = db.update(TABLE_ACCOUNTS, values,COLUMN_USERNAME+"=?", new String[]{username});
        db.close();
    }

    public long getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_ACCOUNTS);
        db.close();
        return count;
    }

    //getPort
    public String getWayPort(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_ACCOUNTS  +" WHERE "+COLUMN_USERNAME+"='gatway';",null);
        String gatway = null;
        if(c.moveToFirst()){
            gatway = c.getString(c.getColumnIndex(COLUMN_PASS));
        }
        db.close();
        return gatway;
    }

    //get all accounts
    public ArrayList<Accounts> getallAccounts() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_ACCOUNTS  +";",null);

        ArrayList<Accounts> a_list = new ArrayList<Accounts> ();

        if(c.moveToFirst()){
            c.moveToNext();
        }

        while(!c.isAfterLast()){
            String user_name = c.getString(c.getColumnIndex(COLUMN_USERNAME));
            String pwd = c.getString(c.getColumnIndex(COLUMN_PASS));
            a_list.add(new Accounts(user_name,pwd));
            c.moveToNext();
        }

        db.close();
        return a_list;
    }

    //Add accounts
    public void addAccounts(ArrayList <Accounts> refined) {
        int i=0;
        Map <String,Accounts> present = new HashMap<String,Accounts>();
        ArrayList <Accounts> a_list = getallAccounts();
        for(i=0;i<a_list.size();i++) {
            present.put(a_list.get(i).getUsername(),a_list.get(i));
        }
        for(i=0;i<refined.size();i++) {
            String user_name = refined.get(i).getUsername();
            String pass = refined.get(i).getPass();
            if(present.containsKey(user_name)) {
                if (present.get(user_name).getPass() != pass) {
                    updateAccount(user_name,pass);
                }
                present.remove(user_name);
            }
            else {
                if(refined.get(i).getUsername()!="dummy") {
                    addAccount(refined.get(i));
                    present.remove(user_name);
                }
            }
        }
        for(String s : present.keySet()){
            removeAccount(s);
        }
    }

}
