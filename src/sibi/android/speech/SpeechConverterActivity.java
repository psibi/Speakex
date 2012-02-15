package sibi.android.speech;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

public class SpeechConverterActivity extends Activity implements TextToSpeech.OnInitListener, OnItemSelectedListener {
    private static final int SPEECH_API_CHECK = 0;
    private TextToSpeech mTts;
    private Button speak;
    private EditText text;
    private Spinner lang;
    long position = 0;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

 	   Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, SPEECH_API_CHECK);
        
        speak = (Button)findViewById(R.id.button1);
        text = (EditText) findViewById(R.id.editText1);
        lang = (Spinner) findViewById(R.id.spinner1);
        
        Locale[] locale = Locale.getAvailableLocales();
        String langs[] = new String[locale.length];
        for (int i=0;i<locale.length;i++)
        {
        	langs[i]=locale[i].getDisplayLanguage() + " " + locale[i].getDisplayName();
        }
       
        
        ArrayAdapter<Object> spinnerArrayAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, langs);
        lang.setAdapter(spinnerArrayAdapter);
    
        lang.setOnItemSelectedListener(this);
        
        speak.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				speak();
			}

			private void speak() {
				// TODO Auto-generated method stub
				Locale[] locale = Locale.getAvailableLocales();
				mTts.setLanguage(locale[(int) position]);		
				String data=text.getText().toString();
				mTts.speak(data, TextToSpeech.QUEUE_FLUSH, null);
			}
		});
        
      }
    
    
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
    	           
        if (requestCode == SPEECH_API_CHECK) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
                int result = mTts.setLanguage(Locale.US);
                
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
    
    @Override
    public void onDestroy()
    {
        // Don't forget to shutdown!
        if (mTts != null)
        {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }


	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);
		} else {
            // Initialization failed.
            Log.e("app", "Could not initialize TextToSpeech.");
        }
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		long selected_position = lang.getSelectedItemId();
		position = (int) selected_position;
		Locale[] locale = Locale.getAvailableLocales();		
		Toast toast = Toast.makeText(getApplicationContext(), locale[(int) position].getDisplayLanguage() , Toast.LENGTH_SHORT);
		toast.show();
		
		//mTts.setLanguage(locale	);
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}