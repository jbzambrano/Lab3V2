package pucp.telecom.moviles.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pucp.telecom.moviles.lab3.Fragments.LocalDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void AgregarLocalDialogFragment(View view){
        LocalDialogFragment localDialogFragment = new LocalDialogFragment();
        localDialogFragment.show(getSupportFragmentManager(),"localDialogFragment");
    }

}