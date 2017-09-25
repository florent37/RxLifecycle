package florent37.github.com.rxlifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static android.arch.lifecycle.Lifecycle.Event.ON_ANY;
import static android.arch.lifecycle.Lifecycle.Event.ON_CREATE;
import static android.arch.lifecycle.Lifecycle.Event.ON_DESTROY;
import static android.arch.lifecycle.Lifecycle.Event.ON_PAUSE;
import static android.arch.lifecycle.Lifecycle.Event.ON_RESUME;
import static android.arch.lifecycle.Lifecycle.Event.ON_START;
import static android.arch.lifecycle.Lifecycle.Event.ON_STOP;

/**
 * Created by florentchampigny on 21/05/2017.
 */

public class RxLifecycle {

    private final Subject<Lifecycle.Event> subject = PublishSubject.<Lifecycle.Event>create().toSerialized();
    private final RxLifecycleObserver observer;
    private final Lifecycle lifecycle;

    public RxLifecycle(Lifecycle lifecycle) {
        this.observer = new RxLifecycleObserver(subject);
        this.lifecycle = lifecycle;
        lifecycle.addObserver(observer);
    }

    public static RxLifecycle with(LifecycleOwner lifecycleOwner) {
        return new RxLifecycle(lifecycleOwner.getLifecycle());
    }

    public static RxLifecycle with(Lifecycle lifecycle) {
        return new RxLifecycle(lifecycle);
    }

    public static RxLifecycle with(AppCompatActivity lifecycleActivity) {
        return new RxLifecycle(lifecycleActivity.getLifecycle());
    }

    public static RxLifecycle with(Fragment lifecycleFragment) {
        return new RxLifecycle(lifecycleFragment.getLifecycle());
    }

    public static Observable<Lifecycle.Event> onEvent(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onEvent();
    }

    public static Observable<Lifecycle.Event> onEvent(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onEvent();
    }

    public Observable<Lifecycle.Event> onEvent() {
        return subject;
    }

    public static Observable<Lifecycle.Event> onCreate(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onCreate();
    }

    public static Observable<Lifecycle.Event> onCreate(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onCreate();
    }

    public Observable<Lifecycle.Event> onCreate() {
        return onEvent().filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(@NonNull Lifecycle.Event event) throws Exception {
                return ON_CREATE.equals(event);
            }
        });
    }

    public static Observable<Lifecycle.Event> onStart(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onStart();
    }

    public static Observable<Lifecycle.Event> onStart(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onStart();
    }

    public Observable<Lifecycle.Event> onStart() {
        return onEvent().filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(@NonNull Lifecycle.Event event) throws Exception {
                return ON_START.equals(event);
            }
        });
    }

    public static Observable<Lifecycle.Event> onResume(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onResume();
    }

    public static Observable<Lifecycle.Event> onResume(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onResume();
    }

    public Observable<Lifecycle.Event> onResume() {
        return onEvent().filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(@NonNull Lifecycle.Event event) throws Exception {
                return ON_RESUME.equals(event);
            }
        });
    }

    public static Observable<Lifecycle.Event> onPause(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onPause();
    }

    public static Observable<Lifecycle.Event> onPause(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onPause();
    }

    public Observable<Lifecycle.Event> onPause() {
        return onEvent().filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(@NonNull Lifecycle.Event event) throws Exception {
                return ON_PAUSE.equals(event);
            }
        });
    }

    public static Observable<Lifecycle.Event> onStop(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onStop();
    }

    public static Observable<Lifecycle.Event> onStop(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onStop();
    }

    public Observable<Lifecycle.Event> onStop() {
        return onEvent().filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(@NonNull Lifecycle.Event event) throws Exception {
                return ON_STOP.equals(event);
            }
        });
    }

    public static Observable<Lifecycle.Event> onDestroy(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onDestroy();
    }

    public static Observable<Lifecycle.Event> onDestroy(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onDestroy();
    }

    public Observable<Lifecycle.Event> onDestroy() {
        return onEvent().filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(@NonNull Lifecycle.Event event) throws Exception {
                return ON_DESTROY.equals(event);
            }
        });
    }

    public static Observable<Lifecycle.Event> onAny(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).onAny();
    }

    public static Observable<Lifecycle.Event> onAny(LifecycleOwner lifecycle) {
        return RxLifecycle.with(lifecycle).onAny();
    }

    public Observable<Lifecycle.Event> onAny() {
        return onEvent().filter(new Predicate<Lifecycle.Event>() {
            @Override
            public boolean test(@NonNull Lifecycle.Event event) throws Exception {
                return ON_ANY.equals(event);
            }
        });
    }

    public static <T> Observable<T> onlyIfResumedOrStarted(LifecycleOwner lifecycleOwner, final T value) {
        return RxLifecycle.with(lifecycleOwner).onlyIfResumedOrStarted(value);
    }

    public static <T> Observable<T> onlyIfResumedOrStarted(Lifecycle lifecycle, final T value) {
        return RxLifecycle.with(lifecycle).onlyIfResumedOrStarted(value);
    }

    public <T> Observable<T> onlyIfResumedOrStarted(final T value) {
        return Observable.just(lifecycle)
                .flatMap(new Function<Lifecycle, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull Lifecycle lifecycle) throws Exception {
                        final Lifecycle.State currentState = lifecycle.getCurrentState();
                        if (currentState.equals(Lifecycle.State.RESUMED) || currentState.equals(Lifecycle.State.STARTED)) {
                            return Observable.just(value);
                        } else {
                            return onResume()
                                    .map(new Function<Lifecycle.Event, T>() {
                                        @Override
                                        public T apply(@NonNull Lifecycle.Event event) throws Exception {
                                            return value;
                                        }
                                    });
                        }
                    }
                });
    }

    public static void disposeOnDestroy(LifecycleOwner lifecycleOwner, final Disposable disposable) {
        RxLifecycle.with(lifecycleOwner).disposeOnDestroy(disposable);
    }

    public static void disposeOnDestroy(Lifecycle lifecycle, final Disposable disposable) {
        RxLifecycle.with(lifecycle).disposeOnDestroy(disposable);
    }

    public void disposeOnDestroy(final Disposable disposable) {
        onDestroy()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Lifecycle.Event>() {
                    @Override
                    public void accept(@NonNull Lifecycle.Event event) throws Exception {
                        disposable.dispose();
                    }
                });
    }

    public static void disposeOnStop(LifecycleOwner lifecycleOwner, final Disposable disposable) {
        RxLifecycle.with(lifecycleOwner).disposeOnStop(disposable);
    }

    public static void disposeOnStop(Lifecycle lifecycle, final Disposable disposable) {
        RxLifecycle.with(lifecycle).disposeOnStop(disposable);
    }

    public void disposeOnStop(final Disposable disposable) {
        onStop()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Lifecycle.Event>() {
                    @Override
                    public void accept(@NonNull Lifecycle.Event event) throws Exception {
                        disposable.dispose();
                    }
                });
    }

    public static void disposeOnPause(LifecycleOwner lifecycleOwner, final Disposable disposable) {
        RxLifecycle.with(lifecycleOwner).disposeOnPause(disposable);
    }

    public static void disposeOnPause(Lifecycle lifecycle, final Disposable disposable) {
        RxLifecycle.with(lifecycle).disposeOnPause(disposable);
    }

    public void disposeOnPause(final Disposable disposable) {
        onPause()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Lifecycle.Event>() {
                    @Override
                    public void accept(@NonNull Lifecycle.Event event) throws Exception {
                        disposable.dispose();
                    }
                });
    }

    public static <T> ObservableTransformer<T, T> disposeOnDestroy(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).disposeOnDestroy();
    }

    public static <T> ObservableTransformer<T, T> disposeOnDestroy(LifecycleOwner lifecycleOwner) {
        return RxLifecycle.with(lifecycleOwner).disposeOnDestroy();
    }

    public <T> ObservableTransformer<T, T> disposeOnDestroy() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        disposeOnDestroy(disposable);
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> disposeOnPause(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).disposeOnPause();
    }

    public static <T> ObservableTransformer<T, T> disposeOnPause(LifecycleOwner lifecycleOwner) {
        return RxLifecycle.with(lifecycleOwner).disposeOnPause();
    }


    public <T> ObservableTransformer<T, T> disposeOnPause() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        disposeOnPause(disposable);
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> disposeOnStop(Lifecycle lifecycle) {
        return RxLifecycle.with(lifecycle).disposeOnStop();
    }

    public static <T> ObservableTransformer<T, T> disposeOnStop(LifecycleOwner lifecycleOwner) {
        return RxLifecycle.with(lifecycleOwner).disposeOnStop();
    }

    public <T> ObservableTransformer<T, T> disposeOnStop() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        disposeOnStop(disposable);
                    }
                });
            }
        };
    }
}
