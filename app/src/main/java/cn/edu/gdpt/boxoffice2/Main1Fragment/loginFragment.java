package cn.edu.gdpt.boxoffice2.Main1Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cn.edu.gdpt.boxoffice2.MainActivity;
import cn.edu.gdpt.boxoffice2.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class loginFragment extends Fragment implements View.OnClickListener{
    private CircleImageView civ;
    private EditText et_number;
    private EditText et_password;
    private EditText et_Repassword;
    private Button register;
    private String number;


    public loginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);

        return view;
    }
    private void initView(View view) {
        civ = (CircleImageView) view.findViewById(R.id.civ);
        et_number = (EditText) view.findViewById(R.id.et_number);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_Repassword = (EditText) view.findViewById(R.id.et_Repassword);
        register = (Button) view.findViewById(R.id.register);

        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                number = et_number.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getContext(), "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                String Repassword = et_Repassword.getText().toString().trim();
                if (TextUtils.isEmpty(Repassword)) {
                    Toast.makeText(getContext(), "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(Repassword)){
                    Toast.makeText(getContext(),"你两次输入的密码不一致",Toast.LENGTH_LONG).show();
                    return;
                }else if (isExistUserName( number )){
                    Toast.makeText(getContext(),"此账号已存在",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Toast.makeText( getContext(),"注册成功,即将跳转",Toast.LENGTH_LONG ).show();
                    saveRegisterInfo( number,password );
                    Timer timer=new Timer(  );
                    TimerTask timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            startActivity(new Intent( getActivity(), MainActivity.class                      ) );
                        }
                    };
                    timer.schedule( timerTask,3000 );
                }

                break;
        }
    }
    private boolean isExistUserName(String userName){
        boolean has_userName=false;
        //注意，Activity和Fragment的区别，获取可能前面要加getActivity
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences( "loginInfo", Context.MODE_PRIVATE );
        String spPsw=sharedPreferences.getString( userName,"" );
        if (!TextUtils.isEmpty( spPsw )){
            has_userName=true;
        }

        return has_userName;
    }
    private void saveRegisterInfo(String userName,String psw){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences( "loginInfo",Context.MODE_PRIVATE );
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString( userName,psw );
        editor.commit();
    }
}
