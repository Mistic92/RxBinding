package com.jakewharton.rxbinding2.support.v4.widget;

import androidx.core.widget.NestedScrollView;
import androidx.core.widget.NestedScrollView.OnScrollChangeListener;
import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class NestedScrollViewScrollChangeEventObservable extends Observable<ViewScrollChangeEvent> {
  private final NestedScrollView view;

  NestedScrollViewScrollChangeEventObservable(NestedScrollView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super ViewScrollChangeEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnScrollChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnScrollChangeListener {
    private final NestedScrollView view;
    private final Observer<? super ViewScrollChangeEvent> observer;

    Listener(NestedScrollView view, Observer<? super ViewScrollChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
        int oldScrollY) {
      if (!isDisposed()) {
        observer.onNext(
            ViewScrollChangeEvent.create(view, scrollX, scrollY, oldScrollX, oldScrollY));
      }
    }

    @Override protected void onDispose() {
      view.setOnScrollChangeListener((OnScrollChangeListener) null);
    }
  }
}
