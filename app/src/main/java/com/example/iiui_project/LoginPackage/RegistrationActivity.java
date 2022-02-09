package com.example.iiui_project.LoginPackage;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iiui_project.AlertDialog.LoadingDialog;
import com.example.iiui_project.MapPackage.SetMapHouse;
import com.example.iiui_project.Model.AppUser;
import com.example.iiui_project.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    DatabaseReference UserStorageDatabase ;

    DatabaseReference UserDataBaseReference  ;

    StorageReference StorageDatabse ;
    CircularImageView PictureImageView ;
    EditText UserName , Password ,Cnic , Phone , Age    , Emailtxt ,Location , Price , ColorEdit;
    Uri picUri  , resulturi , newpicUri;
    Button RegisterBtn;
    final  int PICK_IMAGE_REQUEST = 1 ;
    private FirebaseAuth mAuth; ;

    LoadingDialog loadingDialog ;

    ProgressBar progressBar ;

   String  Lattitude = "";

   String  Longitude = "" ;
    String nname , ppass , eemail,ccnic , pphone ,aage , sskils , ccity , price;
    String emailPattern ;
    Spinner CarList ;
    Spinner USerType ;
    EditText CarNumber ;
    EditText ConformPassword ;

    ImageView Map ;
    String carlst , usertype , carnumber ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        progressBar = findViewById(R.id.progress1);
        ConformPassword = findViewById(R.id.conpassword);
        PictureImageView = findViewById(R.id.profilepic) ;
        UserName = findViewById(R.id.username ) ;
        Password = findViewById(R.id.password) ;
        Cnic =findViewById(R.id.cnic);
        Phone = findViewById(R.id.phone) ;
        Age  = findViewById(R.id.ageedi) ;

        CarList  = findViewById(R.id.cartypelst) ;
        USerType  = findViewById(R.id.typeuser) ;
        CarNumber  = findViewById(R.id.carnumber) ;
        ColorEdit  = findViewById(R.id.colortype) ;
        Map  = findViewById(R.id.mapopen) ;

        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegistrationActivity.this,SetMapHouse.class);
                startActivityForResult(intent,2);

            }
        });

        loadingDialog = new LoadingDialog(RegistrationActivity.this);

        UserDataBaseReference = FirebaseDatabase.getInstance().getReference("User") ;
        USerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (USerType.getSelectedItem().toString().equals("User")){
                    CarList.setVisibility(View.GONE);
                    CarNumber.setVisibility(View.GONE);
                    ColorEdit.setVisibility(View.GONE);
                    Price.setVisibility(View.GONE);



                }
                else if (USerType.getSelectedItem().toString().equals("Driver")){
                    CarList.setVisibility(View.VISIBLE);
                    CarNumber.setVisibility(View.VISIBLE);
                    Price.setVisibility(View.VISIBLE);
                    ColorEdit.setVisibility(View.VISIBLE);

                    if(CarNumber.getText().toString().isEmpty())
                    {
                        CarNumber.setError("Field is Empty");
                        return;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Location = findViewById(R.id.location) ;
        Emailtxt = findViewById(R.id.email);
        Price = findViewById(R.id.price);
        RegisterBtn = findViewById(R.id.btnRegister);

        StorageDatabse = FirebaseStorage.getInstance().getReference("uploads");

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        PictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                nname = UserName.getText().toString().trim() ;
                ppass = Password.getText().toString().trim();
                eemail = Emailtxt.getText().toString().trim();
                ccnic = Cnic.getText().toString().trim();
                pphone = Phone.getText().toString().trim();
                ccity = Location.getText().toString().trim();
                price = Price.getText().toString().trim() ;

                aage = Age.getText().toString().trim() ;

                boolean flag = false;

                if(ConformPassword.getText().toString().isEmpty()){
                    ConformPassword.setError("Field is Empty");
                    return;
                }


                if(!ConformPassword.getText().toString().equals(Password.getText().toString())){
                    ConformPassword.setError("Password  not match");
                    return;
                }



                if(isValidName(nname) == false){
                    flag = true;
                    UserName.setError("Please Enter Valid Name");

                    progressBar.setVisibility(View.INVISIBLE);
                    return;

                }



                if(isPasswordValid(ppass) == false){
                    flag = true;

                    progressBar.setVisibility(View.INVISIBLE);
                    Password.setError("Password Must be atleast 8 characters");
                    return;

                }
                if(isValidEmail(eemail) == false){
                    flag = true;

                    progressBar.setVisibility(View.INVISIBLE);
                    Emailtxt.setError("Please Enter Valid Email");
                    return;

                }

                if(isValidField(ccity) == false){
                    flag = true;

                    progressBar.setVisibility(View.INVISIBLE);
                    Location.setError("Please Enter Valid City");
                    return;

                }
                if(isValidPhone(pphone)==false){
                    flag=true;
                    progressBar.setVisibility(View.INVISIBLE);
                    Phone.setError("Please Enter Valid Phone");
                    return;
                }

                if(USerType.getSelectedItem().equals("Driver")) {
                    if (price.isEmpty()) {

                        progressBar.setVisibility(View.INVISIBLE);
                        Price.setError("Enter price ");
                        return;
                    }
                }

                if(Location.getText().toString().isEmpty()){
                    Location.setError("Enter Location ");

                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (aage.isEmpty()){
                    Age.setError("Enter price ");

                    progressBar.setVisibility(View.INVISIBLE);
                    return ;
                }

                if(isValidcnic(ccnic)==false){
                    flag= true;
                    progressBar.setVisibility(View.INVISIBLE);
                    Cnic.setError("Please enter valid CNIC");
                }
                if(newpicUri == null){

                    progressBar.setVisibility(View.INVISIBLE);
                    return ;
                }else{

                }


                if(Lattitude.isEmpty() || Longitude.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Please Select Location", Toast.LENGTH_SHORT).show();
                    return;
                }



                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(Emailtxt.getText().toString(), Password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {










                                try {


                                    StorageDatabse.child(System.currentTimeMillis() + "." + getExtension(picUri)).putFile(newpicUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

                                                    try {

                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        if (USerType.getSelectedItem().toString().equals("Driver")) {
                                                            AppUser appUser = new AppUser(nname, eemail, ppass, ccnic, Phone.getText().toString(), aage, "0.0", "0.0", uri.toString(), "None", USerType.getSelectedItem().toString(), CarList.getSelectedItem().toString(), price, mAuth.getUid().toString(), ColorEdit.getText().toString() , Location.getText().toString() , CarNumber.getText().toString() );
                                                            UserDataBaseReference.child(mAuth.getUid().toString()).setValue(appUser);
                                                        } else if (USerType.getSelectedItem().toString().equals("User")) {
                                                            AppUser appUser = new AppUser(nname, eemail, ppass, ccnic, Phone.getText().toString(), aage, "0.0", "0.0", uri.toString(), "None", USerType.getSelectedItem().toString(), "NoCar", "None", mAuth.getUid().toString(), "None" , Location.getText().toString() , CarNumber.getText().toString());
                                                  UserDataBaseReference.child(mAuth.getUid().toString()).setValue(appUser);
                                                        }

                                                    }catch (Exception e){
                                                       // Toast.makeText(RegistrationActivity.this, e.toString()+"", Toast.LENGTH_SHORT).show();
                                                    }
                                                    Toast.makeText(RegistrationActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
//                                                    loadingDialog.Dismiss();
                                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                                    startActivity(intent);



                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(RegistrationActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                                            progressBar.setProgress((int) progress);
                                        }
                                    });

                                } catch (Exception e) {
                                    Log.d(e.toString(), "Plz Select Pic");
                                    Toast.makeText(getApplicationContext() ,"Plz Select Pic " + e.toString() , Toast.LENGTH_LONG).show();

                                }



























                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                       // Toast.makeText(RegistrationActivity.this, ""+ e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {

                    }
                });


            }
        });







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
        super.onActivityResult(requestCode, resultCode, data);
       // Toast.makeText(this, "" + requestCode, Toast.LENGTH_SHORT).show();
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_REQUEST){
                picUri = data.getData();
                newpicUri = data.getData();
                CropImage.activity(picUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(RegistrationActivity.this);
            }

            if (null != picUri) {
                ImageView im = findViewById(R.id.profilepic) ;
                String path = getPathFromURI(picUri);
                Picasso.with(getApplicationContext() ).load(picUri.toString()).into(im);
            }



        }



            if (requestCode == 2)
            {
                Lattitude = data.getStringExtra("Lattitude");
                Longitude = data.getStringExtra("Longitude");
            }

           // Toast.makeText(getApplicationContext(), Lattitude, Toast.LENGTH_SHORT).show();



}



    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }
    public static boolean isValidField(String inp) {
        //TODO: Replace this with your own logic
        return inp.length() > 3;
    }

    public static boolean isValidPrice(String inp) {
        //TODO: Replace this with your own logic
        return (Integer.parseInt(inp) > 99);

    }
    public static boolean isValidAge(String inp) {
        //TODO: Replace this with your own logic
        return (Integer.parseInt(inp) > 14);
    }
    public static boolean isValidcnic(String inp) {
        Pattern p=Pattern.compile("[1-7]{5}[0-9]{7}[0-9]{1}");
        Matcher m=p.matcher(inp);
        if(inp.length()==13){
            return m.find();
        }

return false;
    }

    public static boolean isValidPhone(String phn){
        Pattern p=Pattern.compile("(0){1}[3]{1}[0-9]{9}");
        Matcher m=p.matcher(phn);
        if(phn.length()==11){
            return m.find();
        }
        return false;
    }


    public static boolean isValidName(String inp){
        String regex = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inp);
        if(inp.length() > 2)
            return matcher.find();

        return false;
    }

    private  String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver() ;
        MimeTypeMap mime = MimeTypeMap.getSingleton() ;
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

}
