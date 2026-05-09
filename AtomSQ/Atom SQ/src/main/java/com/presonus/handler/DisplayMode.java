// Written by James Bell
// (c) 2023
// Licensed under GPLv3 - https://www.gnu.org/licenses/gpl-3.0.txt

package com.presonus.handler;

import java.util.Map;

import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorBrowserResultItem;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.api.util.midi.SysexBuilder;
//V1.1
import com.bitwig.extension.controller.api.PopupBrowser;

import com.bitwig.extensions.framework.Layer;

import com.presonus.AtomSQExtension;
@SuppressWarnings("OctalInteger")
public class DisplayMode
{
    public enum ControllerMode {
        SONG, SONG2, INST, INST_EMPTY, INST2, INST3, EDIT, USER, BROWSER
    }

    private enum PanelFocus { ABOVE, BELOW, NONE }

    private record DisplayConfig(
        String notification,
        PanelFocus panelFocus,
        String[] buttonTitles,
        String buttonColor,
        boolean resetEncoder9,
        boolean useKeyboardConfig,
        String extraSysex
    ) {}

    private static final String SYSEX_DISPLAY_INIT    = "F0000106221300F7";
    private static final String SYSEX_BUTTON_CONFIG   = "F0000106221400F7";
    private static final String SYSEX_KEYBOARD_CONFIG = "F0000106221401F7";
    private static final String SYSEX_LIVE_MODE       = "F0000106221301F7";
    private static final String SYSEX_EXTRA_LINE      = "F0 00 01 06 22 12 06 00 5B 5B 00 F7";

    private static final SysexHandler sH = new SysexHandler();

    private static final Map<ControllerMode, DisplayConfig> DISPLAY_CONFIGS = Map.ofEntries(
        Map.entry(ControllerMode.SONG, new DisplayConfig(
            "Tracks", PanelFocus.ABOVE,
            new String[]{"Mute", "Solo", "Arm", "", "Move Up", "Move Down"},
            sH.yellow, false, false, null)),
        Map.entry(ControllerMode.SONG2, new DisplayConfig(
            "Tracks", PanelFocus.NONE,
            new String[]{"Active", "Copy", "Delete", "New Audio", "New Inst", "New FX"},
            sH.yellow, false, false, null)),
        Map.entry(ControllerMode.INST, new DisplayConfig(
            "Devices", PanelFocus.BELOW,
            new String[]{"Enabled", "Wndw", "Expand", "RCtrls", "RCPage Down", "RCPage Up"},
            sH.white, true, false, null)),
        Map.entry(ControllerMode.INST_EMPTY, new DisplayConfig(
            "Devices", PanelFocus.BELOW,
            new String[]{"New Dev", "New Dev", "New Dev", "New Dev", "New Dev", "New Dev"},
            sH.white, true, false, null)),
        Map.entry(ControllerMode.INST2, new DisplayConfig(
            null, PanelFocus.NONE,
            new String[]{"", "Copy", "Delete", "<New", "Preset", "New>"},
            sH.white, true, false, null)),
        Map.entry(ControllerMode.INST3, new DisplayConfig(
            null, PanelFocus.NONE,
            new String[]{"", "", "", "", "Move Left", "Move Right"},
            sH.white, true, false, null)),
        Map.entry(ControllerMode.EDIT, new DisplayConfig(
            "Nothing to see here", PanelFocus.NONE,
            new String[]{"", "", "", "", "", ""},
            sH.yellow, false, false, SYSEX_EXTRA_LINE)),
        Map.entry(ControllerMode.USER, new DisplayConfig(
            "Keyboard", PanelFocus.NONE,
            null, null, false, true, null)),
        Map.entry(ControllerMode.BROWSER, new DisplayConfig(
            "Browser", PanelFocus.NONE,
            new String[]{"", "", "", "Preview", "Cancel", "OK"},
            sH.magenta, false, false, SYSEX_EXTRA_LINE))
    );

   private  MidiOut dMidiOut;
   private ControllerHost dHost;
   private CursorTrack dCursorTrack;
   private CursorDevice dCursorDevice;
    public ControllerMode lastMode = ControllerMode.SONG;
   private Layer dBrowserLayer;
   public CursorBrowserResultItem dBrowserResult;
   private Application dApplication;
   //V1.1
   private Layer dInstEmptyLayer;
   private Layer dDeviceBrowserLayer;
   private PopupBrowser dPopupBrowser;

   //V2.0
   private Layer dRCLayer;

    public void start(AtomSQExtension Ext)
   {
       dHost = Ext.mHost;
      //dASQCE.mHost.println("dhost is: "+dHost.getHostProduct().toString());
      dMidiOut = Ext.mMidiOut;
      dBrowserLayer = Ext.mBrowserLayer;
      dApplication = Ext.mApplication;
      dCursorTrack = Ext.mCursorTrack;
      dCursorDevice = Ext.mCursorDevice;
      dBrowserResult = Ext.mBrowserResult;
      //V1.1
      dInstEmptyLayer = Ext.mInstEmptyLayer;
      dDeviceBrowserLayer = Ext.mDeviceBrowserLayer;
      dPopupBrowser = Ext.mPopupBrowser;
      //V2.0
       dRCLayer = Ext.mRCLayer;

    }
      
   public void updateDisplay ()
   {
      //V1.1 Preset Browser. This needs to be above the standard browser layer, as both ar active at the same time. 
       String dPopupBrowsertype;
       if(dDeviceBrowserLayer.isActive()){
         //Main line 1 
         dPopupBrowsertype = dPopupBrowser.selectedContentTypeName().get();
         String pTrack = dCursorTrack.name().get();
         byte[] sysex2 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString("Track: ", 7).addString(pTrack, pTrack.length()).terminate();
            dMidiOut.sendSysex(sysex2);

         //Main line 2
         String pDevice = dBrowserResult.name().get();
         byte[] sysex3 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.magenta).addByte(sH.spc).addString(dPopupBrowsertype, dPopupBrowsertype.length()).addString(": ", 2).addString(pDevice, pDevice.length()).terminate();
         dMidiOut.sendSysex(sysex3);

      }
      else if (dBrowserLayer.isActive()){
         dPopupBrowsertype = dPopupBrowser.selectedContentTypeName().get();
         //Main line 1 
         String pDev = dCursorDevice.name().get();
         byte[] sysex3 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString("Device: ", 8).addString(pDev, pDev.length()).terminate();
         dMidiOut.sendSysex(sysex3);
         //Main line 2      

         String pRes = dBrowserResult.name().get();
         byte[] sysex2 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.magenta).addByte(sH.spc).addString(dPopupBrowsertype, dPopupBrowsertype.length()).addString(": ", 2).addString(pRes, pRes.length()).terminate();
         dMidiOut.sendSysex(sysex2);
      }

      //V1.1 adding extra case for empty, to say "add a device"
      else if (dInstEmptyLayer.isActive()){
         //Main line 1 
         String pTrack = dCursorTrack.name().get();
         byte[] sysex2 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString("Track: ", 7).addString(pTrack, pTrack.length()).terminate();
            dMidiOut.sendSysex(sysex2);
         //Main line 2
         byte[] sysex3 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.white).addByte(sH.spc).addString("Add a Device :) ", 15).terminate();
         dMidiOut.sendSysex(sysex3);
      }
      //V1.1 adding DeviceBrowser option


       //V2.0 going to add one for Remote Control name and value
       else if (dRCLayer.isActive()){
           //Main line 1
           //String pTrack = dCursorTrack.name().get();
           byte[] sysex2 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString("RC:", 3).terminate();
           dMidiOut.sendSysex(sysex2);
           //Main line 2
           byte[] sysex3 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.white).addByte(sH.spc).addString("Value:", 6).terminate();
           dMidiOut.sendSysex(sysex3);
       }

      else 
            {
         //Main line 1 
         String pTrack = dCursorTrack.name().get();
         byte[] sysex2 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString("Track: ", 7).addString(pTrack, pTrack.length()).terminate();
            dMidiOut.sendSysex(sysex2);
         //Main line 2
         String pDev = dCursorDevice.name().get();
         byte[] sysex3 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.white).addByte(sH.spc).addString("Device: ", 8).addString(pDev, pDev.length()).terminate();
         dMidiOut.sendSysex(sysex3);
      }

      //adding momentary display modes here (encoders)

   }

   public void initHW()
   {
      //the HW init
      dMidiOut.sendMidi(176,29,00);
      dMidiOut.sendMidi(176,15,00);
      dMidiOut.sendMidi(176,16,00);
      dMidiOut.sendMidi(176,17,00);
      dMidiOut.sendMidi(176,18,00);
      dMidiOut.sendMidi(176,19,00);
      dMidiOut.sendMidi(176,20,00);
      dMidiOut.sendMidi(176,21,00);
      dMidiOut.sendMidi(143,00,00);

      dMidiOut.sendMidi(176,29,00);
      dMidiOut.sendMidi(176,15,00);
      dMidiOut.sendMidi(176,16,00);
      dMidiOut.sendMidi(176,17,00);
      dMidiOut.sendMidi(176,18,00);
      dMidiOut.sendMidi(176,19,00);
      dMidiOut.sendMidi(176,20,00);
      dMidiOut.sendMidi(176,21,00);
      dMidiOut.sendMidi(143,00,00);

      dMidiOut.sendMidi(176,29,00);
      dMidiOut.sendMidi(176,15,00);
      dMidiOut.sendMidi(176,16,00);
      dMidiOut.sendMidi(176,17,00);
      dMidiOut.sendMidi(176,18,00);
      dMidiOut.sendMidi(176,19,00);
      dMidiOut.sendMidi(176,20,00);
      dMidiOut.sendMidi(176,21,00);
      dMidiOut.sendMidi(143,00,00);

      dMidiOut.sendSysex("F07E7F0601F7");
      dMidiOut.sendSysex("F07E7F0601F7");
      dMidiOut.sendSysex("F07E7F0601F7");
      //Live Mode handshake
      dMidiOut.sendMidi(143,00,01);
      // this line alone turns on the lights (paste in the console)
      dMidiOut.sendSysex(SYSEX_DISPLAY_INIT);
      // SYSEX_KEYBOARD_CONFIG ("...1401F7") can replace SYSEX_LIVE_MODE below if nav keys
      // should not send MIDI messages; current choice keeps them active
      dMidiOut.sendSysex(SYSEX_LIVE_MODE);
   }

   public void applyMode(ControllerMode mode)
   {
      // BROWSER is transient — don't overwrite lastMode so it can be restored on close
      if (mode != ControllerMode.BROWSER)
         lastMode = mode;
      DisplayConfig cfg = DISPLAY_CONFIGS.get(mode);
      if (cfg == null) return;

      if (cfg.notification() != null)
         dHost.showPopupNotification(cfg.notification());

      switch (cfg.panelFocus()) {
         case ABOVE -> dApplication.focusPanelAbove();
         case BELOW -> dApplication.focusPanelBelow();
         case NONE  -> {}
      }

      if (!cfg.useKeyboardConfig()) {
         dMidiOut.sendSysex(SYSEX_DISPLAY_INIT);
         dMidiOut.sendSysex(SYSEX_BUTTON_CONFIG);

         if (cfg.buttonTitles() != null) {
            for (int i = 0; i < cfg.buttonTitles().length; i++) {
               String msg = cfg.buttonTitles()[i];
               byte[] sysex = SysexBuilder.fromHex(sH.sheader)
                  .addByte(sH.sButtonsTitle[i])
                  .addHex(cfg.buttonColor())
                  .addByte(sH.spc)
                  .addString(msg, msg.length())
                  .terminate();
               dMidiOut.sendSysex(sysex);
            }
         }

         if (cfg.resetEncoder9())
            dMidiOut.sendMidi(176, 29, 0);

         if (cfg.extraSysex() != null)
            dMidiOut.sendSysex(cfg.extraSysex());
      } else {
         dMidiOut.sendSysex(SYSEX_KEYBOARD_CONFIG);
      }

      dMidiOut.sendSysex(SYSEX_LIVE_MODE);
   }

   public void SongMode()      { applyMode(ControllerMode.SONG); }
   public void Song2Mode()     { applyMode(ControllerMode.SONG2); }
   public void InstMode()      { applyMode(ControllerMode.INST); }
   public void InstEmptyMode() { applyMode(ControllerMode.INST_EMPTY); }
   public void Inst2Mode()     { applyMode(ControllerMode.INST2); }
   public void Inst3Mode()     { applyMode(ControllerMode.INST3); }
   public void EditMode()      { applyMode(ControllerMode.EDIT); }
   public void UserMode()      { applyMode(ControllerMode.USER); }
   public void BrowserMode()   { applyMode(ControllerMode.BROWSER); }

}
