package florent37.github.com.rxcomponentlifecycle;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by florentchampigny on 21/05/2017.
 */

public class AbstractActivity extends AppCompatActivity implements LifecycleOwner {
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    public LifecycleRegistry getLifecycle() {
        return this.mRegistry;
    }
}
