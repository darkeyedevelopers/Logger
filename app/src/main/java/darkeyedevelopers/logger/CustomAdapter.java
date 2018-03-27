package darkeyedevelopers.logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import static android.R.attr.resource;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by onk_r on 13-03-2018.
 */

class CustomAdapter extends ArrayAdapter <Accounts> {
    public ArrayList<Accounts> a_list;

    DBHandler dbHandler;
    Activity last;
    CustomAdapter(Context context,ArrayList<Accounts> a,DBHandler dbHandler,Activity last) {
        super(context,R.layout.custom_row ,a);
        this.a_list=a;
        this.dbHandler = dbHandler;
        this.last = last;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater custominflater = LayoutInflater.from(getContext());
        final View custom_view = custominflater.inflate(R.layout.custom_row,parent,false);

        final Accounts loc = getItem(position);
        final EditText user_name = (EditText) custom_view.findViewById(R.id.username);
        EditText password = (EditText) custom_view.findViewById(R.id.password);
        Button del = (Button) custom_view.findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(last);
                    // Add the buttons
                builder.setTitle("CONFIRM");
                builder.setMessage("Do you really want to remove this account ?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        a_list.remove(position);
                        dbHandler.removeAccount(String.valueOf(user_name.getText()));
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create();
                builder.show();
            }
        });

        if(loc.getUsername()!="dummy")
            user_name.setText(loc.getUsername());
        if(loc.getPass()!="")
            password.setText(loc.getPass());

        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
                loc.setUsername(c.toString());
            }
            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                loc.setUsername(c.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
                loc.setPass(c.toString());
            }
            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                loc.setPass(c.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return custom_view;
    }

}
