package abc.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {
    private EditText roomName;
    private Button add_room;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private String name;
    private ArrayList<String> list_of_rooms =new ArrayList<>();
    private DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_room = (Button) findViewById(R.id.addroombut);
        roomName = (EditText) findViewById(R.id.roomname);
        listView = (ListView) findViewById(R.id.chatroomlist);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        listView.setAdapter(arrayAdapter);
        request_user_name();
//gggg
        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(roomName.getText().toString(), "");
                dbref.updateChildren(map);
            }
        });
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> hash = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    hash.add(((DataSnapshot) i.next()).getKey());

                }
                list_of_rooms.clear();
                list_of_rooms.addAll(hash);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),Chat_Room.class);
                intent.putExtra("roomname",((TextView)view).getText().toString());
                intent.putExtra("username",name);
                startActivity(intent);


            }
        });
    }
    private void request_user_name() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Enter your Name");
        final EditText input_field=new EditText(this);
        builder.setView(input_field);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name=input_field.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_user_name();

            }
        });
        builder.show();
    }
}
