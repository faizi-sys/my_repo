package com.example.iiui_project.DriverDashoard;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iiui_project.AlertDialog.LoadingDialog;
import com.example.iiui_project.Model.AppUser;
import com.example.iiui_project.R;
import com.example.iiui_project.USerDashboard.UpdateLocationActvity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class DriverUpdateActivity extends AppCompatActivity {

    Uri resulturi;
    Uri newpicUri ;
     final  int PICK_IMAGE_REQUEST = 1 ;
LoadingDialog loadingDialog ;
    String UserName, Password, Emails, Phone, Lattitude, Longitude, picUri, Cnic, status, Skills, City;
    String Age, Price;

    String CarNumber ;
    DatabaseReference UserFirebaseDatabasae ;
    Button Updatebtn , Deletebtn ;

    CircularImageView circularImageView;
    TextView Updateusernametextview ,UpdateEmailtextvieew,UpdatetCity,Skillstextview;
    ImageView imageView ;
    EditText Infousername , InfoEmail , InfoPhone , Infocity , InfoPassword , Infocnic , Infoage , InfoPrice , InfoStatus;
    AppUser [] UserModel ;
    StorageReference UserStorageDatabase  ;;
    String Keys ;

   ImageView  UpdateLocationButton ;

   String CarType , Prices ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofileupdation_layout);
        UpdateLocationButton = findViewById(R.id.updatelocationpostwork);
        UpdateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , UpdateLocationActvity.class);
                i.putExtra("pushkey" , getIntent().getStringExtra("push"));
                startActivity(i);
            }
        });


        loadingDialog = new LoadingDialog(DriverUpdateActivity.this);

        UserStorageDatabase = FirebaseStorage.getInstance().getReference("uploads");

        UserFirebaseDatabasae = FirebaseDatabase.getInstance().getReference("User");

        circularImageView = findViewById(R.id.upimgbtn);
         Updateusernametextview = findViewById(R.id.upusernamebtn);
         UpdateEmailtextvieew = findViewById(R.id.upemailbtn);
         UpdatetCity = findViewById(R.id.upcity);
         Skillstextview =findViewById(R.id.upskill);

        imageView = findViewById(R.id.editcontrolpost);
        Infousername = findViewById(R.id.infousername);
        InfoEmail = findViewById(R.id.infoemail);
        InfoPhone = findViewById(R.id.infophone);
        Infocity = findViewById(R.id.infocity);
        InfoPassword = findViewById(R.id.infopassword);
        Infocnic = findViewById(R.id.infocnic);
        Infoage = findViewById(R.id.infoage);
        InfoPrice = findViewById(R.id.infoprice);
        InfoStatus =findViewById(R.id.infostatus);
         UserModel = new AppUser[1] ;

         Keys =getIntent().getStringExtra("push");





        UserFirebaseDatabasae.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    AppUser usersModelClass = dataSnapshot1.getValue(AppUser.class);
                    if(Keys.equals(usersModelClass.getPushid())){
                        UserName = usersModelClass.getName();
                        Password = usersModelClass.getPassword();
                        Emails = usersModelClass.getEmail() ;
                        Phone = usersModelClass.getPhone();
                        Lattitude = usersModelClass.getLattitude();
                        Longitude = usersModelClass.getLongitude();
                        picUri = usersModelClass.getPicUri();
                        Cnic = usersModelClass.getCnic();
                        status = usersModelClass.getStatus();
                        Skills = usersModelClass.getPassword();
                        City = usersModelClass.getLocation();
                        Age = usersModelClass.getAge() ;
                        Price = usersModelClass.getPrice();
                     //   Toast.makeText(getApplicationContext() , "true " , Toast.LENGTH_LONG).show();

                        CarNumber = usersModelClass.getCarcolor() ;
                        CarType = usersModelClass.getCartype();
                        Prices = usersModelClass.getPrice();


                        setData(UserName , Password , Emails , Phone , Lattitude , Longitude , picUri , Cnic , status ,
                                Skills , City , Age , Price) ;


                    }
                }

         circularImageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 loadingDialog.LoadindAlert();
                 openFileChooser();
                 loadingDialog.Dismiss();
             }
         });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                  Infousername.setEnabled(true);
                  InfoEmail.setEnabled(true);
                  InfoPhone.setEnabled(false) ; Infocity.setEnabled(true) ; InfoPassword.setEnabled(true) ; Infocnic.setEnabled(true) ; Infoage.setEnabled(true) ; InfoPrice.setEnabled(true) ; InfoStatus.setEnabled(true);








             }
         });





        // Update and Detele Account Button //
        Updatebtn =findViewById(R.id.upcontract);
        Deletebtn = findViewById(R.id.Deletepost);



        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(resulturi==null) {

                    loadingDialog.LoadindAlert();
                    UserName = Infousername.getText().toString().trim();
                    Password = InfoPassword.getText().toString().trim();
                    Emails = InfoEmail.getText().toString().trim();
                    Phone = InfoPhone.getText().toString().trim();
                    Cnic = Infocnic.getText().toString().trim();
                    status = InfoStatus.getText().toString();
                    City = Infocity.getText().toString().trim();
                    Price = InfoPrice.getText().toString().trim();
                    Age = Infoage.getText().toString().trim();
                    Toast.makeText(getApplicationContext(), Emails + "show", Toast.LENGTH_LONG).show();


                    AppUser usersModelClass = new AppUser(
                            UserName, Emails, Password, Cnic,  Phone,Age, Lattitude, Longitude,  picUri,  status, "User", CarType, Prices, Keys, "None", City , CarNumber
                    );
                    UserFirebaseDatabasae.child(Keys).setValue(usersModelClass);
                    loadingDialog.Dismiss();
                }else{

                    loadingDialog.LoadindAlert();
                   PictureUpdate() ;

                    loadingDialog.Dismiss();


                }


            }
        });
    }

    private void PictureUpdate() {

        try {


            UserStorageDatabase.child(System.currentTimeMillis() + "." + getExtension(newpicUri)).putFile(resulturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler() ;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {





                        }
                    },500);



                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AppUser usersModelClass = new AppUser(
                                    UserName, Emails, Password, Cnic,  Phone,Age, Lattitude, Longitude,  picUri,  status, "User", CarType, Prices, Keys, "None", City , CarNumber

                            );

                            UserFirebaseDatabasae.child(Keys).setValue(usersModelClass);
                            Toast.makeText(getApplicationContext(), "succeed", Toast.LENGTH_LONG).show();

                            loadingDialog.Dismiss();
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
                }
            });

        } catch (Exception e) {
            Log.d(e.toString(), "Plz Select Pic");
            Toast.makeText(getApplicationContext() ,"Plz Select Pic " , Toast.LENGTH_LONG).show();

        }















    }
















    private void setData(String userName, String password, String emails, String phone, String lattitude, String longitude, String picUri, String cnic, String status, String skills, String city, String age, String price)

    {

    Infousername.setText(userName);
    InfoPassword.setText(password);
    InfoEmail.setText(emails);
    InfoPhone.setText(phone);
    Infocnic.setText(cnic);
    InfoStatus.setText(status);
    Skillstextview.setText(skills);
    Infoage.setText(age);
    InfoPrice.setText(price);
        Infocity.setText(city);
        Updateusernametextview.setText(userName);
        UpdateEmailtextvieew.setText(emails);
        UpdatetCity.setText(city);
        Picasso.with(this).load(picUri).fit().centerCrop().into(circularImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void openFileChooser() {
        Intent i = new Intent() ;
        i.setAction(Intent.ACTION_GET_CONTENT) ;
        i.setType("image/*") ;
        startActivityForResult(i  , PICK_IMAGE_REQUEST);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Toast.makeText(getApplicationContext() , "Call" , Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_REQUEST){
                newpicUri = data.getData();
                CropImage.activity(newpicUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }

            if (null != picUri) {
                ImageView im = findViewById(R.id.upimgbtn) ;
                String path = getPathFromURI(newpicUri);
            }

        }


        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
               resulturi  =   resulturi=result.getUri();
                ImageView ima = findViewById(R.id.upimgbtn) ;
                circularImageView.setImageURI(resulturi);
            }
            else
            {
                if(resultCode== CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                {
                    Exception error=result.getError();

                }
            }
        }
    }



    private  String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver() ;
        MimeTypeMap mime = MimeTypeMap.getSingleton() ;
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }













}













