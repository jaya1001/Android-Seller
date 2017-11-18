package com.example.yashladha.android_seller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public static final String TITLE = "Login";
    TextView tvName, tvPassword, tvRegister, tvForgotPassword, tvNumChar, tvCharLeft;
    EditText etName, etPassword;
    public static final String MyPREFERENCES = "myprfs";
    Button btLogin, btFacebook, btGoogle, btProceed;
    private RequestQueue rq;
    ImageButton ibPassword;
    boolean password2 = false;
    public static final String UID = "UID";
    String UID_i = "";
    String email = "";
    Boolean login, right = false;
    String password = "";

    public LoginActivity() {
        // Required empty public constructor
    }

    public static LoginActivity newInstance() {

        return new LoginActivity();
    }

    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvNumChar = (TextView) findViewById(R.id.tvNumChar);
        tvCharLeft = (TextView) findViewById(R.id.tvCharLeft);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btProceed = (Button) findViewById(R.id.btProceed);
        btFacebook = (Button) findViewById(R.id.btFacebook);
        btGoogle = (Button) findViewById(R.id.btGoogle);
        rq = Volley.newRequestQueue(LoginActivity.this);
        ibPassword = (ImageButton) findViewById(R.id.ibPassword);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (etName.getText().toString().trim().length() < 5) {
                        etName.setError("Minimum length should be 5 characters");
                    } else {
                        etName.setError(null);
                    }
                }
            }
        });


        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (etPassword.getText().toString().trim().length() < 8) {
                        etPassword.setError("Minimum length should be 8 characters");
                    } else {
                        etPassword.setError(null);
                    }
                }
            }
        });
/**
 * The functionality we do when the user wants to login.
 * In this we take the id and password of the user to check if he/she is already in the database or not
 * Then we let him/her proceed to his/her home page
 */
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUserName() && validatePassword()) {

                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("email", email);
                        obj.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST, "http://10.0.2.2:3000/user/login/", obj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(LoginActivity.this, response.get("flag").toString(), Toast.LENGTH_SHORT).show();

                                if (response.get("flag").toString().equals("true")) {
                                    UID_i = response.get("uid").toString();
                                    right = true;

                                    Toast.makeText(LoginActivity.this, "The login credentials are correct, Please click on proceed",
                                            Toast.LENGTH_LONG).show();
                                    btProceed.setEnabled(true);
                                    SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(UID, UID_i);
                                    editor.putString("email", email);
                                    editor.commit();
                                    editor.apply();
                                    Toast.makeText(LoginActivity.this, "The login credentials are correct, Please click on proceed",
                                            Toast.LENGTH_LONG).show();
                                    btProceed.setClickable(true);
                                    btProceed.setEnabled(true);
                                } else {
                                    Toast.makeText(LoginActivity.this, response.get("error authenticating user").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error", error.toString());
                        }
                    });

                    rq.add(jsonObjectRequest);

                }

            }

        });
        btProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(i);
                etPassword.setText("");
                etName.setText("");
            }
        });


        ibPassword.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (password2 == false)
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password2 = !password2;

            }
        });
        etName.addTextChangedListener(new

                                              TextWatcher() {
                                                  @Override
                                                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                  }

                                                  @Override
                                                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                  }

                                                  @Override
                                                  public void afterTextChanged(Editable s) {
                                                      email = etName.getText().toString().trim();


                                                  }
                                              });
        etPassword.addTextChangedListener(new

                                                  TextWatcher() {
                                                      @Override
                                                      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                      }

                                                      @Override
                                                      public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                      }

                                                      @Override
                                                      public void afterTextChanged(Editable s) {
                                                          password = etPassword.getText().toString();


                                                      }
                                                  });

        btFacebook.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });
        btGoogle.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);

            }
        });
    }

    /**
     * validating if the password provided is in the right format or not
     *
     * @return
     */
    private boolean validatePassword() {
        if (etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Invalid Password",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {

        }
        return true;
    }

    /**
     * checking if the email field is empty or not.
     *
     * @return
     */
    private boolean validateUserName() {
        if (email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Invalid User Name",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.isEmpty() && !isValidEmail(email)) {
            Toast.makeText(LoginActivity.this, "Invalid User Name",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {

        }
        return true;
    }

    /**
     * This method is used to take the functionality of the email address checker form the TextUtils class.
     * It uses the format of the given email to check if it is right or not
     *
     * @param email
     * @return
     */
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
