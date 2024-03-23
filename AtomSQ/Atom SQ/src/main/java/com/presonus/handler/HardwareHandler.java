// Written by James Bell
// (c) 2023
// Licensed under GPLv3 - https://www.gnu.org/licenses/gpl-3.0.txt
package com.presonus.handler;

import com.bitwig.extension.api.Color;

public class HardwareHandler 
{
    //Variables from Hardware
    // Transport
    public final  int  CC_PLAY      = 109;
    public final  int  CC_STOP      = 111;
    public final  int  CC_REC       = 107;
    public final  int  CC_METRONOME = 105;
    //Menu Buttons
    public final  int  CC_SONG      = 32;
    public final  int  CC_INST      = 33;
    public final  int  CC_EDIT      = 34;
    public final  int  CC_USER      = 35;
    //Arrow Buttons
    public final  int  CC_UP       =87;
    public final  int  CC_DOWN      =89;
    public final  int  CC_LEFT      =90;
    public final  int  CC_RIGHT      =102;
    //shift
    public final  int  CC_SHIFT     =31;
    //A-H (only A works in Live mode)
    public final  int  CC_BTN_A     =64;
    //public final  int  CHAN_1 = 176;
    //public final  int  CHAN_2 = 177;
    //Display Buttons, top to bottom, left to right
    //1 2 3
    //[   ]
    //4 5 6
    public final  int  CC_BTN_1     =36;
    public final  int  CC_BTN_2     =37;
    public final  int  CC_BTN_3     =38;
    public final  int  CC_BTN_4     =39;
    public final  int  CC_BTN_5     =40;
    public final  int  CC_BTN_6     =41;
    //outside the INST menu, the encoder and buttons on the right
    public final  int  CC_BACK      =42;
    public final  int  CC_FORWARD    =43;
    public final  int  CC_ENCODER_9     =29;
    
    //device name, note on, note off, pressure, ribbon, pitchbend
    public final String  DEV_NAME      = "Keyboard";
    public final  String  NOTE_ON       = "99????";
    public final  String  NOTE_OFF      = "89????";
    public final  String  NOTE_PRES     = "a9????"; //poly aftertouch
    public final  String  NOTE_MOD      = "b001??";
    public final  String  NOTE_BEND     = "e0????";


/*     //Midi Channel 2 Input test!
        //device name, note on, note off, pressure, ribbon, pitchbend
    public final String  DEV_NAME      = "Keyboard";
    public final  String  NOTE_ON       = "91????";
    public final  String  NOTE_OFF      = "81????";
    public final  String  NOTE_PRES     = "a1????"; //poly aftertouch
    public final  String  NOTE_MOD      = "b101??";
    public final  String  NOTE_BEND     = "e1????"; */

    //Encoders
    public final  int  CC_ENCODER_1     = 14;
    //Enc 2-8 not needed because the encoders are created in an iteration below. 

    //ATOM colors
    public  final Color WHITE = Color.fromRGB(1, 1, 1);
    public  final Color BLACK = Color.fromRGB(0, 0, 0);
    public  final Color RED = Color.fromRGB(1, 0, 0);
    public  final Color DIM_RED = Color.fromRGB(0.3, 0.0, 0.0);
    public  final Color GREEN = Color.fromRGB(0, 1, 0);
    public  final Color ORANGE = Color.fromRGB(1, 1, 0);
    public  final Color BLUE = Color.fromRGB(0, 0, 1);
}
