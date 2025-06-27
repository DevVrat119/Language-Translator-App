package com.example.translator;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    // Widgets
    private Spinner fromSpinner, toSpinner;
    private EditText sourceEdt;
    private Button btn;
    private TextView translatedTv;

    //Source Array of Strings - spinners' Data
    String[] fromLanguages = {
            "from", "English","Afrikaans","Arabic","Belarusian,","Bengali","Catalan", "Hindi","Urdu"
    };
    String[] toLanguages = {
            "to", "English","Afrikaans","Arabic","Belarusian,","Bengali","Catalan", "Hindi","Urdu"
    };

    //  Permissions

    private static final int REQUEST_CODE = 1;

    String languageCode, fromLanguageCode, toLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.idEdtSourse);
        btn = findViewById(R.id.button);
        translatedTv = findViewById(R.id.idTvTranslatedTv);
        // Spinner 1
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                fromLanguageCode = GetLanguageCode(fromLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this,
                R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        //Spinner 2
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                toLanguageCode = GetLanguageCode(toLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this,
                R.layout.spinner_item,toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              translatedTv.setText("");

              if(sourceEdt.getText().toString().isEmpty()){
                  Toast.makeText(MainActivity.this, "Please enter your text", Toast.LENGTH_SHORT).show();
              }else if(fromLanguageCode.isEmpty()){
                  Toast.makeText(MainActivity.this, "Please Select Source Language", Toast.LENGTH_SHORT).show();
              }else if(toLanguageCode.isEmpty()){
                  Toast.makeText(MainActivity.this, "Please Select The Target Language", Toast.LENGTH_SHORT).show();
              }else {
                  TranslateText(fromLanguageCode,toLanguageCode,sourceEdt.getText().toString());
              }
          }
      });

    }

    private void TranslateText(String fromLanguageCode, String toLanguageCode, String src) {
        translatedTv.setText("Downloading Language Model");

        try {
            TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(fromLanguageCode)
                    .setTargetLanguage(toLanguageCode).build();

            Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions.Builder().build();

            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    translatedTv.setText("Translating...");
                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            translatedTv.setText(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,
                                    "Fail to translate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,
                            "Fail to download the language", Toast.LENGTH_SHORT).show();
                }
            })
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String GetLanguageCode(String language) {
        String languageCode;

        switch (language) {
            case "English":
                languageCode = TranslateLanguage.ENGLISH;
                break;
            case "Afriakaans":
                languageCode = TranslateLanguage.AFRIKAANS;
                break;
            case "Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;
            case "Belarusian":
                languageCode = TranslateLanguage.BELARUSIAN;
                break;
            case "Bengali":
                languageCode = TranslateLanguage.BENGALI;
                break;
            case "Catalan":
                languageCode = TranslateLanguage.CATALAN;
                break;
            case "Czech":
                languageCode = TranslateLanguage.CZECH;
                break;
            case "Welsh":
                languageCode = TranslateLanguage.WELSH;
                break;
            case "Hindi":
                languageCode = TranslateLanguage.HINDI;
                break;
            case "URDU":
                languageCode = TranslateLanguage.URDU;
                break;
            default:
                languageCode = "";

        }
        return languageCode;
    }
}