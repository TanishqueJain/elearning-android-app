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
import material.study.elearning.Model.Question;
import material.study.elearning.Model.Test;
import material.study.elearning.ViewHolder.Testviweholder;
public class AdminTestActivity extends AppCompatActivity {

    private RecyclerView SubjectReportList;
    private DatabaseReference ClassListRef;

    private Button addclassbtn;
    private EditText classnameET,testtime, questionNo;
    private DatabaseReference classRef;
    private String classID,whoID,subjectID,scope,chapterid,testid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_test);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_setting:
                        Intent intent = new Intent(AdminTestActivity.this, SettingActivity.class);
                        intent.putExtra("who",whoID);
                        startActivity(intent);

//                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_home:

                        Intent intent2 = new Intent(AdminTestActivity.this, HomeActivity.class);
                        intent2.putExtra("who",whoID);
                        startActivity(intent2);

//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_test:
                        Intent intent3 = new Intent(AdminTestActivity.this, TestActivity.class);
                        intent3.putExtra("who",whoID);
                        startActivity(intent3);

//                        startActivity(new Intent(getApplicationContext(),TestActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_logout:
                        Paper.book().destroy();

                        Intent intent4 = new Intent(AdminTestActivity.this, MainActivity.class);
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
        testid =getIntent().getStringExtra("tid");
        SubjectReportList = findViewById(R.id.Test_question_recycler_view);
        SubjectReportList.setHasFixedSize(true);
        scope= getIntent().getStringExtra("scope");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        SubjectReportList.setLayoutManager(linearLayoutManager);

        ClassListRef= FirebaseDatabase.getInstance().getReference().child("Test").child("Class").child(classID).child("Subject").child(subjectID).child("Chapter").child(chapterid).child("Test").child(testid).child("Question");
        classnameET=(EditText) findViewById(R.id.admin_exam_ques_edittext);
        testtime=(EditText) findViewById(R.id.admin_exam_answer_edittext);
        questionNo=(EditText) findViewById(R.id.admin_exam_ques_no_edittext);
        addclassbtn=(Button) findViewById(R.id.admin_exam_add_btn);

//        addclassbtn.setVisibility(View.VISIBLE);
        if(whoID.equals("admin"))
        {
            classnameET.setVisibility(View.VISIBLE);
            testtime.setVisibility(View.VISIBLE);
            questionNo.setVisibility(View.VISIBLE);
            addclassbtn.setVisibility(View.VISIBLE);
        }

        else {
            classnameET.setVisibility(View.GONE);
            testtime.setVisibility(View.GONE);
            questionNo.setVisibility(View.GONE);
            addclassbtn.setVisibility(View.GONE);
        }


        addclassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classnameET.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(classnameET.getText().toString()))
                {
                    Toast.makeText(AdminTestActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    classRef = FirebaseDatabase.getInstance().getReference().child("Test").child("Class");

                    classRef.child(classID).child("Subject").child(subjectID).child("Chapter").child(chapterid).child("Test").child(testid).child("Question").child(questionNo.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                Toast.makeText(AdminTestActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                final HashMap<String, Object> FakeMap = new HashMap<>();
                                FakeMap.put("question", classnameET.getText().toString());
                                FakeMap.put("answer",testtime.getText().toString());
                                FakeMap.put("questionNo",questionNo.getText().toString());

                                classRef.child(classID).child("Subject").child(subjectID).child("Chapter").child(chapterid).child("Test").child(testid).child("Question").child(questionNo.getText().toString()).updateChildren(FakeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(AdminTestActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
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
        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>().setQuery(ClassListRef,Question.class)
                        .build();

        FirebaseRecyclerAdapter<Question, Testviweholder> adapter =
                new FirebaseRecyclerAdapter<Question, Testviweholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Testviweholder holder, int position, @NonNull final Question model) {

                        holder.question.setText(model.getQuestion());
                        holder.answer.setText(model.getAnswer());

                    }
                    @NonNull
                    @Override
                    public Testviweholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_test_card_view, parent,false);
                        Testviweholder holder = new Testviweholder(view);
                        return holder;
                    }
                };
        SubjectReportList.setAdapter(adapter);
        adapter.startListening();
    }

 }