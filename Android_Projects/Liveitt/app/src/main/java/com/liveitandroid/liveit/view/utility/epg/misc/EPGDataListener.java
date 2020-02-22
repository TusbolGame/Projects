package com.liveitandroid.liveit.view.utility.epg.misc;

import com.liveitandroid.liveit.view.utility.epg.EPG;
import com.liveitandroid.liveit.view.utility.epg.EPGData;

public class EPGDataListener {
    private EPG epg;

    public EPGDataListener(EPG epg) {
        this.epg = epg;
    }

    public void processData(EPGData data) {
        this.epg.setEPGData(data);
        this.epg.recalculateAndRedraw(null, false, null, null);
    }
}
