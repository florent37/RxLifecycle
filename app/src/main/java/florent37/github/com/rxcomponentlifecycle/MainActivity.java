package florent37.github.com.rxcomponentlifecycle;

import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import florent37.github.com.rxlifecycle.RxLifecycle;
import io.reactivex.Observable;
import io.reactivex.Single;

import static florent37.github.com.rxlifecycle.RxLifecycle.disposeOnDestroy;
import static florent37.github.com.rxlifecycle.RxLifecycle.onlyIfResumedOrStarted;

public class MainActivity extends AbstractActivity {

    private static final String TAG = "MainActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        Single.timer(10, TimeUnit.MINUTES)
                .doOnSubscribe(disposable -> disposeOnDestroy(this, disposable))
                .subscribe(l -> Log.d(TAG, "test"));

        Observable.timer(10, TimeUnit.SECONDS)
                .compose(disposeOnDestroy(this))
                .flatMap(l -> onlyIfResumedOrStarted(this, l))
                .subscribe(o ->
                        Log.d(TAG, "test")
                );

        RxLifecycle.with(getLifecycle())
                .onEvent()

                .subscribe(event -> {
                    final CharSequence text = textView.getText();
                    textView.setText(text + "\n" + event.toString());
                });
    }

}
