package com.example.sns.main.ui.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.sns.Network.ApiClient;
import com.example.sns.Network.RetrofitService;
import com.example.sns.R;
import com.example.sns.main.ui.Myprofile.FeedimagelistDATA;
import com.example.sns.main.ui.Myprofile.MyProfileDTO;
import com.example.sns.main.ui.feed.FeedViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedFragment extends Fragment {

    private static final String TAG = "FeedFragment";
    private static final String TAG2 = "FeedFragment2";
    private static final String TAG3 = "FeedFragment3";
    private RetrofitService retrofitService;
    private final static String BASE_URL="http://192.168.0.2:8080/sns/gett2/";
    public static ApiClient ourInstance = null;

    private FeedViewModel feedViewModel;
    RecyclerView recyclerView;
    private String token;

    List<FeedimagelistDATA> dataList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);

        // db에서 feed번호와 사진들을 가져온다
        retrofitService = ourInstance.getInstance(BASE_URL,true).create(RetrofitService.class);

        Call<MyProfileDTO> call = retrofitService.getData2("1");
        call.enqueue(new Callback<MyProfileDTO>() {
            @Override
            public void onResponse(Call<MyProfileDTO> call, Response<MyProfileDTO> response) {
                if(response.isSuccessful()){
                    MyProfileDTO dto = response.body();

                    for(int i=0;i<dto.list.size();i++){
                        FeedimagelistDATA data = new FeedimagelistDATA();
                        data.setFeedno(dto.list.get(i).getFeedno());
                        Log.d(TAG,data.getFeedno()+"");
                        data.setImagename(dto.list.get(i).getImagename());
                        Log.d(TAG,data.getImagename());
                        data.setUsername(dto.list.get(i).getUsername());
                        Log.d(TAG,data.getUsername());
                        data.setFeedcontent(dto.list.get(i).getFeedcontent());
                        Log.d(TAG,data.getFeedcontent());
                        data.setMyimagename(dto.list.get(i).getMyimagename());
                        Log.d(TAG,data.getMyimagename());
                        dataList.add(data);

                    }
                /*
                    //dataList 내용물 로그 찍기
                    for(int i=0;i<dataList.size();i++){
                        Log.d(TAG3,  (String) ("dataList feedno:" + (dataList.get(0).getFeedno() != dataList.get(3).getFeedno())));
                        Log.d(TAG3,"dataList 유저이름:"+dataList.get(i).getUsername());
                        Log.d(TAG3,"dataList 피드 텍스트:"+dataList.get(i).getFeedcontent());
                        Log.d(TAG3,"dataList 프로필사진:"+dataList.get(i).getMyimagename());
                    }*/

                    //필요한 5개의 정보중에서 feed_no의 중복을 없애고 4개의 정보를 같이 저장한다.
                    //피드 번호를 1개씩 추출한다 ex> 1,1,1,1,2,2,2,3,3 -> 1,2,3 // 그리고 피드 텍스트와 내 프로필 사진 그리고 유저이름 총 4개를 가져온다
                    List<FeedimagelistDATA> dataList2 = new ArrayList<>();
                    Log.d(TAG2,"dataList의 size():"+dataList.size()+"");

                    for(int i=0;i<dataList.size();i++){
                        if(i==0){
                            FeedimagelistDATA data2 = new FeedimagelistDATA();
                            data2.setFeedno(dataList.get(i).getFeedno());
                            data2.setFeedcontent(dataList.get(i).getFeedcontent());
                            data2.setMyimagename(dataList.get(i).getMyimagename());
                            data2.setUsername(dataList.get(i).getUsername());
                            dataList2.add(data2);

                            /*Log.d(TAG2,"-----dataList2의 피드번호:"+dataList2.get(i).getFeedno()+"");
                            Log.d(TAG2,"-----dataList2의 feed 텍스트:"+dataList2.get(i).getFeedcontent());
                            Log.d(TAG2,"-----dataList2의 프로필 사진이름:"+dataList2.get(i).getMyimagename());
                            Log.d(TAG2,"-----dataList2의 유저 이름:"+dataList2.get(i).getUsername());*/


                        }
                        else if(dataList.get(i).getFeedno() != dataList.get(i-1).getFeedno()){
                            FeedimagelistDATA data2 = new FeedimagelistDATA();
                            data2.setFeedno(dataList.get(i).getFeedno());
                            data2.setFeedcontent(dataList.get(i).getFeedcontent());
                            data2.setMyimagename(dataList.get(i).getMyimagename());
                            data2.setUsername(dataList.get(i).getUsername());
                            dataList2.add(data2);

                        }
                        else{
                           /* Log.d(TAG2,"+++++++++dataList2의 피드번호:"+dataList2.get(i-1).getFeedno()+"");
                            Log.d(TAG2,"+++++++++dataList2의 feed 텍스트:"+dataList2.get(i-1).getFeedcontent());
                            Log.d(TAG2,"+++++++++dataList2의 프로필 사진이름:"+dataList2.get(i-1).getMyimagename());
                            Log.d(TAG2,"+++++++++dataList2의 유저 이름:"+dataList2.get(i-1).getUsername()); */
                           /* Log.d(TAG2,"i:"+i);
                            Log.d(TAG2,"+++++++++dataList의 피드번호:"+dataList.get(i).getFeedno()+"");
                            Log.d(TAG2,"+++++++++dataList의 feed 텍스트:"+dataList.get(i).getFeedcontent());
                            Log.d(TAG2,"+++++++++dataList의 프로필 사진이름:"+dataList.get(i).getMyimagename());
                            Log.d(TAG2,"+++++++++dataList의 유저 이름:"+dataList.get(i).getUsername());*/
                        }

                    }

                  /*  //dataList2 내용물 확인
                    for(int i=0;i<dataList2.size();i++){
                        Log.d(TAG2,"*********dataList2의 피드번호:"+dataList2.get(i).getFeedno()+"");
                        Log.d(TAG2,"*********dataList2의 feed 텍스트:"+dataList2.get(i).getFeedcontent());
                        Log.d(TAG2,"*********dataList2의 프로필 사진이름:"+dataList2.get(i).getMyimagename());
                        Log.d(TAG2,"*********dataList2의 유저 이름:"+dataList2.get(i).getUsername());
                    }*/

                    //feed_no , imagename을 따로 보관한다
                    List<FeedimagelistDATA> dataList3 = new ArrayList<>();
                    for(int i=0;i<dataList.size();i++){
                        FeedimagelistDATA data2 = new FeedimagelistDATA();
                        data2.setFeedno(dataList.get(i).getFeedno());
                        data2.setImagename(dataList.get(i).getImagename());
                        dataList3.add(data2);
                    }

                  /*  Log.d(TAG2,"dataList3의 크기:"+dataList3.size());
                    //dataList3 내용물 확인
                    for(int i=0;i<dataList3.size();i++){
                        Log.d(TAG2,"*********dataList3의 피드번호:"+dataList3.get(i).getFeedno()+"");
                        Log.d(TAG2,"*********dataList3의 이미지 이름들:"+dataList3.get(i).getImagename());
                    }*/

                    // 여기에다가 실제코드 작성
                    recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview1);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);

                    recyclerviewAdapter adapter = new recyclerviewAdapter(getContext());

                    //피드 번호 갯수만큼 addItem을 한다
                    for(int i=0;i<dataList2.size();i++){
                        int viewtype=0;
                        int count=0;

                      List<FeedimagelistDATA> dataList4 = new ArrayList<>();

                        //Log.d(TAG3,"dataList3 크기:"+dataList3.size());
                        for(int a=0;a<dataList3.size();a++){
                            FeedimagelistDATA data = new FeedimagelistDATA();

                            if((a==0 && (dataList2.get(i).getFeedno() == dataList3.get(a).getFeedno())) || (((a>=1) && dataList3.get(a-1).getFeedno() != dataList3.get(a).getFeedno()) && (dataList2.get(i).getFeedno() == dataList3.get(a).getFeedno()))){
                                count++;
                                data.setImagename(dataList3.get(a).getImagename());
                                dataList4.add(data);
                            }
                            else if((dataList2.get(i).getFeedno() == dataList3.get(a).getFeedno()) && (dataList3.get(a-1).getFeedno()==dataList3.get(a).getFeedno())){
                                count++;
                                data.setImagename(dataList3.get(a).getImagename());
                                dataList4.add(data);

                                if(((a<dataList3.size()-1) && (dataList3.get(a).getFeedno()!=dataList3.get(a+1).getFeedno())))
                                {
                                    break;
                                }
                            }
                        }
                        //Log.d(TAG3,"feed번호:"+dataList2.get(i).getFeedno()+"이고"+i+"번째 count:"+count);
                        if(count==0){
                            viewtype=0;
                        }
                        else if(count!=0){
                            viewtype=1;
                        }
                        adapter.addItem(new item(dataList2.get(i).getUsername(),dataList2.get(i).getFeedcontent(),dataList2.get(i).getMyimagename(),dataList4, viewtype));
                        Log.d(TAG3,i+"번째"+"dataList4의 크기:"+dataList4.size());
                    }

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<MyProfileDTO> call, Throwable t) {
                Toast.makeText(getContext(),"서버에서 응답을 받지 못하였습니다",Toast.LENGTH_SHORT).show();
            }
        });

        

        /*adapter.addItem(new item("홍길동","안녕",R.mipmap.ic_cat_round, R.drawable.doughnut, 0));
        adapter.addItem(new item("철수","하이",R.mipmap.ic_lyan1_round, R.drawable.doughnut, 0));
        adapter.addItem(new item("영희","우왕",R.mipmap.ic_lyan2_round, R.drawable.doughnut, 0));
        adapter.addItem(new item("바나나","굳",R.mipmap.ic_lyan3_round, new int[]{R.drawable.lyan3, R.drawable.doughnut,R.drawable.lyan6}, 1));
        adapter.addItem(new item("원숭이","ㅋ",R.mipmap.ic_lyan4_round, R.drawable.doughnut, 0));*/

        //recyclerView.setAdapter(adapter);

        return root;
    }
}