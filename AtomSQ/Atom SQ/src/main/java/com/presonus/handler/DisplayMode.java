package com.presonus.handler;

import java.lang.reflect.Method;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.api.util.midi.SysexBuilder;
import com.presonus.AtomSQExtension;


public class DisplayMode  {

    
   //Variables from Hardware
   // Transport
   private final static int  CC_PLAY      = 109;
   private final static int  CC_STOP      = 111;
   private final static int  CC_REC       = 107;
   private final static int  CC_METRONOME = 105;
   //Menu Buttons
   private final static int  CC_SONG      = 32;
   private final static int  CC_INST      = 33;
   private final static int  CC_EDIT      = 34;
   private final static int  CC_USER      = 35;
   //Arrow Buttons
   private final static int  CC_UP       =87;
   private final static int  CC_DOWN      =89;
   private final static int  CC_LEFT      =90;
   private final static int  CC_RIGHT      =102;
   //shift
   private final static int  CC_SHIFT     =31;
   //A-H (only A works in Live mode)
   private final static int  CC_BTN_A     =64;
   //private final static int  CHAN_1 = 176;
   //private final static int  CHAN_2 = 177;
   //Display Buttons, top to bottom, left to right
   //1 2 3
   //[   ]
   //4 5 6
   private final static int  CC_BTN_1     =36;
   private final static int  CC_BTN_2     =37;
   private final static int  CC_BTN_3     =38;
   private final static int  CC_BTN_4     =39;
   private final static int  CC_BTN_5     =40;
   private final static int  CC_BTN_6     =41;
   //outside of the INST menu, the encoder and buttons on the right
   private final static int  CC_BACK      =42;
   private final static int  CC_FORWARD    =43;
   private final static int  CC_ENCODER_9     =29;
   //device name, note on, note off, pressure, ribbon, pitchbend
   private final static String  DEV_NAME      = "Keyboard";
   private final static String  NOTE_ON       = "99????";
   private final static String  NOTE_OFF      = "89????";
   private final static String  NOTE_PRES     = "a9????"; //poly aftertouch
   private final static String  NOTE_MOD      = "b001??";
   private final static String  NOTE_BEND     = "e0????";
   //Encoders
   private final static int  CC_ENCODER_1     = 14;
   //Enc 2-8 not needed because the encoders are created in an iteration below. 

   //ATOM colors
   private static final Color WHITE = Color.fromRGB(1, 1, 1);
   private static final Color BLACK = Color.fromRGB(0, 0, 0);
   private static final Color RED = Color.fromRGB(1, 0, 0);
   private static final Color DIM_RED = Color.fromRGB(0.3, 0.0, 0.0);
   private static final Color GREEN = Color.fromRGB(0, 1, 0);
   private static final Color ORANGE = Color.fromRGB(1, 1, 0);
   private static final Color BLUE = Color.fromRGB(0, 0, 1);
   private static final ControllerHost mHost = null;
   
    private static SysexHandler sH = new SysexHandler();
   // private static MidiIn dMidiIn;
   private  MidiOut dMidiOut;
   private ControllerHost dHost;
   private CursorTrack dCursorTrack;
    
   private CursorDevice dCursorDevice;
   private AtomSQExtension dASQCE;
   public Method dLastMode;
 
    private static Application dApplication;

    public void init(AtomSQExtension Ext){
      dASQCE = Ext;
      dHost = dASQCE.mHost;
      dMidiOut = dASQCE.mMidiOut;
   
      
      dCursorTrack = dASQCE.mCursorTrack;
      dCursorDevice = dASQCE.mCursorDevice;
 
    }
       

    public void updateDisplay ()
    {
        if(!dASQCE.mBrowserLayer.isActive())
        {
    //CursorTrack mCursorTrack = track;
       //Main line 1 
       String pTrack = dCursorTrack.name().get();
       byte[] sysex2 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString("Track: ", 7).addString(pTrack, pTrack.length()).terminate();
          dMidiOut.sendSysex(sysex2);
       // String pLayout = dApplication.panelLayout().get();
       // byte[] sysex2 = sB.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString(pLayout, pLayout.length()).terminate();
       //    dMidiOut.sendSysex(sysex2);
 
       //Main line 2
       String pDev = dCursorDevice.name().get();
       byte[] sysex3 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.white).addByte(sH.spc).addString("Device: ", 8).addString(pDev, pDev.length()).terminate();
          dMidiOut.sendSysex(sysex3);
        }

        else {
            String pDev = dCursorDevice.name().get();
       byte[] sysex3 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString("Device: ", 8).addString(pDev, pDev.length()).terminate();
          dMidiOut.sendSysex(sysex3);

          String pTrack = dASQCE.mBrowserResult.name().get();
         byte[] sysex2 = SysexBuilder.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.magenta).addByte(sH.spc).addString("Preset: ", 8).addString(pTrack, pTrack.length()).terminate();
            dMidiOut.sendSysex(sysex2);


        }

    }
 
    public void initHW(){
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
      // this.portOut.sendSysex("F0000106221401F7");
      dMidiOut.sendSysex("F0000106221301F7");
    }

    //changing private to public for the mode finder bits in the layer change above
    public void SongMode ()
    {
       //dHost.println("SongMode");
       dHost.showPopupNotification("Tracks");
       dApplication.focusPanelAbove();
 
       //dApplication.setPanelLayout("MIX");
 
       //lights on buttons
       dMidiOut.sendMidi(176, CC_SONG, 127);
       dMidiOut.sendMidi(176, CC_INST, 00);
       dMidiOut.sendMidi(176, CC_EDIT, 00);
       dMidiOut.sendMidi(176, CC_USER, 00);
 
 
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
 
 
       // //Main line 1 
       // String pLayout = dApplication.panelLayout().get();
       // byte[] sysex2 = sB.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString(pLayout, pLayout.length()).terminate();
       //    dMidiOut.sendSysex(sysex2);
 
       // //Main line 2
       // String pTrack = mCursorTrack.name().get();
       // byte[] sysex3 = sB.fromHex(sH.sheader).addByte(sH.MainL2).addHex(sH.yellow).addByte(sH.spc).addString(pTrack, pTrack.length()).terminate();
       //    dMidiOut.sendSysex(sysex3);

      dMidiOut.sendSysex("F0000106221301F7");
    

    }
 
    public void Song2Mode ()
    {
       //dHost.println("SongMode");
       dHost.showPopupNotification("Tracks");
      
       //dApplication.setPanelLayout("MIX");
 
       //lights on buttons
       dMidiOut.sendMidi(176, CC_SONG, 127);
       dMidiOut.sendMidi(176, CC_INST, 00);
       dMidiOut.sendMidi(176, CC_EDIT, 00);
       dMidiOut.sendMidi(176, CC_USER, 00);
 
 
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
       //dHost.println("InstMode");
       //dHost.showPopupNotification("Instrument Mode");
       //dApplication.setPanelLayout("ARRANGE");
       //activate layer, deactivate others (for encoders)
      // mInstLayer.activate();
 
         //lights on buttons
         dMidiOut.sendMidi(176, CC_SONG, 00);
         dMidiOut.sendMidi(176, CC_INST, 127);
         dMidiOut.sendMidi(176, CC_EDIT, 00);
         dMidiOut.sendMidi(176, CC_USER, 00);
 
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
   
    public void Inst2Mode ()
    {
       //dHost.println("InstMode");
       //dHost.showPopupNotification("Instrument Mode");
       //dApplication.setPanelLayout("ARRANGE");
       //activate layer, deactivate others (for encoders)
      // mInstLayer.activate();
       //configure display
       dMidiOut.sendSysex("F0000106221300F7");
       dMidiOut.sendSysex("F0000106221400F7");
      
       //button titles
       //temporarily removing the bits that do not yet work yet
       //String[] mTitles= {"Source", "Dest", "MonMode", "Expand", "Macro", "Controls"};
       String[] mTitles= {"", "Copy", "Delete", "<New", "Preset", "New>"};
       //Track: source, monitor, group?, group expand, destination
       //Device: presets? chain, createDeviceBrowser, isExpanded, isMacroSelectionVisible, isRemoteControlsSectionVisible()
       
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
 
    public void Inst3Mode ()
    {
       //dApplication.setPanelLayout("ARRANGE");
       dMidiOut.sendSysex("F0000106221300F7");
       dMidiOut.sendSysex("F0000106221400F7");
      
       //button titles
       String[] mTitles= {"", "Duplicate", "Delete", "", "Duplicate", "Delete"};
       
       for (int i = 0; i < 3; i++) 
       {
          final String msg = mTitles[i];
          byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.yellow).addByte(sH.spc).addString(msg, msg.length()).terminate();
          dMidiOut.sendSysex(sysex);
       }
       for (int i = 3; i < 6; i++) 
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
       dHost.showPopupNotification("Edit Mode");
 
               //lights on buttons
               dMidiOut.sendMidi(176, CC_SONG, 00);
               dMidiOut.sendMidi(176, CC_INST, 00);
               dMidiOut.sendMidi(176, CC_EDIT, 127);
               dMidiOut.sendMidi(176, CC_USER, 00);
 
 
       dMidiOut.sendSysex("F0000106221300F7");
       dMidiOut.sendSysex("F0000106221400F7");
 
       //button titles
       String[] mTitles= {"1", "2", "3", "4", "5", "6"};
       
       for (int i = 0; i < 6; i++) 
       {
         final String msg = mTitles[i];
          byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.yellow).addByte(sH.spc).addString(msg, msg.length()).terminate();
          dMidiOut.sendSysex(sysex);
       }
 
        //line 1
        dMidiOut.sendSysex("F0 00 01 06 22 12 06 00 5B 5B 00 F7");
 
 //   //Main line 1 
 //   String pLayout = dApplication.panelLayout().get();
 //   byte[] sysex2 = sB.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString(pLayout, pLayout.length()).terminate();
 //      dMidiOut.sendSysex(sysex2);
 
      dMidiOut.sendSysex("F0000106221301F7");
 
    }
 
    public void UserMode ()
    {
       dHost.println("UserMode");
       dHost.showPopupNotification("User Mode");
       
               //lights on buttons
               dMidiOut.sendMidi(176, CC_SONG, 00);
               dMidiOut.sendMidi(176, CC_INST, 00);
               dMidiOut.sendMidi(176, CC_EDIT, 00);
               dMidiOut.sendMidi(176, CC_USER, 127);
 
       // mSongLayer.activate();
      dMidiOut.sendSysex("F0000106221401F7");
      dMidiOut.sendSysex("F0000106221301F7");
    }
 
    public void BrowserMode ()
    {
       //dHost.println("EditMode");
       dHost.showPopupNotification("Browser");
 
               //lights on buttons
               dMidiOut.sendMidi(176, CC_SONG, 00);
               dMidiOut.sendMidi(176, CC_INST, 00);
               dMidiOut.sendMidi(176, CC_EDIT, 00);
               dMidiOut.sendMidi(176, CC_USER, 00);
 
 
       dMidiOut.sendSysex("F0000106221300F7");
       dMidiOut.sendSysex("F0000106221400F7");
 
       //button titles
       String[] mTitles= {"1", "2", "3", "Preview", "Cancel", "OK"};
       
       for (int i = 0; i < 6; i++) 
       {
         final String msg = mTitles[i];
          byte[] sysex = SysexBuilder.fromHex(sH.sheader).addByte(sH.sButtonsTitle[i]).addHex(sH.magenta).addByte(sH.spc).addString(msg, msg.length()).terminate();
          dMidiOut.sendSysex(sysex);
       }
 
        //line 1
        dMidiOut.sendSysex("F0 00 01 06 22 12 06 00 5B 5B 00 F7");
 
 //   //Main line 1 
 //   String pLayout = dApplication.panelLayout().get();
 //   byte[] sysex2 = sB.fromHex(sH.sheader).addByte(sH.MainL1).addHex(sH.yellow).addByte(sH.spc).addString(pLayout, pLayout.length()).terminate();
 //      dMidiOut.sendSysex(sysex2);
 
      dMidiOut.sendSysex("F0000106221301F7");
    }
 


}
