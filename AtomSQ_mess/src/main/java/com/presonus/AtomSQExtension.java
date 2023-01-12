package com.presonus;

import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.ControllerExtension;

//import my stuff
import com.presonus.AtomSQ_MidiMap;
import com.presonus.AtomSQ_Init;
import com.presonus.AtomSQ_Hardware;
// import com.presonus.handler.AtomSQ_CursorHandler;
// import com.presonus.handler.AtomSQ_ModeButtons;
// import com.presonus.handler.AtomSQ_ModeHandler;
import com.presonus.handler.TransportHandler;

public class AtomSQExtension extends ControllerExtension
{
   private Transport       transport;
   private Application     application;
   private CursorTrack     cursorTrack;

   
   // //declare the variables set in Init so they are globally available. It seemed to work without, I think, but Moss does this
   // var transport = null;
   // var hardware = null;
   // var application = null;
   // var transportHandler = null;
   // var cursorHandler = null;
   // var modeHandler = null;
   boolean shiftOn = false;
  string menuMode = null;

   protected AtomSQExtension(final AtomSQExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = this.getHost ();      

      // final MOXFHardware hardware = new MOXFHardware (host.getMidiOutPort (0), host.getMidiInPort (0), this::handleMidi, fixVelocityEnableSetting, fixVelocityValueSetting);
      final AtomSQ_Hardware hardware = new AtomSQ_Hardware (host.getMidiOutPort (0), host.getMidiInPort (0), this::handleMidi);
      //   this.transportHandler = new TransportHandler (host.createTransport (), hardware);
      // transportHandler = new TransportHandler (transport);
  this.transportHandler = new TransportHandler (host.createTransport (), hardware);
      // //   final CursorTrack cursorTrack = host.createCursorTrack ("MOXF_CURSOR_TRACK", "Cursor Track", 0, 0, true);
      // // cursorTrack = host.createCursorTrack ("ASQ_CURSOR_TRACK", "Cursor Track", 0, 0, true);
      // final CursorTrack cursorTrack = host.createCursorTrack ("ASQ_CURSOR_TRACK", "Cursor Track", 0, 0, true);
      // //   final TrackHandler trackHandler = new TrackHandler (host.createMainTrackBank (4, 0, 0), cursorTrack);
      // // modeHandler = new ModeHandler (application);
      // final ModeHandler modeHandler = new ModeHandler (application);
      // // cursorHandler = new CursorHandler (cursorTrack);
      // final CursorHandler cursorHandler = new CursorHandler (cursorTrack);
      // //   final PinnableCursorDevice cursorDevice = cursorTrack.createCursorDevice ("MOXF_CURSOR_DEVICE", "Cursor Device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
      // //   final RemoteControlHandler remoteControlHandler = new RemoteControlHandler (cursorDevice, cursorDevice.createCursorRemoteControlsPage (8));

      AtomSQ_Init(host.getMidiOutPort (0), host.getMidiInPort (0), ShortMidiDataReceivedCallback(0));

      // //FIX this is a bit of a cludge fix rn, as the settings force tke KB editor at the start. Would be nice to get it to default to Inst with the proper menu...
      // modeHandler.User();
   
      // For now just show a popup notification for verification that it is running.
     // host.println ("MOXF initialized!");
      getHost().showPopupNotification("AtomSQ Initialized");
     // host. showPopupNotification ("AtomSQ Initialized");
   }

   public void handleMidi (final int statusByte, final int data1, final int data2)
    {
         // //pass on if a note data
         // if (isNoteOn(status))
         // return;
         // if (hardware.handleShift (data1, data2))
         // return;
         // if (transportHandler.handleMidi (status, data1, data2))
         // return;
         // if (modeHandler.handleMidi (status, data1, data2))
         // return;
         // if (hardware.HandleEncoders(data1, data2))
         // return;
         // host.errorln ("Midi command not processed: " + status + "," + data1 + "," + data2);



        final ShortMidiMessage msg = new ShortMidiMessage (statusByte, data1, data2);
            
      //   if (this.shifHandler.handleMidi (msg))
      //   return;
        
        if (this.transportHandler.handleMidi (msg))
            return;

      //   if (this.transportHandler.handleMidi (msg))
      //       return;

      //   if (this.modeHandler.handleMidi (msg))
      //       return;

        this.getHost ().errorln ("Midi command not processed: " + msg.getStatusByte () + " : " + msg.getData1 ());
    }

   @Override
   public void exit()
   {
      // TODO: Perform any cleanup once the driver exits
      // For now just show a popup notification for verification that it is no longer running.
      getHost().showPopupNotification("AtomSQ Exited");
   }

   @Override
   public void flush()
   {
      // // TODO Send any updates you need here.
      // transportHandler.updateLEDs ();
      // if(menumode == ASQ_SONG, ASQ_INST)
      // {
      //    modeHandler.updateLEDs();
      // }
   }


}
