// Written by James Bell
// (c) 2023
// Licensed under GPLv3 - https://www.gnu.org/licenses/gpl-3.0.txt
package com.presonus;

import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class AtomSQExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("ed5268cb-92e6-4d82-86d4-7bc9da9be1d1");
   
   public AtomSQExtensionDefinition()
   {
      
   }

   @Override
   public String getName()
   {
      return "Atom SQ (by James)";
   }
   
   @Override
   public String getAuthor()
   {
      return "James Bell";
   }

   @Override
   public String getVersion()
   {
      return "2.0";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "PreSonus";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Atom SQ";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 18;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 2;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 2;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
      if (platformType == PlatformType.WINDOWS)
      {
         // TODO: Set the correct names of the ports for auto detection on Windows platform here
         // and uncomment this when port names are correct.
         list.add(new String[]{"ATM SQ", "MIDIIN2 (ATM SQ)"}, new String[]{"ATM SQ", "MIDIOUT2 (ATM SQ"});
      }
      else //noinspection StatementWithEmptyBody
         if (platformType == PlatformType.MAC)
      {
         // TODO: Set the correct names of the ports for auto detection on Apple PCs platform here
         // and uncomment this when port names are correct.
         // list.add(new String[]{"Input Port 0", "Input Port 1"}, new String[]{"Output Port 0", "Output Port 1"});
      }
      else //noinspection StatementWithEmptyBody
            if (platformType == PlatformType.LINUX)
      {
         // TODO: Set the correct names of the ports for auto detection on Linux platform here
         // and uncomment this when port names are correct.
         // list.add(new String[]{"Input Port 0", "Input Port 1"}, new String[]{"Output Port 0", "Output Port 1"});
      }
   }




   @Override
   public AtomSQExtension createInstance(final ControllerHost host)
   {
      return new AtomSQExtension(this, host);
   }
}
