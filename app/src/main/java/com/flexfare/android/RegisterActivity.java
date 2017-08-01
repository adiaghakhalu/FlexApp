package com.flexfare.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity implements RegistrationValidation{

    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.cv_add)
    CardView cvAdd;

    private EditText firstname, lastname, username, password, repeat_password, email, code, mobile;
    private Button signup;

    Registration registration;
    List<Registration> registrationList = new ArrayList<Registration>();

    RegistrationPresenter presenter;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        presenter = new RegistrationPresenter(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });

        initialization();
    }

    private void initialization() {

        pd = new ProgressDialog(RegisterActivity.this);
        firstname = (EditText) findViewById(R.id.f_name);
        lastname = (EditText) findViewById(R.id.l_name);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        repeat_password = (EditText) findViewById(R.id.et_repeatpassword);
        email = (EditText) findViewById(R.id.reg_email);
        code = (EditText) findViewById(R.id.c_code);
        mobile = (EditText) findViewById(R.id.et_mobile);

        signup = (Button) findViewById(R.id.bt_go);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String f_name = firstname.getText().toString().trim();
                String l_name = lastname.getText().toString().trim();
                String user_name = username.getText().toString().trim();
                String p_word = password.getText().toString().trim();
                //tring rpt_password = repeat_password.get
                String user_email = email.getText().toString().trim();
                String verif_code = code.getText().toString().trim();
                String phone_number = mobile.getText().toString().trim();


                presenter.doValidation(f_name, l_name, user_name, p_word, user_email, phone_number);

            }
        });

    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @Override
    public void showEmptyFieldsError() {

        Snackbar.make(signup, "One or more fields missing", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void doRegistratio() {

        pd.setMessage("please wait...");
        pd.setIndeterminate(false);
        pd.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                registration = new Registration(firstname.getText().toString().trim(), lastname.getText().toString().trim(),
                        username.getText().toString().trim(), password.getText().toString().trim(),
                        email.getText().toString().trim(), mobile.getText().toString().trim());
                registrationList.add(registration);

                presenter.registerUser(registrationList);

            }
        },5000);


    }



    @Override
    public void showInvalidEmailError() {
        Snackbar.make(signup, "Invalid Email", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void showInvalidPhoneNumberError() {

        Snackbar.make(signup, "Invalid  mobile number", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void showRegistrationSuccess() {

        pd.dismiss();

        Snackbar.make(signup, "Registration Successful", Snackbar.LENGTH_SHORT).show();
        firstname.setText("");
        lastname.setText("");
        email.setText("");
        password.setText("");
        repeat_password.setText("");
        mobile.setText("");

    }
}
