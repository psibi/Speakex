package sibi.android.speech;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

public class SpeechConverterActivity extends Activity implements TextToSpeech.OnInitListener {
    private static final int SPEECH_API_CHECK = 0;
    private TextToSpeech mTts;
    private Button speak;
    private EditText text;
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
        speak.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				speak();
			}

			private void speak() {
				// TODO Auto-generated method stub
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
}