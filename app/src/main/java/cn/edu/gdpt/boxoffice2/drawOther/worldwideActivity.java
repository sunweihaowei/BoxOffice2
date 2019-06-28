package cn.edu.gdpt.boxoffice2.drawOther;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdpt.boxoffice2.R;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment10;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment2;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment3;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment4;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment5;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment6;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment7;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment8;
import cn.edu.gdpt.boxoffice2.WorldWideFragment.BlankFragment9;
import cn.edu.gdpt.boxoffice2.bean.WorldWideBean;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class worldwideActivity extends AppCompatActivity {
    private List<String> listDate=new ArrayList<>();
    private TabLayout tb_ww_title;
    private ViewPager vp_ww_content;
    private String TAG;
    private List<Fragment> fragmentList=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                String json= (String) msg.obj;
                Gson gson=new Gson();
                WorldWideBean worldWideBean = gson.fromJson(json, WorldWideBean.class);
                String date = worldWideBean.getShowapi_res_body().getMovieGlobalRank().get(0).getDate();
                for (int i=0;i<worldWideBean.getShowapi_res_body().getMovieGlobalRank().size();i++){
                    String date1 = worldWideBean.getShowapi_res_body().getMovieGlobalRank().get(i).getDate();
                    listDate.add(date1);
                }
                for (int i=0;i<listDate.size();i++){
                    tb_ww_title.addTab(tb_ww_title.newTab().setText(listDate.get(i)));
                }
                tb_ww_title.setupWithViewPager(vp_ww_content);
                vp_ww_content.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public Fragment getItem(int i) {
                        return fragmentList.get(i);
                    }

                    @Override
                    public int getCount() {
                        return fragmentList.size();
                    }

                    @Nullable
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return listDate.get(position);
                    }
                });

            }else {
                Log.d(TAG,"失败");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worldwide);
        initView();
        fragmentList.add(new BlankFragment());
        fragmentList.add(new BlankFragment2());
        fragmentList.add(new BlankFragment3());
        fragmentList.add(new BlankFragment4());
        fragmentList.add(new BlankFragment5());
        fragmentList.add(new BlankFragment6());
        fragmentList.add(new BlankFragment7());
        fragmentList.add(new BlankFragment8());
        fragmentList.add(new BlankFragment9());
        fragmentList.add(new BlankFragment10());
      new Thread(){
          @Override
          public void run() {
              super.run();
              OkHttpClient okHttpClient=new OkHttpClient();
              Request request=new Request.Builder().url("https://route.showapi.com/1821-7?showapi_appid=97930&showapi_timestamp=20190628091133&showapi_sign=9756795fe56241b2bedfb89cb8bcc1b9").build();
              Call call=okHttpClient.newCall(request);
              call.enqueue(new Callback() {
                  @Override
                  public void onFailure(@NotNull Call call, @NotNull IOException e) {
                      Log.d(TAG,"失败");
                  }

                  @Override
                  public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                      String string = response.body().string();
                      Message message=new Message();
                      message.what=1;
                      message.obj=string;
                      handler.sendMessage(message);

                  }
              });
          }
      }.start();






    }

    private void initView() {
        tb_ww_title = (TabLayout) findViewById(R.id.tb_ww_title);
        vp_ww_content = (ViewPager) findViewById(R.id.vp_ww_content);
    }
}
