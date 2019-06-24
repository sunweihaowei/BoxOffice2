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
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdpt.boxoffice2.MainActivity;
import cn.edu.gdpt.boxoffice2.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class registerFragment extends Fragment implements View.OnClickListener {
    private CircleImageView civ;
    private EditText et_number;
    private EditText et_password;
    private Button register;
    private TextView wjmm;
    private String number;

    public registerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);

        return view;
    }
    private void initView(View view) {
        civ = (CircleImageView) view.findViewById(R.id.civ);
        et_number = (EditText) view.findViewById(R.id.et_number);
        et_password = (EditText) view.findViewById(R.id.et_password);
        register = (Button) view.findViewById(R.id.register);
        wjmm = (TextView) view.findViewById(R.id.wjmm);

        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                number = et_number.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getContext(), "账号不能位空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (NoExistUserName(number)){
                    Toast.makeText(getContext(), "账号不存在", Toast.LENGTH_SHORT).show();
                    return;
                }else if (NoExistUserName(password)){
                    Toast.makeText(getContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
        }
    }
    private boolean NoExistUserName(String userName){
        boolean has_userName=true;
        //注意，Activity和Fragment的区别，获取可能前面要加getActivity
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences( "loginInfo", Context.MODE_PRIVATE );
        String spPsw=sharedPreferences.getString( userName,"" );
        if (!TextUtils.isEmpty( spPsw )){
            has_userName=false;
        }

        return has_userName;
    }
}
