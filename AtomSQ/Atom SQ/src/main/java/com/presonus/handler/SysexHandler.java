// Written by James Bell
// (c) 2023
// Licensed under GPLv3 - https://www.gnu.org/licenses/gpl-3.0.txt
package com.presonus.handler;
public class SysexHandler
{
    //constant for header
    public final String sheader = "F0 00 01 06 22 12";
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
    //Main line 1
    public final int MainL1 = 0x6;
    //Main line 2
    public final int MainL2 = 0x7;

    //Button Arrays
    public final int[] sButtonsTitle= {B1L1, B2L1, B3L1, B4L2, B5L2, B6L2 };
    public int[] sButtonsValue= {B1L2, B2L2, B3L2, B4L1, B5L1, B6L1 };

    //Colors
    public final String yellow =     "7F7F00";
    public final String grey50 =     "7F7F7F";
    public final String magenta =    "7F007F";
    public final String seafoam =    "007F7F";
    public final String red =        "7F0000";
    public final String black =      "000000";
    public final String blue =       "00007F";
    public final String green =      "007f00";  
    public final String white =      "7F7F7F";
    public final String ltblue =     "6fa8dc";
    
    //const for left
    public final static int spl =  0x1 ;
    //const for center
    public final int spc =  0x0 ;
    //const for right
    public final static int spr =  0x2 ;
    
    // //constant for footer
    // public final static String foot = "F7";
}