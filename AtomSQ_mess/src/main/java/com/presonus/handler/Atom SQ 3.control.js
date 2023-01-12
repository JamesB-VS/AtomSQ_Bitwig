loadAPI(17);
//loading external files
load("AtomSQ_Hardware3.js");
load("AtomSQ_Init.js");
load("AtomSQ_MidiMap.js");
load("AtomSQ_TransportHandler.js");
load("AtomSQ_ModeHandler.js");
load("AtomSQ_CursorHandler.js");
load("AtomSQ_ModeDisplay.js");
load("AtomSQ_ModeButtons.js");



// Remove this if you want to be able to use deprecated methods without causing script to stop.
// This is useful during development.
host.setShouldFailOnDeprecatedUse(true);
host.defineController("Presonus", "Atom SQ 3_test", "0.1", "f23cc375-4166-48a8-af88-15211ccb24cc", "James Bell");
host.defineMidiPorts(2, 2);

if (host.platformIsWindows())
{
   // TODO: Set the correct names of the ports for auto detection on Windows platform here
   // and uncomment this when port names are correct.
  // host.addDeviceNameBasedDiscoveryPair(["ATMSQ"], ["ATMSQ"]),(["MIDIIN2 (ATM SQ)"], ["MIDIOUT2 (ATM SQ)"]);
}
else if (host.platformIsMac())
{
   // TODO: Set the correct names of the ports for auto detection on Mac OSX platform here
   // and uncomment this when port names are correct.
   // host.addDeviceNameBasedDiscoveryPair(["Input Port 0", "Input Port 1"], ["Output Port 0", "Output Port 1"]);
}
else if (host.platformIsLinux())
{
   // TODO: Set the correct names of the ports for auto detection on Linux platform here
   // and uncomment this when port names are correct.
   // host.addDeviceNameBasedDiscoveryPair(["Input Port 0", "Input Port 1"], ["Output Port 0", "Output Port 1"]);
}


var menumode = null;
//declare the variables set in Init so they are globally available. It seemed to work without, I think, but Moss does this
var transport = null;
var hardware = null;
var application = null;
var transportHandler = null;
var cursorHandler = null;
var modeHandler = null;
var shiftOn = false;

function init() {

   hardware = new ATOMSQHardware (host.getMidiOutPort (0), host.getMidiInPort (0), onMidi0);
   //initialize the hardware
   
   //the MOSS script has the createTransport directly in the 2nd line, but this crashes for me. Adding variable, the using variable in next line.
   transport = host.createTransport();
   transportHandler = new TransportHandler (transport);
   application = host.createApplication();
   modeHandler = new ModeHandler (application);
   cursorTrack = host.createCursorTrack ("ASQ_CURSOR_TRACK", "Cursor Track", 0, 0, true);
   cursorHandler = new CursorHandler (cursorTrack);
   
   hardware.block_Init();

   //FIX this is a bit of a cludge fix rn, as the settings force tke KB editor at the start. Would be nice to get it to default to Inst with the proper menu...
   modeHandler.User();
   println("Atom SQ 2 initialized!");
}

// Called when a short MIDI message is received on MIDI input port 0.
function onMidi0(status, data1, data2) 
{
   //pass on if a note data
   if (isNoteOn(status))
   return;
   if (hardware.handleShift (data1, data2))
   return;
   if (transportHandler.handleMidi (status, data1, data2))
   return;
   if (modeHandler.handleMidi (status, data1, data2))
   return;
   if (hardware.HandleEncoders(data1, data2))
   return;
   host.errorln ("Midi command not processed: " + status + "," + data1 + "," + data2);


}

function flush() 
{
   transportHandler.updateLEDs ();
   if(menumode == ASQ_SONG, ASQ_INST)
   {
      modeHandler.updateLEDs();
   }
   println("flush called");
}

function exit() {
   println("exited!");
}