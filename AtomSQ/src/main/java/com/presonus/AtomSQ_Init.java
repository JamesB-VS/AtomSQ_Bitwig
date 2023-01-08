//must load after the hardware file, as this script defines methods based on the hardware function

//initiation Block

ATOMSQHardware.prototype.block_Init = function()
{
   //these appear to be part of the handshake...or they set the Setup mode to the proper view. without them, the Setup menu shows incorrectly.
   sendMidi(176,29,00);
   sendMidi(176,15,00);
   sendMidi(176,16,00);
   sendMidi(176,17,00);
   sendMidi(176,18,00);
   sendMidi(176,19,00);
   sendMidi(176,20,00);
   sendMidi(176,21,00);
   sendMidi(143,00,00);

   sendMidi(176,29,00);
   sendMidi(176,15,00);
   sendMidi(176,16,00);
   sendMidi(176,17,00);
   sendMidi(176,18,00);
   sendMidi(176,19,00);
   sendMidi(176,20,00);
   sendMidi(176,21,00);
   sendMidi(143,00,00);

   sendMidi(176,29,00);
   sendMidi(176,15,00);
   sendMidi(176,16,00);
   sendMidi(176,17,00);
   sendMidi(176,18,00);
   sendMidi(176,19,00);
   sendMidi(176,20,00);
   sendMidi(176,21,00);
   sendMidi(143,00,00);

   sendSysex("F07E7F0601F7");
   sendSysex("F07E7F0601F7");
   sendSysex("F07E7F0601F7");

   //Midimode?
   //sendMidi(143,00,00);
   //Live mode secret handshake
   sendMidi(143,00,01);
   //Presonus mode secret handshare
   //sendMidi(143,00,127)

   // These appear to only turn on specific lights.
   sendMidi(176,31,00); //shift
   sendMidi(176,90,127); //alft
   sendMidi(176,102,127); //argt
   sendMidi(176,87,127); //Aup
   sendMidi(176,89,127); //Adwn
   sendMidi(176,105,00); //Metronome
   sendMidi(177,109,00); //play turn off light
   sendMidi(178,109,12); //play turn on basic light level
   sendMidi(179,109,00); //play turn off light
   sendMidi(176,109,127); //play full brighness
   sendMidi(176,111,00); //stop
   sendMidi(176,42,127); //left
   sendMidi(176,43,127); //right
   sendMidi(176,107,00); //record
   //these seem to be ignored/overwritten atm...the mode still defaults to "Instrument"
   sendMidi(176,33,00); //Inst
   sendMidi(176,32,00); //song
   //sendMidi(176,34,00); //editor
   sendMidi(176,35,127); //user

   //this line alone turns on the lights (paste in the console)
   sendSysex("F0000106221300F7");
   //this line alone makes the Inst menu at least come back to life!
   //it also takes command of the nav keys on the right...if set to 0, the display still shows and navigates, but thie keys ALSO send midi messages
   //sendSysex("F0000106221401F7");
   sendSysex("F0000106221301F7");
  
}