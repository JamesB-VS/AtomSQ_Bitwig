package com.presonus.handler;


function ModeHandler (application)
{
   this.application = application;
   application.setPanelLayout("ARRANGE");

   application.panelLayout().markInterested();

}

//Methods


//all midi CCs that belong to this handler
ModeHandler.prototype.handleMidi = function (status, data1, data2)
{
   //shiftOn = this.shiftOn;
   println(shiftOn + " in handle midi");
    // return uf a button is released (would remove if there is a need for momentary commands, like Shift)
    if (data2 == 0)
        return true;

    if (isChannelController(status) && data2 == 127)
   {
         switch (data1)
            {
              
               case ASQ_SONG:
                  modeHandler.Song();
                  menumode = ASQ_SONG;
                  encodermode = ASQ_SONG;
                  application.setPanelLayout("MIX");
                  modeHandler.displayButtonLightsOn();
                  modeHandler.updateLEDs();
                  return true;
               case ASQ_INST:
                  modeHandler.Inst();
                  menumode = ASQ_INST;
                  application.setPanelLayout("ARRANGE");
                  return true;
               case ASQ_EDIT:
                  println ("the Editor button has been disabled in this build.")
                  //FIX editmode. disabling as the settings are too much for this round. 
                  // modeHandler.Edit();
                  // menumode = ASQ_EDIT;
                  // application.setPanelLayout("EDIT")
                  return true;
               case ASQ_USER:
                  modeHandler.User();
                  menumode = ASQ_USER;
                  //as this menu now hosts the keyboard controls, it is probably not necessary to force Arranger mode.
                  //application.setPanelLayout("ARRANGE");
                  return true;
               case ASQ_BTN_1:
                  modeHandler.displayButtons(menumode, ASQ_BTN_1);
                  return true;
               case ASQ_BTN_2:
                  modeHandler.displayButtons(menumode, ASQ_BTN_2);
                  return true;
               case ASQ_BTN_3:
                  modeHandler.displayButtons(menumode, ASQ_BTN_3);
                  return true;
               case ASQ_BTN_4:
                  modeHandler.displayButtons(menumode, ASQ_BTN_4);
                  return true;
               case ASQ_BTN_5:
                  modeHandler.displayButtons(menumode, ASQ_BTN_5);
                  return true;
               case ASQ_BTN_6:
                  modeHandler.displayButtons(menumode, ASQ_BTN_6);
                  return true;
               case ASQ_AUP:
                     if(shiftOn)
                     {
                        application.focusPanelAbove();
                        println (application.panelLayout);
                        return true;
                     }
                     else 
                     {
                        application.arrowKeyUp()
                        return true;
                     }
   
               case ASQ_ADWN:
                  if(shiftOn)
                  {
                     application.focusPanelBelow();
                     println (application.panelLayout);
                     return true;
                  }
                  else 
                  {
                     println ("shift + arrow down");
                     application.arrowKeyDown();
                     return true;
                  }
  
               case ASQ_ALFT:
                  if(shiftOn)
                  {
                     application.focusPanelToLeft();
                     //cursorTrack.selectPrevious();
                     return true;
                  }
                  else 
                  {
                     println ("shift + arrow left");
                     application.arrowKeyLeft();
                     return true;
                  }
               
               case ASQ_ARGT:
                  if(shiftOn)
                  {
                     application.focusPanelToRight();
                     //cursorTrack.selectNext();
                     return true;
                  }
                  else 
                  {
                     application.arrowKeyRight();
                     return true;
                  }   
            
               default:
                 // host.errorln ("Command " + data1 + " is not supported");
                  return false;
            }

   }
}

ModeHandler.prototype.updateLEDs = function()
{
   println ("ModeHandler: track state")
      hardware.updateLEDs(ASQ_BTN_1, cursorTrack.solo().get());
      println ("- solo = " + cursorTrack.solo().get());
      hardware.updateLEDs(ASQ_BTN_2, cursorTrack.mute().get());
      println ("- Mute = " + cursorTrack.mute().get());
      hardware.updateLEDs(ASQ_BTN_3, cursorTrack.arm().get());
      println ("- Arm = " + cursorTrack.arm().get());
      hardware.updateLEDs(ASQ_BTN_4, cursorDevice.isEnabled().get());
      println ("- Enabled = " + cursorDevice.isEnabled().get());
      hardware.updateLEDs(ASQ_BTN_5, cursorDevice.isWindowOpen().get());
      println ("- Wndw = " + cursorDevice.isWindowOpen().get());
      hardware.updateLEDs(ASQ_BTN_6, cursorTrack.isActivated().get());
      println ("- Active = " + cursorTrack.isActivated().get());
}
