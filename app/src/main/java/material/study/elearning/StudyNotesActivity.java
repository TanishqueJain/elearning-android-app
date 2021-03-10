package material.study.elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import io.paperdb.Paper;

public class StudyNotesActivity extends AppCompatActivity {
    private Button pdfuploadbtn;
    private Uri pdfUri;
    private static final int GalleryPick = 1;
    private ProgressDialog loadingBar;
    private StorageReference pdfRef;
    private DatabaseReference ShowpdfRef, classRef;;
    private String downloadpdfUrl;
    private String classID,whoID="user",subjectID;
    private String chapterID;
    private PDFView pdfView;
//    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_notes);
        loadingBar = new ProgressDialog(this);



        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_setting:
                        Intent intent = new Intent(StudyNotesActivity.this, SettingActivity.class);
                        intent.putExtra("who",whoID);
                        startActivity(intent);

//                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_home:

                        Intent intent2 = new Intent(StudyNotesActivity.this, HomeActivity.class);
                        intent2.putExtra("who",whoID);
                        startActivity(intent2);

//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_test:
                        Intent intent3 = new Intent(StudyNotesActivity.this, TestActivity.class);
                        intent3.putExtra("who",whoID);
                        startActivity(intent3);

//                        startActivity(new Intent(getApplicationContext(),TestActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_logout:
                        Paper.book().destroy();

                        Intent intent4 = new Intent(StudyNotesActivity.this, MainActivity.class);
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
        chapterID =getIntent().getStringExtra("lid");
        ShowpdfRef= FirebaseDatabase.getInstance().getReference();

        pdfuploadbtn = (Button) findViewById(R.id.pdf_upload_btn);
        pdfRef = FirebaseStorage.getInstance().getReference().child("Study Material Pdf");


        if(whoID.equals("admin"))
        {
            pdfuploadbtn.setVisibility(View.VISIBLE);

        }

        else {
            pdfuploadbtn.setVisibility(View.GONE);

        }



        pdfuploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openfilemanager();

            }
        });
        pdfView=(PDFView) findViewById(R.id.pdfView);


        classRef = FirebaseDatabase.getInstance().getReference().child("Class");

        classRef.child(classID).child("Subject").child(subjectID).child("Chapter")
                .child(chapterID).child("Notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String link = dataSnapshot.child("pdf").getValue(String.class);
                    new RetrievePDFStream().execute(link);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        url="https://firebasestorage.googleapis.com/v0/b/elearning-c1a5c.appspot.com/o/Study%20Material%20Pdf%2Fprimary%3ADownload%2FDetailedSyllabus_CS_2009-10_Main-III_IV.pdfClass%205Chemistrychapter%201.pdf?alt=media&token=c9c2033f-877f-4f99-92de-d24f4b9589bf";

//        new RetrievePDFStream().execute(url);

    }

    class RetrievePDFStream extends AsyncTask<String,Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream=null;

            try{

                URL urlx=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) urlx.openConnection();
                if(urlConnection.getResponseCode()==200){
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());

                }
            }catch (IOException e){
                return null;
            }
            return inputStream;

        }
        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.fromStream(inputStream).load();
        }
    }

    private void openfilemanager()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("application/pdf");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            pdfUri = data.getData();
            uploadingpdf();
//            addcategoryimage.setImageURI(ImageUri);
        }
    }

    private void uploadingpdf() {
        loadingBar.setTitle("Add New pdf");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new pdf.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        final StorageReference filePath = pdfRef.child(pdfUri.getLastPathSegment() + classID + subjectID+ chapterID + ".pdf");

        final UploadTask uploadTask = filePath.putFile(pdfUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(StudyNotesActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(StudyNotesActivity.this, "Category Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadpdfUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadpdfUrl = task.getResult().toString();

                            Toast.makeText(StudyNotesActivity.this, "got the pdf Url Successfully...", Toast.LENGTH_SHORT).show();

                            SavePdfInfoToDatabase();
                        }
                    }
                });
            }
        });

    }
    private void SavePdfInfoToDatabase()
    {

        final HashMap<String, Object> CategoryMap = new HashMap<>();

        CategoryMap.put("pdf", downloadpdfUrl);


        ShowpdfRef.child("Class").child(classID).child("Subject").child(subjectID).child("Chapter").child(chapterID).child("Notes").updateChildren(CategoryMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {


                            loadingBar.dismiss();
                            Toast.makeText(StudyNotesActivity.this, "pdf is added successfully..", Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(StudyNotesActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
