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
import material.study.elearning.Model.Test;
import material.study.elearning.ViewHolder.SubjectListViewHolder;

public class TestSeriesActivity extends AppCompatActivity {
    
    private RecyclerView SubjectReportList;
    private DatabaseReference ClassListRef;

    private Button addclassbtn;
    private EditText classnameET,testtime;
    private DatabaseReference classRef;
    private String classID,whoID,subjectID,scope,chapterid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_setting:
                        Intent intent = new Intent(TestSeriesActivity.this, SettingActivity.class);
                        intent.putExtra("who",whoID);
                        startActivity(intent);

//                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_home:

                        Intent intent2 = new Intent(TestSeriesActivity.this, HomeActivity.class);
                        intent2.putExtra("who",whoID);
                        startActivity(intent2);

//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_test:
                        Intent intent3 = new Intent(TestSeriesActivity.this, TestActivity.class);
                        intent3.putExtra("who",whoID);
                        startActivity(intent3);

//                        startActivity(new Intent(getApplicationContext(),TestActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_logout:
                        Paper.book().destroy();

                        Intent intent4 = new Intent(TestSeriesActivity.this, MainActivity.class);
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
        chapterid =getIntent().getStringExtra("lid");
        SubjectReportList = findViewById(R.id.Test_list_recycler_view);
        SubjectReportList.setHasFixedSize(true);
        scope= getIntent().getStringExtra("scope");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        SubjectReportList.setLayoutManager(linearLayoutManager);

        ClassListRef= FirebaseDatabase.getInstance().getReference().child("Test").child("Class").child(classID).child("Subject").child(subjectID).child("Chapter").child(chapterid).child("Test");

        classnameET=(EditText) findViewById(R.id.admin_test_name_edittext);
        testtime=(EditText) findViewById(R.id.admin_test_time_edittext);

        addclassbtn=(Button) findViewById(R.id.admin_test_series_add_btn);

//        addclassbtn.setVisibility(View.VISIBLE);



        if(whoID.equals("admin"))
        {
            classnameET.setVisibility(View.VISIBLE);
            testtime.setVisibility(View.VISIBLE);
            addclassbtn.setVisibility(View.VISIBLE);
        }

        else {
            classnameET.setVisibility(View.GONE);
            testtime.setVisibility(View.INVISIBLE);
            addclassbtn.setVisibility(View.GONE);
        }


        addclassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classnameET.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(classnameET.getText().toString()))
                {
                    Toast.makeText(TestSeriesActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    classRef = FirebaseDatabase.getInstance().getReference().child("Test").child("Class");

                    classRef.child(classID).child("Subject").child(subjectID).child("Chapter").child(classnameET.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                Toast.makeText(TestSeriesActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                final HashMap<String, Object> FakeMap = new HashMap<>();
                                FakeMap.put("Testname", classnameET.getText().toString());
                                FakeMap.put("Testtime",testtime.getText().toString());

                                classRef.child(classID).child("Subject").child(subjectID).child("Chapter").child(chapterid).child("Test").child(classnameET.getText().toString()).updateChildren(FakeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(TestSeriesActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
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

        FirebaseRecyclerOptions<Test> options =
                new FirebaseRecyclerOptions.Builder<Test>().setQuery(ClassListRef,Test.class)
                        .build();


        FirebaseRecyclerAdapter<Test, SubjectListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Test, SubjectListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SubjectListViewHolder holder, int position, @NonNull final Test model) {
                        holder.subjectname.setText(model.getTestname()+"  "+model.getTesttime());
                        if(whoID.equals("admin")) {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TestSeriesActivity.this, AdminTestActivity.class);
                                    intent.putExtra("sid", String.valueOf(subjectID));
                                    intent.putExtra("cid", String.valueOf(classID));
                                    intent.putExtra("lid", String.valueOf(chapterid));
                                    intent.putExtra("tid", model.getTestname());
                                    intent.putExtra("time", model.getTesttime());
                                    intent.putExtra("who", whoID);
                                    startActivity(intent);
                                }
                            });
                        }
                         else{
                             holder.itemView.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent = new Intent(TestSeriesActivity.this, UserTestActivity.class);
                                     intent.putExtra("sid", String.valueOf(subjectID));
                                     intent.putExtra("cid", String.valueOf(classID));
                                     intent.putExtra("lid", String.valueOf(chapterid));
                                     intent.putExtra("tid", model.getTestname());
                                     intent.putExtra("time", model.getTesttime());
                                     intent.putExtra("who", whoID);
                                     startActivity(intent);
                                 }
                             });
                        }


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
