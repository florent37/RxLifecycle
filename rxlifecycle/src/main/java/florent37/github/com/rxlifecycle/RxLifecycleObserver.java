package florent37.github.com.rxlifecycle;

/**
 * Created by florentchampigny on 21/05/2017.
 */

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import io.reactivex.subjects.Subject;

import static android.arch.lifecycle.Lifecycle.Event.ON_ANY;
import static android.arch.lifecycle.Lifecycle.Event.ON_CREATE;
import static android.arch.lifecycle.Lifecycle.Event.ON_DESTROY;
import static android.arch.lifecycle.Lifecycle.Event.ON_PAUSE;
import static android.arch.lifecycle.Lifecycle.Event.ON_RESUME;
import static android.arch.lifecycle.Lifecycle.Event.ON_START;
import static android.arch.lifecycle.Lifecycle.Event.ON_STOP;

class RxLifecycleObserver implements LifecycleObserver {

    private final Subject<Lifecycle.Event> subject;

    public RxLifecycleObserver(Subject<Lifecycle.Event> subject) {
        this.subject = subject;
    }

    @OnLifecycleEvent(ON_CREATE)
    public void onViewCreated() {
        subject.onNext(ON_CREATE);
    }

    @OnLifecycleEvent(ON_START)
    public void onViewStarted() {
        subject.onNext(ON_START);
    }

    @OnLifecycleEvent(ON_RESUME)
    public void onViewResumed() {
        subject.onNext(ON_RESUME);
    }

    @OnLifecycleEvent(ON_PAUSE)
    public void onViewPaused() {
        subject.onNext(ON_PAUSE);
    }

    @OnLifecycleEvent(ON_STOP)
    public void onViewStopped() {
        subject.onNext(ON_STOP);
    }

    @OnLifecycleEvent(ON_DESTROY)
    public void onViewDestroyed() {
        subject.onNext(ON_DESTROY);
    }

    @OnLifecycleEvent(ON_ANY)
    public void onViewEvent() {
        subject.onNext(ON_ANY);
    }
};