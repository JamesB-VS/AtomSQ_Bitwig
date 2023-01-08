//FIX THIS not sure if I really need both sets of MIdi, but this is in for testing purposes. 
function ATOMSQHardware (outputPort1, inputPort1, inputCallback1)
{
    this.portOut1 = outputPort1;
    this.portIn1 = inputPort1;   
    this.portIn1.setMidiCallback (inputCallback1); 
    this.noteIn = this.portIn1.createNoteInput(DEV_NAME, NOTE_ON, NOTE_OFF, NOTE_PRES, NOTE_MOD, NOTE_BEND );
   //array for the LEDs. cache is the size of the possible number of CC values (so may be able to be adjusted down in the future)
   this.ledCache = initArray (-1, 128);
}

//add methods with the prototype feature

ATOMSQHardware.prototype.updateLEDs = function (cc, isOn)
{
    var value = isOn ? 127 : 0;
    if (this.ledCache[cc] != value)
    {
        hardware.ledSwitcher(cc, value);

    }
}

//this bit is seperated from the updateLEDs as I want to call it elsewhere to update LEDs without requiring a CC input
ATOMSQHardware.prototype.ledSwitcher = function(cc, value)
{
    println (cc + "in cache is " + this.ledCache[cc] );
    this.ledCache[cc] = value;
    this.portOut1.sendMidi (CHAN_1, cc, value);
    println (cc + "Updated to " + this.ledCache[cc]);
}

ATOMSQHardware.prototype.turnOnLights = function()
{
    //all lights are dimmed, except the A-H, which are off when booted. Turns on these lights
    sendMidi(176,00,00);
    sendMidi(176,01,00);
    sendMidi(176,02,00);
    sendMidi(176,03,00);
    sendMidi(176,04,00);
    sendMidi(176,05,00);
    sendMidi(176,06,00);
    sendMidi(176,07,00);
}

//functions for both on and off of the shift CC, sets global variable "shiftOn"
ATOMSQHardware.prototype.handleShift = function(data1, data2)
{
   //var shiftOn = this.shift;
   //this currently returns "null" so the variable is not being set outside the switch function....
   //println (shiftOn);
   
   if (data1 == ASQ_SHIFT)
   {
   switch (data2)
   {
      case 127:
      sendMidi(176, ASQ_SHIFT, 127);
      shiftOn = true;
      println (shiftOn);
      return true;

      case 0:
         sendMidi(176, ASQ_SHIFT, 0);
      shiftOn = false;
      println (shiftOn);
      return true;
   }
}
return false;

}

ATOMSQHardware.prototype.HandleEncoders = function(CC, value)
{
    println ("Encoders are go!")
    println (CC);
    switch(menumode)
    {
        case (ASQ_SONG):
            println ("Song mode Encoders");
            switch (CC)
            {
                case ASQ_ENC_5:
                    println("Encoder5 activated")
                    var value = value > 64 ? 64 - value : value;
                    host.showPopupNotification ("Track Pan");
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
            switch (CC)
            {
                case ASQ_ENC_1:
                    host.showPopupNotification("Remote 1");
                    var value = value > 64 ? 64 - value : value;
                   remoteControlsBank.getParameter (0).inc (value, 128);
                    return true;
                case ASQ_ENC_2:
                    host.showPopupNotification("Remote 2");
                    var value = value > 64 ? 64 - value : value;
                    remoteControlsBank.getParameter (1).inc (value, 128);
                    return true;
                case ASQ_ENC_3:
                    host.showPopupNotification("Remote 3");
                    var value = value > 64 ? 64 - value : value;
                    remoteControlsBank.getParameter (2).inc (value, 128);
                    return true;
                 case ASQ_ENC_4:
                    host.showPopupNotification("Remote 4");
                    var value = value > 64 ? 64 - value : value;
                    remoteControlsBank.getParameter (3).inc (value, 128);
                    return true;
                case ASQ_ENC_5:
                    host.showPopupNotification("Remote 5");
                    var value = value > 64 ? 64 - value : value;
                    remoteControlsBank.getParameter (4).inc (value, 128);
                    return true;
                case ASQ_ENC_6:
                    host.showPopupNotification("Remote 6");
                    var value = value > 64 ? 64 - value : value;
                    remoteControlsBank.getParameter (5).inc (value, 128);
                    return true;
                case ASQ_ENC_7:
                    host.showPopupNotification("Remote 7");
                    var value = value > 64 ? 64 - value : value;
                    remoteControlsBank.getParameter (6).inc (value, 128);
                    return true;
                case ASQ_ENC_8:
                    host.showPopupNotification("Remote 8");
                    var value = value > 64 ? 64 - value : value;
                   remoteControlsBank.getParameter (7).inc (value, 128);
                    return true;



            }
        }

    }

/* 
ATOMSQHardware.prototype.turnOffChannels = function()
{
    //not sure if these are necessary, but they come up in the midi in the beginning
    sendMidi(176,123,00);
    sendMidi(176,121,00);
    sendMidi(177,123,00);
    sendMidi(177,121,00);
    sendMidi(178,123,00);
    sendMidi(178,121,00);
    sendMidi(179,123,00);
    sendMidi(179,121,00);
    sendMidi(180,123,00);
    sendMidi(180,121,00);
    sendMidi(181,123,00);
    sendMidi(181,121,00);
    sendMidi(182,123,00);
    sendMidi(182,121,00);
    sendMidi(183,123,00);
    sendMidi(183,121,00);
    sendMidi(184,123,00);
    sendMidi(184,121,00);
    sendMidi(185,123,00);
    sendMidi(185,121,00);
    sendMidi(186,123,00);
    sendMidi(186,121,00);
    sendMidi(187,123,00);
    sendMidi(187,121,00);
    sendMidi(188,123,00);
    sendMidi(188,121,00);
    sendMidi(189,123,00);
    sendMidi(189,121,00);
    sendMidi(190,123,00);
    sendMidi(190,121,00);
    sendMidi(191,123,00);
    sendMidi(191,121,00);
} */