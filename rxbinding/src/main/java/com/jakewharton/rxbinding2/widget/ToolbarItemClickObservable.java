package com.jakewharton.rxbinding2.widget;

import androidx.annotation.RequiresApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import android.widget.Toolbar.OnMenuItemClickListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

@RequiresApi(21)
final class ToolbarItemClickObservable extends Observable<MenuItem> {
  private final Toolbar view;

  ToolbarItemClickObservable(Toolbar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super MenuItem> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnMenuItemClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnMenuItemClickListener {
    private final Toolbar view;
    private final Observer<? super MenuItem> observer;

    Listener(Toolbar view, Observer<? super MenuItem> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
      if (!isDisposed()) {
        observer.onNext(item);
        return true;
      }
      return false;
    }

    @Override protected void onDispose() {
      view.setOnMenuItemClickListener(null);
    }
  }
}
