package cn.edu.gdpt.boxoffice2;

import android.graphics.Color;
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
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
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
    private PieChartView pcv;
    private View view,view1,view2;


    private int[] pieData=new int[]{
            10,20,30,35,5
    };
    private int[] color=new int[]{
            Color.parseColor( "#356fb3" ),
            Color.parseColor( "#b53633" ),
            Color.parseColor( "#86aa3d" ),
            Color.parseColor( "#6a4b90" ),
            Color.parseColor( "#2e9cba" )
    };
    List<SliceValue> sliceValues=new ArrayList <>(  );
    private String[] stateChar={"高等教育1","高等教育2","高等教育3","高等教育4","高等教育5"};
    private PieChartData data;



    /*private PullToRefreshView pulltorefreshView;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        view=View.inflate(MainActivity.this,R.layout.pie_item,null);
        viewList.add(view);
        view1=View.inflate(MainActivity.this,R.layout.hc_line,null);
        viewList.add(view1);
        view2=View.inflate(MainActivity.this,R.layout.hc_c,null);
        viewList.add(view2);
        pcv=view.findViewById( R.id.pie );
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



                    for (int i=0;i<pieData.length;i++){
                        SliceValue sliceValue=new SliceValue( pieData[i],color[i] );
                        sliceValues.add( sliceValue );
                    }
                    data=new PieChartData(  );
                    data.setCenterCircleColor( Color.WHITE );//设置中心圆的颜色
                    data.setHasCenterCircle( true );//设置是否有中心圆

                    data.setHasLabels( true );//是否有数据显示
                    data.setCenterCircleScale( 0.5f );//设置中心相对于全部的占比大小
                    data.setCenterText1( "数据" );//设置center默认数据
                    data.setHasLabelsOnlyForSelected( true );//这个表示是否选中时才显示数据，true为选中时才有数据
                    data.setHasLabelsOutside( false );//这个表示数据是否显示在外面
                    data.setValues(sliceValues);//这个表示为PieChartData建立数据（List类型数据）

                    pcv.setPieChartData( data );
                    pcv.setValueSelectionEnabled( true );//是否可以选择，true为可以且会变大
                    pcv.setAlpha( 0.9f );//透明度
                    pcv.setCircleFillRatio( 1 );//设置相对于view大小，默认为全部
                    final PieChartOnValueSelectListener pieChartOnValueSelectListener=new PieChartOnValueSelectListener() {
                        @Override
                        public void onValueSelected(int i, SliceValue sliceValue) {//i表示点击的那一项，slicevalue为点击的那个的值
                            data.setCenterText1( stateChar[i] );
                            data.setCenterText2( sliceValue.getValue()+"("+calPercent( i )+")" );
                        }

                        @Override
                        public void onValueDeselected() {

                        }
                    };
                    pcv.setOnValueTouchListener( pieChartOnValueSelectListener );

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
    private String calPercent(int i){//有参数就要return,
        String result="";
        int sum=0;
        for (int j=0;j<pieData.length;j++){//这里计算出pieData的全部值的和，sum
            sum+=pieData[j];
        }
        result=String.format( "%.2f",(float)pieData[i]*100/sum )+"%";
        return result;
    }

}