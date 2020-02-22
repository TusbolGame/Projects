package com.liveitandroid.liveit.view.utility.epg;

import com.liveitandroid.liveit.view.utility.epg.domain.EPGChannel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGEvent;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGChannel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGEvent;

public interface EPGClickListener {
    void onChannelClicked(int i, EPGChannel ePGChannel);

    void onEventClicked(int i, int i2, EPGEvent ePGEvent);

    void onResetButtonClicked();
}
