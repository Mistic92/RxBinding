package com.jakewharton.rxbinding2.support.v4.view;

import androidx.core.view.MenuItemCompat.OnActionExpandListener;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.view.MenuItemActionViewCollapseEvent;
import com.jakewharton.rxbinding2.view.MenuItemActionViewEvent;
import com.jakewharton.rxbinding2.view.MenuItemActionViewExpandEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

import static androidx.core.view.MenuItemCompat.setOnActionExpandListener;
import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class MenuItemActionViewEventObservable extends Observable<MenuItemActionViewEvent> {
  private final MenuItem menuItem;
  private final Predicate<? super MenuItemActionViewEvent> handled;

  MenuItemActionViewEventObservable(MenuItem menuItem,
      Predicate<? super MenuItemActionViewEvent> handled) {
    this.menuItem = menuItem;
    this.handled = handled;
  }

  @Override protected void subscribeActual(Observer<? super MenuItemActionViewEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(menuItem, handled, observer);
    observer.onSubscribe(listener);
    setOnActionExpandListener(menuItem, listener);
  }

  static final class Listener extends MainThreadDisposable implements OnActionExpandListener {
    private final MenuItem menuItem;
    private final Predicate<? super MenuItemActionViewEvent> handled;
    private final Observer<? super MenuItemActionViewEvent> observer;

    Listener(MenuItem menuItem, Predicate<? super MenuItemActionViewEvent> handled,
        Observer<? super MenuItemActionViewEvent> observer) {
      this.menuItem = menuItem;
      this.handled = handled;
      this.observer = observer;
    }

    @Override public boolean onMenuItemActionExpand(MenuItem item) {
      return onEvent(MenuItemActionViewExpandEvent.create(item));
    }

    @Override public boolean onMenuItemActionCollapse(MenuItem item) {
      return onEvent(MenuItemActionViewCollapseEvent.create(item));
    }

    private boolean onEvent(MenuItemActionViewEvent event) {
      if (!isDisposed()) {
        try {
          if (handled.test(event)) {
            observer.onNext(event);
            return true;
          }
        } catch (Exception e) {
          observer.onError(e);
          dispose();
        }
      }
      return false;
    }

    @Override protected void onDispose() {
      setOnActionExpandListener(menuItem, null);
    }
  }
}
