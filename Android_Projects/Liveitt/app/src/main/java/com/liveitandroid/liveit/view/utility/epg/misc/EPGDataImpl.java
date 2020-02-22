package com.liveitandroid.liveit.view.utility.epg.misc;

import com.liveitandroid.liveit.view.utility.epg.domain.EPGChannel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liveitandroid.liveit.view.utility.epg.EPGData;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGChannel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EPGDataImpl implements EPGData {
    private List<EPGChannel> channels = Lists.newArrayList();
    private Map<String, EPGChannel> channelsByName = new HashMap();

    public EPGDataImpl(Map<EPGChannel, List<EPGEvent>> data) {
        this.channels = Lists.newArrayList(data.keySet());
        indexChannels();
    }

    public EPGChannel getChannel(int position) {
        return (EPGChannel) this.channels.get(position);
    }

    public EPGChannel getOrCreateChannel(String channelName, String imageUrl, String streamID, String num, String epgChannelID, String catgrID) {
        EPGChannel channel = (EPGChannel) this.channelsByName.get(num);
        return channel != null ? channel : addNewChannel(channelName, imageUrl, streamID, num, epgChannelID,catgrID);
    }

    public List<EPGEvent> getEvents(int channelPosition) {
        return ((EPGChannel) this.channels.get(channelPosition)).getEvents();
    }

    public EPGEvent getEvent(int channelPosition, int programPosition) {
        return (EPGEvent) ((EPGChannel) this.channels.get(channelPosition)).getEvents().get(programPosition);
    }

    public int getChannelCount() {
        return this.channels.size();
    }

    public boolean hasData() {
        return !this.channels.isEmpty();
    }

    public EPGChannel addNewChannel(String channelName, String imageUrl, String streamID, String num, String epgChannelID, String catgrID) {
        int newChannelID = this.channels.size();
        EPGChannel newChannel = new EPGChannel(imageUrl, channelName, newChannelID, streamID, num, epgChannelID, catgrID);
        if (newChannelID > 0) {
            EPGChannel previousChannel = (EPGChannel) this.channels.get(newChannelID - 1);
            previousChannel.setNextChannel(newChannel);
            newChannel.setPreviousChannel(previousChannel);
        }
        this.channels.add(newChannel);
        this.channelsByName.put(newChannel.getNum(), newChannel);
        return newChannel;
    }

    private void indexChannels() {
        this.channelsByName = Maps.newLinkedHashMap();
        for (int j = 0; j < this.channels.size(); j++) {
            EPGChannel channel = (EPGChannel) this.channels.get(j);
            this.channelsByName.put(channel.getNum(), channel);
        }
    }
}
