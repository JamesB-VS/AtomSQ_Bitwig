package com.presonus;

//frop ATOM
import java.util.function.Consumer;
import java.util.function.Supplier;
//from Hardware
import java.util.Arrays;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorDeviceFollowMode;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;

//FromATOM
import com.bitwig.extension.api.Color;
import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.Action;
import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.Arpeggiator;
import com.bitwig.extension.controller.api.Clip;
import com.bitwig.extension.controller.api.ClipLauncherSlot;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorDeviceFollowMode;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.DrumPad;
import com.bitwig.extension.controller.api.DrumPadBank;
import com.bitwig.extension.controller.api.HardwareButton;
import com.bitwig.extension.controller.api.HardwareControlType;
import com.bitwig.extension.controller.api.HardwareLightVisualState;
import com.bitwig.extension.controller.api.HardwareSurface;
import com.bitwig.extension.controller.api.MidiExpressions;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.MultiStateHardwareLight;
import com.bitwig.extension.controller.api.NoteInput;
import com.bitwig.extension.controller.api.NoteStep;
import com.bitwig.extension.controller.api.OnOffHardwareLight;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.PinnableCursorDevice;
import com.bitwig.extension.controller.api.PlayingNote;
import com.bitwig.extension.controller.api.RelativeHardwareKnob;
import com.bitwig.extension.controller.api.Scene;
import com.bitwig.extension.controller.api.SceneBank;
import com.bitwig.extension.controller.api.Transport;
//these are not in the regular APIs...they come from the Bitwig repo though. 
// import com.bitwig.extensions.framework.BooleanObject;
// import com.bitwig.extensions.framework.Layer;
// import com.bitwig.extensions.framework.Layers;
// import com.bitwig.extensions.util.NoteInputUtils;


// import com.presonus.handler.TransportHandler;
// //import com.presonus.handler.CursorHandler;
// import com.presonus.handler.ModeHandler;


public class AtomSQExtension extends ControllerExtension
{

   //Variables from Hardware
   // Transport
   private final static int  CC_PLAY      = 109;
   private final static int  CC_STOP      = 111;
   private final static int  CC_REC       = 107;
   private final static int  CC_METRONOME = 105;
   //Menu Buttons
   private final static int  CC_SONG      = 32;
   private final static int  CC_INST      = 33;
   private final static int  CC_EDIT      = 34;
   private final static int  CC_USER      = 35;
   //Arrow Buttons
   private final static int  CC_AUP       =87;
   private final static int  CC_ADWN      =89;
   private final static int  CC_ALFT      =90;
   private final static int  CC_ARGT      =102;
   //shift
   private final static int  CC_SHIFT     =31;
   //A-H (only A works in Live mode)
   private final static int  CC_BTN_A     =64;
   private final static int  CHAN_1 = 176;
   //private final static int  CHAN_2 = 177;
   //Display Buttons, top to bottom, left to right
   //1 2 3
   //[   ]
   //4 5 6
   private final static int  CC_BTN_1     =36;
   private final static int  CC_BTN_2     =37;
   private final static int  CC_BTN_3     =38;
   private final static int  CC_BTN_4     =39;
   private final static int  CC_BTN_5     =40;
   private final static int  CC_BTN_6     =41;
   //outside of the INST menu, the encoder and buttons on the right
   private final static int  CC_LEFT      =42;
   private final static int  CC_RIGHT     =43;
   private final static int  CC_ENCODER_9     =29;
   //device name, note on, note off, pressure, ribbon, pitchbend
   private final static String  DEV_NAME      = "Keyboard";
   private final static String  NOTE_ON       = "99????";
   private final static String  NOTE_OFF      = "89????";
   private final static String  NOTE_PRES     = "a9????";
   private final static String  NOTE_MOD      = "b001??";
   private final static String  NOTE_BEND     = "e0????";
   private final static int  CC_ENCODER_1     = 14;
   private final static int  CC_ENCODER_2     = 15;
   private final static int  CC_ENCODER_3     = 16;
   private final static int  CC_ENCODER_4     = 17;
   private final static int  CC_ENCODER_5     = 18;
   private final static int  CC_ENCODER_6     = 19;
   private final static int  CC_ENCODER_7     = 20;
   private final static int  CC_ENCODER_8     = 21;
   private final static int[]  ENCODERS = {14,15,16,17,18,19,29,21};
   private final static int[] BUTTONARRAY = {36, 37, 38, 39, 40, 41};


   // private TransportHandler transportHandler;
   // private AtomSQHardware hardware;
   // private ModeHandler modeHandler;
   private final int []          ledCache             = new int [128];


  public AtomSQExtension(final AtomSQExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = getHost();
      mApplication = host.createApplication();

      final MidiIn midiIn = host.getMidiInPort(0);
      midiIn.setMidiCallback((ShortMidiMessageReceivedCallback)msg -> onMidi0(msg));
      mNoteInput = midiIn.createNoteInput (DEV_NAME, NOTE_ON, NOTE_OFF, NOTE_MOD, NOTE_BEND, NOTE_PRES);

      mMidiOut = host.getMidiOutPort(0);
      mMidiIn = host.getMidiInPort(0);

      //Cursor Track / Device stuff
      mCursorTrack = host.createCursorTrack(2, 0);
      mCursorTrack.arm().markInterested();
     // mSceneBank = host.createSceneBank(LAUNCHER_SCENES);
      //mTransport = host.createTransport();
      mCursorDevice = mCursorTrack.createCursorDevice("Current", "Current", 8,  CursorDeviceFollowMode.FOLLOW_SELECTION);
      mRemoteControls = mCursorDevice.createCursorRemoteControlsPage("CursorPage1", 8, "");

   //HW  Layout creation
      mRemoteControls.setHardwareLayout(HardwareControlType.ENCODER, 8);
      for (int i = 0; i < 8; ++i)
         mRemoteControls.getParameter(i).setIndication(true);

      mCursorDevice.isEnabled ().markInterested ();
      mCursorDevice.isWindowOpen ().markInterested ();
      mCursorTrack.solo().markInterested();
      mCursorTrack.mute().markInterested();
      mCursorTrack.arm().markInterested();
      mCursorTrack.volume().markInterested();
      mCursorTrack.pan().markInterested();
      mCursorTrack.isActivated ().markInterested();
      mCursorTrack.color ().markInterested();


      //Transport
      mTransport = host.createTransport();
      mTransport.isPlaying().markInterested();
     // mTransport.getPosition().markInterested();
     mTransport.isArrangerRecordEnabled ().markInterested ();
     mTransport.isMetronomeEnabled ().markInterested();
  
     //mCursorTrack.playingNotes().addValueObserver(notes -> mPlayingNotes = notes);

     //mDrumPadBank = mCursorDevice.createDrumPadBank(16);
    // mDrumPadBank.exists().markInterested();
     mCursorTrack.color().markInterested();

     Arrays.fill (this.ledCache, -1);

     // Turn on Native Mode
     mMidiOut.sendMidi(143,00,01);

//the HW init
    

//Methods
      createHardwareSurface();

      //initLayers();

//Notifications      
      host.showPopupNotification("Atom SQ Initialized");
   }

   private void createHardwareSurface()
   {
      final ControllerHost host = getHost();
      final HardwareSurface surface = host.createHardwareSurface();
      mHardwareSurface = surface;

      surface.setPhysicalSize(202, 195);

      mShiftButton = createToggleButton("shift", CC_SHIFT, ORANGE);
      mShiftButton.setLabel("Shift");

      // NAV section
      mUpButton = createToggleButton("up", CC_UP, ORANGE);
      mUpButton.setLabel("Up");
      mDownButton = createToggleButton("down", CC_DOWN, ORANGE);
      mDownButton.setLabel("Down");
      mLeftButton = createToggleButton("left", CC_LEFT, ORANGE);
      mLeftButton.setLabel("Left");
      mRightButton = createToggleButton("right", CC_RIGHT, ORANGE);
      mRightButton.setLabel("Right");
      mSelectButton = createRGBButton("select", CC_SELECT);
      mSelectButton.setLabel("Select");
      mZoomButton = createToggleButton("zoom", CC_ZOOM, ORANGE);
      mZoomButton.setLabel("Zoom");

      // TRANS section
      mClickCountInButton = createToggleButton("click_count_in", CC_CLICK_COUNT_IN, BLUE);
      mClickCountInButton.setLabel("Click\nCount in");
      mRecordSaveButton = createToggleButton("record_save", CC_RECORD_SAVE, RED);
      mRecordSaveButton.setLabel("Record\nSave");
      mPlayLoopButton = createToggleButton("play_loop", CC_PLAY_LOOP_TOGGLE, GREEN);
      mPlayLoopButton.setLabel("Play\nLoop");
      mStopUndoButton = createToggleButton("stop_undo", CC_STOP_UNDO, ORANGE);
      mStopUndoButton.setLabel("Stop\nUndo");

      // SONG section
      mSetupButton = createToggleButton("setup", CC_SETUP, ORANGE);
      mSetupButton.setLabel("Setup");
      mSetLoopButton = createToggleButton("set_loop", CC_SET_LOOP, ORANGE);
      mSetLoopButton.setLabel("Set Loop");

      // EVENT section
      mEditorButton = createToggleButton("editor", CC_EDITOR, ORANGE);
      mEditorButton.setLabel("Editor");
      mNudgeQuantizeButton = createToggleButton("nudge_quantize", CC_NUDGE_QUANTIZE, ORANGE);
      mNudgeQuantizeButton.setLabel("Nudge\nQuantize");

      // INST section
      mShowHideButton = createToggleButton("show_hide", CC_SHOW_HIDE, ORANGE);
      mShowHideButton.setLabel("Show/\nHide");
      mPresetPadSelectButton = createToggleButton("preset_pad_select", CC_PRESET_PAD_SELECT, WHITE);
      mPresetPadSelectButton.setLabel("Preset +-\nFocus");
      mBankButton = createToggleButton("bank", CC_BANK_TRANSPOSE, WHITE);
      mBankButton.setLabel("Bank");

      // MODE section
      mFullLevelButton = createToggleButton("full_level", CC_FULL_LEVEL, RED);
      mFullLevelButton.setLabel("Full Level");
      mNoteRepeatButton = createToggleButton("note_repeat", CC_NOTE_REPEAT, RED);
      mNoteRepeatButton.setLabel("Note\nRepeat");

      // Pads

      for (int i = 0; i < 16; i++)
      {
         final DrumPad drumPad = mDrumPadBank.getItemAt(i);
         drumPad.exists().markInterested();
         drumPad.color().markInterested();

         createPadButton(i);
      }

      for (int i = 0; i < 4; i++)
      {
         createEncoder(i);
      }

      initHardwareLayout();
   }

   private void initHardwareLayout()
   {

   }


   @Override
   public void exit()
   {
      // TODO: Perform any cleanup once the driver exits
      // For now just show a popup notification for verification that it is no longer running.
      getHost().showPopupNotification("Atom SQ Exited");
   }

   @Override
   public void flush()
   {
      //this.transportHandler.updateLED ();
   }


private void onMidi0(final ShortMidiMessage msg)
{
   // getHost().println(msg.toString());
}
   /** Called when we receive short MIDI message on port 0. */
//    private void handleMidi(final int statusByte, final int data1, final int data2)
//    {
//       // final ShortMidiMessage msg = new ShortMidiMessage (statusByte, data1, data2);
//       // //this.shiftOn = shiftOn;
//       // //int menumode;
//       // int menumode = AtomSQHardware.CC_USER;

//       // if (this.hardware.handleShift(data1, data2, false))
//       // return;
      
//       // if (this.transportHandler.handleMidi (msg))
//       // return;

//       // if (this.modeHandler.handleMidi (msg))
//       // return;
      
//       // if (this.hardware.HandleEncoders (msg, menumode, false))
//       // return;

// //   if (this.modeHandler.handleMidi (msg))
// //       return;
      
//       //this.getHost ().println(msg.getStatusByte () +" "+ msg.getData1 () +" "+ msg.getData2 ());
//       //this.getHost ().errorln ("Midi command not processed: " + msg.getStatusByte () + " : " + msg.getData1 ());
//    }


  // private Transport mTransport;
  private CursorTrack mCursorTrack;

  //changed from pinnable cursor device
  private CursorDevice mCursorDevice;

  private CursorRemoteControlsPage mRemoteControls;

  private Transport mTransport;

  private MidiIn mMidiIn;

  private MidiOut mMidiOut;

  private Application mApplication;

  private DrumPadBank mDrumPadBank;

  private boolean mShift;

  private NoteInput mNoteInput;

//   private PlayingNote[] mPlayingNotes;

//   private Clip mCursorClip;

//   private int mPlayingStep;

//   private int[] mStepData = new int[16];

//   private int mCurrentPadForSteps;

//   private int mCurrentPageForSteps;

  private HardwareSurface mHardwareSurface;

  private HardwareButton mShiftButton, mUpButton, mDownButton, mLeftButton, mRightButton, mSelectButton,
     mZoomButton, mClickCountInButton, mRecordSaveButton, mPlayLoopButton, mStopUndoButton, mSetupButton,
     mSetLoopButton, mEditorButton, mNudgeQuantizeButton, mShowHideButton, mPresetPadSelectButton,
     mBankButton, mFullLevelButton, mNoteRepeatButton;

//   private HardwareButton[] mPadButtons = new HardwareButton[16];

//   private MultiStateHardwareLight[] mPadLights = new MultiStateHardwareLight[16];

  private RelativeHardwareKnob[] mEncoders = new RelativeHardwareKnob[8];

//   private final Layers mLayers = new Layers(this)
//   {
//      @Override
//      protected void activeLayersChanged()
//      {
//         super.activeLayersChanged();

//         final boolean shouldPlayDrums = !mStepsLayer.isActive() && !mNoteRepeatShiftLayer.isActive()
//            && !mLauncherClipsLayer.isActive() && !mStepsZoomLayer.isActive()
//            && !mStepsSetupLoopLayer.isActive();

//         mNoteInput
//            .setKeyTranslationTable(shouldPlayDrums ? NoteInputUtils.ALL_NOTES : NoteInputUtils.NO_NOTES);
//      }
//   };

//   private Layer mBaseLayer, mStepsLayer, mLauncherClipsLayer, mNoteRepeatLayer, mNoteRepeatShiftLayer,
//      mStepsZoomLayer, mStepsSetupLoopLayer;

//   private Arpeggiator mArpeggiator;

//   private SceneBank mSceneBank;
}
