
ModeHandler.prototype.Song = function()
{
   host.showPopupNotification ("Mixer");
  
   //modeHandler.displayButtons();
   //Mixer Mode
   //lights on buttons
   sendMidi(176, ASQ_SONG, 127);
   sendMidi(176, ASQ_INST, 00);
   sendMidi(176, ASQ_EDIT, 00);
   sendMidi(176, ASQ_USER, 00);
   //this one is in the origina code, to turn off the lights on the keys....I have the turn-on thing below for now, but should figure out the desired behavior
   sendSysex("F0000106221300F7");
   sendSysex("F0000106221400F7");

   //Display
   //B1 L1 Solo
   sendSysex('F0 00 01 06 22 12 00 00 5B 5B 00 53 6F 6C 6F F7');
   //B2 L1 Mute
   sendSysex('F0 00 01 06 22 12 01 00 5B 5B 00 4D 75 74 65 F7');
   //B3 L1 Arm
   sendSysex('F0 00 01 06 22 12 02 00 5B 5B 00 41 72 6D F7');
   //B1 L2
   sendSysex('F0 00 01 06 22 12 03 00 5B 5B 00 F7');
   //B2 L2
   sendSysex('F0 00 01 06 22 12 04 00 5B 5B 00 F7');
   //B3 L2
   sendSysex('F0 00 01 06 22 12 05 00 5B 5B 00 F7');
   //B4 L2 Enable
   sendSysex('F0 00 01 06 22 12 0B 00 5B 5B 00 45 6E 61 62 6c 65 64 F7');
   //B5 L2 Wndw
   sendSysex('F0 00 01 06 22 12 0C 00 5B 5B 00 57 6E 64 77 F7');
   //B6 L2 active
   sendSysex('F0 00 01 06 22 12 0D 00 5B 5B 00 61 63 74 69 76 65 F7');

   //FIX NOT WORKING RN
   //B4 L1 Device
   sendSysex('F0 00 01 06 22 12 0E 00 5B 5B 00 44 65 76 F7');
   //B5 L1 Device 
   sendSysex('F0 00 01 06 22 12 0F 00 5B 5B 00 44 65 76 F7');

   
   //B6 L1 Track
   sendSysex('F0 00 01 06 22 12 0A 00 5B 5B 00 54 72 61 63 6B F7');
  //line 1 Mixer
  sendSysex('F0 00 01 06 22 12 06 00 5B 5B 00 4d 69 78 65 72 F7');
  //line 2  Track:
  sendSysex('F0 00 01 06 22 12 07 00 5B 5B 00 54 72 61 63 6B 3A F7');



   
   // Encoder 9...must recenter it? 00 and 127 have no other visible effect.
   sendMidi(176, 29, 00);
   //this line alone turns on the lights (paste in the console)
   sendSysex("F0000106221301F7");
   //line 2 of display with folder path
   //FIX this needs to get updated by content from Bitwig, just like the key colors eventually
   //sendSysex("F00001062212077F7F00000A20496E737472200520496E737472756D656E74732007F7");
   //a simple function to poll the LEDcache and set the lights appropriately.
   modeHandler.displayButtonLightsOn();
}

ModeHandler.prototype.Inst = function()
{
   host.showPopupNotification ("Arranger");
   //modeHandler.displayButtons();
   //lights on buttons
   sendMidi(176, ASQ_SONG, 00);
   sendMidi(176, ASQ_INST, 127);
   sendMidi(176, ASQ_EDIT, 00);
   sendMidi(176, ASQ_USER, 00);

   //running these seems to allow the screen to refresh. Otherwise, when switching from the "User" keyboard mode, the menu is blank. switching from Song mode worked, as it has thsi code already.
   sendSysex("F0000106221300F7");
   sendSysex("F0000106221400F7");

   //Display
   //B1 L1 Solo
   sendSysex('F0 00 01 06 22 12 00 00 5B 5B 00 53 6F 6C 6F F7');
   //B2 L1 Mute
   sendSysex('F0 00 01 06 22 12 01 00 5B 5B 00 4D 75 74 65 F7');
   //B3 L1 Arm
   sendSysex('F0 00 01 06 22 12 02 00 5B 5B 00 41 72 6D F7');
   //B1 L2
   sendSysex('F0 00 01 06 22 12 03 00 5B 5B 00 F7');
   //B2 L2
   sendSysex('F0 00 01 06 22 12 04 00 5B 5B 00 F7');
   //B3 L2
   sendSysex('F0 00 01 06 22 12 05 00 5B 5B 00 F7');
   //B4 L2 Enable
   sendSysex('F0 00 01 06 22 12 0B 00 5B 5B 00 45 6E 61 62 6c 65 64 F7');
   //B5 L2 Wndw
   sendSysex('F0 00 01 06 22 12 0C 00 5B 5B 00 57 6E 64 77 F7');
   //B6 L2 active
   sendSysex('F0 00 01 06 22 12 0D 00 5B 5B 00 61 63 74 69 76 65 F7');

   //FIX NOT WORKING RN
   //B4 L1 Device
   sendSysex('F0 00 01 06 22 12 0E 00 5B 5B 00 44 65 76 F7');
   //B5 L1 Device 
   sendSysex('F0 00 01 06 22 12 0F 00 5B 5B 00 44 65 76 F7');


   //B6 L1 Track
   sendSysex('F0 00 01 06 22 12 0A 00 5B 5B 00 54 72 61 63 6B F7');
   //line 1 Arranger
   sendSysex('F0 00 01 06 22 12 06 00 5B 5B 00 41 72 72 61 6e 67 65 72 F7');
   //line 2  Track:
   sendSysex('F0 00 01 06 22 12 07 00 5B 5B 00 54 72 61 63 6B 3A F7');

   // Encoder 9...must recenter it? 00 and 127 have no other visible effect.
   sendMidi(176, 29, 00);
   sendSysex("F0000106221301F7");

   //a simple function to poll the LEDcache and set the lights appropriately.
   modeHandler.displayButtonLightsOn();

}
/* 
//FIX editmode. currently disabled. Could have left, but this is clearer.
ModeHandler.prototype.Edit = function()
{
   host.showPopupNotification ("Custom");
   //modeHandler.displayButtons();
   //lights on buttons
   sendMidi(176, ASQ_SONG, 00);
   sendMidi(176, ASQ_INST, 00);
   sendMidi(176, ASQ_EDIT, 127);
   sendMidi(176, ASQ_USER, 00);
   // //turn on arrow key lights
   // sendMidi(176,87,127)
   // sendMidi(176,89,127)
   // sendMidi(176,90,00)
   // sendMidi(176,102,00)

      //running these seems to allow the screen to refresh. Otherwise, when switching from the "User" keyboard mode, the menu is blank. switching from Song mode worked, as it has thsi code already.
      sendSysex("F0000106221300F7");
      sendSysex("F0000106221400F7");
   
           //Display clear
      //B1 L1 CC
      sendSysex('F0 00 01 06 22 12 00 00 5B 5B 00 43 43 F7');
      //B2 L1 CC
      sendSysex('F0 00 01 06 22 12 01 00 5B 5B 00 43 43 F7');
      //B3 L1 CC
      sendSysex('F0 00 01 06 22 12 02 00 5B 5B 00 43 43 F7');
      //B1 L2 36
      sendSysex('F0 00 01 06 22 12 03 00 5B 5B 00 33 36 F7');
      //B2 L2 37
      sendSysex('F0 00 01 06 22 12 04 00 5B 5B 00 33 37 F7');
      //B3 L2 38
      sendSysex('F0 00 01 06 22 12 05 00 5B 5B 00 33 38 F7');
      //B4 L2 39
      sendSysex('F0 00 01 06 22 12 0B 00 5B 5B 00 33 39 F7');
      //B5 L2 40
      sendSysex('F0 00 01 06 22 12 0C 00 5B 5B 00 34 30 F7');
      //B6 L2 41
      sendSysex('F0 00 01 06 22 12 0D 00 5B 5B 00 34 31 F7');
      //B4 L1 CC
      sendSysex('F0 00 01 06 22 12 0E 00 5B 5B 00 43 43 F7');
      //B5 L1 CC
      sendSysex('F0 00 01 06 22 12 0F 00 5B 5B 00 43 43 F7');
      //B6 L1 CC
      sendSysex('F0 00 01 06 22 12 0A 00 5B 5B 00 43 43 F7');
      //line 1 User
      sendSysex('F0 00 01 06 22 12 06 00 5B 5B 00 55 73 65 72 F7');
      //line 2 Assignable
      sendSysex('F0 00 01 06 22 12 07 00 5B 5B 00 41 73 73 69 67 6e 61 62 6c 65 F7');

   //sendSysex("F0000106221400F7");
   //this line alone turns on the lights (paste in the console)
   sendSysex("F0000106221301F7");
} */

ModeHandler.prototype.User = function()
{
   host.showPopupNotification ("AtomSQ Keyboard");
   modeHandler.displayButtonLightsOff();
   //lights on buttons
   sendMidi(176, ASQ_SONG, 00);
   sendMidi(176, ASQ_INST, 00);
   sendMidi(176, ASQ_EDIT, 00);
   sendMidi(176, ASQ_USER, 127);
   // //turn on arrow key lights
   // sendMidi(176,87,127)
   // sendMidi(176,89,127)
   // sendMidi(176,90,00)
   // sendMidi(176,102,00)


   sendSysex("F0000106221401F7");
   sendSysex("F0000106221301F7");

}

    //this line alone turns on the lights (paste in the console)
//    sendSysex("F0000106221301F7");
    //this line alone turns oOFF the lights (paste in the console)
//    sendSysex("F0000106221300F7");
   //this line alone makes the Inst menu at least come back to life!
//    sendSysex("F0000106221401F7");
   //this line alone makes the Inst menu go away
//    sendSysex("F0000106221400F7");

/*            
      //Display clear
      //B1 L1
      sendSysex('F0 00 01 06 22 12 00 00 5B 5B 00 F7');
      //B2 L1
      sendSysex('F0 00 01 06 22 12 01 00 5B 5B 00 F7');
      //B3 L1
      sendSysex('F0 00 01 06 22 12 02 00 5B 5B 00 F7');
      //B1 L2
      sendSysex('F0 00 01 06 22 12 03 00 5B 5B 00 F7');
      //B2 L2
      sendSysex('F0 00 01 06 22 12 04 00 5B 5B 00 F7');
      //B3 L2
      sendSysex('F0 00 01 06 22 12 05 00 5B 5B 00 F7');
      //B4 L2
      sendSysex('F0 00 01 06 22 12 0B 00 5B 5B 00 F7');
      //B5 L2
      sendSysex('F0 00 01 06 22 12 0C 00 5B 5B 00 F7');
      //B6 L2
      sendSysex('F0 00 01 06 22 12 0D 00 5B 5B 00 F7');
      //B4 L1
      sendSysex('F0 00 01 06 22 12 0E 00 5B 5B 00 F7');
      //B5 L1
      sendSysex('F0 00 01 06 22 12 0F 00 5B 5B 00 F7');
      //B6 L1
      sendSysex('F0 00 01 06 22 12 0A 00 5B 5B 00 F7');
      //line 1
      sendSysex('F0 00 01 06 22 12 06 00 5B 5B 00 F7');
      //line 2
      sendSysex('F0 00 01 06 22 12 07 00 5B 5B 00 F7');
 */
