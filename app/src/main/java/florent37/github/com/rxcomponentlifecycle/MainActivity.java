package florent37.github.com.rxcomponentlifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import florent37.github.com.rxlifecycle.RxLifecycle;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AbstractActivity implements LifecycleRegistryOwner {

    private static final String TAG = "MainActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        Single.timer(10, TimeUnit.MINUTES)
                .doOnSubscribe(disposable -> RxLifecycle.with(getLifecycle()).<Long>disposeOnDestroy(disposable))
                .subscribe(l -> Log.d(TAG, "test"));

        Observable.timer(10, TimeUnit.SECONDS)

                .compose(RxLifecycle.with(getLifecycle()).<Long>disposeOnDestroy())

                .flatMap(l -> RxLifecycle.with(getLifecycle()).onlyIfResumedOrStarted(l))

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
