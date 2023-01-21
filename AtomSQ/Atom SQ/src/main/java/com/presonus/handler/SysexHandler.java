package com.presonus.handler;

import java.util.HexFormat;
import com.bitwig.extension.api.util.midi.SysexBuilder;
import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.CursorTrack;
import org.apache.commons.codec.binary.Hex;

import com.presonus.AtomSQExtension;

public class SysexHandler
{
    // MidiOut mMidiOut = AtomSQExtension.mMidiOut;
    // Application mApplication = AtomSQExtension.mApplication;
    // SysexBuilder sB = AtomSQExtension.sB;
    // CursorTrack mCursorTrack = AtomSQExtension.mCursorTrack;

    //constant for header
    public final static String sheader = "F0 00 01 06 22 12";
    //const for B1L1
    public final static int B1L1 =  0x0 ;
    //const for B1L2
    public final static int B1L2 =  0x3 ;
    //const for B2L1
    public final static int B2L1 =  0x1 ;
    //const for B2L2
    public final static int B2L2 =  0x4 ;
    //const for B3L1
    public final static int B3L1 =  0x2 ;
    //const for B3L2
    public final static int B3L2 =  0x5 ;
    //const for B4L1
    public final static int B4L1 =  0x8 ;
    //const for B4L2
    public final static int B4L2 =  0xB ;
    //const for B5L1
    public final static int B5L1 =  0x9 ;
    //const for B5L2
    public final static int B5L2 =  0xC ;
    //const for B6L1
    public final static int B6L1 =  0xA ;
    //const for B6L2
    public final static int B6L2 =  0xD ;

    public final static int MainL1 = 0x6;

    public final static int MainL2 = 0x7;

    public int[] sButtonsTitle= {B1L1, B2L1, B3L1, B4L2, B5L2, B6L2 };
    public int[] sButtonsValue= {B1L2, B2L2, B3L2, B4L1, B5L1, B6L1 };

    //Array for colors?
    public final static String ltblue = "7F7F00";

    //const for left
    public final static int spl =  0x1 ;
    //const for center
    public final static int spc =  0x0 ;
    //const for right
    public final static int spr =  0x2 ;
    
    //constant for footer
    public final static String foot = "F7";


    //Method for hexify
    public String Hexify (final String text)
    {
        //convert String to HEX
        String hexString = Hex.encodeHexString(text.getBytes());
        //return string
        System.out.println(hexString);
        return hexString;
    } 

    //Method for Sysex builder
    // public Sysexbuilder (final String button, final String color, final String justify, final String text)
    // {


    // }





}