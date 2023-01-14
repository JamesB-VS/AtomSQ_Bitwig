package com.presonus;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorDeviceFollowMode;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;

import com.presonus.handler.TransportHandler;
import com.presonus.handler.CursorHandler;
import com.presonus.handler.ModeHandler;


public class AtomSQExtension extends ControllerExtension
{

   private TransportHandler transportHandler;
   private AtomSQHardware hardware;
   private ModeHandler modeHandler;
   private CursorHandler cursorHandler;
  // private ShiftHandler shiftHandler;

   //private boolean shiftOn;
   protected AtomSQExtension(final AtomSQExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   /**
    * 
    */
   @Override
   public void init()
   {

      final ControllerHost host = getHost();      

      //mTransport = host.createTransport();

      this.hardware = new AtomSQHardware (host.getMidiOutPort (0), host.getMidiInPort (0), this::handleMidi);
      this.transportHandler = new TransportHandler (host.createTransport (), hardware);

      //without declaring this here, the handle Midi below fails as soon as a call gets tot he modeHandler, as it is declared as Null. 
      this.modeHandler = new ModeHandler();

      final CursorTrack cursorTrack = host.createCursorTrack(2, 0);
      final CursorDevice cursorDevice = cursorTrack.createCursorDevice("Current", "Current", 8,  CursorDeviceFollowMode.FOLLOW_SELECTION);
      final CursorRemoteControlsPage remoteControlsBank = cursorDevice.createCursorRemoteControlsPage("CursorPage1", 8, "");

      this.cursorHandler = new CursorHandler (cursorDevice, cursorTrack, remoteControlsBank);
   
      
      //the HW init
      hardware.AtomSQ_Start(hardware.portOut);

      host.showPopupNotification("Atom SQ Initialized");
   }

   @Override
   public void exit()
   {
      // TODO: Perform any cleanup once the driver exits
      // For now just show a popup notification for verification that it is no longer running.
      getHost().showPopupNotification("Atom SQ Exited");
   }

   @Override
   public void flush()
   {
      this.transportHandler.updateLED ();
   }

   /** Called when we receive short MIDI message on port 0. */
   private void handleMidi(final int statusByte, final int data1, final int data2)
   {
      final ShortMidiMessage msg = new ShortMidiMessage (statusByte, data1, data2);
      //this.shiftOn = shiftOn;
      //int menumode;
      int menumode = AtomSQHardware.ASQ_USER;

      if (this.hardware.handleShift(data1, data2, false))
      return;
      
      if (this.transportHandler.handleMidi (msg))
      return;

      if (this.modeHandler.handleMidi (msg))
      return;
      
      // if (this.hardware.HandleEncoders (msg, menumode, false))
      // return;

//   if (this.modeHandler.handleMidi (msg))
//       return;
      
      this.getHost ().println(msg.getStatusByte () +" "+ msg.getData1 () +" "+ msg.getData2 ());
      //this.getHost ().errorln ("Midi command not processed: " + msg.getStatusByte () + " : " + msg.getData1 ());
   }


  // private Transport mTransport;
}
