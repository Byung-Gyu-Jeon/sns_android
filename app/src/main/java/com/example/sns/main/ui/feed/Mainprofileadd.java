package com.example.sns.main.ui.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//게시글 추가 화면
public class Mainprofileadd extends AppCompatActivity {

    private static final String TAG = "Mainprodileadd";
    private final static String BASE_URL="http://192.168.0.2:8080/sns/get/";
    private final static String BASE_URL2="http://192.168.0.2:8080/sns/postss/";
    private RetrofitService retrofitService;
    private RetrofitService retrofitService2;
    public static ApiClient ourInstance = null;
    String ptoname= null;


    //이미지의 URL를 담을 객체
    ArrayList<Uri> uriList = new ArrayList<>();
    //이미지의 절대경로를 담을 객체
    ArrayList<String> img_path = new ArrayList<>();
    //이미지 절대경로
    String img_path2;
    //어텝터
    MainprofileaddAdapter adapter;
    RecyclerView recyclerView;
    //게시글 작성 텍스트
    EditText editText;
    //보낼 사진들
    List<MultipartBody.Part> imgptos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainprofileadd);

        //프로필 이미지
        ImageView imageView = (ImageView) findViewById(R.id.image7);
        //이름
        TextView textView = (TextView) findViewById(R.id.text25);
        //뒤로가기 버튼
        ImageButton imageButton = (ImageButton) findViewById(R.id.image4);
        //리사이클러뷰
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.MyprofileaddrecyclerView2);
        //카메라 사진
        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imagebtn5);
        //게시 버튼
       Button button = (Button) findViewById(R.id.btn11);
       //게시글 작성 텍스트
        EditText editText = (EditText) findViewById(R.id.mypro2);


        // 내 프로필 사진과 이름을 서버에서 불러오기 위한 작업
        retrofitService = ourInstance.getInstance(BASE_URL,true).create(RetrofitService.class);

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

                    ptoname = dto.getPtoname();
                    textView.setText(username);

                    Log.d(TAG,"사진이름:"+ptoname);

                    //사진 불러오기
                    Glide.with(getApplicationContext()).load("http://192.168.0.2:8080/sns/download?fileName=" + ptoname).apply(new RequestOptions().circleCrop())
                            .into(imageView);
                }
                else{
                    Toast.makeText(getApplicationContext(),"게시글 추가 화면에서 응답을 제대로 받지 못했습니다", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {

            }
        });

        //뒤로가기 버튼 눌렀을때 내 프로필 화면으로 돌아간다
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 카메라 사진을 눌렀을때 갤러리로 이동 사진 여러개 클릭 가능
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_PICK);
               intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
               intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE,true);
               intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(intent,3);
            }
        });
        //게시버튼을 눌렀을때 다시 내 프로필 화면으로 돌아간다
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //게시글 텍스트를 가져온다
                String feedtext = editText.getText().toString();
                RequestBody feedtext2 = RequestBody.create(MediaType.parse("text/plain"),feedtext);
                HashMap<String, RequestBody> profilesend = new HashMap<>();
                profilesend.put("feedtext",feedtext2);

                //ArrayList에 저장된 사진의 갯수만큼 반복한다
                int arrysize = img_path.size();
                for(int i=0;i<arrysize;i++){
                    File file = new File(img_path.get(i));
                    RequestBody feedpto = RequestBody.create(MediaType.parse("image/jpeg"),file);
                    String filename = UUID.randomUUID().toString()+".jpg";
                    imgptos.add(MultipartBody.Part.createFormData("photo",filename,feedpto));
                }

                retrofitService2 = ourInstance.getInstance(BASE_URL2,true).create(RetrofitService.class);
                Call<MyFeedaddDATA> call3 = retrofitService2.postData3(profilesend,imgptos);
                call3.enqueue(new Callback<MyFeedaddDATA>() {

                    @Override
                    public void onResponse(Call<MyFeedaddDATA> call, Response<MyFeedaddDATA> response) {
                        Log.d(TAG,"게시글 추가화면 사진 텍스트를 스프링으로 전송 성공\n");

                    }

                    @Override
                    public void onFailure(Call<MyFeedaddDATA> call, Throwable t) {
                        Log.d(TAG,"게시글 추가화면 사진 텍스트를 스프링으로 전송 실패하였습니다 onFailure: "+ t.getMessage());
                    }
                });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         recyclerView = findViewById(R.id.MyprofileaddrecyclerView2);
        //사진을 선택하지 않았을경우
        if(requestCode==3) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다", Toast.LENGTH_LONG).show();
            } else {
                //이미지를 1개선택한 경우
                if (data.getClipData() == null) {
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);

                    adapter = new MainprofileaddAdapter(uriList, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                }
                //이미지를 여러장 선택한 경우
                else {
                    ClipData clipData = data.getClipData();
                    //사진이 10장 이상인 경우
                    if (clipData.getItemCount() > 10) {
                        Toast.makeText(getApplicationContext(), "사진은 10장까지만 선택가능합니다", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            //이미지의 uri를 가져온다
                            Uri imageUri = clipData.getItemAt(i).getUri();
                            //이미지 절대경로를 가져온다 why? -> 스프링으로 전송하기 위해서
                            img_path2 = createCopyAndReturnRealPath(this,imageUri);
                            try {
                                //ArrayList에 url, 절대 경로를 각각 저장
                                uriList.add(imageUri);
                                img_path.add(img_path2);
                            } catch (Exception e) {
                                Log.d(TAG, "사진 불러오기 실패");
                            }
                        }
                        adapter = new MainprofileaddAdapter(uriList, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                    }
                }
            }
        }
    }
    // 파일 절대 경로 파악할때 사용하는 매서드
    @NonNull
    public static String createCopyAndReturnRealPath(@NonNull Context context, @NonNull Uri url){
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