# Presonus Atom SQ Bitwig Extension Manual


## Requirements
- This extension is based on the API version 17, available in Bitwig 4.4 
- It has only been tested on a Windows 11 sytem. Feedback from anyone using it on a Mac or Linux would be most welcome.


## Installation (Windows)

- Add the AtomSQ.bwextension file into the Extensions folder under My Documents:
- C:\Users\USER\Documents\Bitwig Studio\Extensions
- In Bitwig, under the Settings/Controllers, add a new controller, and select the Atom SQ (by James) controller under the PreSonus folder. 
- Assign the **ATM SQ** and **MID IN/OUT 2 (ATM SQ)** inputs and outputs to controller. 
- Thats it. The **Inst** button on the controller should now be lit, and if you have a project already loaded, the Track and Device names should appear on the controller display.



## Quick Start

- The B-H buttons, pitch bend, mod, and notes work as expected. 
- There are two "modes" currently configured. These are triggered by the **Song** (for Tracks) and **Inst** (for Devices) buttons. 
- The **User button** accesses the controller-native keyboard controls.  
- Extra "pages" of menu are indicated by the Fwd/Bck buttons under the encoder on the right of the screen being lit. 
- Undo/Redo are bound to Shift-Back and Shift-Forward. 
- Based on the mode (including in the preset/device pop-up browser) the encoders do different things. Not always all 8. 


## The Slow start, I guess?

### The Keys
As stated above: the The B-H buttons, pitch bend, mod, and notes work as expected. Octaves can be changed by the B-H buttons, the keyboard scale, root note et al can be manipulated in the Keyboard menu as in Midi Mode.

Currently, the A button does nothing. Working on it. 
### Transport
The usual stuff, but with some shift functions too:
|Key |Function  |Shift  |  
|-  |-  |-  |
|Stop  |Stops playback, sets play to beginning  |-  |  
|Play  |Starts/pauses playback  |Enables loop mode  |
|Record  |Starts recording  |Saves project  |  
|Metronome  |Toggles metronome  |-  |  

### Navigation
The  main click encoder (#9) and arrow buttons (Back/Fwd) to the right of the display, as well as the 4 direction bottons below it (Left/Right/Up/Down).

|Control  |Function  |Shift  |
|---|---|---|
|Encoder 9 |Moves Playhead in Arranger. Not the smoothest, working on it |-  |
|Back  |Page back in a mode. lights up if this is possible.  |Undo  |
|Forward  |Page forward in a mode. lights up if this is possible.  |Redo  |
|Up /Down  |Moves between tracks. lights up if this is possible.  |-  |
|Left/Right  |Moves between devices. lights up if this is possible.  |-  |
### Modes
The concept here was to leverate the four buttons on the left of the display to make 4 Modes for use. I went back and forth a good while trying different things and settled on the following Modes:

|Button |Mode   |Description|
|---|---|---|
|Song   |Tracks     |Controls related to the Tracks, like Mute, Solo, creating new tracks etc|
|Inst   |Devices    |Controls related to Devices, like Enable, Move, view Remote Controls, select Prefix etc.|
|Editor |-          |Nothing to see here. Yet. I had to stop somewhere, or this would never have come out. |
|User   |Keyboard   |Native Keyboard controls from the Atom SQ itself.|
|-  |*Browser  |Selecting presets or new devices. Opens either by a button in another mode, or when the browser is opened in the app  |

Each Mode, or more specifically each page of each mode, controls the display, the buttons around it, and the encoders (1-8). The keyboard mode is really self explanatory, and the editor mode is TBD, so we will only look at the other two, Tracks and Devices, in detail. 

for all modes, the encoders are as numbered, top left to bottom right, on the controller. 
The buttons are also top left to bottom right, thusly:

1    2    3
[ display ]
4    5    6

#### Tracks Mode
When the Song button is lit, this mode is active. It provides access to controls for the Tracks. There are two pages of controls, indicated by the Fwd/Back buttons being lit. I know, this is not how it was done in f.e. Studio One, but I could not yet be bothered to find the right code for the little "pagination" icons. Working on it. 

In this mode, the center of the display shows the Track: in yellow and the Device: in white. The button text is also in yellow.

##### Page 1
|Control  |Function  |Shift  |
|-  |-  |-  |
|Encoders 1-6  |Controls the send amount for sends 1-6, if present |-  |
|Encoder 7  |Controls track panning  |-  |
|Encoder 8  |Controls track volume  |-  |
|Button 1   |Toggles track mute  |-  |
|Button 2   |Toggles track solo |-  |
|Button 3   |Toggles track being armed  |-  |
|Button 4   |-  |-  |
|Button 5   |Moves track up |-  |
|Button 6   |Moves track down  |-  |

##### Page 2
|Control  |Function  |Shift  |
|-  |-  |-  |
|Encoders 1-6  |Controls the send amount for sends 1-6, if present |-  |
|Encoder 7  |Controls track panning  |-  |
|Encoder 8  |Controls track volume  |-  |
|Button 1   |Toggles track being active  |-  |
|Button 2   |Copies track (below) |-  |
|Button 3   |Deletes track |-  |
|Button 4   |Creates new Audio Track (below)  |-  |
|Button 5   |Creates new Inst Track (below) |-  |
|Button 6   |Creates new FX Track (below existing)  |-  |


#### Devices Mode

When the Inst button is lit, this mode is active. It provides access to controls for the Devices. There are two pages of controls, indicated by the Fwd/Back buttons being lit.

In this mode, the center of the display shows the Track: in yellow and the Device: in white. The button text is also in white.

V1.1: If a track has no device in it, the display menu will change to a helpful reminder to insert a device, and all 6 display buttons will bring up the pop-up browser to the Device menu.

##### Page 1
|Control  |Function  |Shift  |
|-  |-  |-  |
|Encoders 1-8  |Device remote controls 1-8  |-  |
|Button 1   |Toggles device enabled  |-  |
|Button 2   |Toggles display of device window  |-  |
|Button 3   |Toggles display of expanded/collapsed device  |-  |
|Button 4   |Toggles display of remote controls  |-  |
|Button 5   |Moves the device to the left  |-  |
|Button 6   |Moves the device to the right  |-  |

##### Page 2
|Control  |Function  |Shift  |
|-  |-  |-  |
|Encoders 1-8  |Device remote controls 1-8  |-  |
|Button 1   |-  |-  |
|Button 2   |Copies device (to the right)  |-  |
|Button 3   |Deletes device  |-  |
|Button 4   |Opens the browser to insert a new device to the left  |-  |
|Button 5   |Opens the preset browser  |-  |
|Button 6   |Opens the browser to insert a new device to the right   |-  |


#### Browser

Browser mode is activated either by the new device button, preset buttons in page 2 of the device mode, or when the browser is activated in Bitwig itself. 

This mode has been updated in version 1.1:
Now each of the "types" of inserts (Devices, Presets, Multisamples, Samples, Music) will display this type in the 2nd line followed by the selection from the results column (the right-most column of the browser).

When inserting a device, the top line of display text will show the trackname, otherwise the device name will be shown in white and the "type" in magenta. The button text is also in magenta.

The mappings of the encoders are as follows. Yeah, it looks sort of complicated. The idea is that Smart Folders are always on Encoder 1, and the rest flows from the right, as displayed (if you have a customized view, hidden columns will pop into existance when you the relevant encoder, then go away again). 

##### Page 1
|Encoders  |Devices |Presets |Multisamples|Samples/Music|
|-  |-  |-  |- |- |
|Encoder 1 |Smart Folders |Smart Folders |Smart Folders |Smart Folders |
|Encoder 2 |-  |-  |- |- |
|Encoder 3 |-  |Locations |Locations |- |
|Encoder 4 |Locations  |Devices  |File Type |- |
|Encoder 5 |File Type  |Category|Category |- |
|Encoder 6 |Category  |Tags  |Tags |File Type |
|Encoder 7 |Creator |Creator  |Creator |Locations |
|Encoder 8 |Results |Results |Results |Results |

|Buttons|Browser Modes |
|- |-|
|Buttons 1-3  |-|
|Button 4   |Toggles preset preview  |
|Button 5   |Cancels selection and closes the browser  |
|Button 6   |OKs the selection and closes the browser  |
## Technical Info
This extension is written in Java to leverage the Hardware abstraction in the API, which I will not describe here because Moss does an excellent job in his Youtube channel, and you should check that out if you want to know more: https://www.youtube.com/watch?v=kTf_SSIyBEg

Additionally, I have imported the Framework folder from the Bitwig repository (https://github.com/bitwig/bitwig-extensions/tree/api-17/src/main/java/com/bitwig/extensions/framework) because, while learning to git gud at both Java and controller scripting, I discovered the Layers defined here and used in a ton of the officially provided scripts. The framework stuff is not at all documented (that I could find, at least) so the TL:DR is that Layers allow multiple layers of control bindings that can be combined, or used to replace, bindings from other layers. This makes what I refer to as "Modes" much easier to conceptualize and program. Example: the Inst (Devices) layer binds the 8 main encoders to the remote control settings for a device in Bitwig, as well as binding the display buttons. Inst 2 (Devices 2nd page) leaves the encoders alone, and only re-binds the display buttons to the new functions displayed for that page. Go back to the first page, and these are deactivated again. Noice. 


## Not Included

The stuff I am aware of that I could, and probably will, improve or add:

1. Automatic recognition of the controller
2. Track Color on the device, esp for the keys
3. Arranger/Clip focus switching. I would like to get the clip stuff on the Editor menu, but tbh I have a Launchpad, which I think is much better for this. Considering a companion controller setup with a super simple Launchpad script as an alternative.
4. Preset save
5. Grouping for Tracks
6. Device Type selection in the browser (bottom left) and smart folders . Should be easy though.
   
   
    
Stuff that, to me, seems unnecessary to add, but if you see a good arguement for it, let me know. :)
1.  anything to control complicated devices like layers, multi-fx etc. will evaluate the usefullness/complexity of managing these things via controller. 
2.   Device modulators. No idea how to toggle the viewing at the moment. Selection should then be easy with the browser. the connections for modulators to the synth is a whole other ballgame though, probably not something one would want to do with a controller. 


These are not exhaustive lists. I have a ton of functions listed out that could theoretically be bound to a controller. If you dig my extension, but feel it really needs X or Y function added, just let me know and I can take a look for a future version.

