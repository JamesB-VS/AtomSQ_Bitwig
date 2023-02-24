package com.presonus.handler;


import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorDeviceFollowMode;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;

public class CursorHandler
{

    CursorTrack cursorTrack;
    CursorDevice cursorDevice;
    CursorRemoteControlsPage remoteControlsBank;

    public CursorHandler (CursorDevice cursorDevice, CursorTrack cursorTrack, CursorRemoteControlsPage remoteControlsBank)
    {
        this.cursorTrack = cursorTrack;
        this.cursorDevice = cursorDevice;
        this.remoteControlsBank = remoteControlsBank;

    // var followMode = CursorDeviceFollowMode.FOLLOW_SELECTION;
    //cursorTrack = host.createCursorTrack(2, 0);
    // cursorDevice = cursorTrack.createCursorDevice("Current", "Current", 8, followMode);
   // remoteControlsBank = cursorDevice.createCursorRemoteControlsPage("CursorPage1", 8, "");

    this.cursorDevice.isEnabled ().markInterested ();
    this.cursorDevice.isWindowOpen ().markInterested ();
    this.cursorTrack.solo().markInterested();
    this.cursorTrack.mute().markInterested();
    this.cursorTrack.arm().markInterested();
    this.cursorTrack.volume().markInterested();
    this.cursorTrack.pan().markInterested();
    this.cursorTrack.isActivated ().markInterested();
    this.cursorTrack.color ().markInterested();
    }





}

