//further controls, all nested under the ModeHandler, due to varying functionalities based on mode.

ModeHandler.prototype.displayButtonLightsOff = function()
{
    sendMidi(176,ASQ_BTN_1, 00)
    sendMidi(176,ASQ_BTN_2, 00)
    sendMidi(176,ASQ_BTN_3, 00)
    sendMidi(176,ASQ_BTN_4, 00)
    sendMidi(176,ASQ_BTN_5, 00)
    sendMidi(176,ASQ_BTN_6, 00)

}

//called via the mode buttons to update the LEDs initially for the monitored track.
ModeHandler.prototype.displayButtonLightsOn = function()
{
    sendMidi(176,ASQ_BTN_1, hardware.ledCache[ASQ_BTN_1]);
    sendMidi(176,ASQ_BTN_2, hardware.ledCache[ASQ_BTN_2]);
    sendMidi(176,ASQ_BTN_3, hardware.ledCache[ASQ_BTN_3]);
    sendMidi(176,ASQ_BTN_4, hardware.ledCache[ASQ_BTN_4]);
    sendMidi(176,ASQ_BTN_5, hardware.ledCache[ASQ_BTN_5]);
    sendMidi(176,ASQ_BTN_6, hardware.ledCache[ASQ_BTN_6]);

}

ModeHandler.prototype.displayButtons = function(Menu, CC)
{
    
    switch (Menu)
    {
        
        case ASQ_SONG:
            switch(CC)
            {
                //B1  solo
                case ASQ_BTN_1:
                   
                    //sendMidi(176,ASQ_BTN_1, 127)
                    //println(ASQ_BTN_1);
                    cursorTrack.solo().toggle();
                    break;
                //B2  Mute
                case ASQ_BTN_2:
                   
                    //sendMidi(176,ASQ_BTN_2, 127)
                    //println(ASQ_BTN_2);
                    cursorTrack.mute().toggle();
                    break;
                //B3  Arm
                case ASQ_BTN_3:
                   
                    //sendMidi(176,ASQ_BTN_3, 127)
                    cursorTrack.arm().toggle();
                    break;
                //B4  Clip
                case ASQ_BTN_4:
                    //idea: control Vol with ENC_9
                   
                   //sendMidi(176,ASQ_BTN_4, 127)
                   cursorDevice.isEnabled().toggle ();
                    //cursorTrack.pan();
                    //enc 9
                    //sendMidi(176, 29, 00);
                    break;
                //B5  Scene
                case ASQ_BTN_5:
                    cursorDevice.isWindowOpen ().toggle ();
                    //sendMidi(176,ASQ_BTN_5, 127)
                    //sendMidi(176,ASQ_BTN_5, 127)
                    //control Pan with ENC_9
                    break;
                //B6  Stop
                case ASQ_BTN_6:
                    cursorTrack.isActivated ().toggle ();
                   // sendMidi(176,ASQ_BTN_6, 127)
                    break;

            }
            break;

        case ASQ_INST:
            switch(CC)
            {
                //B1  solo
                case ASQ_BTN_1:
                   
                   // sendMidi(176,ASQ_BTN_1, 127)
                    //println(ASQ_BTN_1);
                    cursorTrack.solo().toggle();
                    break;
                //B2  Mute
                case ASQ_BTN_2:
                   
                   // sendMidi(176,ASQ_BTN_2, 127)
                    //println(ASQ_BTN_2);
                    cursorTrack.mute().toggle();
                    break;
                //B3  Arm
                case ASQ_BTN_3:
                   
                   // sendMidi(176,ASQ_BTN_3, 127)
                    cursorTrack.arm().toggle();
                    break;
                //B4  Clip
                case ASQ_BTN_4:
                    //idea: control Vol with ENC_9
                   
                  // sendMidi(176,ASQ_BTN_4, 127)
                   cursorDevice.isEnabled().toggle ();
                    //cursorTrack.pan();
                    //enc 9
                    //sendMidi(176, 29, 00);
                    break;
                //B5  Scene
                case ASQ_BTN_5:
                    cursorDevice.isWindowOpen ().toggle ();
                  //  sendMidi(176,ASQ_BTN_5, 127)
                    //sendMidi(176,ASQ_BTN_5, 127)
                    //control Pan with ENC_9
                    break;
                //B6  Stop
                case ASQ_BTN_6:
                    cursorTrack.isActivated ().toggle ();
                  //  sendMidi(176,ASQ_BTN_6, 127)
                    break;

            }
            break; 
/*  //fIX editmode. removing all edit bits.
        case ASQ_EDIT:
            modeHandler.displayButtonLightsOff();
            break;

 */

    }

}
