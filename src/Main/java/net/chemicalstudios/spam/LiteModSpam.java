package net.chemicalstudios.spam;

import com.mumfrey.liteloader.ChatFilter;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteModSpam implements ChatFilter, Tickable
{
    //TODO: add ablility to use commands to manage everthing
	String prevMessage;
    Minecraft mc;
    String Path;
	File muteList;
	
	KeyBinding mKey = new KeyBinding("key.guispam.toggle", Keyboard.KEY_M, "key.categories.litemods");
	
	@Override
	public String getName() {
		return "Spam Protection Mod";
	}

	@Override
	public String getVersion() {
		return "1.0.3";
	}

    @Override
    public void init(File configPath)
    {
        GuiSpam Spamscreen = new GuiSpam(this);
		LiteLoader.getInput().registerKeyBinding(mKey);
		
		prevMessage = new String();

        Path = configPath.getPath() + "//MuteList.txt";
        File muteList = new File(Path);
        try
            {
                muteList.createNewFile();
            }catch (Exception e){e.printStackTrace();}
        Spamscreen.initList();
//        final File ModFolder = new File(mc.mcDataDir.getPath() +"mods/Taciturn");
//        if (!ModFolder.exists())
//        {
//            ModFolder.getParentFile().mkdirs();
//        }
		// Create text file
		//File muteTxt = new File(configPath + FileName);


	}

	@Override
	public void upgradeSettings(String version, File configPath,File oldConfigPath) {
	}

	@Override
	public boolean onChat(S02PacketChat chatPacket, IChatComponent chat,String message)
    {

        //TODO: add option/command to disable this, cause i don't really like it the way it is right now
		/* This is here to block repeating messages */
        if (prevMessage.equals(message))
        {
            return false;
        }
		
		/* This is here to block users on the blocklist */
        String preUsername = new String();

        String[] words = message.split(" ");
        preUsername = words[0];
        String username = new String();
        GuiSpam gs = new GuiSpam(this);
        if (!gs.mutedPlayers.isEmpty())
        {
            for (int i = 0; i < gs.mutedPlayers.size(); i++)
            {
                Pattern p = Pattern.compile(gs.getMutedPlayers().get(i));
                Matcher m = p.matcher(preUsername.toLowerCase());
                System.out.println("pattern is " + gs.mutedPlayers.get(i));
                System.out.println("Matcher is " + preUsername.toLowerCase());
                if (m.find())
                {
                    System.out.println("Filter");
                    return false;
                }
            }
        }
        else{
            System.out.println("nobody muted");
        }

		/*for(int i = 0; i < preUsername.length(); i++) {
			if(!(preUsername.charAt(i) == '\u00A7' || ((i > 0) && preUsername.charAt(i-1) == '\u00A7')) && !((preUsername.charAt(i) == '<') || preUsername.charAt(i) == '>')&& !((preUsername.charAt(i) == '[') || preUsername.charAt(i) == ']') && !(preUsername.charAt(i) == ':')) {
				username += preUsername.charAt(i);
			}
		}
		
		username = username.toLowerCase();
		
		if(namesArray != null) {
			for(int i = 0; i < namesArray.length; i++) {
				if(namesArray[i] != null && username.equals(namesArray[i].toLowerCase())) {
					return false;
				}
			}
		} else {
			return true;
		}
		prevMessage = message;
		return true;
		*/
        return true;
	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame,
			boolean clock) {
		if(mKey.isPressed()) {
			minecraft.displayGuiScreen(new GuiSpam(this));
		}
	}
    public File getMuteList()
    {
        return muteList = new File(Path);
    }
}