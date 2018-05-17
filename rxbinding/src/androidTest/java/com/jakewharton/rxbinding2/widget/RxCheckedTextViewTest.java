package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.UiThreadTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.widget.CheckedTextView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RxCheckedTextViewTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final CheckedTextView view = new CheckedTextView(context);

  @Test @UiThreadTest public void check() throws Exception {
    view.setChecked(false);
    RxCheckedTextView.check(view).accept(true);
    assertEquals(true, view.isChecked());
  }
}
