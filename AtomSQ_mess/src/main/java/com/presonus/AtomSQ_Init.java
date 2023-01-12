//must load after the hardware file, as this script defines methods based on the hardware function
package com.presonus;
//initiation Block

//import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;

public class AtomSQ_Init extends AtomSQ_Hardware
{

   public AtomSQ_Init(MidiOut outputPort, MidiIn inputPort, ShortMidiDataReceivedCallback inputCallback) {
      super(outputPort, inputPort, inputCallback);
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
      outputPort.sendMidi(176,31,00); //shift
      outputPort.sendMidi(176,90,127); //alft
      outputPort.sendMidi(176,102,127); //argt
      outputPort.sendMidi(176,87,127); //Aup
      outputPort.sendMidi(176,89,127); //Adwn
      outputPort.sendMidi(176,105,00); //Metronome
      outputPort.sendMidi(177,109,00); //play turn off light
      outputPort.sendMidi(178,109,12); //play turn on basic light level
      outputPort.sendMidi(179,109,00); //play turn off light
      outputPort.sendMidi(176,109,127); //play full brighness
      outputPort.sendMidi(176,111,00); //stop
      outputPort.sendMidi(176,42,127); //left
      outputPort.sendMidi(176,43,127); //right
      outputPort.sendMidi(176,107,00); //record
      //these seem to be ignored/overwritten atm...the mode still defaults to "Instrument"
      outputPort.sendMidi(176,33,00); //Inst
      outputPort.sendMidi(176,32,00); //song
      //outputPort.sendMidi(176,34,00); //editor
      outputPort.sendMidi(176,35,127); //user

      //this line alone turns on the lights (paste in the console)
      outputPort.sendSysex("F0000106221300F7");
      //this line alone makes the Inst menu at least come back to life!
      //it also takes command of the nav keys on the right...if set to 0, the display still shows and navigates, but thie keys ALSO send midi messages
      //outputPort.sendSysex("F0000106221401F7");
      outputPort.sendSysex("F0000106221301F7");

   }
   //private final MidiOut         portOut;
   //     this.portIn1 = inputPort1;  

  
  
}