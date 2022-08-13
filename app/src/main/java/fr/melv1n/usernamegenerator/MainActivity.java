package fr.melv1n.usernamegenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /*declaration*/
    private Button copyBtn, generateBtn;
    private TextView generatedUsername, ApplicationTitle;
    private List<String> word;
    private Switch Switch;
    private final Random rand = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, "ca-app-pub-7800224877695926/1077158543")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd NativeAd) {
                        // Show the ad.
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();


        loadUsername();

        copyBtn = findViewById(R.id.copyBtn);
        generateBtn = findViewById(R.id.generateBtn);
        generatedUsername = findViewById(R.id.generatedUsername);
        Switch = findViewById(R.id.numberSwitch);


        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Switch.isChecked())
                    generatedUsername.setText(word.get(rand.nextInt(word.size())) + rand.nextInt(1000));
                else
                    generatedUsername.setText(word.get(rand.nextInt(word.size())));
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generatedUsername.getText().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("username", generatedUsername.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast toast = Toast.makeText(MainActivity.this, "Votre nouveau pseudonyme: " + generatedUsername.getText() + " est copier.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    public void loadUsername() {
        String[] prefixWord = {"lord", "master", "cyber", "hyper", "pro", "dude", "super", "guide", "guy", "noob", "random", "fan", "monster"};


        word = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("words.txt"), StandardCharsets.UTF_8));

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                word.add(prefixWord[rand.nextInt(prefixWord.length)] + mLine);
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }


    }


}