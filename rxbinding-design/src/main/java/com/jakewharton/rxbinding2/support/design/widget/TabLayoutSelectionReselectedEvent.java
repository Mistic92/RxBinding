package com.jakewharton.rxbinding2.support.design.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TabLayoutSelectionReselectedEvent extends TabLayoutSelectionEvent {
  @CheckResult @NonNull
  public static TabLayoutSelectionReselectedEvent create(@NonNull TabLayout view,
      @NonNull TabLayout.Tab tab) {
    return new AutoValue_TabLayoutSelectionReselectedEvent(view, tab);
  }

  TabLayoutSelectionReselectedEvent() {
  }
}
