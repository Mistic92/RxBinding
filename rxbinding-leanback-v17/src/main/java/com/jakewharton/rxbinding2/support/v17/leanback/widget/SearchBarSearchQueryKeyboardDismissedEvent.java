package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.leanback.widget.SearchBar;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchBarSearchQueryKeyboardDismissedEvent extends SearchBarSearchQueryEvent {
  @CheckResult @NonNull
  public static SearchBarSearchQueryKeyboardDismissedEvent create(@NonNull SearchBar view,
      @NonNull String query) {
    return new AutoValue_SearchBarSearchQueryKeyboardDismissedEvent(view, query);
  }

  SearchBarSearchQueryKeyboardDismissedEvent() {
  }
}
