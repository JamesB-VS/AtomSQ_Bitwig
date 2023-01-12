package com.presonus;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class AtomSQExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("2cef2107-b7a6-4a39-9542-2025694e2d97");
   
   public AtomSQExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "AtomSQ";
   }
   
   @Override
   public String getAuthor()
   {
      return "James Bell";
   }

   @Override
   public String getVersion()
   {
      return "0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "Presonus";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "AtomSQ";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 17;
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
   }

   @Override
   public AtomSQExtension createInstance(final ControllerHost host)
   {
      return new AtomSQExtension(this, host);
   }
}
