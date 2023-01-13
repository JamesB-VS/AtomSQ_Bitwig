package com.presonus.handler;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.Application;


import com.presonus.AtomSQHardware;


public class EncoderHandler {


public boolean HandleEncoders  (final ShortMidiMessage message, int menuMode, Object host)
{
    //println ("Encoders are go!")
    //println (CC);
    switch(menuMode)
    {
        case (ASQ_SONG):
            //println ("Song mode Encoders");
            switch (message.getData1())
            {
                case ASQ_ENC_5:
                    //println("Encoder5 activated")
                    final int data2 = message.getData2 ();
                    final Integer value = Integer.valueOf (data2 > 64 ? 64 - data2 : data2);
                    application.showPopupNotification ("Track Pan");
                    cursorTrack.pan().inc (value, 128);
                
                    return true;
                case ASQ_ENC_6:
                    var value = value > 64 ? 64 - value : value;
                    host.showPopupNotification ("Track Vol");
                    cursorTrack.volume().inc (value, 128);
                    //cursorDevice.vol();

                    return true;
                default:
                    //this should make the other encoders in this mode do nothing. I hope.
                    return false;
            }
        case (ASQ_INST):
            switch (message.getData1())
            {
                case ASQ_ENC_1:
                    host.showPopupNotification("Remote 1");
                    var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (0).inc (value, 128);
                    return true;
                case ASQ_ENC_2:
                    host.showPopupNotification("Remote 2");
                    var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (1).inc (value, 128);
                    return true;
                case ASQ_ENC_3:
                    host.showPopupNotification("Remote 3");
                    var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (2).inc (value, 128);
                    return true;
                case ASQ_ENC_4:
                    host.showPopupNotification("Remote 4");
                    var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (3).inc (value, 128);
                    return true;
                case ASQ_ENC_5:
                    host.showPopupNotification("Remote 5");
                    var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (4).inc (value, 128);
                    return true;
                case ASQ_ENC_6:
                    host.showPopupNotification("Remote 6");
                    var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (5).inc (value, 128);
                    return true;
                case ASQ_ENC_7:
                    host.showPopupNotification("Remote 7");
                    var value = value > 64 ? 64 - value : value;
                    this.remoteControlsBank.getParameter (6).inc (value, 128);
                    return true;
                case ASQ_ENC_8:
                    host.showPopupNotification("Remote 8");
                    var value = value > 64 ? 64 - value : value;
                this.remoteControlsBank.getParameter (7).inc (value, 128);
                    return true;



            }
    }

 }
}
