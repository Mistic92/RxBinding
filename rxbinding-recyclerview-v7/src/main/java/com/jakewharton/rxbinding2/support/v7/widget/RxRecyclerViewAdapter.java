package com.jakewharton.rxbinding2.support.v7.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/** Static factory methods for creating {@linkplain Observable observables} for {@link Adapter}. */
public final class RxRecyclerViewAdapter {
  /**
   * Create an observable of data change events for {@code RecyclerView.adapter}.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static <T extends Adapter<? extends ViewHolder>> InitialValueObservable<T> dataChanges(
      @NonNull T adapter) {
    checkNotNull(adapter, "adapter == null");
    return new RecyclerAdapterDataChangeObservable<>(adapter);
  }

  private RxRecyclerViewAdapter() {
    throw new AssertionError("No instances.");
  }
}
