package commands;

import common.Album;
import common.descriptions.AlbumDescription;
import common.descriptions.CommandDescription;
import common.MusicBand;
import common.descriptions.LoadDescription;
import common.descriptions.MusicBandDescription;
import receivers.MusicReceiver;
import result.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpCommand extends Command<MusicReceiver> {

    public HelpCommand() throws Exception{
        super(MusicReceiver.GetInstance());}
    @Override
    public Result<Void> execute() {
        return null;
    }
}
