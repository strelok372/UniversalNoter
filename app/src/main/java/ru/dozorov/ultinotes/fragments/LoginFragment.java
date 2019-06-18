package ru.dozorov.ultinotes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import ru.dozorov.ultinotes.R;

public class LoginFragment extends Fragment implements View.OnClickListener {
    Button signInButton;
    ImageView userIcon;
    TextView lastSaved;
    TextView userLogin;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    String lastBU;
    private static final int RC_SIGN_IN = 22;
    int Mode = 1;

    public LoginFragment(String lastBU) {
        this.lastBU = lastBU;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        view.findViewById(R.id.b_backup_now).setOnClickListener(this);
        view.findViewById(R.id.b_backup_to_file).setOnClickListener(this);
        view.findViewById(R.id.b_restore_from_file).setOnClickListener(this);

        signInButton = view.findViewById(R.id.b_signin_signout);
        signInButton.setOnClickListener(this);

        lastSaved = view.findViewById(R.id.tv_last_backup);
        lastSaved.setText("Last backup: " + lastBU);
        userLogin = view.findViewById(R.id.tv_user_login);
        userIcon = view.findViewById(R.id.iv_account_icon);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        account = GoogleSignIn.getLastSignedInAccount(getContext());

        if (account != null) updateUi();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    void updateUi(){
        signInButton.setClickable(true);
        signInButton.setBackgroundColor(getResources().getColor(R.color.deepBlue, );
//            userIcon.setImageDrawable(account.getPhotoUrl().);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            Toast.makeText(getContext(),"Auth success for " + account.getEmail(), Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            Log.w("MY_LOG_HERE: ", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getContext(),"Auth failed with code " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_restore_from_file:

                break;
            case R.id.b_signin_signout:

                signIn();
                break;
            case R.id.b_backup_now:

                break;
            case R.id.b_backup_to_file:

                break;
        }
    }
}
