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

        Observable.timer(10, TimeUnit.SECONDS)
                .compose(RxLifecycle.with(getLifecycle()).bindUntil<Long>(Lifecycle.State.DESTROYED))
                .flatMap(new Function<Long, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(@NonNull Long aLong) throws Exception {
                        return RxLifecycle.with(getLifecycle()).onlyIfResumedOrStarted(aLong);
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long o) throws Exception {
                        Log.d(TAG, "test");
                    }
                });

        RxLifecycle.with(getLifecycle())
                .onEvent()
                .subscribe(new Consumer<Lifecycle.Event>() {
                    @Override
                    public void accept(@NonNull Lifecycle.Event event) throws Exception {
                        final CharSequence text = textView.getText();
                        textView.setText(text + "\n" + event.toString());
                    }
                });
    }

}
