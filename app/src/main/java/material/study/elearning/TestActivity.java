package material.study.elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;
import material.study.elearning.Model.Class;
import material.study.elearning.ViewHolder.ClassListViewHolder;

public class TestActivity extends AppCompatActivity {

//    private String whoID;
    private RecyclerView ClassReportList;
    private DatabaseReference ClassListRef;
    private String whoID="user";
    private EditText classnameET;
    private Button addclassbtn;
    private DatabaseReference classRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        whoID = getIntent().getStringExtra("who");






        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setSelectedItemId(R.id.menu_test);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_test:
                        return true;

                    case R.id.menu_setting:
                        Intent intent3 = new Intent(TestActivity.this, SettingActivity.class);
                        intent3.putExtra("who",whoID);
                        startActivity(intent3);

//                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.menu_home:
                        Intent intent2 = new Intent(TestActivity.this, HomeActivity.class);
                        intent2.putExtra("who",whoID);
                        startActivity(intent2);

//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_logout:
                        Paper.book().destroy();

                        Intent intent = new Intent(TestActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                }
                return false;
            }
        });
        ClassReportList = findViewById(R.id.user_test_list_recycler_view);
        ClassReportList.setHasFixedSize(true);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
//                false);
        LinearLayoutManager linearLayoutManager= new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        ClassReportList.setLayoutManager(linearLayoutManager);

        ClassListRef= FirebaseDatabase.getInstance().getReference().child("Test").child("Class");


        classnameET=(EditText) findViewById(R.id.admin_test_name_edittext);


        addclassbtn=(Button) findViewById(R.id.admin_test_add_btn);



        if(whoID.equals("admin"))
        {
            classnameET.setVisibility(View.VISIBLE);
            addclassbtn.setVisibility(View.VISIBLE);
        }

        else {
            classnameET.setVisibility(View.GONE);
            addclassbtn.setVisibility(View.GONE);
        }




        addclassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classnameET.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(classnameET.getText().toString()))
                {
                    Toast.makeText(TestActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    classRef = FirebaseDatabase.getInstance().getReference().child("Test").child("Class");

                    classRef.child(classnameET.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                Toast.makeText(TestActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                final HashMap<String, Object> FakeMap = new HashMap<>();
                                FakeMap.put("classname", classnameET.getText().toString());

                                classRef.child(classnameET.getText().toString()).updateChildren(FakeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(TestActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                classnameET.setText("");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }
    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Class> options =
                new FirebaseRecyclerOptions.Builder<Class>()
                        .setQuery(ClassListRef, Class.class)
                        .build();

        FirebaseRecyclerAdapter<Class, ClassListViewHolder> adapter=
                new FirebaseRecyclerAdapter<Class, ClassListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ClassListViewHolder holder, int position, @NonNull final Class model) {

                        holder.classname.setText(model.getClassname());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TestActivity.this, TestSubjectActivity.class);
                                intent.putExtra("cid", String.valueOf(model.getClassname()));
                                intent.putExtra("scope","test");
                                intent.putExtra("who",whoID);
                                startActivity(intent);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ClassListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list_view_holder, parent, false);
                        ClassListViewHolder holder = new ClassListViewHolder(view);
                        return holder;
                    }
                };
        ClassReportList.setAdapter(adapter);
        adapter.startListening();

    }

}

