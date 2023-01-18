package com.presonus;

//frop ATOM
import java.util.function.Consumer;
import java.util.function.Supplier;
//from Hardware
//import java.util.Arrays;

 import com.bitwig.extension.controller.api.CursorDevice;
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
import com.bitwig.extensions.framework.BooleanObject;
import com.bitwig.extensions.framework.Layer;
import com.bitwig.extensions.framework.Layers;
import com.bitwig.extensions.util.NoteInputUtils;


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
   private final static int  CC_UP       =87;
   private final static int  CC_DOWN      =89;
   private final static int  CC_LEFT      =90;
   private final static int  CC_RIGHT      =102;
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
   private final static int  CC_BACK      =42;
   private final static int  CC_FORWARD    =43;
   private final static int  CC_ENCODER_9     =29;
   //device name, note on, note off, pressure, ribbon, pitchbend
   private final static String  DEV_NAME      = "Keyboard";
   private final static String  NOTE_ON       = "99????";
   private final static String  NOTE_OFF      = "89????";
   private final static String  NOTE_PRES     = "a9????"; //poly aftertouch
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

//ATOM colors
   private static final Color WHITE = Color.fromRGB(1, 1, 1);
   private static final Color BLACK = Color.fromRGB(0, 0, 0);
   private static final Color RED = Color.fromRGB(1, 0, 0);
   private static final Color DIM_RED = Color.fromRGB(0.3, 0.0, 0.0);
   private static final Color GREEN = Color.fromRGB(0, 1, 0);
   private static final Color ORANGE = Color.fromRGB(1, 1, 0);
   private static final Color BLUE = Color.fromRGB(0, 0, 1);

   //private final int []          ledCache             = new int [128];


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
      mCursorDevice = mCursorTrack.createCursorDevice("Current", "Current", 8,  CursorDeviceFollowMode.FOLLOW_SELECTION);
      mRemoteControls = mCursorDevice.createCursorRemoteControlsPage("CursorPage1", 8, "");

      //Cursor HW Layout creation
      mRemoteControls.setHardwareLayout(HardwareControlType.ENCODER, 8);
      //Encoder indication
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
      mTransport.isArrangerRecordEnabled ().markInterested ();
      mTransport.isMetronomeEnabled ().markInterested();
   
      //the HW init
      mMidiOut.sendMidi(176,29,00);
      mMidiOut.sendMidi(176,15,00);
      mMidiOut.sendMidi(176,16,00);
      mMidiOut.sendMidi(176,17,00);
      mMidiOut.sendMidi(176,18,00);
      mMidiOut.sendMidi(176,19,00);
      mMidiOut.sendMidi(176,20,00);
      mMidiOut.sendMidi(176,21,00);
      mMidiOut.sendMidi(143,00,00);

      mMidiOut.sendMidi(176,29,00);
      mMidiOut.sendMidi(176,15,00);
      mMidiOut.sendMidi(176,16,00);
      mMidiOut.sendMidi(176,17,00);
      mMidiOut.sendMidi(176,18,00);
      mMidiOut.sendMidi(176,19,00);
      mMidiOut.sendMidi(176,20,00);
      mMidiOut.sendMidi(176,21,00);
      mMidiOut.sendMidi(143,00,00);

      mMidiOut.sendMidi(176,29,00);
      mMidiOut.sendMidi(176,15,00);
      mMidiOut.sendMidi(176,16,00);
      mMidiOut.sendMidi(176,17,00);
      mMidiOut.sendMidi(176,18,00);
      mMidiOut.sendMidi(176,19,00);
      mMidiOut.sendMidi(176,20,00);
      mMidiOut.sendMidi(176,21,00);
      mMidiOut.sendMidi(143,00,00);

      mMidiOut.sendSysex("F07E7F0601F7");
      mMidiOut.sendSysex("F07E7F0601F7");
      mMidiOut.sendSysex("F07E7F0601F7");
      //Live Mode handshake
      mMidiOut.sendMidi(143,00,01);
      //this line alone turns on the lights (paste in the console)
      mMidiOut.sendSysex("F0000106221300F7");
      //this line alone makes the Inst menu at least come back to life!
      //it also takes command of the nav keys on the right...if set to 0, the display still shows and navigates, but thie keys ALSO send midi messages
      // this.portOut.sendSysex("F0000106221401F7");
      mMidiOut.sendSysex("F0000106221301F7");
         

      //Methods
      createHardwareSurface();

      initLayers();

      //mSongButton.pressedAction(getHost().println("Booyah"));


      //Notifications      
      host.showPopupNotification("Atom SQ Initialized");
   }

   private void createHardwareSurface()
   {
      //called in Init
      final ControllerHost host = getHost();
      final HardwareSurface surface = host.createHardwareSurface();
      mHardwareSurface = surface;

      surface.setPhysicalSize(400, 200);
      
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
      mBackButton = createToggleButton("back", CC_BACK, ORANGE);
      mBackButton.setLabel("Back");
      mForwardButton = createToggleButton("forward", CC_FORWARD, ORANGE);
      mForwardButton.setLabel("Forward");

      // TRANS section
      mClickCountInButton = createToggleButton("click_count_in", CC_METRONOME, BLUE);
      mClickCountInButton.setLabel("Click\nCount in");
      mRecordSaveButton = createToggleButton("record_save", CC_REC, RED);
      mRecordSaveButton.setLabel("Record\nSave");
      mPlayLoopButton = createToggleButton("play_loop", CC_PLAY, GREEN);
      mPlayLoopButton.setLabel("Play\nLoop");
      mStopUndoButton = createToggleButton("stop_undo", CC_STOP, ORANGE);
      mStopUndoButton.setLabel("Stop\nUndo");

      // SONG section
      mSongButton = createToggleButton("song", CC_SONG, ORANGE);
      mSongButton.setLabel("SONG");
      mEditorButton = createToggleButton("editor", CC_EDIT, ORANGE);
      mEditorButton.setLabel("Editor");
      mInstButton = createToggleButton("inst", CC_INST, ORANGE);
      mInstButton.setLabel("Inst");
      mUserButton = createToggleButton("user", CC_USER, ORANGE);
      mUserButton.setLabel("User");
      mAButton = createToggleButton("a", CC_BTN_A, RED);
      mAButton.setLabel("A");

      m1Button = createToggleButton("1", CC_BTN_1, ORANGE);
      m1Button.setLabel ("Btn 1");
      m2Button = createToggleButton("2", CC_BTN_2, ORANGE);
      m2Button.setLabel ("Btn 2");
      m3Button = createToggleButton("3", CC_BTN_3, ORANGE);
      m3Button.setLabel ("Btn 3");
      m4Button = createToggleButton("4", CC_BTN_4, ORANGE);
      m4Button.setLabel ("Btn 4");
      m5Button = createToggleButton("5", CC_BTN_5, ORANGE);
      m5Button.setLabel ("Btn 5");
      m6Button = createToggleButton("6", CC_BTN_6, ORANGE);
      m6Button.setLabel ("Btn 6");

     
      for (int i = 0; i < 9; i++)
      {
         createEncoder(i);
      }

      initHardwareLayout();
   }

   private void initHardwareLayout()
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


   @Override
   public void exit()
   {
      // TODO: Perform any cleanup once the driver exits
      // For now just show a popup notification for verification that it is no longer running.
            //Exit Live Mode
            mMidiOut.sendMidi(143,00,00);
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

private void initLayers()
   {
      //called in Init
      mBaseLayer = createLayer("Base");
      mInstLayer = createLayer("Instrument");
      mSongLayer = createLayer("Song");

      initBaseLayer();
      initInstLayer();
      initSongLayer();

      // DebugUtilities.createDebugLayer(mLayers, mHardwareSurface).activate();
   }

   private Layer createLayer(final String name)
   {
      return new Layer(mLayers, name);
   }

   private void initBaseLayer()
   {
      mBaseLayer.bindIsPressed(mShiftButton, this::setIsShiftPressed);
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

      mBaseLayer.bindToggle(mUpButton, mCursorTrack.selectPreviousAction(), mCursorTrack.hasPrevious());
      mBaseLayer.bindToggle(mDownButton, mCursorTrack.selectNextAction(), mCursorTrack.hasNext());
      mBaseLayer.bindToggle(mLeftButton, mCursorDevice.selectPreviousAction(), mCursorDevice.hasPrevious());
      mBaseLayer.bindToggle(mRightButton, mCursorDevice.selectNextAction(), mCursorDevice.hasNext());
      //add toggles for the "layers"
  
      mBaseLayer.bindToggle(mInstButton, mInstLayer);
      //(mInstButton, () -> { mInstLayer.activate();}, () ->!mInstLayer.isActive().get());
     // mSongLayer.bindPressed(mSongButton, () ->{SongMode();
     //                                           getHost().println("Booyah");});
      //(mSongButton,() -> { mSongLayer.activate();}, () ->!mSongLayer.isActive().get());

      // mBaseLayer.bindToggle(mEditorButton, mStepsLayer);

      for (int i = 0; i < 8; i++)
      {
         final Parameter parameter = mRemoteControls.getParameter(i);
         final RelativeHardwareKnob encoder = mEncoders[i];

         mBaseLayer.bind(encoder, parameter);
      }

      mBaseLayer.activate();
   }

   private void InstMode ()
   {
      getHost().println("InstMode");
   }
   private void SongMode ()
   {
      getHost().println("SongMode");
   }

   private void initInstLayer()
   {
      //initialize the bindings for this layer
      getHost().println("Booyah");

      
 
   }
   private void initSongLayer()
   {
      getHost().println("Booyah Song");
      //bind Encoders to cursor Track Pan, Vol and FX
     mSongLayer.bind(mEncoders[4], mCursorTrack.pan());
     mSongLayer.bindToggle(m1Button, mCursorTrack.mute());

  
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
      final MidiExpressions midiExpressions = getHost().midiExpressions();

      button.pressedAction().setActionMatcher(mMidiIn
         .createActionMatcher(midiExpressions.createIsCCExpression(0, controlNumber) + " && data2 > 0"));
      button.releasedAction().setActionMatcher(mMidiIn.createCCActionMatcher(0, controlNumber, 0));
      button.setLabelColor(BLACK);

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
      encoder.setAdjustValueMatcher(mMidiIn.createRelativeSignedBitCCValueMatcher(0, CC_ENCODER_1 + index, 100));

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


  private CursorTrack mCursorTrack;

  //changed from pinnable cursor device
  private CursorDevice mCursorDevice;

  private CursorRemoteControlsPage mRemoteControls;

  private Transport mTransport;

  private MidiIn mMidiIn;

  private MidiOut mMidiOut;

  private Application mApplication;

  //private DrumPadBank mDrumPadBank;

  private boolean mShift;

  private NoteInput mNoteInput;

//   private PlayingNote[] mPlayingNotes;
//   private Clip mCursorClip;
//   private int mPlayingStep;
//   private int[] mStepData = new int[16];
//   private int mCurrentPadForSteps;
//   private int mCurrentPageForSteps;

  private HardwareSurface mHardwareSurface;

  private HardwareButton mShiftButton, mUpButton, mDownButton, mLeftButton, mRightButton, mForwardButton,
     mBackButton, mClickCountInButton, mRecordSaveButton, mPlayLoopButton, mStopUndoButton, mSongButton,
     mSetLoopButton, mEditorButton, mNudgeQuantizeButton, mInstButton, mPresetPadSelectButton,
     mBankButton, mUserButton, mNoteRepeatButton, mAButton, m1Button, m2Button, m3Button, m4Button, m5Button, m6Button;

  private RelativeHardwareKnob[] mEncoders = new RelativeHardwareKnob[9];
  private final Layers mLayers = new Layers(this)
  {
     @Override
     protected void activeLayersChanged()
     {
        super.activeLayersChanged();

        //final boolean shouldPlayDrums = mBaseLayer.isActive();
      //   !mStepsLayer.isActive() && !mNoteRepeatShiftLayer.isActive()
      //      && !mLauncherClipsLayer.isActive() && !mStepsZoomLayer.isActive()
      //      && !mStepsSetupLoopLayer.isActive();

        //mNoteInput
         //  .setKeyTranslationTable(shouldPlayDrums ? NoteInputUtils.ALL_NOTES : NoteInputUtils.NO_NOTES);
     }
  };

 private Layer mBaseLayer, mInstLayer, mSongLayer;

}
