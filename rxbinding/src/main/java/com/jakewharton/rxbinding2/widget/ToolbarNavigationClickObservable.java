package com.jakewharton.rxbinding2.widget;

import androidx.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toolbar;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

@RequiresApi(21)
final class ToolbarNavigationClickObservable extends Observable<Object> {
  private final Toolbar view;

  ToolbarNavigationClickObservable(Toolbar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setNavigationOnClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnClickListener {
    private final Toolbar view;
    private final Observer<? super Object> observer;

    Listener(Toolbar view, Observer<? super Object> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onClick(View v) {
      if (!isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override protected void onDispose() {
      view.setNavigationOnClickListener(null);
    }
  }
}
