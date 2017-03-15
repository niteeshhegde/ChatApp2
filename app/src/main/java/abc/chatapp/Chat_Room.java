package abc.chatapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat_Room extends AppCompatActivity {
    private TextView conversation;
    private EditText inputmsg;
    private Button sendbut;
    private String user_name,room_name;
    private DatabaseReference dbref;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room);
        sendbut=(Button)findViewById(R.id.btn_send);
        inputmsg=(EditText)findViewById(R.id.nsg_input);
        user_name=getIntent().getExtras().get("username").toString();
        room_name=getIntent().getExtras().get("roomname").toString();
        dbref= FirebaseDatabase.getInstance().getReference().child(room_name);
        conversation=(TextView)findViewById(R.id.textView);
//gyg6yfff
        setTitle("Room - "+room_name);
        sendbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
                Map<String,Object> map= new HashMap<String, Object>();
                temp_key=dbref.push().getKey();
                dbref.updateChildren(map);
                DatabaseReference dbref2=dbref.child(temp_key);
                Map<String,Object> map2= new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",inputmsg.getText().toString());
                dbref2.updateChildren(map2);
            }
        });
        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private String usrnm,msg;
    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i=dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            msg=(String)(((DataSnapshot) i.next()).getValue());
            usrnm=(String)(((DataSnapshot) i.next()).getValue());
            conversation.append(usrnm+" : "+msg +"\n");
        }
    }


}
