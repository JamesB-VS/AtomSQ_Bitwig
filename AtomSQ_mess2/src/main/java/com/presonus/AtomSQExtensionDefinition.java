// Written by James Bell
// (c) 2023
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package com.presonus;

import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;


/**
 * The description of the extension.
 *
 * @author
 */
public class AtomSQExtensionDefinition extends ControllerExtensionDefinition
{
    private static final UUID DRIVER_ID = UUID.fromString ("2cef2107-b7a6-4a39-9542-2025694e2d97");


    /**
     * Constructor.
     */
    public AtomSQExtensionDefinition ()
    {
        // Intentionally empty
    }


    /** {@inheritDoc} */
    @Override
    public String getName ()
    {
        return "AtomSQ";
    }


    /** {@inheritDoc} */
    @Override
    public String getAuthor ()
    {
        return "James Bell";
    }


    /** {@inheritDoc} */
    @Override
    public String getVersion ()
    {
        return "0.1";
    }


    /** {@inheritDoc} */
    @Override
    public UUID getId ()
    {
        return DRIVER_ID;
    }


    /** {@inheritDoc} */
    @Override
    public String getHardwareVendor ()
    {
        return "Presonus";
    }


    /** {@inheritDoc} */
    @Override
    public String getHardwareModel ()
    {
        return "AtomSQ (Java)";
    }


    /** {@inheritDoc} */
    @Override
    public int getRequiredAPIVersion ()
    {
        return 17;
    }


    /** {@inheritDoc} */
    @Override
    public int getNumMidiInPorts ()
    {
        return 2;
    }


    /** {@inheritDoc} */
    @Override
    public int getNumMidiOutPorts ()
    {
        return 2;
    }


    /** {@inheritDoc} */
    @Override
    public void listAutoDetectionMidiPortNames (final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
    {
        // Note: Only an example on my system. If you use the MOXF with USB replace these with tzhe
        // correct names

        list.add (new String []
        {
            "ATM SQ"
        }, new String []
        {
            "MIDIIN (ATM SQ)"
        });
    }


    /** {@inheritDoc} */
    @Override
    public AtomSQExtension createInstance (final ControllerHost host)
    {
        return new AtomSQExtension (this, host);
    }
}
