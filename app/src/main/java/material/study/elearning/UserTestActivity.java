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
import material.study.elearning.Model.Question;
import material.study.elearning.ViewHolder.Testviweholder;

public class UserTestActivity extends AppCompatActivity {
    private RecyclerView SubjectReportList;
    private DatabaseReference ClassListRef;
    int i = 0;
    private Button submitTestBtn;

    private DatabaseReference classRef;
    private String classID,whoID,subjectID,chapterID,testID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_test);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setSelectedItemId(R.id.menu_test);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_setting:
                        Intent intent = new Intent(UserTestActivity.this, SettingActivity.class);
                        intent.putExtra("who",whoID);
                        startActivity(intent);

//                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_home:

                        Intent intent2 = new Intent(UserTestActivity.this, HomeActivity.class);
                        intent2.putExtra("who",whoID);
                        startActivity(intent2);

//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_test:
                        Intent intent3 = new Intent(UserTestActivity.this, TestActivity.class);
                        intent3.putExtra("who",whoID);
                        startActivity(intent3);

//                        startActivity(new Intent(getApplicationContext(),TestActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_logout:
                        Paper.book().destroy();

                        Intent intent4 = new Intent(UserTestActivity.this, MainActivity.class);
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
        chapterID = getIntent().getStringExtra("lid");
        testID = getIntent().getStringExtra("tid");

        SubjectReportList = findViewById(R.id.chapter_list_recycler_view);
        SubjectReportList.setHasFixedSize(true);
//        scope= getIntent().getStringExtra("scope");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        SubjectReportList.setLayoutManager(linearLayoutManager);

        ClassListRef= FirebaseDatabase.getInstance().getReference().child("Test").child("Class").child(classID).child("Subject").child(subjectID).child("Chapter").child(chapterID).child("Test").child(testID).child("Question");
//        addclassbtn.setVisibility(View.VISIBLE);

        submitTestBtn = (Button) findViewById(R.id.user_submit_test_btn);

        submitTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTestActivity.this, ResultActivity.class);
                intent.putExtra("result",String.valueOf(i));
                startActivity(intent);
                Toast.makeText(UserTestActivity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        });

//
//        if(whoID.equals("admin"))
//        {
//            classnameET.setVisibility(View.VISIBLE);
//            addclassbtn.setVisibility(View.VISIBLE);
//        }
//
//        else {
//            classnameET.setVisibility(View.GONE);
//            addclassbtn.setVisibility(View.GONE);
//        }


//        addclassbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                classnameET.setVisibility(View.VISIBLE);
//
//                if(TextUtils.isEmpty(classnameET.getText().toString()))
//                {
//                    Toast.makeText(UserTestActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    classRef = FirebaseDatabase.getInstance().getReference().child("Class");
//
//                    classRef.child(classID).child("Subject").child(subjectID).child("Question").child(classnameET.getText().toString()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if(dataSnapshot.exists())
//                            {
//                                Toast.makeText(UserTestActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                final HashMap<String, Object> FakeMap = new HashMap<>();
//                                FakeMap.put("Questionname", classnameET.getText().toString());
//
//                                classRef.child(classID).child("Subject").child(subjectID).child("Question").child(classnameET.getText().toString()).updateChildren(FakeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if(task.isSuccessful())
//                                        {
//                                            Toast.makeText(UserTestActivity.this, "Successfully Created", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//
//                                classnameET.setText("");
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//        });


    }


    @Override
    protected void onStart() {

        super.onStart();
        Toast.makeText(this, "class"+classID, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>().setQuery(ClassListRef,Question.class)
                        .build();


        FirebaseRecyclerAdapter<Question, Testviweholder> adapter =
                new FirebaseRecyclerAdapter<Question, Testviweholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final Testviweholder holder, int position, @NonNull final Question model) {
                        holder.question.setText(model.getQuestion());


                        holder.submitAnswer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(holder.answer.getText().equals(model.getAnswer())){
                                    i=i+1;
                                    holder.submitAnswer.setVisibility(View.GONE);
                                }
                                else
                                    {
                                        i=i-1;
                                        holder.submitAnswer.setVisibility(View.GONE);
                                    }

                            }
                        });


//                            if(whoID.equals("user")) {
//                                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(UserTestActivity.this, UserTestActivity.class);
//                                        intent.putExtra("sid", String.valueOf(subjectID));
//                                        intent.putExtra("cid", String.valueOf(classID));
//                                        intent.putExtra("lid", String.valueOf(model.getQuestionname()));
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
//                                        Intent intent = new Intent(UserTestActivity.this, AdminTestActivity.class);
//                                        intent.putExtra("sid", String.valueOf(subjectID));
//                                        intent.putExtra("cid", String.valueOf(classID));
//                                        intent.putExtra("lid", String.valueOf(model.getQuestionname()));
//                                        intent.putExtra("who", whoID);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(UserTestActivity.this, StudyNotesActivity.class);
//                                intent.putExtra("sid", String.valueOf(subjectID));
//                                intent.putExtra("cid", String.valueOf(classID));
//                                intent.putExtra("lid", String.valueOf(model.getQuestionname()));
//                                intent.putExtra("who", whoID);
//                                startActivity(intent);
//
//                            }
//                        });


                    }

                    @NonNull
                    @Override
                    public Testviweholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_test_card_view,parent,false);
                        Testviweholder holder = new Testviweholder(view);
                        return holder;
                    }
                };
        SubjectReportList.setAdapter(adapter);
        adapter.startListening();


    }


}