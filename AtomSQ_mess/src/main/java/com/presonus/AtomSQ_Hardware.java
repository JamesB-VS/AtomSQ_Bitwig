package com.presonus;


import com.bitwig.extension.api.Host;
import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.NoteInput;
// import com.bitwig.extension.controller.api.SettableEnumValue;
// import com.bitwig.extension.controller.api.SettableRangedValue;

import java.util.Arrays;

//FIX THIS not sure if I really need both sets of MIdi, but this is in for testing purposes. 
//function ATOMSQHardware (outputPort1, inputPort1, inputCallback1)
public class AtomSQ_Hardware
{
    //Moss has the variables here, that I packed into the  MIdiMap. Maybe move until the linking works

    
    //     this.portOut1 = outputPort1;
    private final MidiOut         portOut;
    //     this.portIn1 = inputPort1;  
    private final MidiIn          portIn;
    //    //array for the LEDs. cache is the size of the possible number of CC values (so may be able to be adjusted down in the future)
    //    this.ledCache = initArray (-1, 128);
    private final int []          ledCache             = new int [128];
    //     this.noteIn = this.portIn1.createNoteInput(DEV_NAME, NOTE_ON, NOTE_OFF, NOTE_PRES, NOTE_MOD, NOTE_BEND );
    private final NoteInput       noteIn;
   
    //     this.portIn1.setMidiCallback (inputCallback1); 

    public AtomSQ_Hardware (final MidiOut outputPort, final MidiIn inputPort, final ShortMidiDataReceivedCallback inputCallback)
    {
        this.portOut = outputPort;
        this.portIn = inputPort;

        Arrays.fill (this.ledCache, -1);

        this.portIn.setMidiCallback (inputCallback);

        this.noteIn = this.portIn.createNoteInput (AtomSQ_MidiMap.DEV_NAME, AtomSQ_MidiMap.NOTE_ON, AtomSQ_MidiMap.NOTE_OFF, AtomSQ_MidiMap.NOTE_MOD, AtomSQ_MidiMap.NOTE_BEND, AtomSQ_MidiMap.NOTE_PRES);
    }

    public void updateLEDs (final int cc, final boolean isOn)
    {
         //this bit is seperated from the updateLEDs as I want to call it elsewhere to update LEDs without requiring a CC input
        final Integer value = isOn ? 127 : 0;
        if (this.ledCache[cc] != value)
        {
            //do I need a this. here?
            ledSwitcher(cc, value);
        }
    }

    public void ledSwitcher  (final int cc, final int value)
    {
        //println (cc + "in cache is " + this.ledCache[cc] );
        this.ledCache[cc] = value;
        this.portOut.sendMidi (AtomSQ_MidiMap.CHAN_1, cc, value);
    // println (cc + "Updated to " + this.ledCache[cc]);
    }

    public void turnOnLights ()
    {
        //all lights are dimmed, except the A-H, which are off when booted. Turns on these lights
        this.portOut.sendMidi(176,00,00);
        this.portOut.sendMidi(176,01,00);
        this.portOut.sendMidi(176,02,00);
        this.portOut.sendMidi(176,03,00);
        this.portOut.sendMidi(176,04,00);
        this.portOut.sendMidi(176,05,00);
        this.portOut.sendMidi(176,06,00);
        this.portOut.sendMidi(176,07,00);
    }

    //functions for both on and off of the shift CC, sets global variable "shiftOn"
    public boolean handleShift (final int data1, final int data2, boolean shiftOn)
    {
        //var shiftOn = this.shift;
        //this currently returns "null" so the variable is not being set outside the switch function....
        //println (shiftOn);
        
        if (data1 == AtomSQ_MidiMap.ASQ_SHIFT)
        {
            switch (data2)
            {
                case 127:
                this.portOut.sendMidi(AtomSQ_MidiMap.CHAN_1, AtomSQ_MidiMap.ASQ_SHIFT, 127);
                shiftOn = true;
            // println (shiftOn);
                return true;

                case 0:
                this.portOut.sendMidi(AtomSQ_MidiMap.CHAN_1, AtomSQ_MidiMap.ASQ_SHIFT, 0);
                shiftOn = false;
            // println (shiftOn);
                return true;
            }
        }
        return false;

    }

    public boolean HandleEncoders  (final ShortMidiMessage message, int menuMode, Object host)
    {
        //println ("Encoders are go!")
        //println (CC);
        switch(menuMode)
        {
            case (AtomSQ_MidiMap.ASQ_SONG):
                //println ("Song mode Encoders");
                switch (message.getData1())
                {
                    case AtomSQ_MidiMap.ASQ_ENC_5:
                        //println("Encoder5 activated")
                        final int data2 = message.getData2 ();
                        final Integer value = Integer.valueOf (data2 > 64 ? 64 - data2 : data2);
                        application.showPopupNotification ("Track Pan");
                        cursorTrack.pan().inc (value, 128);
                    
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_6:
                        var value = value > 64 ? 64 - value : value;
                        host.showPopupNotification ("Track Vol");
                        cursorTrack.volume().inc (value, 128);
                        //cursorDevice.vol();

                        return true;
                    default:
                        //this should make the other encoders in this mode do nothing. I hope.
                        return false;
                }
            case (AtomSQ_MidiMap.ASQ_INST):
                switch (message.getData1())
                {
                    case AtomSQ_MidiMap.ASQ_ENC_1:
                        host.showPopupNotification("Remote 1");
                        var value = value > 64 ? 64 - value : value;
                        this.remoteControlsBank.getParameter (0).inc (value, 128);
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_2:
                        host.showPopupNotification("Remote 2");
                        var value = value > 64 ? 64 - value : value;
                        this.remoteControlsBank.getParameter (1).inc (value, 128);
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_3:
                        host.showPopupNotification("Remote 3");
                        var value = value > 64 ? 64 - value : value;
                        this.remoteControlsBank.getParameter (2).inc (value, 128);
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_4:
                        host.showPopupNotification("Remote 4");
                        var value = value > 64 ? 64 - value : value;
                        this.remoteControlsBank.getParameter (3).inc (value, 128);
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_5:
                        host.showPopupNotification("Remote 5");
                        var value = value > 64 ? 64 - value : value;
                        this.remoteControlsBank.getParameter (4).inc (value, 128);
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_6:
                        host.showPopupNotification("Remote 6");
                        var value = value > 64 ? 64 - value : value;
                        this.remoteControlsBank.getParameter (5).inc (value, 128);
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_7:
                        host.showPopupNotification("Remote 7");
                        var value = value > 64 ? 64 - value : value;
                        this.remoteControlsBank.getParameter (6).inc (value, 128);
                        return true;
                    case AtomSQ_MidiMap.ASQ_ENC_8:
                        host.showPopupNotification("Remote 8");
                        var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (7).inc (value, 128);
                        return true;



                }
        }

    }
}