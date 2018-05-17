package com.jakewharton.rxbinding2.support.design.widget;

import android.content.Context;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.UiThreadTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.support.design.test.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class) public final class RxBottomNavigationViewTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final BottomNavigationView view = new BottomNavigationView(context);
  private final Menu menu = view.getMenu();

  @Before public void setUp() {
    view.inflateMenu(R.menu.menu);
  }

  @After public void teardown() {
    menu.clear();
  }

  @Test @UiThreadTest public void itemSelections() {
    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    RxBottomNavigationView.itemSelections(view).subscribe(o);

    // initial value
    assertEquals(R.id.menu_item_one, o.takeNext().getItemId());

    menu.performIdentifierAction(R.id.menu_item_two, 0);
    assertEquals(R.id.menu_item_two, o.takeNext().getItemId());

    menu.performIdentifierAction(R.id.menu_item_one, 0);
    assertEquals(R.id.menu_item_one, o.takeNext().getItemId());

    o.dispose();

    menu.performIdentifierAction(R.id.menu_item_two, 0);
    o.assertNoMoreEvents();
  }
}
