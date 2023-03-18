# Presonus Atom SQ Bitwig Extension

This is an extension for Bitwig Studio for the Atom SQ Controller by Presonus. 

## Brief Description

This controller extension provides many functions above and beyond the basic Midi Mode implementation. It is written in Java in order to leverage certain advanced features of the Bitwig API that are not available in Javascript, primarily the hardware abstraction. A few key features:
1. Endless Encoders!
2. Several Modes, based on the menu buttons on the side of the display, each with it's own mapping of the display buttons and encoders.
3. multiple Track and Device controls, such as preset browsing, adding, deleting and moving tracks or devices.
4. functional and dynamic use of the display. Track, Device and Preset names are shown and updated automagically.
5. Retains native Midi-mode hardware control of the keyboard settings (keyboard mode, Range, Oct, Scale Root, Velocity).


## Why tho?
Because I could not accept that my cool Atom SQ reverted to providing non-endless encoders in Midi Mode, but was clearly capable of this, as the work that way in Live and in Studio One. Once I got that working, things sort of snowballed, as there was so much cool stuff that could be included.


## How?
Condensing this into a few sentences feels cheap, as there were literal months of mornings and weekends that went into figuring this all out, but in the end, the extension came to be thusly:
1. refusal to accept the presented reality of a shitty basic-ass midi implementation for this awesome controller in my software of choice.
2. got gud at Midi: monitored and recorded midi traffic between the controller and other Daws to understand the flow of things. Made a midi-map.
3. learned the secret handshake: this has been mentioned in randome places on the internets before, but the key to endless encoders is to get the controller to work in Live mode. this is done by sending a midi message of 143,00,01.
4. Wrote a nice javascript extension. patted self on back. 
5. learned there were better ways and newer things. swalled the bitter pill that I would have to start over with Java. 
6. Rememberd I did not know Java. 
7. Learned me some Java.
8. wrote this script. 






## AS-IS