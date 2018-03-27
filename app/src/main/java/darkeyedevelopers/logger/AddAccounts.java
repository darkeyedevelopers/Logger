package darkeyedevelopers.logger;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.valueOf;

public class AddAccounts extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accounts);


        final DBHandler dbHandler;
        dbHandler = new DBHandler(this);

        final ArrayList<Accounts> a_list;
        final EditText ip = (EditText)findViewById(R.id.ip);
        final EditText port = (EditText)findViewById(R.id.port);
        final String[] gateway = {""};
        final String[] portno = {""};

        ArrayList<Accounts> saved = dbHandler.getallAccounts();

        if(saved.size()==0) {
            a_list = new ArrayList<Accounts>();
        }
        else {
            a_list = saved;
        }
        final CustomAdapter customAdapter = new CustomAdapter(this,a_list,dbHandler,this);

        if(dbHandler.getCount()==0) {
            gateway[0] = valueOf(ip.getText());
            portno[0] = valueOf(port.getText());
            dbHandler.savePort(gateway[0], portno[0]);
        }
        else {
            String ip_port = dbHandler.getWayPort();
            ip.setText(ip_port.substring(0,ip_port.indexOf(":")));
            port.setText(ip_port.substring(ip_port.indexOf(":")+1));
            gateway[0] = valueOf(ip.getText());
            portno[0] = valueOf(port.getText());
        }

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(customAdapter);

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a_list.add(new Accounts("dummy",""));
                customAdapter.notifyDataSetChanged();
            }
        });

        ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gateway[0] = String.valueOf(ip.getText());
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gateway[0]=  String.valueOf(ip.getText());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        port.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                portno[0] = String.valueOf(port.getText());
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                portno[0] = String.valueOf(port.getText());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.updateAccount("gatway",gateway[0]+":"+portno[0]);
                dbHandler.addAccounts(a_list);
            }
        });

    }

}
