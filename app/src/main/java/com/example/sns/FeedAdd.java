package com.example.sns;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.example.sns.main.ui.feed.MainprofileaddAdapter;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class FeedAdd extends Fragment {

    private static final String TAG = "FeedAdd";
    private final static String BASE_URL="http://192.168.0.2:8080/sns/get/";
    private RetrofitService retrofitService;
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
    //게시글
    EditText editText;
    //게시 버튼
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feed_add, container, false);
        //프로필 이미지
        ImageView imageView = (ImageView) root.findViewById(R.id.imageView7);
        //이름
        TextView textView = (TextView) root.findViewById(R.id.textView25);

        //리사이클러뷰
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.MyprofileaddrecyclerView);
        //카메라 사진
        ImageButton imageButton1 = (ImageButton) root.findViewById(R.id.imageButton5);


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



                    //사진 불러오기
                    Glide.with(getContext()).load("http://192.168.0.2:8080/sns/download?fileName=" + ptoname).apply(new RequestOptions().circleCrop())
                            .into(imageView);
                }
                else{
                    Toast.makeText(getContext(),"게시글 추가 화면에서 응답을 제대로 받지 못했습니다", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {

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



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        recyclerView = getView().findViewById(R.id.MyprofileaddrecyclerView);
        //사진을 선택하지 않았을경우
        if(requestCode==3) {
            if (data == null) {
                Toast.makeText(getContext(), "이미지를 선택하지 않았습니다", Toast.LENGTH_LONG).show();
            } else {
                //이미지를 1개선택한 경우
                if (data.getClipData() == null) {
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);

                    adapter = new MainprofileaddAdapter(uriList, getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                }
                //이미지를 여러장 선택한 경우
                else {
                    ClipData clipData = data.getClipData();
                    //사진이 10장 이상인 경우
                    if (clipData.getItemCount() > 10) {
                        Toast.makeText(getContext(), "사진은 10장까지만 선택가능합니다", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            //이미지의 uri를 가져온다
                            Uri imageUri = clipData.getItemAt(i).getUri();
                            //이미지 절대경로를 가져온다 why? -> 스프링으로 전송하기 위해서
                            img_path2 = createCopyAndReturnRealPath(getContext(),imageUri);
                            try {
                                //ArrayList에 url, 절대 경로를 각각 저장
                                uriList.add(imageUri);
                                img_path.add(img_path2);
                            } catch (Exception e) {
                                Log.d(TAG, "사진 불러오기 실패");
                            }
                        }
                        adapter = new MainprofileaddAdapter(uriList, getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
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