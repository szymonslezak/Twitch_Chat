package pl.edu.uwr.pum.footballapp.view.master;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import pl.edu.uwr.pum.footballapp.databinding.LoginLayoutBinding;
import pl.edu.uwr.pum.footballapp.view.master.loginInterface.LoginButton;
import pl.edu.uwr.pum.footballapp.view.master.LoginFragmentDirections;


public class LoginFragment extends Fragment implements LoginButton {

    private LoginLayoutBinding binding;
    private String nick;
    private String oAuth;
    private SharedPreferences sharedPref;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LoginLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        nick = sharedPref.getString("nick","");
        oAuth = sharedPref.getString("oAuth","");

        binding.setListener(this);
        binding.editUserName.setText(nick);
        binding.oAuthCode.setText(oAuth);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    @Override
    public void onOkClick(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nick",binding.editUserName.getText().toString());
        editor.putString("oAuth",binding.oAuthCode.getText().toString());
        editor.apply();

        Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToChatFragment());
    }
}
