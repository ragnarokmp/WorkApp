package it.mobileprogramming.ragnarok.workapp.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class Voice extends AsyncTask implements TextToSpeech.OnInitListener {

    private final Context context;
    private TextToSpeech textToSpeech;

    public Voice(Context context) {
        this.context = context;
    }

    @Override
    public void onInit(int status) {
        textToSpeech.setLanguage(Locale.ITALIAN);
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak("Benvenuto in work app, pronto a muovere le chiappe? Per non fare schifo devi spingere come un maledetto!", TextToSpeech.QUEUE_ADD, null);
        } else {
            textToSpeech.speak("Benvenuto in work app, pronto a muovere le chiappe? Per non fare schifo devi spingere come un maledetto!", TextToSpeech.QUEUE_ADD, null,"pippo");
            /*textToSpeech.setSpeechRate((float) 0.5);
            textToSpeech.speak("E se mi gira ti posso far andare molto pianoooooooooo", TextToSpeech.QUEUE_ADD, null, "pippo");
            textToSpeech.setSpeechRate((float) 3);
            textToSpeech.speak("Oppure molto forte!", TextToSpeech.QUEUE_ADD, null, "pippo");
            textToSpeech.setSpeechRate(1);
            textToSpeech.speak("Un solo grido! Un solo allarme! Milano in fiamme! Milano in fiamme!", TextToSpeech.QUEUE_ADD, null, "pippo");
        */
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {

        textToSpeech = new TextToSpeech(this.context, this);
        return null;
    }
}
