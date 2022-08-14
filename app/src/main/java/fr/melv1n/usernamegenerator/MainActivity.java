package fr.melv1n.usernamegenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.melv1n.usernamegenerator.utils.AdMobManager;


public class MainActivity extends AppCompatActivity {

    private final Random rand = new Random();
    private Button copyBtn, generateBtn;
    private TextView generatedUsername;
    private List<String> word;
    private Switch Switch;
    private int counter;
    private AdMobManager adMobManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adMobManager = new AdMobManager(MainActivity.this);
        adMobManager.setup();

        setupUsernameGeneration();

        copyBtn = findViewById(R.id.copyBtn);
        generateBtn = findViewById(R.id.generateBtn);
        generatedUsername = findViewById(R.id.generatedUsername);
        Switch = findViewById(R.id.numberSwitch);
        counter = 0;

        Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareAd();
            }
        });
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareAd();
                if (Switch.isChecked())
                    generatedUsername.setText(word.get(rand.nextInt(word.size())) + rand.nextInt(1000));
                else
                    generatedUsername.setText(word.get(rand.nextInt(word.size())));
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareAd();
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

    public void setupUsernameGeneration() {
        word = new ArrayList<String>();
        List<String> prefix = new ArrayList<String>();
        String mLine;
        BufferedReader reader = null;

        try {
            /* PREFIX */
            reader = new BufferedReader(new InputStreamReader(getAssets().open("prefix.txt"), StandardCharsets.UTF_8));
            while ((mLine = reader.readLine()) != null) {
                prefix.add(mLine);
            }

            /* PREFIX + WORDS */
            reader = new BufferedReader(new InputStreamReader(getAssets().open("words.txt"), StandardCharsets.UTF_8));
            while ((mLine = reader.readLine()) != null) {
                word.add(prefix.get(rand.nextInt(prefix.size())) + mLine);
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

    public void prepareAd() {
        counter++;
        if (counter == 5){
            adMobManager.showAds();
            counter = 0;
        }
    }
}