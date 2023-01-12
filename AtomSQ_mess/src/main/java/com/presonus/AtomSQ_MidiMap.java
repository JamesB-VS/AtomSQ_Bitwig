
package com.presonus;
//import java.util.ArrayList;
//import com.presonus.AtomSQ_Hardware;

public class AtomSQ_MidiMap
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

}
