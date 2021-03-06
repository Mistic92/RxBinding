package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class) public final class RxPopupMenuTest {
  @Rule public final ActivityTestRule<RxPopupMenuTestActivity> activityRule =
      new ActivityTestRule<>(RxPopupMenuTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  PopupMenu view;

  @Before public void setUp() {
    view = activityRule.getActivity().popupMenu;
  }

  @Test @UiThreadTest public void itemClicks() {
    Menu menu = view.getMenu();
    MenuItem item1 = menu.add(0, 1, 0, "Hi");
    MenuItem item2 = menu.add(0, 2, 0, "Hey");

    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    RxPopupMenu.itemClicks(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertSame(item2, o.takeNext());

    menu.performIdentifierAction(1, 0);
    assertSame(item1, o.takeNext());

    o.dispose();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }

  @Test public void dismisses() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxPopupMenu.dismisses(view).subscribeOn(AndroidSchedulers.mainThread()).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.show();
      }
    });
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.dismiss();
      }
    });
    assertNotNull(o.takeNext());

    o.dispose();
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.show();
      }
    });
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.dismiss();
      }
    });

    o.assertNoMoreEvents();
  }
}
