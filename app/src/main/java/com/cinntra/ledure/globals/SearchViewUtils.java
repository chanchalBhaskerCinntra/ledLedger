package com.cinntra.ledure.globals;

import android.os.Handler;

import androidx.appcompat.widget.SearchView;


public class SearchViewUtils {
    private static Handler handler = new Handler();
    private static Runnable runnable;

    public static void setupSearchView(SearchView searchView, final int delayMillis, final OnQueryTextListener listener) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (listener != null) {
                    return listener.onQueryTextSubmit(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                // Remove any previously scheduled API calls
                handler.removeCallbacks(runnable);

                // Schedule an API call after the specified delay
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        // Call your API here
                        if (listener != null) {
                            listener.onQueryTextChange(newText);
                        }
                    }
                };
                handler.postDelayed(runnable, delayMillis);

                return true;
            }
        });
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);
        void onQueryTextChange(String newText);
    }
}

