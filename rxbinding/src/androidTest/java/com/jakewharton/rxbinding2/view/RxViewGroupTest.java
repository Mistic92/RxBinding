package com.jakewharton.rxbinding2.view;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.UiThreadTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.LinearLayout;
import com.jakewharton.rxbinding2.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public final class RxViewGroupTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getTargetContext();
  private final LinearLayout parent = new LinearLayout(context);
  private final View child = new View(context);

  @Test @UiThreadTest public void childViewEvents() {
    RecordingObserver<ViewGroupHierarchyChangeEvent> o = new RecordingObserver<>();
    RxViewGroup.changeEvents(parent).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    parent.addView(child);
    assertEquals(ViewGroupHierarchyChildViewAddEvent.create(parent, child), o.takeNext());

    parent.removeView(child);
    assertEquals(ViewGroupHierarchyChildViewRemoveEvent.create(parent, child), o.takeNext());

    o.dispose();

    parent.addView(child);
    o.assertNoMoreEvents();
  }
}
