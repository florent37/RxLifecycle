package florent37.github.com.rxcomponentlifecycle;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import florent37.github.com.rxlifecycle.RxLifecycle;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static florent37.github.com.rxlifecycle.RxLifecycle.DISPOSE_EVENT.DESTROY;
import static florent37.github.com.rxlifecycle.RxLifecycle.disposeOn;
import static florent37.github.com.rxlifecycle.RxLifecycle.disposeOnDestroy;
import static florent37.github.com.rxlifecycle.RxLifecycle.onlyIfResumedOrStarted;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        Flowable
                .create(e -> {

                }, BackpressureStrategy.LATEST)
                .doOnSubscribe(subscription -> RxLifecycle.disposeOnDestroy(this, subscription))
                .subscribe();

        Single.timer(10, TimeUnit.MINUTES)
                .compose(disposeOnDestroy(getLifecycle()))
                .subscribe(l -> Log.d(TAG, "test"));

        Single.timer(10, TimeUnit.MINUTES)
                .doOnSubscribe(disposable -> disposeOn(getLifecycle(), DESTROY, disposable))
                .subscribe(l -> Log.d(TAG, "test"));

        Observable.timer(10, TimeUnit.SECONDS)
                .compose(disposeOnDestroy(getLifecycle()))
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
