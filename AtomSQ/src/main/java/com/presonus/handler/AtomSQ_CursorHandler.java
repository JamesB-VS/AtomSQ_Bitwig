function CursorHandler()
{

    followMode = CursorDeviceFollowMode.FOLLOW_SELECTION;
    cursorTrack = host.createCursorTrack(2, 0);
    cursorDevice = cursorTrack.createCursorDevice("Current", "Current", 8, followMode);
    remoteControlsBank = cursorDevice.createCursorRemoteControlsPage("CursorPage1", 8, "");

    cursorDevice.isEnabled ().markInterested ();
    cursorDevice.isWindowOpen ().markInterested ();
    cursorTrack.solo().markInterested();
    cursorTrack.mute().markInterested();
    cursorTrack.arm().markInterested();
    cursorTrack.volume().markInterested();
    cursorTrack.pan().markInterested();
    cursorTrack.isActivated ().markInterested();
    cursorTrack.color ().markInterested();

}

