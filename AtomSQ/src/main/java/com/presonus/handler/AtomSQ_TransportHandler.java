/* Transport control handler
Buttons included:
    1. Traditional Transports on the left 
 */

//Init function (class)
function TransportHandler (transport)
{
    this.transport = transport;

   //Observers
   this.transport.isPlaying ().markInterested();
   this.transport.isMetronomeEnabled ().markInterested();
   this.transport.isArrangerRecordEnabled ().markInterested();
}

//Methods
TransportHandler.prototype.handleMidi = function (status, data1, data2)
{
    // return uf a button is released (would remove if there is a need for momentary commands, like Shift)
    if (data2 == 0)
        return true;

    if (isChannelController(status) && data2 == 127)
   {
         switch (data1)
            {
               case ASQ_STOP:
                  println ("STOP");
                  this.transport.stop();
                  return true;
               case ASQ_PLAY:
                  println ("PLAY");
                  this.transport.play();
                  return true;
               case ASQ_REC:
                  println ("RECORD");
                  this.transport.record();
                  return true;   
               case ASQ_METRONOME:
                  println ("METRONOME");
                  this.transport.isMetronomeEnabled().toggle();
                  return true;  
               default:
                  return false;
            }
   }
}

TransportHandler.prototype.updateLEDs = function()
{
    hardware.updateLEDs (ASQ_PLAY, this.transport.isPlaying().get());
    hardware.updateLEDs (ASQ_STOP, this.transport.isPlaying().get());
    hardware.updateLEDs (ASQ_METRONOME, this.transport.isMetronomeEnabled().get());
    hardware.updateLEDs (ASQ_REC, this.transport.isArrangerRecordEnabled().get());
}
