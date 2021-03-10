package material.study.elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import material.study.elearning.Admin.AdminActivity;
import material.study.elearning.Model.Subjects;
import material.study.elearning.ViewHolder.SubjectListViewHolder;

public class SubjectActivity extends AppCompatActivity {
    private RecyclerView SubjectReportList;
    private DatabaseReference ClassListRef;

    private Button addclassbtn;
    private EditText classnameET;
    private DatabaseReference classRef;
    private String classID,whoID,scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);



        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_setting:
                        Intent intent = new Intent(SubjectActivity.this, SettingActivity.class);
                        intent.putExtra("who",whoID);
                        startActivity(intent);

//                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_home:

                        Intent intent2 = new Intent(SubjectActivity.this, HomeActivity.class);
                        intent2.putExtra("who",whoID);
                        startActivity(intent2);

//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_test:
                        Intent intent3 = new Intent(SubjectActivity.this, TestActivity.class);
                        intent3.putExtra("who",whoID);
                        startActivity(intent3);

//                        startActivity(new Intent(getApplicationContext(),TestActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_logout:
                        Paper.book().destroy();

                        Intent intent4 = new Intent(SubjectActivity.this, MainActivity.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        finish();
                }
                return false;
            }
        });



        classID = getIntent().getStringExtra("cid");
        whoID = getIntent().getStringExtra("who");
        scope= getIntent().getStringExtra("scope");
        SubjectReportList = findViewById(R.id.admin_subject_list_recycler_view);
        SubjectReportList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        SubjectReportList.setLayoutManager(linearLayoutManager);

        ClassListRef= FirebaseDatabase.getInstance().getReference().child("Class").child(classID).child("Subject");

        classnameET=(EditText) findViewById(R.id.admin_subject_name_edittext);


        addclassbtn=(Button) findViewById(R.id.admin_subject_add_btn);

//        addclassbtn.setVisibility(View.VISIBLE);



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
                    Toast.makeText(SubjectActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    classRef = FirebaseDatabase.getInstance().getReference().child("Class");

                    classRef.child(classID).child("Subject").child(classnameET.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                Toast.makeText(SubjectActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                final HashMap<String, Object> FakeMap = new HashMap<>();
                                FakeMap.put("Subjectname", classnameET.getText().toString());

                                classRef.child(classID).child("Subject").child(classnameET.getText().toString()).updateChildren(FakeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(SubjectActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "class"+classID, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<Subjects> options =
                new FirebaseRecyclerOptions.Builder<Subjects>().setQuery(ClassListRef,Subjects.class)
                .build();


        FirebaseRecyclerAdapter<Subjects,SubjectListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Subjects, SubjectListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SubjectListViewHolder holder, int position, @NonNull final Subjects model) {
                        holder.subjectname.setText(model.getSubjectname());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SubjectActivity.this, ChapterActivity.class);
                                intent.putExtra("sid", String.valueOf(model.getSubjectname()));
                                intent.putExtra("cid", String.valueOf(classID));
                                intent.putExtra("who",whoID);

                                startActivity(intent);

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public SubjectListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_view_holder,parent,false);
                        SubjectListViewHolder holder = new SubjectListViewHolder(view);
                        return holder;
                    }
                };
        SubjectReportList.setAdapter(adapter);
        adapter.startListening();


    }


}
