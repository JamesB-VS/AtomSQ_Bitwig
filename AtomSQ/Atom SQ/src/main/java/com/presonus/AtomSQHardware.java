// Written by James Bell
// (c) 2023
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package com.presonus;

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.NoteInput;


import java.util.Arrays;


/**
 * Encapsulates the button and knob IDs of the MOXF and the MIDI connection.
 *
 * @author James Bell
 */
public class AtomSQHardware
{

    // Transport
    public static final int  ASQ_PLAY      = 109;
    public static final int  ASQ_STOP      = 111;
    public static final int  ASQ_REC       = 107;
    public static final int  ASQ_METRONOME = 105;

    //Menu Buttons
    public static final int  ASQ_SONG      = 32;
    public static final int  ASQ_INST      = 33;
    public static final int  ASQ_EDIT      = 34;
    public static final int  ASQ_USER      = 35;

    //Arrow Buttons
    public static final int  ASQ_AUP       =87;
    public static final int  ASQ_ADWN      =89;
    public static final int  ASQ_ALFT      =90;
    public static final int  ASQ_ARGT      =102;

    //shift
    public static final int  ASQ_SHIFT     =31;

    //A-H (only A works in Live mode)
    public static final int  ASQ_BTN_A     =64;

    //Display Buttons, top to bottom, left to right
    //1 2 3
    //[   ]
    //4 5 6
    public static final int  ASQ_BTN_1     =36;
    public static final int  ASQ_BTN_2     =37;
    public static final int  ASQ_BTN_3     =38;
    public static final int  ASQ_BTN_4     =39;
    public static final int  ASQ_BTN_5     =40;
    public static final int  ASQ_BTN_6     =41;
    //outside of the INST menu, the encoder and buttons on the right
    public static final int  ASQ_LEFT      =42;
    public static final int  ASQ_RIGHT     =43;
    public static final int  ASQ_ENC_9     =29;

    //device name, note on, note off, pressure, ribbon, pitchbend
    public static final String  DEV_NAME      = "Keyboard";
    public static final String  NOTE_ON       = "99????";
    public static final String  NOTE_OFF      = "89????";
    public static final String  NOTE_PRES     = "a9????";
    //FIX THIS these two are not proper anymore...and the mod/pitch stuff is wack on the controller. 
    //not anymore, this maaay have something to with the mono-pressure? last thing I changed before noticeing ti was working again.
    public static final String  NOTE_MOD      = "b001??";
    public static final String  NOTE_BEND     = "e0????";

    public static final int  CHAN_1 = 176;
    //no shift functions in the Live mode (native)
    //public static final int  CHAN_2 = 177;

    public static final int  ASQ_ENC_1     = 14;
    public static final int  ASQ_ENC_2     = 15;
    public static final int  ASQ_ENC_3     = 16;
    public static final int  ASQ_ENC_4     = 17;
    public static final int  ASQ_ENC_5     = 18;
    public static final int  ASQ_ENC_6     = 19;
    public static final int  ASQ_ENC_7     = 20;
    public static final int  ASQ_ENC_8     = 21;

    public static final int[]  ENCODERS = {14,15,16,17,18,19,29,21};
    public static final int[] BUTTONARRAY = {36, 37, 38, 39, 40, 41};





    /** The options for turning on/off the fixed velocity option. */
    //going to use this for the shift mode
    public static final String [] BOOLEAN_OPTIONS      =
    {
        "Off",
        "On"
    };

    public final MidiOut         portOut; //has to be public, so that the Start method can call it from the Init
    private final MidiIn          portIn;
    private final int []          ledCache             = new int [128];
    private final NoteInput       noteIn;


    //private boolean               isFixedVelocity;


    /**
     * Construcotr.
     *
     * @param outputPort The MIDI output port this.portOut
     * @param inputPort The MIDI input port
     * @param inputCallback The MIDI input callback
     * @param fixVelocityEnableSetting The fixed velocity setting
     * @param fixVelocityValueSetting The value for the fixed velocity setting
     */


     
    public void AtomSQ_Start (final MidiOut outputPort)
    {
  
        //TODO Auto-generated constructor stub
        outputPort.sendMidi(176,29,00);
        outputPort.sendMidi(176,15,00);
        outputPort.sendMidi(176,16,00);
        outputPort.sendMidi(176,17,00);
        outputPort.sendMidi(176,18,00);
        outputPort.sendMidi(176,19,00);
        outputPort.sendMidi(176,20,00);
        outputPort.sendMidi(176,21,00);
        outputPort.sendMidi(143,00,00);
  
        outputPort.sendMidi(176,29,00);
        outputPort.sendMidi(176,15,00);
        outputPort.sendMidi(176,16,00);
        outputPort.sendMidi(176,17,00);
        outputPort.sendMidi(176,18,00);
        outputPort.sendMidi(176,19,00);
        outputPort.sendMidi(176,20,00);
        outputPort.sendMidi(176,21,00);
        outputPort.sendMidi(143,00,00);
  
        outputPort.sendMidi(176,29,00);
        outputPort.sendMidi(176,15,00);
        outputPort.sendMidi(176,16,00);
        outputPort.sendMidi(176,17,00);
        outputPort.sendMidi(176,18,00);
        outputPort.sendMidi(176,19,00);
        outputPort.sendMidi(176,20,00);
        outputPort.sendMidi(176,21,00);
        outputPort.sendMidi(143,00,00);
  
        outputPort.sendSysex("F07E7F0601F7");
        outputPort.sendSysex("F07E7F0601F7");
        outputPort.sendSysex("F07E7F0601F7");
  
        //Midimode?
        //outputPort.sendMidi(143,00,00);
        //Live mode secret handshake
        outputPort.sendMidi(143,00,01);
        //Presonus mode secret handshare
        //outputPort.sendMidi(143,00,127)
  
        // These appear to only turn on specific lights.
        this.portOut.sendMidi(176,31,00); //shift
        this.portOut.sendMidi(176,90,127); //alft
        this.portOut.sendMidi(176,102,127); //argt
        this.portOut.sendMidi(176,87,127); //Aup
        this.portOut.sendMidi(176,89,127); //Adwn
        this.portOut.sendMidi(176,105,00); //Metronome
        this.portOut.sendMidi(177,109,00); //play turn off light
        this.portOut.sendMidi(178,109,12); //play turn on basic light level
        this.portOut.sendMidi(179,109,00); //play turn off light
        this.portOut.sendMidi(176,109,127); //play full brighness
        this.portOut.sendMidi(176,111,00); //stop
        this.portOut.sendMidi(176,42,127); //left
        this.portOut.sendMidi(176,43,127); //right
        this.portOut.sendMidi(176,107,00); //record
        //these seem to be ignored/overwritten atm...the mode still defaults to "Instrument"
        this.portOut.sendMidi(176,33,00); //Inst
        this.portOut.sendMidi(176,32,00); //song
        // this.portOut.sendMidi(176,34,00); //editor
        this.portOut.sendMidi(176,35,127); //user
  
        //this line alone turns on the lights (paste in the console)
        this.portOut.sendSysex("F0000106221300F7");
        //this line alone makes the Inst menu at least come back to life!
        //it also takes command of the nav keys on the right...if set to 0, the display still shows and navigates, but thie keys ALSO send midi messages
        // this.portOut.sendSysex("F0000106221401F7");
        this.portOut.sendSysex("F0000106221301F7");
    }

    public AtomSQHardware (final MidiOut outputPort, final MidiIn inputPort, final ShortMidiDataReceivedCallback inputCallback)
    {
        this.portOut = outputPort;
        this.portIn = inputPort;

        Arrays.fill (this.ledCache, -1);

        this.portIn.setMidiCallback (inputCallback);
        this.noteIn = this.portIn.createNoteInput (DEV_NAME, NOTE_ON, NOTE_OFF, NOTE_MOD, NOTE_BEND, NOTE_PRES);
    

    }


    /**
     * Update an LED on the MOXF (only an example for how to do this but not working with the MOXF).
     *
     * @param note The note
     * @param isOn True to turn on
     */


    public void updateLED (final int cc, final boolean isOn)
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
        this.portOut.sendMidi (CHAN_1, cc, value);
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

 public boolean handleShift (final int data1, final int data2, boolean shiftOn)
 {
     //var shiftOn = this.shift;
     //this currently returns "null" so the variable is not being set outside the switch function....
     //println (shiftOn);
     
     if (data1 == ASQ_SHIFT)
     {
         switch (data2)
         {
             case 127:
             this.portOut.sendMidi(CHAN_1, ASQ_SHIFT, 127);
             shiftOn = true;
         // println (shiftOn);
             return true;

             case 0:
             this.portOut.sendMidi(CHAN_1, ASQ_SHIFT, 0);
             shiftOn = false;
         // println (shiftOn);
             return true;
         }
     }
     return false;

 }

 

}