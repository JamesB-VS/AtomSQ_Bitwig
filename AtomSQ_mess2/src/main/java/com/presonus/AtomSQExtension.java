// Written by James Bell
// (c) 2023
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package com.presonus;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.ControllerHost;
// import com.bitwig.extension.controller.api.CursorDeviceFollowMode;
// import com.bitwig.extension.controller.api.CursorTrack;
// import com.bitwig.extension.controller.api.DocumentState;
// import com.bitwig.extension.controller.api.PinnableCursorDevice;
// import com.bitwig.extension.controller.api.Preferences;
// import com.bitwig.extension.controller.api.SettableEnumValue;
// import com.bitwig.extension.controller.api.SettableRangedValue;
// import com.presonus.handler.Mode;
// import com.presonus.handler.ModeHandler;
// import com.presonus.handler.RemoteControlHandler;
// import com.presonus.handler.TrackHandler;
// import com.presonus.handler.TransportHandler;


/**
 * Bitwig extension for the Yamaha AtomSQ.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class AtomSQExtension extends ControllerExtension
{
   // private TransportHandler transportHandler;
  //  private ModeHandler      modeHandler;


    /**
     * Constructor.
     *
     * @param definition The definition object
     * @param host The controller host
     */
    protected AtomSQExtension (final AtomSQExtensionDefinition definition, final ControllerHost host)
    {
        super (definition, host);
    }


    /** {@inheritDoc} */
    @Override
    public void init ()
    {
        final ControllerHost host = this.getHost ();

        // // Preferences
        // final Preferences preferences = host.getPreferences ();
        // final SettableEnumValue modeSetting = preferences.getEnumSetting ("Mode", "Global", ModeHandler.MODE_OPTIONS, ModeHandler.MODE_OPTIONS[0]);

        // // Document States
        // final DocumentState documentState = host.getDocumentState ();
        // final SettableEnumValue fixVelocityEnableSetting = documentState.getEnumSetting ("Enable", "Fix velocity", AtomSQHardware.BOOLEAN_OPTIONS, AtomSQHardware.BOOLEAN_OPTIONS[0]);
        // final SettableRangedValue fixVelocityValueSetting = documentState.getNumberSetting ("Velocity", "Fix velocity", 0, 127, 1, "", 127);

        final AtomSQHardware hardware = new AtomSQHardware (host.getMidiOutPort (0), host.getMidiInPort (0), this::handleMidi);
        // this.transportHandler = new TransportHandler (host.createTransport (), hardware);

        // final CursorTrack cursorTrack = host.createCursorTrack ("AtomSQ_CURSOR_TRACK", "Cursor Track", 0, 0, true);
        // final TrackHandler trackHandler = new TrackHandler (host.createMainTrackBank (4, 0, 0), cursorTrack);

        // final PinnableCursorDevice cursorDevice = cursorTrack.createCursorDevice ("AtomSQ_CURSOR_DEVICE", "Cursor Device", 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
        // final RemoteControlHandler remoteControlHandler = new RemoteControlHandler (cursorDevice, cursorDevice.createCursorRemoteControlsPage (8));

        // final Mode [] modes = new Mode []
        // {
        //     trackHandler,
        //     remoteControlHandler
        // };
       // this.modeHandler = new ModeHandler (modes, modeSetting, host);

        host.println ("AtomSQ initialized!");
    }


    /**
     * Callback for receiving MIDI data.
     *
     * @param statusByte The status byte
     * @param data1 The data1 byte
     * @param data2 The data2 byte
     */
    public void handleMidi (final int statusByte, final int data1, final int data2)
    {
        final ShortMidiMessage msg = new ShortMidiMessage (statusByte, data1, data2);

        // if (this.transportHandler.handleMidi (msg))
        //     return;

        // if (this.modeHandler.handleMidi (msg))
        //     return;

        this.getHost ().errorln ("Midi command not processed: " + msg.getStatusByte () + " : " + msg.getData1 ());
    }


    /** {@inheritDoc} */
    @Override
    public void exit ()
    {
        this.getHost ().println ("AtomSQ Exited.");
    }


    /** {@inheritDoc} */
    @Override
    public void flush ()
    {
       // this.transportHandler.updateLEDs ();
    }
}
