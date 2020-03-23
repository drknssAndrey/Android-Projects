package com.example.ambientcontrollproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText textName, textEmail, textPassword;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textName = findViewById(R.id.textInputUserRegister);
        textEmail = findViewById(R.id.textInputEmailRegister);
        textPassword = findViewById(R.id.textInputPasswordRegister);
    }
    public void registerUser(View view){
        if (checkFields()){
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkFields(){
        if(!textName.getText().toString().isEmpty()){
            if(!textEmail.getText().toString().isEmpty()){
                if(!textPassword.getText().toString().isEmpty()){
                    return true;
                }else{
                    Toast.makeText(this, "Preencha o campo Senha", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(this, "Preencha o campo Email", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Preencha o campo Usuário", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
