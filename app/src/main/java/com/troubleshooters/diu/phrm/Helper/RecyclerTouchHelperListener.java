package com.troubleshooters.diu.phrm.Helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Arif on 27-03-18.
 */

public interface RecyclerTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction,int position);

}
