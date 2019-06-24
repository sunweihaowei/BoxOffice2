package cn.edu.gdpt.boxoffice2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import cn.edu.gdpt.boxoffice2.drawOther.dayActivity;
import cn.edu.gdpt.boxoffice2.drawOther.mouthActivity;
import cn.edu.gdpt.boxoffice2.drawOther.weekActivity;
import cn.edu.gdpt.boxoffice2.drawOther.weekendActivity;
import cn.edu.gdpt.boxoffice2.drawOther.worldwideActivity;
import cn.edu.gdpt.boxoffice2.drawOther.yearActivity;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/*
总结：
数据类型的转换
float.valueof();这个是用来转换的，其他的会报错
注意细节，是什么类型的，double类型还是int类型，还是其他的
HelloChartView不能用double类型
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler handler;
    private TextView tv_sumBoxOffice;
    private ListView lv;
    private TabLayout tb;
    private ViewPager vp;
    private LinearLayout ll_content;
    private LinearLayout ll_main;
    private pagerAdapter pagerAdapter;
    private List <View> viewList = new ArrayList <>();
    private List <String> nameList = new ArrayList <>();
    private String[] names = new String[]{"饼状图", "条形图"};
    private PieChartView pcv;
    private ColumnChartView ccv;
    private View view, view1, view2;


    private float[] pieData;
    private int[] color = new int[]{
            Color.parseColor( "#356fb3" ),
            Color.parseColor( "#b53633" ),
            Color.parseColor( "#86aa3d" ),
            Color.parseColor( "#6a4b90" ),
            Color.parseColor( "#2e9cba" ),
            Color.parseColor( "#356fb3" ),
            Color.parseColor( "#b53633" ),
            Color.parseColor( "#86aa3d" ),
            Color.parseColor( "#6a4b90" ),
            Color.parseColor( "#2e9cba" )
    };
    List <SliceValue> sliceValues = new ArrayList <>();
    private String[] stateChar;
    private PieChartData data;


    private String[] year;
    List <AxisValue> axisXValues = new ArrayList <>();
    int[] columnY = {
            1000,
            500,
            400,
            300,
            200,
            100
    };
    List <AxisValue> axisYValues = new ArrayList <>();
    float[] columnValues;
    int[] columnColor = {
            Color.parseColor( "#356fb3" ),
            Color.parseColor( "#b53633" ),
            Color.parseColor( "#86aa3d" ),
            Color.parseColor( "#6a4b90" ),
            Color.parseColor( "#2e9cba" ),
            Color.parseColor( "#356fb3" ),
            Color.parseColor( "#b53633" ),
            Color.parseColor( "#86aa3d" ),
            Color.parseColor( "#6a4b90" ),
            Color.parseColor( "#2e9cba" )
    };
    List <Column> columns = new ArrayList <>();
    private Button back;
    private Button dayBoxOffice;
    private Button weekBoxOffice;
    private Button weekendBoxOffice;
    private Button monthBoxOffice;
    private Button yearBoxOffice;
    private Button worldwideBoxOffice;
    private DrawerLayout dl;


    /*private PullToRefreshView pulltorefreshView;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView( R.layout.activity_main );
        initView();

        initVpAndtb();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage( msg );

                if (msg.what == 1) {
                    String json = (String) msg.obj;
                    Gson gson = new Gson();

                    main main = gson.fromJson( json, main.class );
                    final List <Map <String, Object>> mapList = new ArrayList <>();
                    Map <String, Object> map;
                    String realTimeBoxOffice = main.getShowapi_res_body().getRealTimeRank().getRealTimeBoxOffice();
                    for (int i = 0; i < 10; i++) {
                        String boxOffice = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get( i ).getBoxOffice();
                        double BoxPercent = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get( i ).getBoxPercent();
                        String Name = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get( i ).getName();
                        String Rank = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get( i ).getRank();
                        String ShowDay = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get( i ).getShowDay();
                        String SumBoxOffice = main.getShowapi_res_body().getRealTimeRank().getMovieRank().get( i ).getSumBoxOffice();
                        map = new HashMap <>();
                        map.put( "boxOffice", boxOffice );
                        map.put( "BoxPercent", BoxPercent );
                        map.put( "Name", Name );
                        map.put( "Rank", Rank );
                        map.put( "ShowDay", ShowDay );
                        map.put( "SumBoxOffice", SumBoxOffice );
                        mapList.add( map );
                    }
                    year = new String[]{
                            mapList.get( 0 ).get( "Name" ).toString(),
                            mapList.get( 1 ).get( "Name" ).toString(),
                            mapList.get( 2 ).get( "Name" ).toString(),
                            mapList.get( 3 ).get( "Name" ).toString(),
                            mapList.get( 4 ).get( "Name" ).toString(),
                            mapList.get( 5 ).get( "Name" ).toString(),
                            mapList.get( 6 ).get( "Name" ).toString(),
                            mapList.get( 7 ).get( "Name" ).toString(),
                            mapList.get( 8 ).get( "Name" ).toString(),
                            mapList.get( 9 ).get( "Name" ).toString(),
                    };
                    stateChar = new String[]{
                            mapList.get( 0 ).get( "Name" ).toString(),
                            mapList.get( 1 ).get( "Name" ).toString(),
                            mapList.get( 2 ).get( "Name" ).toString(),
                            mapList.get( 3 ).get( "Name" ).toString(),
                            mapList.get( 4 ).get( "Name" ).toString(),
                            mapList.get( 5 ).get( "Name" ).toString(),
                            mapList.get( 6 ).get( "Name" ).toString(),
                            mapList.get( 7 ).get( "Name" ).toString(),
                            mapList.get( 8 ).get( "Name" ).toString(),
                            mapList.get( 9 ).get( "Name" ).toString()
                    };
                    float value = Float.valueOf( mapList.get( 0 ).get( "BoxPercent" ).toString() );
                    float value1 = Float.valueOf( mapList.get( 1 ).get( "BoxPercent" ).toString() );
                    float value2 = Float.valueOf( mapList.get( 2 ).get( "BoxPercent" ).toString() );
                    float value3 = Float.valueOf( mapList.get( 3 ).get( "BoxPercent" ).toString() );
                    float value4 = Float.valueOf( mapList.get( 4 ).get( "BoxPercent" ).toString() );
                    float value5 = Float.valueOf( mapList.get( 5 ).get( "BoxPercent" ).toString() );
                    float value6 = Float.valueOf( mapList.get( 6 ).get( "BoxPercent" ).toString() );
                    float value7 = Float.valueOf( mapList.get( 7 ).get( "BoxPercent" ).toString() );
                    float value8 = Float.valueOf( mapList.get( 8 ).get( "BoxPercent" ).toString() );
                    float value9 = Float.valueOf( mapList.get( 9 ).get( "BoxPercent" ).toString() );


                    pieData = new float[]{
                            value,
                            value1,
                            value2,
                            value3,
                            value4,
                            value5,
                            value6,
                            value7,
                            value8,
                            value9
                    };
                    tv_sumBoxOffice.setText( realTimeBoxOffice );
                    for (int i = 0; i < pieData.length; i++) {
                        SliceValue sliceValue = new SliceValue( pieData[i], color[i] );
                        sliceValues.add( sliceValue );
                    }
                    data = new PieChartData();
                    data.setCenterCircleColor( Color.WHITE );//设置中心圆的颜色
                    data.setHasCenterCircle( true );//设置是否有中心圆

                    data.setHasLabels( true );//是否有数据显示
                    data.setCenterCircleScale( 0.5f );//设置中心相对于全部的占比大小
                    data.setCenterText1( "数据" );//设置center默认数据
                    data.setHasLabelsOnlyForSelected( true );//这个表示是否选中时才显示数据，true为选中时才有数据
                    data.setHasLabelsOutside( false );//这个表示数据是否显示在外面
                    data.setValues( sliceValues );//这个表示为PieChartData建立数据（List类型数据）
                    data.setCenterText1FontSize( 20 );

                    pcv.setPieChartData( data );
                    pcv.setValueSelectionEnabled( true );//是否可以选择，true为可以且会变大
                    pcv.setAlpha( 0.9f );//透明度
                    pcv.setCircleFillRatio( 1 );//设置相对于view大小，默认为全部

                    final PieChartOnValueSelectListener pieChartOnValueSelectListener = new PieChartOnValueSelectListener() {
                        @Override
                        public void onValueSelected(int i, SliceValue sliceValue) {//i表示点击的那一项，slicevalue为点击的那个的值
                            data.setCenterText1( stateChar[i] );
                            data.setCenterText2( sliceValue.getValue() + "(" + calPercent( i ) + ")" );
                        }

                        @Override
                        public void onValueDeselected() {
                        }
                    };
                    pcv.setOnValueTouchListener( pieChartOnValueSelectListener );
                    MyAdapter adapter = new MyAdapter( MainActivity.this, mapList );
                    lv.setAdapter( adapter );


                    float bo = Float.parseFloat( mapList.get( 0 ).get( "boxOffice" ).toString() );
                    float bo1 = Float.parseFloat( mapList.get( 1 ).get( "boxOffice" ).toString() );
                    float bo2 = Float.parseFloat( mapList.get( 2 ).get( "boxOffice" ).toString() );
                    float bo3 = Float.parseFloat( mapList.get( 3 ).get( "boxOffice" ).toString() );
                    float bo4 = Float.parseFloat( mapList.get( 4 ).get( "boxOffice" ).toString() );
                    float bo5 = Float.parseFloat( mapList.get( 5 ).get( "boxOffice" ).toString() );
                    float bo6 = Float.parseFloat( mapList.get( 6 ).get( "boxOffice" ).toString() );
                    float bo7 = Float.parseFloat( mapList.get( 7 ).get( "boxOffice" ).toString() );
                    float bo8 = Float.parseFloat( mapList.get( 8 ).get( "boxOffice" ).toString() );
                    float bo9 = Float.parseFloat( mapList.get( 9 ).get( "boxOffice" ).toString() );//2990.33要用这个Float.parseFloat，39.99用Float.valueOf
                    columnValues = new float[]{
                            bo,
                            bo1,
                            bo2,
                            bo3,
                            bo4,
                            bo5,
                            bo6,
                            bo7,
                            bo8,
                            bo9,
                    };
                    for (int i = 0; i < 5; i++) {
                        axisXValues.add( new AxisValue( i ).setLabel( year[i] ) );
                    }
                    for (int i = 0; i < 6; i++) {
                        axisYValues.add( new AxisValue( i ).setValue( columnY[i] ) );
                    }
                    Axis axisY = new Axis( axisYValues );
                    axisY.setMaxLabelChars(3);
                    axisY.setTextSize( 10 );
                    axisY.setTextColor( Color.BLACK );
                    axisY.setHasLines( true );
                    Axis axisX = new Axis( axisXValues );
                    axisX.setTextColor( Color.BLACK );
                    axisX.setTextSize( 10 );
                    axisX.setLineColor( Color.BLACK );
                    axisX.setHasLines( false );
                    for (int i = 0; i < 5; i++) {
                        List <SubcolumnValue> subcolumnValues = new ArrayList <>();
                        subcolumnValues.add( new SubcolumnValue( columnValues[i], columnColor[i] ) );
                        columns.add( new Column( subcolumnValues ).setHasLabelsOnlyForSelected( true ) );
                    }

                    ColumnChartData columnChartData = new ColumnChartData( columns );
                    columnChartData.setAxisXBottom( axisX );
                    columnChartData.setAxisYLeft( axisY );
                    columnChartData.setValueLabelTextSize( 20 );
                    ccv.setColumnChartData( columnChartData );
                    lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                            ll_content.setVisibility( View.VISIBLE );
                            ll_main.setVisibility( View.GONE );
                        }
                    } );
                }
            }
        };
        getjson();
        initcontent();
    }
    private void initVpAndtb() {
        view = View.inflate( MainActivity.this, R.layout.pie_item, null );
        viewList.add( view );
        view1 = View.inflate( MainActivity.this, R.layout.hc_c, null );
        viewList.add( view1 );
        pcv = view.findViewById( R.id.pie );
        ccv = view1.findViewById( R.id.ccv );
        for (int i = 0; i < 2; i++) {
            nameList.add( names[i] );
        }
    }
    private void initcontent() {
        tb.addTab( tb.newTab().setText( nameList.get( 0 ) ) );
        tb.addTab( tb.newTab().setText( nameList.get( 1 ) ) );
        pagerAdapter = new pagerAdapter( MainActivity.this, viewList, nameList );
        vp.setAdapter( pagerAdapter );
        tb.setupWithViewPager( vp );
    }
    private void getjson() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url( "https://route.showapi.com/1821-1?showapi_appid=97930&showapi_timestamp=20190617123611&showapi_sign=9756795fe56241b2bedfb89cb8bcc1b9" )
                        .build();
                Call call = okHttpClient.newCall( request );
                call.enqueue( new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e( null, "获取网络失败" );
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.obj = string;
                        handler.sendMessage( message );
                    }
                } );
            }
        }.start();
    }
    private void initView() {
        tv_sumBoxOffice = (TextView) findViewById( R.id.tv_sumBoxOffice );
        lv = (ListView) findViewById( R.id.lv );
        /*pulltorefreshView = (PullToRefreshView) findViewById( R.id.pulltorefreshView );*/
        tb = (TabLayout) findViewById( R.id.tb );
        vp = (ViewPager) findViewById( R.id.vp );
        ll_content = (LinearLayout) findViewById( R.id.ll_content );
        ll_main = (LinearLayout) findViewById( R.id.ll_main );
        back = (Button) findViewById( R.id.back );
        back.setOnClickListener( this );
        dayBoxOffice = (Button) findViewById( R.id.dayBoxOffice );
        dayBoxOffice.setOnClickListener( this );
        weekBoxOffice = (Button) findViewById( R.id.weekBoxOffice );
        weekBoxOffice.setOnClickListener( this );
        weekendBoxOffice = (Button) findViewById( R.id.weekendBoxOffice );
        weekendBoxOffice.setOnClickListener( this );
        monthBoxOffice = (Button) findViewById( R.id.monthBoxOffice );
        monthBoxOffice.setOnClickListener( this );
        yearBoxOffice = (Button) findViewById( R.id.yearBoxOffice );
        yearBoxOffice.setOnClickListener( this );
        worldwideBoxOffice = (Button) findViewById( R.id.worldwideBoxOffice );
        worldwideBoxOffice.setOnClickListener( this );
        dl = (DrawerLayout) findViewById( R.id.dl );
        dl.setOnClickListener( this );
    }
    private String calPercent(int i) {//有参数就要return,
        String result = "";
        int sum = 0;
        for (int j = 0; j < pieData.length; j++) {//这里计算出pieData的全部值的和，sum
            sum += pieData[j];
        }
        result = String.format( "%.2f", (float) pieData[i] * 100 / sum ) + "%";
        return result;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ll_content.setVisibility( View.GONE );
                ll_main.setVisibility( View.VISIBLE );
                break;
            case R.id.dayBoxOffice:
                startActivity( new Intent( MainActivity.this, dayActivity.class ) );
                break;
            case R.id.weekBoxOffice:
                startActivity( new Intent( MainActivity.this, weekActivity.class ) );
                break;
            case R.id.weekendBoxOffice:
                startActivity( new Intent( MainActivity.this, weekendActivity.class ) );
                break;
            case R.id.monthBoxOffice:
                startActivity( new Intent( MainActivity.this, mouthActivity.class ) );
                break;
            case R.id.yearBoxOffice:
                startActivity( new Intent( MainActivity.this, yearActivity.class ) );
                break;
            case R.id.worldwideBoxOffice:
                startActivity( new Intent( MainActivity.this, worldwideActivity.class ) );
                break;
        }
    }
}