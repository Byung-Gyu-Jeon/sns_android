
package com.example.sns.main.ui.Myprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Random;

public class MyprofileUpdate extends AppCompatActivity {

    private Object Response;
    private static final String TAG = "MyprofileUpdate";
    private final static String BASE_URL="http://192.168.0.2:8080/sns/posts/";
    private final static String BASE_URL2="http://192.168.0.2:8080/sns/get/";
    private RetrofitService retrofitService;
    private RetrofitService retrofitService2;
    public static ApiClient ourInstance = null;
    //사진과 관련된 변수들
    private int PICK_IMAGE_REQUEST=1;
    private String img_path;
    Bitmap bmRotated;
    Uri url;
    String ptoname= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile_update);
        //확인 버튼
        Button btn = (Button) findViewById(R.id.button8);
        //이름,자기소개
        EditText edit = (EditText) findViewById(R.id.editTextTextPersonName8);
        EditText edit2 = (EditText) findViewById(R.id.editTextTextPersonName9);

        // 내사진
        ImageView imageView = (ImageView) findViewById(R.id.imageView3);




        // 기존에 db에 저장되어 잇는 사진과,이름,소개를 가져온다
        retrofitService = ourInstance.getInstance(BASE_URL2,true).create(RetrofitService.class);
        MyProfileDTO dto = new MyProfileDTO();
        Call<MyProfileDTO> call = retrofitService.getGets("1");
        call.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO>call, Response<MyProfileDTO> response) {
                String username = null;
                String userIntroduce= null;

                Gson gson = new Gson();

                if(response.isSuccessful()){
                    MyProfileDTO dto = response.body();
                    username = dto.getUsername();
                    userIntroduce = dto.getUserIntroduce();
                    ptoname = dto.getPtoname();
                    edit.setText(username);
                    edit2.setText(userIntroduce);
                    Log.d(TAG,"사진이름:"+ptoname);

                    //사진 불러오기
                    Glide.with(getBaseContext()).load("http://192.168.0.2:8080/sns/download?fileName=" + ptoname).apply(new RequestOptions().circleCrop())
                            .into(imageView);
                }
                else{
                    Toast.makeText(getBaseContext(),"서버에서 응답을 제대로 받지 못했습니다", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {

            }
        });



        //이미지뷰 클릭시 갤러리로 접근 사용자가 사진 1개를 선택한다
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

            }
        });


        //확인 버튼 클릭시
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* //텍스트를 스프링으로 보내기 위한 작업
                Gson gson = new Gson();
                MyProfileDTO dto = new MyProfileDTO();
                String username = edit.getText().toString();
                String userIntroduce=edit2.getText().toString();
                dto.setUsername(username);
                dto.setUserIntroduce(userIntroduce);

                //string -> json 변경
                String objJson = gson.toJson(dto)*/

                String username = edit.getText().toString();
                String userIntroduce=edit2.getText().toString();


                //텍스트는 map<string,RequestBody> 형태로 보내기 위한 코드들
                RequestBody usrname = RequestBody.create(MediaType.parse("text/plain"),username);
                RequestBody usrIntroduce = RequestBody.create(MediaType.parse("text/plain"),userIntroduce);

                HashMap<String,RequestBody> requestMap = new HashMap<>();
                requestMap.put("name",usrname);
                requestMap.put("Introduce",usrIntroduce);

                Log.d("tag","이름:"+requestMap.get("name"));
                Log.d("tag","자기소개:"+requestMap.get("Introduce"));



                // 사진은 multipartBody.part 형태로 보낸다
                File file = new File(img_path);
                RequestBody usrProfile = RequestBody.create(MediaType.parse("image/jpeg"),file);
                MultipartBody.Part imfile = MultipartBody.Part.createFormData("photo","photo.jpg",usrProfile);


                // 이미지와 텍스트를 spring으로 보낸다
                retrofitService2 = ourInstance.getInstance(BASE_URL,true).create(RetrofitService.class);
                Call<MyProfileDTO> call2 = retrofitService2.postData2(requestMap,imfile);

                call2.enqueue(new Callback<MyProfileDTO>() {

                    @Override
                    public void onResponse(Call<MyProfileDTO> call, Response<MyProfileDTO> response){
                        if(response.isSuccessful()){
                            MyProfileDTO result = response.body();
                            Log.d(TAG,"내 프로필 사진 텍스트 스프링으로 보내기 성공, 결과\n"+ result.toString());
                            setResult(RESULT_OK);
                            finish();
                        } else{
                            Log.d(TAG,"onResponse: 이미지+텍스트 스프링 전송실패"+response.code());
                            Log.d("fail",response.errorBody().toString());
                            Log.d("fail",call.request().body().toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<MyProfileDTO> call, Throwable t) {
                        Log.d(TAG,"전송 실패하였습니다 onFailure: "+ t.getMessage());
                    }
                });


            }
        });
    }
    //갤러리에서 사진 선택후 관련정보를 가져오기 위한 메서드
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK  && data.getData() != null){
            //사진 url 경로
             url = data.getData();

            try{

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),url);
                //회전 되어 있는 사진을 원래대로 돌리기 위한 코드
                ExifInterface exif = null;
                try{
                    //사진의 절대경로를 받기 위한 creat 메서드
                    img_path = createCopyAndReturnRealPath(this,url);
                    exif = new ExifInterface(img_path);
                }catch (IOException e){
                    e.printStackTrace();
                }

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);

                bmRotated = rotateBitmap(bitmap,orientation);

                ImageView imageView = (ImageView) findViewById(R.id.imageView3);
                imageView.setImageBitmap(bmRotated);

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //비트맵 회전시키기
    public Bitmap rotateBitmap(Bitmap bitmap,int rotate){
        Matrix matrix = new Matrix();
        switch (rotate){
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    // 파일 절대 경로 파악할때 사용하는 매서드
    @NonNull
    public static String createCopyAndReturnRealPath(@NonNull Context context,@NonNull Uri url){
        final ContentResolver contentResolver = context.getContentResolver();

        if(contentResolver==null){
            return null;
        }
        //사진의 경로 만들기
        String filePath = context.getApplicationContext().getDataDir()+ File.separator+System.currentTimeMillis();
        File file = new File(filePath);
        try{
            InputStream inputStream = contentResolver.openInputStream(url);
            if(inputStream == null){
                return null;
            }
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = inputStream.read(buf))>0)
                outputStream.write(buf,0,len);
                outputStream.close();
                inputStream.close();
        }catch (IOException ignore){
            return null;
        }
        return file.getAbsolutePath();
    }



}