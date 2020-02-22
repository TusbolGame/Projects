package com.liveitandroid.liveit.view.utility.epg.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGChannel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGEvent;
import com.google.common.collect.Maps;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.model.pojo.XMLTVProgrammePojo;
import com.liveitandroid.liveit.view.utility.epg.EPGData;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGChannel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGEvent;
import com.liveitandroid.liveit.view.utility.epg.misc.EPGDataImpl;
import com.liveitandroid.liveit.view.utility.epg.misc.EPGDataListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.joda.time.LocalDateTime;

public class EPGService {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static SimpleDateFormat nowFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private Context context;
    DatabaseHandler database;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetail;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref_epg_channel_update;

    public EPGService(Context context) {
        this.context = context;
    }

    public EPGData getData(EPGDataListener listener, int dayOffset, String catID) {
        this.loginPreferencesSharedPref_epg_channel_update = this.context.getSharedPreferences(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, 0);
        try {
            if (this.loginPreferencesSharedPref_epg_channel_update.getString(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, "").equals("all")) {
                return parseDataall_channels(catID);
            }
            return parseData(catID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<LiveStreamsDBModel> getFavourites() {
        if (this.context != null) {
            this.database = new DatabaseHandler(this.context);
            if (this.database != null) {
                ArrayList<FavouriteDBModel> allFavourites = this.database.getAllFavourites("live");
                ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
                Iterator it = allFavourites.iterator();
                while (it.hasNext()) {
                    FavouriteDBModel favListItem = (FavouriteDBModel) it.next();
                    LiveStreamsDBModel channelAvailable = new LiveStreamDBHandler(this.context).getLiveStreamFavouriteRow(favListItem.getCategoryID(), String.valueOf(favListItem.getStreamID()));
                    if (channelAvailable != null) {
                        favouriteStreams.add(channelAvailable);
                    }
                }
                if (!(favouriteStreams == null || favouriteStreams.size() == 0)) {
                    return favouriteStreams;
                }
            }
        }
        return null;
    }

    private EPGData parseData(String catID) throws RuntimeException {
        ArrayList<LiveStreamsDBModel> allChannels;
        Throwable ex;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        EPGChannel firstChannel = null;
        EPGChannel prevChannel = null;
        EPGChannel currentChannel = null;
        EPGEvent prevEvent = null;
        Map<EPGChannel, List<EPGEvent>> map = Maps.newLinkedHashMap();
        LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (catID.equals("-1")) {
            allChannels = getFavourites();
        } else {
            allChannels = liveStreamDBHandler.getAllLiveStreasWithCategoryId(catID, "live");
        }
        this.categoryWithPasword = new ArrayList();
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || allChannels == null) {
            this.liveListDetailAvailable = allChannels;
        } else {
            this.listPassword = getPasswordSetCategories();
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(allChannels, this.listPassword);
            }
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        }
        if (this.liveListDetailAvailable != null) {
            int k = 0;
            int i = 0;
            EPGChannel currentChannel2 = null;
            while (i < this.liveListDetailAvailable.size()) {
                try {
                    String channelName = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getName();
                    String channelID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
                    String streamIcon = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamIcon();
                    String streamID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamId();
                    String num = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getNum();
                    String epgChannelId = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
                    String categID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getCategoryId();
                    if (!channelID.equals("")) {
                        ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(channelID);
                        Long epgTempStop = null;
                        boolean isVisible = false;
                        if (!(xmltvProgrammePojos == null || xmltvProgrammePojos.size() == 0)) {
                            currentChannel = new EPGChannel(streamIcon, channelName, k, streamID, num, epgChannelId, categID);
                            k++;
                            if (firstChannel == null) {
                                firstChannel = currentChannel;
                            }
                            if (prevChannel != null) {
                                currentChannel.setPreviousChannel(prevChannel);
                                prevChannel.setNextChannel(currentChannel);
                            }
                            prevChannel = currentChannel;
                            List<EPGEvent> epgEvents = new ArrayList();
                            map.put(currentChannel, epgEvents);
                            for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                                String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                                String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                                String Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                                String Desc = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getDesc();
                                Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                                Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                                if (Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context) ) {
                                    long starttesting1;
                                    long endtesting1;
                                    EPGEvent ePGEvent;
                                    isVisible = true;
                                    long currentTime = LocalDateTime.now().toDateTime().getMillis() + Utils.getTimeShiftMilliSeconds(this.context);
                                    if (epgStartDateToTimestamp.longValue() <= currentTime + 12600000) {
                                        EPGEvent r20;
                                        if (epgTempStop != null && epgStartDateToTimestamp.equals(epgTempStop)) {
                                            EPGEvent epgEvent = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                            if (prevEvent != null) {
                                                epgEvent.setPreviousEvent(prevEvent);
                                                prevEvent.setNextEvent(epgEvent);
                                            }
                                            prevEvent = epgEvent;
                                            currentChannel.addEvent(epgEvent);
                                            epgEvents.add(epgEvent);
                                        } else if (epgTempStop != null) {
                                            try {
                                                r20 = new EPGEvent(currentChannel, epgTempStop.longValue(), epgStartDateToTimestamp.longValue(), this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                                if (prevEvent != null) {
                                                    r20.setPreviousEvent(prevEvent);
                                                    prevEvent.setNextEvent(r20);
                                                }
                                                prevEvent = r20;
                                                currentChannel.addEvent(r20);
                                                epgEvents.add(r20);
                                                r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                                if (prevEvent != null) {
                                                    r20.setPreviousEvent(prevEvent);
                                                    prevEvent.setNextEvent(r20);
                                                }
                                                prevEvent = r20;
                                                currentChannel.addEvent(r20);
                                                epgEvents.add(r20);
                                            } catch (Throwable th) {
                                                ex = th;
                                            }
                                        } else {
                                            r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
                                            if (prevEvent != null) {
                                                r20.setPreviousEvent(prevEvent);
                                                prevEvent.setNextEvent(r20);
                                            }
                                            prevEvent = r20;
                                            currentChannel.addEvent(r20);
                                            epgEvents.add(r20);
                                        }
                                    }
                                    epgTempStop = epgStopDateToTimestamp;
                                    long nowTime = currentTime;
                                    if (j == xmltvProgrammePojos.size() - 1 && epgTempStop.longValue() < nowTime) {
                                        starttesting1 = epgTempStop.longValue();
                                        endtesting1 = starttesting1 + Long.parseLong("3600000");
                                        for (int l = 0; l < 3; l++) {
                                            ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                            if (prevEvent != null) {
                                                ePGEvent.setPreviousEvent(prevEvent);
                                                prevEvent.setNextEvent(ePGEvent);
                                            }
                                            prevEvent = ePGEvent;
                                            currentChannel.addEvent(ePGEvent);
                                            epgEvents.add(ePGEvent);
                                            starttesting1 = endtesting1;
                                            endtesting1 = starttesting1 + Long.parseLong("3600000");
                                        }
                                    }
                                    if (j == 0 && epgStartDateToTimestamp.longValue() > nowTime) {
                                        starttesting1 = nowTime;
                                        endtesting1 = epgStartDateToTimestamp.longValue();
                                        for (int m = 0; m < 3; m++) {
                                            ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
                                            if (prevEvent != null) {
                                                ePGEvent.setPreviousEvent(prevEvent);
                                                prevEvent.setNextEvent(ePGEvent);
                                            }
                                            prevEvent = ePGEvent;
                                            currentChannel.addEvent(ePGEvent);
                                            epgEvents.add(ePGEvent);
                                            starttesting1 = endtesting1;
                                            endtesting1 = starttesting1 + Long.parseLong("3600000");
                                        }
                                    }
                                }
                            }
                            continue;
                            //i++;
                            //currentChannel2 = currentChannel;
                        }
                    }
                    currentChannel = currentChannel2;
                    i++;
                    currentChannel2 = currentChannel;
                } catch (Throwable th2) {
                    ex = th2;
                    currentChannel = currentChannel2;
                }
            }
            currentChannel = currentChannel2;
        }
        if (currentChannel != null) {
            currentChannel.setNextChannel(firstChannel);
        }
        if (firstChannel != null) {
            firstChannel.setPreviousChannel(currentChannel);
        }
        return new EPGDataImpl(map);
        //throw new RuntimeException(ex.getMessage(), ex);
    }

//    private EPGData parseDataall_channels(String catID) {
//        ArrayList<LiveStreamsDBModel> allChannels;
//        Throwable ex;
//        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
//        EPGChannel firstChannel = null;
//        EPGChannel prevChannel = null;
//        EPGChannel currentChannel = null;
//        EPGEvent prevEvent = null;
//        Map<EPGChannel, List<EPGEvent>> map = Maps.newLinkedHashMap();
//        LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
//        if (catID.equals("-1")) {
//            allChannels = getFavourites();
//        } else {
//            allChannels = liveStreamDBHandler.getAllLiveStreasWithCategoryId(catID, "live");
//        }
//        this.categoryWithPasword = new ArrayList();
//        this.liveListDetailUnlcked = new ArrayList();
//        this.liveListDetailUnlckedDetail = new ArrayList();
//        this.liveListDetailAvailable = new ArrayList();
//        this.liveListDetail = new ArrayList();
//        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || allChannels == null) {
//            this.liveListDetailAvailable = allChannels;
//        } else {
//            this.listPassword = getPasswordSetCategories();
//            if (this.listPassword != null) {
//                this.liveListDetailUnlckedDetail = getUnlockedCategories(allChannels, this.listPassword);
//            }
//            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
//        }
//        if (this.liveListDetailAvailable != null) {
//            int i = 0;
//            EPGChannel currentChannel2 = null;
//            while (i < this.liveListDetailAvailable.size()) {
//                try {
//                    String channelName = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getName();
//                    String channelID = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId();
//                    String streamIcon = ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamIcon();
//                    currentChannel = new EPGChannel(streamIcon, channelName, i, ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getStreamId(), ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getNum(), ((LiveStreamsDBModel) this.liveListDetailAvailable.get(i)).getEpgChannelId());
//                    if (firstChannel == null) {
//                        firstChannel = currentChannel;
//                    }
//                    if (prevChannel != null) {
//                        try {
//                            currentChannel.setPreviousChannel(prevChannel);
//                            prevChannel.setNextChannel(currentChannel);
//                        } catch (Throwable th) {
//                            ex = th;
//                        }
//                    }
//                    prevChannel = currentChannel;
//                    List<EPGEvent> epgEvents = new ArrayList();
//                    map.put(currentChannel, epgEvents);
//                    long starttesting1;
//                    long endtesting1;
//                    int k;
//                    EPGEvent ePGEvent;
//                    if (channelID.equals("")) {
//                        starttesting1 = LocalDateTime.now().toDateTime().getMillis();
//                        endtesting1 = starttesting1 + Long.parseLong("3600000");
//                        for (k = 0; k < 3; k++) {
//                            ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
//                            if (prevEvent != null) {
//                                ePGEvent.setPreviousEvent(prevEvent);
//                                prevEvent.setNextEvent(ePGEvent);
//                            }
//                            prevEvent = ePGEvent;
//                            currentChannel.addEvent(ePGEvent);
//                            epgEvents.add(ePGEvent);
//                            starttesting1 = endtesting1;
//                            endtesting1 = starttesting1 + Long.parseLong("3600000");
//                        }
//                    } else {
//                        ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(channelID);
//                        Long epgTempStop = null;
//                        boolean isVisible = false;
//                        if (xmltvProgrammePojos == null || xmltvProgrammePojos.size() == 0) {
//                            starttesting1 = LocalDateTime.now().toDateTime().getMillis();
//                            endtesting1 = starttesting1 + Long.parseLong("3600000");
//                            for (k = 0; k < 3; k++) {
//                                ePGEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, this.context.getResources().getString(R.string.no_information), streamIcon, "");
//                                if (prevEvent != null) {
//                                    ePGEvent.setPreviousEvent(prevEvent);
//                                    prevEvent.setNextEvent(ePGEvent);
//                                }
//                                prevEvent = ePGEvent;
//                                currentChannel.addEvent(ePGEvent);
//                                epgEvents.add(ePGEvent);
//                                starttesting1 = endtesting1;
//                                endtesting1 = starttesting1 + Long.parseLong("3600000");
//                            }
//                        } else {
//                            for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
//                                String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
//                                String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
//                                String Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
//                                String Desc = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getDesc();
//                                Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
//                                Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
//                                if (Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context) || isVisible) {
//                                    isVisible = true;
//                                    if (epgStartDateToTimestamp.longValue() > (LocalDateTime.now().toDateTime().getMillis() + Utils.getTimeShiftMilliSeconds(this.context)) + 12600000) {
//                                        continue;
//                                    } else {
//                                        EPGEvent r20;
//                                        if (epgTempStop != null && epgStartDateToTimestamp.equals(epgTempStop)) {
//                                            EPGEvent epgEvent = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
//                                            if (prevEvent != null) {
//                                                epgEvent.setPreviousEvent(prevEvent);
//                                                prevEvent.setNextEvent(epgEvent);
//                                            }
//                                            prevEvent = epgEvent;
//                                            currentChannel.addEvent(epgEvent);
//                                            epgEvents.add(epgEvent);
//                                        } else if (epgTempStop != null) {
//                                            r20 = new EPGEvent(currentChannel, epgTempStop.longValue(), epgStartDateToTimestamp.longValue(), this.context.getResources().getString(R.string.no_information), streamIcon, "");
//                                            if (prevEvent != null) {
//                                                r20.setPreviousEvent(prevEvent);
//                                                prevEvent.setNextEvent(r20);
//                                            }
//                                            prevEvent = r20;
//                                            currentChannel.addEvent(r20);
//                                            epgEvents.add(r20);
//                                            r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
//                                            if (prevEvent != null) {
//                                                r20.setPreviousEvent(prevEvent);
//                                                prevEvent.setNextEvent(r20);
//                                            }
//                                            prevEvent = r20;
//                                            currentChannel.addEvent(r20);
//                                            epgEvents.add(r20);
//                                        } else {
//                                            r20 = new EPGEvent(currentChannel, epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), Title, streamIcon, Desc);
//                                            if (prevEvent != null) {
//                                                r20.setPreviousEvent(prevEvent);
//                                                prevEvent.setNextEvent(r20);
//                                            }
//                                            prevEvent = r20;
//                                            currentChannel.addEvent(r20);
//                                            epgEvents.add(r20);
//                                        }
//                                        epgTempStop = epgStopDateToTimestamp;
//                                    }
//                                }
//                            }
//                            continue;
//                        }
//                    }
//                    i++;
//                    currentChannel2 = currentChannel;
//                } catch (Throwable th2) {
//                    ex = th2;
//                    currentChannel = currentChannel2;
//                }
//            }
//            currentChannel = currentChannel2;
//        }
//        if (currentChannel != null) {
//            currentChannel.setNextChannel(firstChannel);
//        }
//        if (firstChannel != null) {
//            firstChannel.setPreviousChannel(currentChannel);
//        }
//        return new EPGDataImpl(map);
//        //throw new RuntimeException(ex.getMessage(), ex);
//    }


    private EPGData parseDataall_channels (String catID) {
        liveStreamDBHandler = new LiveStreamDBHandler(context);
        EPGChannel firstChannel = null;
        EPGChannel prevChannel = null;
        EPGChannel currentChannel = null;
        EPGEvent prevEvent = null;
        try {
            Map<EPGChannel, List<EPGEvent>> map = Maps.newLinkedHashMap();
            LiveStreamDBHandler dbObj = new LiveStreamDBHandler(context);
            ArrayList<LiveStreamsDBModel> allChannels = dbObj.getAllLiveStreasWithCategoryId(catID, "live");
            categoryWithPasword = new ArrayList<PasswordStatusDBModel>();
            liveListDetailUnlcked = new ArrayList<LiveStreamsDBModel>();
            liveListDetailUnlckedDetail = new ArrayList<LiveStreamsDBModel>();
            liveListDetailAvailable = new ArrayList<LiveStreamsDBModel>();
            liveListDetail = new ArrayList<LiveStreamsDBModel>();
            int parentalStatusCount = liveStreamDBHandler.getParentalStatusCount();
            if (parentalStatusCount > 0 && allChannels!=null) {
                listPassword = getPasswordSetCategories();
                if(listPassword!=null) {
                    liveListDetailUnlckedDetail = getUnlockedCategories(allChannels,
                            listPassword);
                }
                liveListDetailAvailable = liveListDetailUnlckedDetail;
            } else {
                liveListDetailAvailable = allChannels;
            }

            if(liveListDetailAvailable!=null) {
                for (int i = 0; i < liveListDetailAvailable.size(); i++) {
                    String channelName = liveListDetailAvailable.get(i).getName();
                    String channelID = liveListDetailAvailable.get(i).getEpgChannelId();
                    String streamIcon = liveListDetailAvailable.get(i).getStreamIcon();
                    String streamID = liveListDetailAvailable.get(i).getStreamId();
                    String num = liveListDetailAvailable.get(i).getNum();
                    String epgChannelId = liveListDetailAvailable.get(i).getEpgChannelId();
                    String catggID = liveListDetailAvailable.get(i).getCategoryId();

                    currentChannel = new EPGChannel(streamIcon, channelName, i, streamID,num,epgChannelId,catggID);
                    if (firstChannel == null) {
                        firstChannel = currentChannel;
                    }
                    if (prevChannel != null) {
                        currentChannel.setPreviousChannel(prevChannel);
                        prevChannel.setNextChannel(currentChannel);
                    }
                    prevChannel = currentChannel;
                    List<EPGEvent> epgEvents = new ArrayList<>();
                    map.put(currentChannel, epgEvents);

                    if (!channelID.equals("")) {
                        ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = liveStreamDBHandler.getEPG(channelID);
                        String startDateTime;
                        String stopDateTime;
                        String Title;
                        String Desc;
                        Long epgTempStop = null;

                        if (xmltvProgrammePojos != null && xmltvProgrammePojos.size()!=0) {
                            for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                                startDateTime = xmltvProgrammePojos.get(j).getStart();
                                stopDateTime = xmltvProgrammePojos.get(j).getStop();
                                Title = xmltvProgrammePojos.get(j).getTitle();
                                Desc = xmltvProgrammePojos.get(j).getDesc();
                                Long epgStartDateToTimestamp = Utils.epgTimeConverter(startDateTime);
                                Long epgStopDateToTimestamp = Utils.epgTimeConverter(stopDateTime);

                                if(epgTempStop!=null && epgStartDateToTimestamp.equals(epgTempStop)){
                                    EPGEvent epgEvent = new EPGEvent(currentChannel, epgStartDateToTimestamp, epgStopDateToTimestamp, Title, streamIcon, Desc);
                                    if (prevEvent != null) {
                                        epgEvent.setPreviousEvent(prevEvent);
                                        prevEvent.setNextEvent(epgEvent);
                                    }
                                    prevEvent = epgEvent;
                                    currentChannel.addEvent(epgEvent);
                                    epgEvents.add(epgEvent);
                                }else{
                                    if(epgTempStop!=null) {
                                        EPGEvent epgEvent = new EPGEvent(currentChannel, epgTempStop, epgStartDateToTimestamp, context.getResources().getString(R.string.no_information), streamIcon, "");
                                        if (prevEvent != null) {
                                            epgEvent.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(epgEvent);
                                        }
                                        prevEvent = epgEvent;
                                        currentChannel.addEvent(epgEvent);
                                        epgEvents.add(epgEvent);


                                        EPGEvent epgEvent1 = new EPGEvent(currentChannel, epgStartDateToTimestamp, epgStopDateToTimestamp, Title, streamIcon, Desc);
                                        if (prevEvent != null) {
                                            epgEvent1.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(epgEvent1);
                                        }
                                        prevEvent = epgEvent1;
                                        currentChannel.addEvent(epgEvent1);
                                        epgEvents.add(epgEvent1);

                                    }else{
                                        EPGEvent epgEvent1 = new EPGEvent(currentChannel, epgStartDateToTimestamp, epgStopDateToTimestamp, Title, streamIcon, Desc);
                                        if (prevEvent != null) {
                                            epgEvent1.setPreviousEvent(prevEvent);
                                            prevEvent.setNextEvent(epgEvent1);
                                        }
                                        prevEvent = epgEvent1;
                                        currentChannel.addEvent(epgEvent1);
                                        epgEvents.add(epgEvent1);
                                    }
                                }
                                epgTempStop = epgStopDateToTimestamp;


                            }
                        }else{
                            long nowTime = System.currentTimeMillis();
                            long starttesting1 = nowTime - Long.parseLong("86400000");
                            long endtesting1 = starttesting1 + Long.parseLong("7200000");
                            for(int k=0;k<50;k++){
                                EPGEvent epgEvent = new EPGEvent(currentChannel, starttesting1, endtesting1, context.getResources().getString(R.string.no_information), streamIcon, "");
                                if (prevEvent != null) {
                                    epgEvent.setPreviousEvent(prevEvent);
                                    prevEvent.setNextEvent(epgEvent);
                                }
                                prevEvent = epgEvent;
                                currentChannel.addEvent(epgEvent);
                                epgEvents.add(epgEvent);
                                starttesting1 = endtesting1;
                                endtesting1 = starttesting1 + Long.parseLong("7200000");
                            }
                        }
                    }else{
                        long nowTime = System.currentTimeMillis();
                        long starttesting = nowTime - Long.parseLong("86400000");
                        long endtesting = starttesting + Long.parseLong("7200000");
                        for(int k=0;k<50;k++){
                            EPGEvent epgEvent = new EPGEvent(currentChannel, starttesting, endtesting, context.getResources().getString(R.string.no_information), streamIcon, "");
                            if (prevEvent != null) {
                                epgEvent.setPreviousEvent(prevEvent);
                                prevEvent.setNextEvent(epgEvent);
                            }
                            prevEvent = epgEvent;
                            currentChannel.addEvent(epgEvent);
                            epgEvents.add(epgEvent);
                            starttesting = endtesting;
                            endtesting = starttesting + Long.parseLong("7200000");
                        }
                    }
                }
            }
            if (currentChannel != null) {
                currentChannel.setNextChannel(firstChannel);
            }
            if (firstChannel != null) {
                firstChannel.setPreviousChannel(currentChannel);
            }
            EPGData data = new EPGDataImpl(map);
            return data;

        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    private ArrayList<LiveStreamsDBModel> getUnlockedCategories(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }
}
