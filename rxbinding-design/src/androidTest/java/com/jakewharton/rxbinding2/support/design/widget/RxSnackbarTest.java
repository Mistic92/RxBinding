package com.jakewharton.rxbinding2.support.design.widget;

import android.app.Instrumentation;
import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.widget.FrameLayout;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.support.design.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.android.material.snackbar.Snackbar.Callback.DISMISS_EVENT_MANUAL;
import static com.google.android.material.snackbar.Snackbar.LENGTH_SHORT;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class) public final class RxSnackbarTest {
  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final FrameLayout parent = new FrameLayout(context);

  @Test public void dismisses() {
    final Snackbar view = Snackbar.make(parent, "Hey", LENGTH_SHORT);

    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxSnackbar.dismisses(view).subscribeOn(AndroidSchedulers.mainThread()).subscribe(o);
    o.assertNoMoreEvents();

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
    assertEquals(DISMISS_EVENT_MANUAL, o.takeNext().intValue());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.show();
      }
    });
    o.dispose();
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.dismiss();
      }
    });
    o.assertNoMoreEvents();
  }
}
