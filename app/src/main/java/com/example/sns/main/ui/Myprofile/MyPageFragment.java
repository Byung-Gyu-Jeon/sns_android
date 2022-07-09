package com.example.sns.main.ui.Myprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.sns.FeedAdd;
import com.example.sns.Network.ApiClient;
import com.example.sns.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.MainActivity;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.example.sns.main.ui.Myprofile.MyprofileAdapter;
import com.example.sns.main.ui.Myprofile.MyprofileUpdate;
import com.example.sns.main.ui.feed.Mainprofileadd;
import com.example.sns.main.ui.mypage.MyPageViewModel;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;



public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";
    private MyPageViewModel myPageViewModel;
    private MyprofileAdapter adapter;
    //프로필 사진이랑 이름,소개글 db에서 가져오기
    private final static String BASE_URL="http://59.13.221.12:80/sns/get/";
    //게시글 추가할때 올린 사진들 가져오기
    private final static String BASE_URL2="http://59.13.221.12:80/sns/gett/";
    private RetrofitService retrofitService;
    public static ApiClient ourInstance = null;
    String ptoname= null;

    //리사이클러뷰
    RecyclerView recyclerView;








    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        myPageViewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);

        List<FeedimagelistDATA> dataList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recycler2);


        //이름,자기소개 텍스트뷰
        TextView text = (TextView) root.findViewById(R.id.textView38);
        TextView text1 = (TextView) root.findViewById(R.id.textView39);

        //이미지뷰
        ImageView imageView = (ImageView) root.findViewById(R.id.imageView2);

        //+버튼 텍스트뷰
        TextView textView = (TextView) root.findViewById(R.id.textView12);

        //게시물 개수
        TextView textView2 = (TextView) root.findViewById(R.id.textView7);


        //프로필 사진이랑 자기 소개 가져오기
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
                    userIntroduce = dto.getUserIntroduce();
                    ptoname = dto.getPtoname();
                    text.setText(username);
                    text1.setText(userIntroduce);
                    Log.d(TAG,"사진이름:"+ptoname);

                    //사진 불러오기
                    Glide.with(getContext()).load("http://59.13.221.12:80/sns/download?fileName=" + ptoname).apply(new RequestOptions().circleCrop())
                            .into(imageView);
                }
                else{
                    Toast.makeText(getContext(),"내 프로필 화면에서 서버에서 응답을 제대로 받지 못했습니다", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {

            }
        });

        //게시글 추가화면에서 올린 사진이름들 가져오기 - (gilde를 이용해서 받아온 사진이름을 뒤에 붙여서 사진을 가져온다)
        retrofitService = ourInstance.getInstance(BASE_URL2,true).create(RetrofitService.class);



        Call<MyProfileDTO> call2 = retrofitService.getData("2");
        call2.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO> call, Response<MyProfileDTO> response) {


                if(response.isSuccessful()){
                    MyProfileDTO dto2 = response.body();

                    Log.d(TAG,"서버에서 보낸 데이터"+dto2.list.toString());
                    for(int i=0;i<dto2.list.size();i++){

                        if(i==0){
                            FeedimagelistDATA data = new FeedimagelistDATA();
                            data.setFeedno(dto2.list.get(i).getFeedno());
                           // Log.d(TAG,data.getFeedno()+"");
                            data.setImagename(dto2.list.get(i).getImagename());
                            //Log.d(TAG,data.getImagename());
                            dataList.add(data);
                        }

                        else if(i>=1 && (dto2.list.get(i).getFeedno() != dto2.list.get(i-1).getFeedno())){
                            FeedimagelistDATA data = new FeedimagelistDATA();
                            data.setFeedno(dto2.list.get(i).getFeedno());
                           // Log.d(TAG,data.getFeedno()+"");
                            data.setImagename(dto2.list.get(i).getImagename());
                           // Log.d(TAG,data.getImagename());
                            dataList.add(data);

                        }
                      for(FeedimagelistDATA data: dataList){
                          Log.d(TAG,data.getFeedno()+"");
                          Log.d(TAG,data.getImagename());
                      }

                    }
                    //게시글 갯수를 표현 해준다
                    textView2.setText(dataList.size()+"");
                    MyPageFragmentAdapter  myPageFragmentAdapter = new MyPageFragmentAdapter(dataList,getContext());
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
                    recyclerView.setAdapter(myPageFragmentAdapter);
                }

            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {
                  Toast.makeText(getContext(),"서버로부터 응답을 받지 못하였습니다",Toast.LENGTH_SHORT).show();
            }
        });

        // + 버튼을 누룰시 게시글 추가 화면으로 이동
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Mainprofileadd.class);
                startActivity(intent);
            }
        });

        //프로필편집버튼
        Button btn = (Button) root.findViewById(R.id.button7);
        btn.setOnClickListener(new Button.OnClickListener() {
          @Override
            public void onClick(View view){
              Intent intent = new Intent(getActivity(), MyprofileUpdate.class);
              startActivityForResult(intent,0);
          }

       });



        return root;
    }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

            if(requestCode==RESULT_OK){
               Toast.makeText(getContext(),"프로필 수정화면에서 프로필 화면으로 이동 성공",Toast.LENGTH_SHORT).show();

                //이름,자기소개 텍스트뷰
                TextView text = (TextView) getView().findViewById(R.id.textView38);
                TextView text1 = (TextView) getView().findViewById(R.id.textView39);

                //이미지뷰
                ImageView imageView = (ImageView) getView().findViewById(R.id.imageView2);

               //이미지와 텍스트를 서버에서 가져오기 위한 코드
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
                            userIntroduce = dto.getUserIntroduce();
                            ptoname = dto.getPtoname();
                            text.setText(username);
                            text1.setText(userIntroduce);
                            Log.d(TAG,"사진이름:"+ptoname);

                            //사진 불러오기
                            Glide.with(getContext()).load("http://59.13.221.12:80/sns/download?fileName=" + ptoname).apply(new RequestOptions().circleCrop())
                                    .into(imageView);
                        }
                        else{
                            Toast.makeText(getContext(),"내 프로필 화면에서 서버에서 응답을 제대로 받지 못했습니다", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<MyProfileDTO> call, Throwable t) {

                    }
                });

            }

    }
}