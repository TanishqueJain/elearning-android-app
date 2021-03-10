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
import material.study.elearning.Model.Chapter;
import material.study.elearning.ViewHolder.SubjectListViewHolder;

public class ChapterActivity extends AppCompatActivity {
    private RecyclerView SubjectReportList;
    private DatabaseReference ClassListRef;

    private Button addclassbtn;
    private EditText classnameET;
    private DatabaseReference classRef;
    private String classID,whoID,subjectID,scope;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_setting:
                        Intent intent = new Intent(ChapterActivity.this, SettingActivity.class);
                        intent.putExtra("who",whoID);
                        startActivity(intent);

//                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_home:

                        Intent intent2 = new Intent(ChapterActivity.this, HomeActivity.class);
                        intent2.putExtra("who",whoID);
                        startActivity(intent2);

//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_test:
                        Intent intent3 = new Intent(ChapterActivity.this, TestActivity.class);
                        intent3.putExtra("who",whoID);
                        startActivity(intent3);

//                        startActivity(new Intent(getApplicationContext(),TestActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_logout:
                        Paper.book().destroy();

                        Intent intent4 = new Intent(ChapterActivity.this, MainActivity.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                        finish();
                }
                return false;
            }
        });



        classID = getIntent().getStringExtra("cid");
        whoID = getIntent().getStringExtra("who");
        subjectID = getIntent().getStringExtra("sid");
        SubjectReportList = findViewById(R.id.chapter_list_recycler_view);
        SubjectReportList.setHasFixedSize(true);
        scope= getIntent().getStringExtra("scope");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        SubjectReportList.setLayoutManager(linearLayoutManager);

        ClassListRef= FirebaseDatabase.getInstance().getReference().child("Class").child(classID).child("Subject").child(subjectID).child("Chapter");

        classnameET=(EditText) findViewById(R.id.admin_chapter_name_edittext);


        addclassbtn=(Button) findViewById(R.id.admin_chapter_add_btn);

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
                    Toast.makeText(ChapterActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    classRef = FirebaseDatabase.getInstance().getReference().child("Class");

                    classRef.child(classID).child("Subject").child(subjectID).child("Chapter").child(classnameET.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                Toast.makeText(ChapterActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                final HashMap<String, Object> FakeMap = new HashMap<>();
                                FakeMap.put("Chaptername", classnameET.getText().toString());

                                classRef.child(classID).child("Subject").child(subjectID).child("Chapter").child(classnameET.getText().toString()).updateChildren(FakeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(ChapterActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
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

        FirebaseRecyclerOptions<Chapter> options =
                new FirebaseRecyclerOptions.Builder<Chapter>().setQuery(ClassListRef,Chapter.class)
                        .build();


        FirebaseRecyclerAdapter<Chapter, SubjectListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Chapter, SubjectListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SubjectListViewHolder holder, int position, @NonNull final Chapter model) {
                        holder.subjectname.setText(model.getChaptername());


//                            if(whoID.equals("user")) {
//                                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ChapterActivity.this, UserTestActivity.class);
//                                        intent.putExtra("sid", String.valueOf(subjectID));
//                                        intent.putExtra("cid", String.valueOf(classID));
//                                        intent.putExtra("lid", String.valueOf(model.getChaptername()));
//                                        intent.putExtra("who", whoID);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//
//                            else{
//                                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ChapterActivity.this, AdminTestActivity.class);
//                                        intent.putExtra("sid", String.valueOf(subjectID));
//                                        intent.putExtra("cid", String.valueOf(classID));
//                                        intent.putExtra("lid", String.valueOf(model.getChaptername()));
//                                        intent.putExtra("who", whoID);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }

                          holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChapterActivity.this, StudyNotesActivity.class);
                                    intent.putExtra("sid", String.valueOf(subjectID));
                                    intent.putExtra("cid", String.valueOf(classID));
                                    intent.putExtra("lid", String.valueOf(model.getChaptername()));
                                    intent.putExtra("who", whoID);
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
