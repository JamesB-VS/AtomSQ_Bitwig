// Written by James Bell
// (c) 2023
// Licensed under GPLv3 - https://www.gnu.org/licenses/gpl-3.0.txt

package com.presonus.handler;

import java.lang.reflect.Method;

import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.Browser;
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
public class DisplayMode  
{
    private static SysexHandler sH = new SysexHandler();
   // private static MidiIn dMidiIn;
   private  MidiOut dMidiOut;
   private ControllerHost dHost;
   private CursorTrack dCursorTrack;
   private CursorDevice dCursorDevice;
   private AtomSQExtension dASQCE;
   public Method dLastMode;
   private Layer dBrowserLayer;
   public CursorBrowserResultItem dBrowserResult;
   private Application dApplication;
   //V1.1
   private Layer dInstEmptyLayer;
   private Layer dDeviceBrowserLayer;
   private PopupBrowser dPopupBrowser;
   private String dPopupBrowsertype;

   public void start(AtomSQExtension Ext)
   {
      dASQCE = Ext;
      dHost = dASQCE.mHost;
      //dASQCE.mHost.println("dhost is: "+dHost.getHostProduct().toString());
      dMidiOut = dASQCE.mMidiOut;
      dBrowserLayer = dASQCE.mBrowserLayer;
      dApplication = dASQCE.mApplication;
      dCursorTrack = dASQCE.mCursorTrack;
      dCursorDevice = dASQCE.mCursorDevice;
      dBrowserResult = dASQCE.mBrowserResult;
      //V1.1
      dInstEmptyLayer = dASQCE.mInstEmptyLayer;
      dDeviceBrowserLayer = dASQCE.mDeviceBrowserLayer;
      dPopupBrowser = dASQCE.mPopupBrowser;

    }
      
   public void updateDisplay ()
   {
      //V1.1 Preset Browser. This needs to be above the standard browser layer, as both ar active at the same time. 
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
      //this line alone turns on the lights (paste in the console)
      dMidiOut.sendSysex("F0000106221300F7");
      //this line alone makes the Inst menu at least come back to life!
      //it also takes command of the nav keys on the right...if set to 0, the display still shows and navigates, but thie keys ALSO send midi messages
      //dMidiOut.sendSysex("F0000106221401F7");
      dMidiOut.sendSysex("F0000106221301F7");
   }

   //changing private to public for the mode finder bits in the layer change above
   public void SongMode ()
   {
      //dHost.println("SongMode");
      dHost.showPopupNotification("Tracks");
      dApplication.focusPanelAbove();

      //dApplication.setPanelLayout("MIX");
      dMidiOut.sendSysex("F0000106221300F7");
      dMidiOut.sendSysex("F0000106221400F7");
   
      //button titles
      String[] mTitles= {"Mute", "Solo", "Arm", "", "Move Up", "Move Down"};
      for (int i = 0; i < 6; i++) 
      {
         final String msg = mTitles[i];
         byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.yellow).addByte(sH.spc).addString(msg, msg.length()).terminate();
         dMidiOut.sendSysex(sysex);
      }
   dMidiOut.sendSysex("F0000106221301F7");
   }
 



   public void Song2Mode ()
   {
      //dHost.println("SongMode");
      dHost.showPopupNotification("Tracks");
      //dApplication.setPanelLayout("MIX");
      dMidiOut.sendSysex("F0000106221300F7");
      dMidiOut.sendSysex("F0000106221400F7");
   
      //button titles
      String[] mTitles= {"Active", "Copy", "Delete", "New Audio", "New Inst", "New FX"};
      for (int i = 0; i < 6; i++) 
      {
         final String msg = mTitles[i];
         byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.yellow).addByte(sH.spc).addString(msg, msg.length()).terminate();
         dMidiOut.sendSysex(sysex);
      }
      dMidiOut.sendSysex("F0000106221301F7");
   }
 
   public void InstMode ()
   {
   dHost.showPopupNotification("Devices");
      dApplication.focusPanelBelow();
      dHost.println("InstMode");
      //dHost.showPopupNotification("Instrument Mode");
      //dApplication.setPanelLayout("ARRANGE");

      //configure display
      dMidiOut.sendSysex("F0000106221300F7");
      dMidiOut.sendSysex("F0000106221400F7");
   
      //button titles
      String[] mTitles= {"Enabled", "Wndw", "Expand", "RCtrls", "Move Left", "Move Right"};
      for (int i = 0; i < 6; i++) 
      {
         final String msg = mTitles[i];
         byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.white).addByte(sH.spc).addString(msg, msg.length()).terminate();
         dMidiOut.sendSysex(sysex);
      }

      // Encoder 9...must recenter it? 00 and 127 have no other visible effect.
      dMidiOut.sendMidi(176, 29, 00);

      //turn on button light
      dMidiOut.sendSysex("F0000106221301F7");
   }
 
//v1.1 Adding new mode for an empty track. only new device button
   //changing private to public for the mode finder bits in the layer change above
public void InstEmptyMode ()
   {
   dHost.showPopupNotification("Devices");
      dApplication.focusPanelBelow();
      dHost.println("InstEmptyMode");
      //dHost.showPopupNotification("Instrument Mode");
      //dApplication.setPanelLayout("ARRANGE");

      //configure display
      dMidiOut.sendSysex("F0000106221300F7");
      dMidiOut.sendSysex("F0000106221400F7");
   
      //button titles
      String[] mTitles= {"New Dev","New Dev","New Dev","New Dev","New Dev","New Dev",};
      for (int i = 0; i < 6; i++) 
      {
         final String msg = mTitles[i];
         byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.white).addByte(sH.spc).addString(msg, msg.length()).terminate();
         dMidiOut.sendSysex(sysex);
      }

      // Encoder 9...must recenter it? 00 and 127 have no other visible effect.
      dMidiOut.sendMidi(176, 29, 00);

      //turn on button light
      dMidiOut.sendSysex("F0000106221301F7");
   }



    public void Inst2Mode ()
    {
       //dHost.println("InstMode");
       //dHost.showPopupNotification("Instrument Mode");
       //dApplication.setPanelLayout("ARRANGE");

       //configure display
       dMidiOut.sendSysex("F0000106221300F7");
       dMidiOut.sendSysex("F0000106221400F7");
      
       //button titles
       String[] mTitles= {"", "Copy", "Delete", "<New", "Preset", "New>"};
       
       for (int i = 0; i < 6; i++) 
       {
          final String msg = mTitles[i];
          byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.white).addByte(sH.spc).addString(msg, msg.length()).terminate();
          dMidiOut.sendSysex(sysex);
       }
       // Encoder 9...must recenter it? 00 and 127 have no other visible effect.
       dMidiOut.sendMidi(176, 29, 00);
       dMidiOut.sendSysex("F0000106221301F7");
    }
 
    public void EditMode ()
    {
      //dHost.println("EditMode");
      dHost.showPopupNotification("Nothing to see here");

      dMidiOut.sendSysex("F0000106221300F7");
      dMidiOut.sendSysex("F0000106221400F7");

      //button titles
      String[] mTitles= {"", "", "", "", "", ""};
      
      for (int i = 0; i < 6; i++) 
      {
      final String msg = mTitles[i];
         byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.yellow).addByte(sH.spc).addString(msg, msg.length()).terminate();
         dMidiOut.sendSysex(sysex);
      }

      //line 1
      dMidiOut.sendSysex("F0 00 01 06 22 12 06 00 5B 5B 00 F7");
 
      dMidiOut.sendSysex("F0000106221301F7");
    }
 
    public void UserMode ()
    {
      dHost.println("Keyboard");
      dHost.showPopupNotification("Keyboard");
      dMidiOut.sendSysex("F0000106221401F7");
      dMidiOut.sendSysex("F0000106221301F7");
    }
 
    public void BrowserMode ()
    {
      //dHost.println("EditMode");
      dHost.showPopupNotification("Browser");
      dMidiOut.sendSysex("F0000106221300F7");
      dMidiOut.sendSysex("F0000106221400F7");

      //button titles
      String[] mTitles= {"", "", "", "Preview", "Cancel", "OK"};
      for (int i = 0; i < 6; i++) 
      {
      final String msg = mTitles[i];
         byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.magenta).addByte(sH.spc).addString(msg, msg.length()).terminate();
         dMidiOut.sendSysex(sysex);
      }
      dMidiOut.sendSysex("F0 00 01 06 22 12 06 00 5B 5B 00 F7");
      dMidiOut.sendSysex("F0000106221301F7");
    }

 
}
