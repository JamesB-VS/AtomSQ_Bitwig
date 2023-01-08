//Transport
const ASQ_PLAY      = 109;
const ASQ_STOP      = 111;
const ASQ_REC       = 107;
const ASQ_METRONOME = 105;

//Menu Buttons
const ASQ_SONG      = 32;
const ASQ_INST      = 33;
const ASQ_EDIT      = 34;
const ASQ_USER      = 35;

//Arrow Buttons
const ASQ_AUP       =87;
const ASQ_ADWN      =89;
const ASQ_ALFT      =90;
const ASQ_ARGT      =102;

//shift
const ASQ_SHIFT     =31;

//A-H (only A works in Live mode)
const ASQ_BTN_A     =64;

//Display Buttons, top to bottom, left to right
//1 2 3
//[   ]
//4 5 6
const ASQ_BTN_1     =36;
const ASQ_BTN_2     =37;
const ASQ_BTN_3     =38;
const ASQ_BTN_4     =39;
const ASQ_BTN_5     =40;
const ASQ_BTN_6     =41;
//outside of the INST menu, the encoder and buttons on the right
const ASQ_LEFT      =42;
const ASQ_RIGHT     =43;
const ASQ_ENC_9     =29;

//device name, note on, note off, pressure, ribbon, pitchbend
const DEV_NAME      = "Keyboard";
const NOTE_ON       = "99????";
const NOTE_OFF      = "89????";
const NOTE_PRES     = "a9????";
//FIX THIS these two are not proper anymore...and the mod/pitch stuff is wack on the controller. 
//not anymore, this maaay have something to with the mono-pressure? last thing I changed before noticeing ti was working again.
const NOTE_MOD      = "b001??";
const NOTE_BEND     = "e0????";

const CHAN_1 = 176;
//no shift functions in the Live mode (native)
//const CHAN_2 = 177;

const ASQ_ENC_1     = 14;
const ASQ_ENC_2     = 15;
const ASQ_ENC_3     = 16;
const ASQ_ENC_4     = 17;
const ASQ_ENC_5     = 18;
const ASQ_ENC_6     = 19;
const ASQ_ENC_7     = 20;
const ASQ_ENC_8     = 21;

const ENCODERS = [14,15,16,17,18,19,29,21];

const BUTTONARRAY = [36, 37, 38, 39, 40, 41];
