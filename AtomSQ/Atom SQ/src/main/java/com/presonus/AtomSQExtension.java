package com.presonus;

import java.util.ArrayList;
import java.util.List;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.Action;
import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorBrowserFilterItem;
import com.bitwig.extension.controller.api.CursorBrowserResultItem;
import com.bitwig.extension.controller.api.CursorDeviceFollowMode;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.HardwareButton;
import com.bitwig.extension.controller.api.HardwareControlType;
import com.bitwig.extension.controller.api.HardwareLightVisualState;
import com.bitwig.extension.controller.api.HardwareSurface;
import com.bitwig.extension.controller.api.MidiExpressions;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.OnOffHardwareLight;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.RelativeHardwareKnob;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.api.SendBank;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;
import com.bitwig.extension.controller.api.MasterTrack;
import com.bitwig.extension.controller.api.HardwareActionBindable;
import com.bitwig.extension.controller.api.DeviceBank;
import com.bitwig.extension.controller.api.Device;
import com.bitwig.extension.controller.api.PopupBrowser;

//Bitwig Framework components
import com.bitwig.extensions.framework.Layer;
import com.bitwig.extensions.framework.Layers;

//Local components
import com.presonus.handler.DisplayMode;
import com.presonus.handler.HardwareHandler;;
public class AtomSQExtension extends ControllerExtension
{
  public AtomSQExtension(final AtomSQExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {

      mHost = getHost();
      DM = new DisplayMode();
     
      mApplication = mHost.createApplication();
      mApplication.panelLayout().markInterested();
      mApplication.canRedo().markInterested();
      mApplication.canUndo().markInterested();

      mMidiOut = mHost.getMidiOutPort(0);
      mMidiIn = mHost.getMidiInPort(0);
      //HINT: Notes not playing? these values are configured for CH 10 on the midi controller, which is the default. If this is not set, close BW, then reset in the generic controller menu!
      mMidiIn.createNoteInput (hH.DEV_NAME, hH.NOTE_ON, hH.NOTE_OFF, hH.NOTE_MOD, hH.NOTE_BEND, hH.NOTE_PRES);

      //Cursor Track / Device stuff
      //the first int here dictates the number of sends! this is different than the arrainger track itself, so the number of sends on the actual track are not relevant.
      mCursorTrack = mHost.createCursorTrack(6, 0);
      mCursorTrack.solo().markInterested();
      mCursorTrack.mute().markInterested();
      mCursorTrack.arm().markInterested();
      mCursorTrack.volume().markInterested();
      mCursorTrack.pan().markInterested();
      mCursorTrack.isActivated ().markInterested();
      mCursorTrack.color ().markInterested();
      mCursorTrack.name().markInterested();
      mCursorTrack.hasPrevious().markInterested();
      mCursorTrack.hasNext().markInterested();
      mCursorTrack.monitorMode().markInterested(); 
    
      mCursorTrack.monitorMode().markInterested();
      mCursorTrack.isMonitoring().markInterested();
      mCursorTrack.position().markInterested();

      mSendBank = mCursorTrack.sendBank();

      //MasterTrack
      mMasterTrack = mHost.createMasterTrack(0);
      mMasterTrack.volume().markInterested();

      //Cursor HW Layout creation

      mCursorDevice = mCursorTrack.createCursorDevice("Current", "Current", 8,  CursorDeviceFollowMode.FOLLOW_SELECTION);
      mCursorDevice.isEnabled ().markInterested ();
      mCursorDevice.isWindowOpen ().markInterested ();
      mCursorDevice.name().markInterested();
      mCursorDevice.isRemoteControlsSectionVisible().markInterested();
      mCursorDevice.isExpanded ().markInterested ();

      mCursorDevice.position().markInterested();
      mCursorDevice.exists().markInterested();
     

      mPopupBrowser = mHost.createPopupBrowser();
      mPopupBrowser.exists().markInterested();
      mPopupBrowser.selectedContentTypeIndex().markInterested();

      mBrowserResult = (CursorBrowserResultItem) mPopupBrowser.resultsColumn().createCursorItem();
      mBrowserCategory =(CursorBrowserFilterItem) mPopupBrowser.categoryColumn().createCursorItem();
      mBrowserCreator = (CursorBrowserFilterItem) mPopupBrowser.creatorColumn().createCursorItem();
      mBrowserTag = (CursorBrowserFilterItem) mPopupBrowser.tagColumn().createCursorItem();
      mBrowserResult.exists().markInterested();
      mBrowserCategory.exists().markInterested();
      mBrowserCreator.exists().markInterested();
      mBrowserTag.exists().markInterested();
      mBrowserResult.name().markInterested();
     
      mCDLDBnk = mCursorTrack.createDeviceBank(3);
      mCDLDBnk.canScrollBackwards().markInterested();
      mCDLDBnk.canScrollForwards().markInterested();
      mCDLDBnk.scrollPosition().markInterested();
      mCDLDBnk.itemCount().markInterested();

      for (int i = 0; i < mCDLDBnk.getSizeOfBank(); i++) {
         Device device = mCDLDBnk.getDevice(i);
         device.deviceType().markInterested();
         device.name().markInterested();
         device.position().markInterested();
      }

      //this! this makes the bank follow the cursor device!
      mCursorDevice.position().addValueObserver(cp -> {
         if (cp >= 0) {
            mCDLDBnk.scrollPosition().set(cp - 1);
         }
      });

      mTrackBank = mHost.createTrackBank(3,0,0,true);
      mTrackBank.followCursorTrack(mCursorTrack);
      mTrackBank.canScrollBackwards().markInterested();
      mTrackBank.canScrollForwards().markInterested();
      mTrackBank.itemCount().markInterested();
      mTrackBank.scrollPosition().markInterested();
      mTrackBank.cursorIndex().markInterested();
  

  for (int i = 0; i < mTrackBank.getSizeOfBank(); i++) {
         final Track track = mTrackBank.getItemAt(i);
         track.trackType().markInterested();
         track.name().markInterested();
         track.position().markInterested();
         track.isGroup().markInterested();
      }

      mCursorTrack.position().addValueObserver(cp -> {
         if (cp >= 0) {
            mTrackBank.scrollPosition().set(cp - 1);
         }
      });

      mRemoteControls = mCursorDevice.createCursorRemoteControlsPage("CursorPage1", 8, "");
      mRemoteControls.setHardwareLayout(HardwareControlType.ENCODER, 8);
    
      //Transport
      mTransport = mHost.createTransport();
      mTransport.isPlaying().markInterested();
      mTransport.isArrangerRecordEnabled ().markInterested ();
      mTransport.isMetronomeEnabled ().markInterested();
      mTransport.playStartPosition().markInterested();
      mTransport.playPositionInSeconds().markInterested();

      //API Hardware surface
      inithardwareSurface(mHost);

      //Modes
      //TODO this does not have any effect it seems. Still need to force the app to select the first track on start to get the ball rolling.
      mApplication.selectFirst();
      
      //Layers
      initLayers(); 
    
      //as a value observer, this is evaluated AFTER the init is completed. this is where, f.e. the Baselayer was being re-activated during startup. 
      mPopupBrowser.exists().addValueObserver(exists -> {
         if (exists)
         {
            activateLayer(mBrowserLayer, null);
            DM.BrowserMode();
         } 
         else{
            mBrowserLayer.deactivate();
            activateLayer(mLastLayer, null);
            //TODO this is ugly and manual, but it should work. I cannot get a good logic to do this.
            if (mLastLayer == mInstLayer){DM.InstMode();}
            if (mLastLayer == mInst2Layer){DM.Inst2Mode();}
            if (mLastLayer == mSongLayer){DM.SongMode();}
            if (mLastLayer == mSong2Layer){DM.Song2Mode();}
            if (mLastLayer == mEditLayer){DM.EditMode();}
            if (mLastLayer == mUserLayer){DM.UserMode();}
         }
            
      });

      //these HAVE to stay at the bottom! this does allow the package file to use "this" to access the variable tho!
      DM.start(this);
      DM.initHW();
      DM.InstMode();

      //mLastLayer must be the layer you intend to start with. if you set it to Base, odd shit happens. So we are priming the variable here. 
      mLastLayer = mInstLayer;
      activateLayer(mInstLayer, null);
      mHost.println("Init complete");

      //Notifications      
      mHost.showPopupNotification("Atom SQ Initialized");

   }

     ////////////////////////
    //  Hardware Surface  //
   ////////////////////////

   private void inithardwareSurface(ControllerHost mHost)
   {
      //called in Init
      final ControllerHost host = mHost;
      final HardwareSurface surface = host.createHardwareSurface();
      mHardwareSurface = surface;
      surface.setPhysicalSize(400, 200);

      //Shift
      mShiftButton = createToggleButton("shift", hH.CC_SHIFT, hH.ORANGE);
      mShiftButton.setLabel("Shift");

      // NAV section
      mUpButton = createToggleButton("up", hH.CC_UP, hH.ORANGE);
      mUpButton.setLabel("Up");
      mDownButton = createToggleButton("down", hH.CC_DOWN, hH.ORANGE);
      mDownButton.setLabel("Down");
      mLeftButton = createToggleButton("left", hH.CC_LEFT, hH.ORANGE);
      mLeftButton.setLabel("Left");
      mRightButton = createToggleButton("right", hH.CC_RIGHT, hH.ORANGE);
      mRightButton.setLabel("Right");
      mBackButton = createToggleButton("back", hH.CC_BACK, hH.ORANGE);
      mBackButton.setLabel("Back");
      mForwardButton = createToggleButton("forward", hH.CC_FORWARD, hH.ORANGE);
      mForwardButton.setLabel("Forward");

      // TRANS section
      mClickCountInButton = createToggleButton("click_count_in", hH.CC_METRONOME, hH.BLUE);
      mClickCountInButton.setLabel("Click\nCount in");
      mRecordSaveButton = createToggleButton("record_save", hH.CC_REC, hH.RED);
      mRecordSaveButton.setLabel("Record\nSave");
      mPlayLoopButton = createToggleButton("play_loop", hH.CC_PLAY, hH.GREEN);
      mPlayLoopButton.setLabel("Play\nLoop");
      mStopUndoButton = createToggleButton("stop_undo", hH.CC_STOP, hH.ORANGE);
      mStopUndoButton.setLabel("Stop\nUndo");

      // SONG section
      mSongButton = createToggleButton("song", hH.CC_SONG, hH.ORANGE);
      mSongButton.setLabel("SONG");
      mEditorButton = createToggleButton("editor", hH.CC_EDIT, hH.ORANGE);
      mEditorButton.setLabel("Editor");
      mInstButton = createToggleButton("inst", hH.CC_INST, hH.ORANGE);
      mInstButton.setLabel("Inst");
      mUserButton = createToggleButton("user", hH.CC_USER, hH.ORANGE);
      mUserButton.setLabel("User");
      mAButton = createToggleButton("a", hH.CC_BTN_A, hH.RED);
      mAButton.setLabel("A");

      //Buttons
      m1Button = createToggleButton("1", hH.CC_BTN_1, hH.ORANGE);
      m1Button.setLabel ("Btn 1");
      m2Button = createToggleButton("2", hH.CC_BTN_2, hH.ORANGE);
      m2Button.setLabel ("Btn 2");
      m3Button = createToggleButton("3", hH.CC_BTN_3, hH.ORANGE);
      m3Button.setLabel ("Btn 3");
      m4Button = createToggleButton("4", hH.CC_BTN_4, hH.ORANGE);
      m4Button.setLabel ("Btn 4");
      m5Button = createToggleButton("5", hH.CC_BTN_5, hH.ORANGE);
      m5Button.setLabel ("Btn 5");
      m6Button = createToggleButton("6", hH.CC_BTN_6, hH.ORANGE);
      m6Button.setLabel ("Btn 6");

      //Encoders
      for (int i = 0; i < 9; i++)
      {
         createEncoder(i);
      }
      
      setPhysicalPositions();
   }

   private void setPhysicalPositions()
   {
      //called in CreateHardwareSurface
      final HardwareSurface surface = mHardwareSurface;
      //Paste the code from the hardware designer here:
      surface.hardwareElementWithId("shift").setBounds(368.75, 98.25, 13.0, 8.5);
      surface.hardwareElementWithId("up").setBounds(313.75, 90.5, 13.25, 5.25);
      surface.hardwareElementWithId("down").setBounds(313.75, 101.0, 13.25, 5.25);
      surface.hardwareElementWithId("left").setBounds(295.0, 101.25, 13.25, 5.25);
      surface.hardwareElementWithId("right").setBounds(332.0, 101.0, 13.25, 5.25);
      surface.hardwareElementWithId("back").setBounds(360.25, 67.0, 11.25, 5.5);
      surface.hardwareElementWithId("forward").setBounds(377.25, 67.0, 11.0, 5.5);
      surface.hardwareElementWithId("click_count_in").setBounds(62.0, 93.25, 12.75, 12.75);
      surface.hardwareElementWithId("record_save").setBounds(44.5, 92.75, 12.75, 12.75);
      surface.hardwareElementWithId("play_loop").setBounds(27.25, 93.0, 12.75, 12.75);
      surface.hardwareElementWithId("stop_undo").setBounds(8.75, 93.0, 12.75, 12.75);
      surface.hardwareElementWithId("song").setBounds(262.5, 21.25, 13.25, 9.0);
      surface.hardwareElementWithId("editor").setBounds(262.5, 50.25, 13.0, 9.0);
      surface.hardwareElementWithId("inst").setBounds(262.25, 36.25, 13.25, 9.0);
      surface.hardwareElementWithId("user").setBounds(262.5, 64.5, 12.75, 9.0);
      surface.hardwareElementWithId("a").setBounds(8.75, 67.75, 13.25, 6.5);
      surface.hardwareElementWithId("encoder1").setBounds(115.75, 21.25, 11.5, 10.5);
      surface.hardwareElementWithId("encoder2").setBounds(155.75, 21.0, 10.0, 10.0);
      surface.hardwareElementWithId("encoder3").setBounds(196.0, 21.25, 10.0, 10.0);
      surface.hardwareElementWithId("encoder4").setBounds(235.5, 21.5, 10.0, 10.0);
      surface.hardwareElementWithId("encoder5").setBounds(95.5, 53.25, 10.0, 10.0);
      surface.hardwareElementWithId("encoder6").setBounds(135.5, 53.0, 10.0, 10.0);
      surface.hardwareElementWithId("encoder7").setBounds(175.5, 53.5, 10.0, 10.0);
      surface.hardwareElementWithId("encoder8").setBounds(215.75, 53.25, 10.0, 10.0);
      surface.hardwareElementWithId("encoder9").setBounds(369.25, 42.0, 10.0, 10.0);
      surface.hardwareElementWithId("1").setBounds(294.75, 11.25, 12.75, 5.75);
      surface.hardwareElementWithId("2").setBounds(313.5, 11.75, 13.25, 5.5);
      surface.hardwareElementWithId("3").setBounds(332.5, 12.0, 13.25, 5.5);
      surface.hardwareElementWithId("4").setBounds(295.0, 76.5, 13.25, 5.5);
      surface.hardwareElementWithId("5").setBounds(314.0, 76.25, 13.25, 5.5);
      surface.hardwareElementWithId("6").setBounds(332.25, 76.75, 13.25, 5.5);
   }

   private HardwareButton createToggleButton(
      final String id,
      final int controlNumber,
      final Color onLightColor)
   {
      final HardwareButton button = createButton(id, controlNumber);
      final OnOffHardwareLight light = mHardwareSurface.createOnOffHardwareLight(id + "_light");
      final Color offColor = Color.mix(onLightColor, Color.blackColor(), 0.5);

      light.setStateToVisualStateFunction(
         isOn -> isOn ? HardwareLightVisualState.createForColor(onLightColor, Color.blackColor())
            : HardwareLightVisualState.createForColor(offColor, Color.blackColor()));
      button.setBackgroundLight(light);
      light.isOn().onUpdateHardware(value -> {
         mMidiOut.sendMidi(0xB0, controlNumber, value ? 127 : 0);
      });

      return button;
   }

   private HardwareButton createButton(final String id, final int controlNumber)
   {
      final HardwareButton button = mHardwareSurface.createHardwareButton(id);
      final MidiExpressions midiExpressions = mHost.midiExpressions();

      button.pressedAction().setActionMatcher(mMidiIn
         .createActionMatcher(midiExpressions.createIsCCExpression(0, controlNumber) + " && data2 > 0"));
      button.releasedAction().setActionMatcher(mMidiIn.createCCActionMatcher(0, controlNumber, 0));
      button.setLabelColor(hH.BLACK);

      return button;
   }

   private void createEncoder(final int index)
   {
      assert index >= 0 && index < 8;

      final RelativeHardwareKnob encoder = mHardwareSurface
         .createRelativeHardwareKnob("encoder" + (index + 1));
      encoder.setLabel(String.valueOf(index + 1));
      encoder.setIndexInGroup(index);
      //here you can adjust the last number to adjust the encoder sensitivity wthin BW. Smaller numbers are jumpy, but move faster
      //the knobs ARE speed sensitive
      if (index <= 7){
      encoder.setAdjustValueMatcher(mMidiIn.createRelativeSignedBitCCValueMatcher(0, hH.CC_ENCODER_1 + index, 100));
      }
      //as the CC for encoder 9 is not sequencial, have to do it seperately. 
      else {
        encoder.setAdjustValueMatcher(mMidiIn.createRelativeSignedBitCCValueMatcher(0, hH.CC_ENCODER_9, 127));
        //this could be worked on...the stepped encoder does not move as smoothly in BW as the others.
       // encoder.setStepSize(4.0);

      }
     
      mEncoders[index] = encoder;

   }

   private void save()
   {
      final Action saveAction = mApplication.getAction("Save");
      if (saveAction != null)
      {
         saveAction.invoke();
      }
   }

   private void setIsShiftPressed(final boolean value)
   {
      if (value != mShift)
      {
         mShift = value;
         mLayers.setGlobalSensitivity(value ? 0.1 : 1);
      }
   }

   private void changePlayPosition (final Double num)
   {
      
      //this has to be abstracted, cannot set the start position directly int he binding. It returns an error about void.
      final double pos = mTransport.playStartPosition().get();
      double down = (int)pos;
      double up = (int)pos+1; 
      boolean match = (pos == down);
      if (match)
      {
         mTransport.playStartPosition().inc(num);
         mHost.println("match must be true");
      }
      else if (num == -1)
      {
         mHost.println("match must be false");
         mTransport.playStartPosition().set(down);
      }
      else
      {
         mTransport.playStartPosition().set(up);
      };
     
    }  

   public void moveDeviceLeft() 
   {
      final Device previousDevice = mCDLDBnk.getDevice(0);

      previousDevice.beforeDeviceInsertionPoint().moveDevices(mCursorDevice);
      mCursorDevice.selectPrevious();
      mCursorDevice.selectNext();
   }

   public void moveDeviceRight() 
   {
      final Device nextDevice = mCDLDBnk.getDevice(2);

      nextDevice.afterDeviceInsertionPoint().moveDevices(mCursorDevice);
      mCursorDevice.selectPrevious();
      mCursorDevice.selectNext();
   }

   public void moveTrackUp() 
   {
      final Track previousTrack = mTrackBank.getItemAt(0);
      previousTrack.beforeTrackInsertionPoint().moveTracks(mCursorTrack);
      mCursorTrack.selectPrevious();
      mCursorTrack.selectNext();
   }

   public void moveTrackDown() 
   {
      final Track nextTrack = mTrackBank.getItemAt(2);
      nextTrack.afterTrackInsertionPoint().moveTracks(mCursorTrack);
      mCursorTrack.selectPrevious();
      mCursorTrack.selectNext();
   }
 
     ////////////////////////
    //       Layers       //
   ////////////////////////

   private void initLayers()
   {
      // We create all the layers here because the main layer might bind actions to activate other layers.
      //called in Init
      mBaseLayer = createLayer("Base");
      mSongLayer = createLayer("Song");
      mSong2Layer = createLayer("Song2");
      mInstLayer = createLayer("Inst");
      mInst2Layer = createLayer ("Inst2");
      mEditLayer = createLayer ("Edit");
      mUserLayer = createLayer("User");
      mShiftLayer = createLayer ("Shift");
      mBrowserLayer = createLayer("Browser");

      createBaseLayer();
      createInstLayer();
      createSongLayer();
      createEditLayer();
      createUserLayer();
      createInst2Layer();
      createShiftLayer();
      createSong2Layer();
      createBrowserLayer();

      // DebugUtilities.createDebugLayer(mLayers, mHardwareSurface).activate();
   }

   private Layer createLayer(final String name)
   {
      //helper function referenced in initLayers. Saves some typing
      return new Layer(mLayers, name);
   }

   private void activateLayer(final Layer layer, final Layer leaveit) 
   {
      Layer mLeaveIt = leaveit;
      final String layername = layer.getName().toString();
      mHost.println("requested layer to activate: "+layername);

      for (Layer sts : mActiveLayers)
      {
         String stsname = sts.getName().toString();
         mHost.println("layer to evaluate: "+stsname);
         //moved the last layer functionality out of flush so it can indicate the previous layer in activateLayer()
         if (stsname == "Browser"){continue;} 
         else if (stsname == "Shift") {continue;} 
         else if (stsname == "Base") {continue;} 
         else {mLastLayer = sts;}
      }
       
       mHost.println("previous layer is: "+mLastLayer.getName().toString());
      //for all layers except Base, deactivate
      for(Layer alayer: mLayerList)
      { 
         String alayername = alayer.getName().toString();
         mHost.println("iteration layer is: "+alayername);
         if (alayer == mBaseLayer){continue;}
         else if (alayer == mShiftLayer){continue;}
         else if (alayer == mLeaveIt){continue;}
         else if (alayer == layer){
            mHost.println("activating layer: "+alayername);
            alayer.activate();}
         else {alayer.deactivate();}
      }
   }

   private void getactiveLayers(Layers mLayers)
   {
      mActiveLayers.clear();
      int mLayersCount = mLayers.getLayers().size();
      mHost.println("Initialized layer count: "+ Integer.toString (mLayersCount));
      for ( Layer str : mLayerList )
      {
         if (str.isActive()) {mActiveLayers.add(str);}
     }
   }

   private void createBaseLayer()
   {
      //Shift
      mBaseLayer.bindIsPressed(mShiftButton, this::setIsShiftPressed);

      //alternative, for a shift layer:
      mBaseLayer.bindPressed(mShiftButton, mShiftLayer.getActivateAction());
      mBaseLayer.bindReleased(mShiftButton, mShiftLayer.getDeactivateAction());

      //Master Controls
      //Encoder 9
     final HardwareActionBindable inc = mHost.createAction(() ->  changePlayPosition(1.0),  () -> "+");;
      final HardwareActionBindable dec = mHost.createAction(() -> changePlayPosition(-1.0),  () -> "-");
      mBaseLayer.bind(mEncoders[8], mHost.createRelativeHardwareControlStepTarget(inc, dec));

      //left and right shift function for undo/redo
      mBaseLayer.bindPressed(mBackButton, () -> {
         if (mShift){
            mApplication.undo(); 
            mHost.showPopupNotification("Undo");
         }
         else if (mInst2Layer.isActive()) {
            activateLayer(mInstLayer, null);
            DM.InstMode();
         }
         else if (mSong2Layer.isActive()){
            activateLayer(mSongLayer, null);
            DM.SongMode();
         }
         
      });
         
      mBaseLayer.bindPressed(mForwardButton, () -> {
         if (mShift) {
         mApplication.redo(); 
         mHost.showPopupNotification("Redo");
         }
         else if (mInstLayer.isActive()) {
           activateLayer(mInst2Layer, mInstLayer);
            DM.Inst2Mode();
         
         }
         else if (mSongLayer.isActive()){
           activateLayer(mSong2Layer, mSongLayer);
            DM.Song2Mode();
         }

      });
   
      //Menu Buttons
      mBaseLayer.bindPressed(mSongButton, () -> {
         activateLayer(mSongLayer, null);
         DM.SongMode(); 
         });
      
      mBaseLayer.bindPressed(mInstButton, () -> {
         activateLayer(mInstLayer, null);
         DM.InstMode();
         });

      mBaseLayer.bindPressed(mEditorButton, () -> {
         activateLayer(mEditLayer, null);
         DM.EditMode();
         });

      mBaseLayer.bindPressed(mUserButton, () -> {
         activateLayer(mUserLayer, null);
         DM.UserMode();
         });
      
      //Transport
      mBaseLayer.bindToggle(mClickCountInButton, mTransport.isMetronomeEnabled());
      mBaseLayer.bindToggle(mPlayLoopButton, () -> {
         if (mShift)
            mTransport.isArrangerLoopEnabled().toggle();
         else
            mTransport.play();
      }, mTransport.isPlaying());

      mBaseLayer.bindToggle(mStopUndoButton, () -> {
         if (mShift)
            mApplication.undo();
         else
            mTransport.stop();
      }, () -> !mTransport.isPlaying().get());

      mBaseLayer.bindToggle(mRecordSaveButton, () -> {
         if (mShift)
            save();
         else
            mTransport.isArrangerRecordEnabled().toggle();
      }, mTransport.isArrangerRecordEnabled());

      //Nav buttons
      //TODO if there is a way to identify the panels by name, this would be better than above/below
      mBaseLayer.bindToggle(mUpButton, mCursorTrack.selectPreviousAction(), mCursorTrack.hasPrevious());
      mBaseLayer.bindToggle(mDownButton, mCursorTrack.selectNextAction(), mCursorTrack.hasNext());
      mBaseLayer.bindPressed(mUpButton, () -> {mApplication.focusPanelAbove();});
      mBaseLayer.bindPressed(mDownButton, () -> {mApplication.focusPanelAbove();});
      mBaseLayer.bindToggle(mLeftButton, mCursorDevice.selectPreviousAction(),mCursorDevice.hasPrevious());
      mBaseLayer.bindToggle(mRightButton, mCursorDevice.selectNextAction(), mCursorDevice.hasNext());
      mBaseLayer.bindPressed(mLeftButton, () -> {mApplication.focusPanelBelow();});
      mBaseLayer.bindPressed(mRightButton, () -> {mApplication.focusPanelBelow();});

      //only done in the base layer, because base layer.
      mBaseLayer.activate();
   }

   private void createShiftLayer()
   {
      //this can coincide with other shift functions in the base layer!
      mShiftLayer.bind(mEncoders[8], mMasterTrack.volume());
      mShiftLayer.bindPressed(mUpButton, () ->{
         mApplication.focusPanelAbove();
      } );
      mShiftLayer.bindPressed(mDownButton, () ->{
         mApplication.focusPanelBelow();
      } );
      mShiftLayer.bindPressed(mLeftButton, () ->{
         mApplication.focusPanelToLeft();
      } );
      mShiftLayer.bindPressed(mRightButton, () ->{
         mApplication.focusPanelToRight();
      } );
   }

   private void createSongLayer()
   {
      //this turns lights on and off. 
      mSongLayer.bind(() -> true, mForwardButton);
      mSongLayer.bind(() -> true, mSongButton);

      //Display buttons
      mSongLayer.bindToggle(m1Button, mCursorTrack.mute() );
      mSongLayer.bindToggle(m2Button, mCursorTrack.solo());
      mSongLayer.bindToggle(m3Button, mCursorTrack.arm());
      //mSongLayer.bindToggle(m4Button, mCursorDevice.isEnabled());
      mSongLayer.bindPressed(m5Button,() ->{moveTrackUp();});
      mSongLayer.bindPressed(m6Button, () ->{moveTrackDown();});
     
      //Track Encoders
      mSongLayer.bind (mEncoders[6], mCursorTrack.pan());
      mSongLayer.bind (mEncoders[7], mCursorTrack.volume());

      //Send Encoders
      for (int i = 0; i < 6 ; i++)
      {
         final Parameter parameter = mSendBank.getItemAt(i);
         final RelativeHardwareKnob encoder = mEncoders[i];
         mSongLayer.bind(encoder, parameter);
      }
   }

   private void createSong2Layer() 
   {
      //this turns lights on and off. 
      mSong2Layer.bind(() -> true, mBackButton);
      mSong2Layer.bind(() -> false, mForwardButton);
      mSong2Layer.bind(() -> true, mSongButton);

      mSong2Layer.bindToggle(m1Button, mCursorTrack.isActivated());
      mSong2Layer.bindPressed(m2Button, () -> {mApplication.focusPanelAbove(); mApplication.duplicate();});
      mSong2Layer.bindPressed(m3Button, () -> {mApplication.focusPanelAbove(); mCursorTrack.deleteObject();});
      mSong2Layer.bindPressed(m4Button, () -> {mApplication.createAudioTrack(mCursorTrack.position().get()+1);});  
      mSong2Layer.bindPressed(m5Button, () -> {mApplication.createInstrumentTrack(mCursorTrack.position().get()+1);});  
      mSong2Layer.bindPressed(m6Button, () -> {mApplication.createEffectTrack(mCursorTrack.position().get()+1);});  
   }

   private void createInstLayer()
   {
      //this turns lights on and off. 
      mInstLayer.bind(() -> true, mForwardButton);
      mInstLayer.bind(() -> true, mInstButton);

      mInstLayer.bindToggle(m1Button, mCursorDevice.isEnabled(), mCursorDevice.isEnabled() );
      mInstLayer.bindToggle(m2Button, mCursorDevice.isWindowOpen(), mCursorDevice.isWindowOpen());
      mInstLayer.bindToggle(m3Button, mCursorDevice.isExpanded(), mCursorDevice.isExpanded());
      mInstLayer.bindToggle(m4Button, mCursorDevice.isRemoteControlsSectionVisible(), mCursorDevice.isRemoteControlsSectionVisible());
      mInstLayer.bindPressed(m5Button, () ->{moveDeviceLeft();});
      mInstLayer.bindPressed(m6Button, () ->{moveDeviceRight();});
        
      //Encoders
      for (int i = 0; i < 8; i++)
      {
         final Parameter parameter = mRemoteControls.getParameter(i);
         final RelativeHardwareKnob encoder = mEncoders[i];

         mInstLayer.bind(encoder, parameter);
      }
   }

   private void createInst2Layer()
   {
      //this turns lights on and off. 
      mInst2Layer.bind(() -> true, mBackButton);
      mInst2Layer.bind(() -> false, mForwardButton);
      mInst2Layer.bind(() -> true, mInstButton);
      
      //mInst2Layer.bindToggle(m5Button, mCursorDevice.isMacroSectionVisible()); //macro is wrong, want modulation, cannot find rn.
      mInst2Layer.bindPressed(m2Button, () -> {mApplication.focusPanelBelow(); mApplication.duplicate();});
      mInst2Layer.bindPressed(m3Button, () -> {mApplication.focusPanelBelow(); mCursorDevice.deleteObject();});
      mInst2Layer.bindPressed(m4Button, () -> {mCursorDevice.beforeDeviceInsertionPoint().browse();});
      mInst2Layer.bindPressed(m5Button, () -> {startPresetBrowsing();});
      mInst2Layer.bindPressed(m6Button, () -> {mCursorDevice.afterDeviceInsertionPoint().browse();});
   }

   private void createEditLayer()
   {
      mEditLayer.bind(() -> true, mEditorButton);
   }

   private void createUserLayer()
   {

      mUserLayer.bind(() -> true, mUserButton);

   }
  
 private void createBrowserLayer()
   {
      final HardwareActionBindable inc4 = mHost.createAction(() ->  mBrowserCategory.selectNext(),  () -> "+");
      final HardwareActionBindable dec4 = mHost.createAction(() -> mBrowserCategory.selectPrevious(),  () -> "-");
      mBrowserLayer.bind(mEncoders[4], mHost.createRelativeHardwareControlStepTarget(inc4, dec4));

      final HardwareActionBindable inc5 = mHost.createAction(() ->  mBrowserTag.selectNext(),  () -> "+");
      final HardwareActionBindable dec5 = mHost.createAction(() -> mBrowserTag.selectPrevious(),  () -> "-");
      mBrowserLayer.bind(mEncoders[5], mHost.createRelativeHardwareControlStepTarget(inc5, dec5));

      final HardwareActionBindable inc6 = mHost.createAction(() ->  mBrowserCreator.selectNext(),  () -> "+");
      final HardwareActionBindable dec6 = mHost.createAction(() -> mBrowserCreator.selectPrevious(),  () -> "-");
      mBrowserLayer.bind(mEncoders[6], mHost.createRelativeHardwareControlStepTarget(inc6, dec6));

      final HardwareActionBindable inc7 = mHost.createAction(() ->  mBrowserResult.selectNext(),  () -> "+");
      final HardwareActionBindable dec7 = mHost.createAction(() -> mBrowserResult.selectPrevious(),  () -> "-");
      mBrowserLayer.bind(mEncoders[7], mHost.createRelativeHardwareControlStepTarget(inc7, dec7));

      mBrowserLayer.bindToggle(m4Button, mPopupBrowser.shouldAudition(), mPopupBrowser.shouldAudition());
      mBrowserLayer.bindPressed(m5Button, mPopupBrowser.cancelAction());
      mBrowserLayer.bindPressed(m6Button, mPopupBrowser.commitAction());

   }  

private void startPresetBrowsing()
{
   if (mCursorDevice.exists().get())
   {
      mCursorDevice.replaceDeviceInsertionPoint().browse();
   }
   else
   {
      mCursorDevice.deviceChain().endOfDeviceChainInsertionPoint().browse();
   }
}


     ////////////////////////
    //  Standard Methods  //
   ////////////////////////

   private void onMidi0(final ShortMidiMessage msg)
   {
      //uncomment this to allow the midi messages to display in the console. 
      //mHost.println(msg.toString());
   }
   
   @Override
   public void flush()
   {
      mHost.println("FLUSH INFO:");

      //Flush actions
      mHardwareSurface.updateHardware();
      DM.updateDisplay();
      getactiveLayers(mLayers);

      //Layer Troubleshooting infos
      //   mActiveLayers.clear();
      //    int mLayersCount = mLayers.getLayers().size();
      //    mHost.println("Initialized layer count: "+ Integer.toString (mLayersCount));
      //    //List<Layer> mLayerList = mLayers.getLayers();
      //    for ( Layer str : mLayerList ) {
      //     // String active = Boolean.toString(str.isActive());
      //      // mHost.println("Layer:  "+str.getName().toString()+" is active: " +active);
      //      // mHost.println(active);
      //    if (str.isActive()) {mActiveLayers.add(str);}
      //   }

      //this works too! uses the list of layers above, and that is defined at the very top.
      //   mHost.println("***These are the active layers:***");
      //   for (Layer sts : mActiveLayers)
      //   {
      //    mHost.println(sts.getName().toString());

      // }
      // mHost.println("Last Layer is: "+ mLastLayer.getName().toString());
      
      // //for testing the calling of devices from the device bank.
      // String mcdname = mCursorDevice.name().get();
      // mHost.println("Cursor Device is: "+mcdname);
      // for (int i = 0; i < mCDLDBnk.getSizeOfBank(); i++) {
      //    Device device = mCDLDBnk.getDevice(i);
      //    String  devname = device.name().get();
      //    mHost.println("device"+i+" name is "+devname);
      // }

      // //to review track bank
      // String  mctname = mCursorTrack.name().get();
      // mHost.println("Cursor Track is: "+mctname);
      // for (int i = 0; i < mTrackBank.getSizeOfBank(); i++) {
      //    Track track = mTrackBank.getItemAt(i);
      //    String  trackname = track.name().get();
      //    mHost.println("Track"+i+" name is "+trackname);
      // }

   }

   @Override
   public void exit()
{
   //Exit Live Mode
   mMidiOut.sendMidi(143,00,00);
   mHost.showPopupNotification("Atom SQ Exited");
}

     ////////////////////////
    // Host Proxy Objects //
   ////////////////////////

   public CursorDevice mCursorDevice;
   public CursorTrack mCursorTrack;
   private  DeviceBank mCDLDBnk;
   private TrackBank mTrackBank;
   public PopupBrowser mPopupBrowser;
   public CursorBrowserResultItem mBrowserResult;
   private CursorBrowserFilterItem mBrowserCategory;
   private CursorBrowserFilterItem mBrowserCreator;
   private CursorBrowserFilterItem mBrowserTag;
   private DisplayMode DM;
   public ControllerHost mHost;
   private static HardwareHandler hH = new HardwareHandler();

   private MasterTrack mMasterTrack;
   private SendBank mSendBank;
   public int sends;
   private CursorRemoteControlsPage mRemoteControls;
   private Transport mTransport;
   private MidiIn mMidiIn;
   public MidiOut mMidiOut;
   public Application mApplication;
   private boolean mShift;
   private HardwareSurface mHardwareSurface;
   private HardwareButton mShiftButton, mUpButton, mDownButton, mLeftButton, mRightButton, mForwardButton,
      mBackButton, mClickCountInButton, mRecordSaveButton, mPlayLoopButton, mStopUndoButton, mSongButton,
      mEditorButton, mInstButton, mUserButton,  mAButton, m1Button, m2Button, m3Button, m4Button, m5Button, m6Button;
   private RelativeHardwareKnob[] mEncoders = new RelativeHardwareKnob[9];
   private final Layers mLayers = new Layers(this)
   {
      @Override
      protected void activeLayersChanged()
      {
         super.activeLayersChanged();
      }
   }; 
   public Layer mBaseLayer, mInstLayer, mSongLayer, mSong2Layer, mEditLayer, mUserLayer, mInst2Layer, mShiftLayer, mInst3Layer, mBrowserLayer;
   private Layer mLastLayer;
   //final int mLayersCount = mLayers.getLayers().size();
   final List<Layer> mLayerList = mLayers.getLayers();
   final List<Layer> mActiveLayers = new ArrayList<>();

}