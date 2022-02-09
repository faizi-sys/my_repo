package com.example.iiui_project.DriverActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iiui_project.LoginPackage.RegistrationActivity;
import com.example.iiui_project.MapPackage.MapsActivity;
import com.example.iiui_project.MapPackage.SetMapHouse;
import com.example.iiui_project.Model.AddDeriveModelClass;
import com.example.iiui_project.Model.AppUser;
import com.example.iiui_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddDerives extends AppCompatActivity {
    Button ChoosePhotoBtn ;
    Uri  picUri ;
    TimePickerDialog timePickerDialog ;
    public static final String DATE_FORMAT_1 = "hh:mm:ss a";
    public static final int DIALOG_ID = 0;
    private int Year_x, Month_x, Dat_x;
    ImageView CheckPoint ,CheckPoint2  ;
    Button AddDerive ;
    EditText Title , Address ;
    ProgressBar progressBar ;

    FirebaseAuth mAuth ;
    FirebaseUser user ;

    String  Lattitude = "";
    String  Lattitude2 = "";

    String  Longitude = "" ;
    String  Longitude2 = "" ;

    int MIN , HOURS ;
    EditText DateV  , TimeV;
    String Time  = " ";
    String DateofDay = " ";
    final  int PICK_IMAGE_REQUEST = 1 ;
    DatabaseReference UserDataBaseReferenceDrive  ;
    AddDeriveModelClass addDeriveModelClass;
    StorageReference StorageDatabse ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addderive_of_driver);


        AddDerive = findViewById(R.id.adddrive);



        StorageDatabse = FirebaseStorage.getInstance().getReference("uploads");

        Calendar calendar = Calendar.getInstance();
        Year_x = calendar.get(calendar.YEAR);
        Month_x = calendar.get(calendar.MONTH);
        Dat_x = calendar.get(calendar.DAY_OF_MONTH);


        progressBar = findViewById(R.id.prog) ;
        MIN = calendar.get(calendar.MINUTE);
        HOURS = calendar.get(calendar.HOUR_OF_DAY);

        Title = findViewById(R.id.issue_type);

        Address = findViewById(R.id.address);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UserDataBaseReferenceDrive = FirebaseDatabase.getInstance().getReference("UserDerive").child(user.getUid()) ;

        (CheckPoint = findViewById(R.id.check)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDerives.this, MapsActivity.class);
                startActivityForResult(intent,2);
            }
        }); ;

        (CheckPoint2 = findViewById(R.id.check2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddDerives.this, "s", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddDerives.this, MapsActivity.class);
                startActivityForResult(intent,3);
            }
        }); ;



        (DateV = findViewById(R.id.date)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        (TimeV = findViewById(R.id.time)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                timePickerDialog =
                        new TimePickerDialog(AddDerives.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Time =   getTimeFormated(hourOfDay,minute) ;

                                TimeV.setText(Time);



                            }
                        },HOURS,MIN,android.text.format.DateFormat.is24HourFormat(getApplicationContext()));
                timePickerDialog.show();

            }
        });



        (ChoosePhotoBtn = findViewById(R.id.upload_photo) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        AddDerive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                if(Title.getText().toString().isEmpty()){
                    Title.setError("This Field is Empty");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }


                if(Integer.valueOf(Function(DateofDay , getCurrentDateOFDay()))<0){
                    DateV.setError("Please Enter Valid Date");
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Date", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return ;
                }

                if(Time.isEmpty()){
                    TimeV.setError("This Field is Empty");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;

                }
                if(DateofDay.isEmpty()){
                    DateV.setError("This Field is Empty");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;

                }
                if(Address.getText().toString().isEmpty()){
                    Address.setError("This Field is Empty");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if(Lattitude.isEmpty() || Longitude.isEmpty()){
                    Toast.makeText(AddDerives.this, "Please Select Location ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;

                }
                String pushid = UserDataBaseReferenceDrive.push().getKey().toString();

                if(picUri == null) {
                     addDeriveModelClass =   new AddDeriveModelClass(String.valueOf(user.getUid()),pushid, Title.getText().toString(), Time, DateofDay, Address.getText().toString(), Lattitude, Longitude, "None", Lattitude2 , Longitude2) ;
                     UserDataBaseReferenceDrive.child(pushid).setValue(addDeriveModelClass);
                    Toast.makeText(AddDerives.this, "Check Point Added", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
                else{


                    try {


                        StorageDatabse.child(System.currentTimeMillis() + "." + getExtension(picUri)).putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Handler handler = new Handler() ;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        addDeriveModelClass =   new AddDeriveModelClass(String.valueOf(user.getUid()),pushid, Title.getText().toString(), Time, DateofDay, Address.getText().toString(), Lattitude, Longitude, "None", Lattitude2 , Longitude2) ;
                                        UserDataBaseReferenceDrive.child(pushid).setValue(addDeriveModelClass);
                                        Toast.makeText(AddDerives.this, "Check Point Added", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddDerives.this, ViewDerives.class);
                                        startActivity(intent);


                                    }
                                },500);



                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {


                                        addDeriveModelClass =   new AddDeriveModelClass(String.valueOf(user.getUid()),pushid, Title.getText().toString(), Time, DateofDay, Address.getText().toString(), Lattitude, Longitude, uri.toString(), Lattitude2 , Longitude2) ;
                                        UserDataBaseReferenceDrive.child(pushid).setValue(addDeriveModelClass);
                                        Toast.makeText(AddDerives.this, "Check Point Added", Toast.LENGTH_SHORT).show();


                                        progressBar.setVisibility(View.INVISIBLE);



                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressBar.setProgress((int)progress);
                            }
                        });

                    } catch (Exception e) {
                        Log.d(e.toString(), "Plz Select Pic");
                        Toast.makeText(getApplicationContext() ,"Plz Select Pic " + e.toString() , Toast.LENGTH_LONG).show();

                    }


                }


            }
        });




    }

    private void openFileChooser() {
        Intent i = new Intent() ;
        i.setAction(Intent.ACTION_GET_CONTENT) ;
        i.setType("image/*") ;
        startActivityForResult(i  , PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

      //  Toast.makeText(getApplicationContext() , "Call" , Toast.LENGTH_LONG).show();

        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "" + requestCode, Toast.LENGTH_SHORT).show();
        if (resultCode == RESULT_OK) {
            if(requestCode == 1){
                picUri = data.getData();
                //newpicUri = data.getData();
                CropImage.activity(picUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(AddDerives.this);
            }

            if (null != picUri) {
                Toast.makeText(this, "Picture Selected", Toast.LENGTH_SHORT).show();
            }
            if(picUri == null){
                Toast.makeText(this, "Picture Not Selected", Toast.LENGTH_SHORT).show();
            }



        }



        if (requestCode == 2)
        {
            Lattitude = data.getStringExtra("Lattitude");
            Longitude = data.getStringExtra("Longitude");
        }
        if (requestCode == 3)
        {
            Lattitude2 = data.getStringExtra("Lattitude");
            Longitude2 = data.getStringExtra("Longitude");
        }


      //  Toast.makeText(getApplicationContext(), Lattitude, Toast.LENGTH_SHORT).show();



    }

    private String getTimeFormated(int hr,int min) {
        java.sql.Time tme = new Time(hr,min,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("hh:mm:ss a");
        return formatter.format(tme);
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }



    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month , int dayOfMonth) {
            Year_x = year;
            Month_x = (month + 1);
            Dat_x = dayOfMonth;
            DateofDay = String.valueOf(Dat_x) + " " + String.valueOf(Month_x)+ " " + String.valueOf(Year_x) ;
            DateV.setText(DateofDay);

          //  Toast.makeText(getApplicationContext(), "Date" + month, Toast.LENGTH_SHORT).show();


        }

    };
    @NotNull
    private  String Function(String  inp1 , String inp2){

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String inputString1 = inp1;
        String inputString2 = inp2;
        long diff = 0;
        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            diff = date1.getTime() - date2.getTime();
            Log.d("Days: " , TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + "" ;
    }


    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ID)
            return new DatePickerDialog(AddDerives.this, dpickerListener, Year_x, Month_x, Dat_x);
        return null;
    }
    private  String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver() ;
        MimeTypeMap mime = MimeTypeMap.getSingleton() ;
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    public  String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public  String getCurrentDateOFDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
}
