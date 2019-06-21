package cn.edu.gdpt.boxoffice2;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.gdpt.boxoffice2.Adapter.MyAdapter;
import cn.edu.gdpt.boxoffice2.Adapter.pagerAdapter;
import cn.edu.gdpt.boxoffice2.bean.main;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private TextView tv_sumBoxOffice;
    private ListView lv;
    private TabLayout tb;
    private ViewPager vp;
    private LinearLayout ll_content;
    private LinearLayout ll_main;
    private pagerAdapter pagerAdapter;
    private List<View> viewList=new ArrayList<>();
    private List<String> nameList=new ArrayList<>();
    private String[] names=new String[]{"饼状图","条形图","线形图"};
    /*private PullToRefreshView pulltorefreshView;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        View view=View.inflate(MainActivity.this,R.layout.pie_item,null);
        viewList.add(view);
        View view1=View.inflate(MainActivity.this,R.layout.hc_line,null);
        viewList.add(view1);
        View view2=View.inflate(MainActivity.this,R.layout.hc_c,null);
        viewList.add(view2);
        for (int i=0;i<3;i++){
            nameList.add(names[i]);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    String json = (String) msg.obj;
                    Gson gson = new Gson();

                    main main = gson.fromJson(json, main.class);
                    final List<Map<String, Object>> mapList = new ArrayList<>();
                    Map<String, Object> map;
                    String realTimeBoxOffice = main.getShowapi_res_body().getRealTimeRank().getRealTimeBoxOffice();
                    for (int i = 0; i < 10; i++) {
                        String boxOffice = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get(i).getBoxOffice();
                        float BoxPercent = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get(i).getBoxPercent();
                        String Name = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get(i).getName();
                        String Rank = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get(i).getRank();
                        String ShowDay = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get(i).getShowDay();
                        String SumBoxOffice = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get(i).getSumBoxOffice();
                        map = new HashMap<>();
                        map.put("boxOffice", boxOffice);
                        map.put("BoxPercent", BoxPercent);
                        map.put("Name", Name);
                        map.put("Rank", Rank);
                        map.put("ShowDay", ShowDay);
                        map.put("SumBoxOffice", SumBoxOffice);
                        mapList.add(map);
                    }
                    tv_sumBoxOffice.setText(realTimeBoxOffice);
                    MyAdapter adapter = new MyAdapter(MainActivity.this, mapList);
                    lv.setAdapter(adapter);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ll_content.setVisibility(View.VISIBLE);
                            ll_main.setVisibility(View.GONE);
                        }
                    });
                }
            }
        };
        getjson();
        initcontent();
    }

    private void initcontent() {
        tb.addTab(tb.newTab().setText(nameList.get(0)));
        tb.addTab(tb.newTab().setText(nameList.get(1)));
        tb.addTab(tb.newTab().setText(nameList.get(2)));
        pagerAdapter=new pagerAdapter( MainActivity.this,viewList,nameList);
        vp.setAdapter(pagerAdapter);
        tb.setupWithViewPager(vp);
    }

    private void getjson() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url("https://route.showapi.com/1821-1?showapi_appid=97930&showapi_timestamp=20190617123611&showapi_sign=9756795fe56241b2bedfb89cb8bcc1b9")
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(null, "获取网络失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.obj = string;
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }
    private void initView() {
        tv_sumBoxOffice = (TextView) findViewById(R.id.tv_sumBoxOffice);
        lv = (ListView) findViewById(R.id.lv);
        /*pulltorefreshView = (PullToRefreshView) findViewById( R.id.pulltorefreshView );*/
        tb = (TabLayout) findViewById(R.id.tb);
        vp = (ViewPager) findViewById(R.id.vp);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
    }
}