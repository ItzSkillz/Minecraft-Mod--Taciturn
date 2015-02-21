package net.chemicalstudios.spam;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import java.io.*;
import java.util.ArrayList;

public class GuiSpam extends GuiScreen {

	// All variables for file handling and what not
	private FileReader reader;
	private FileWriter writer;
	private String curLine = null;
	private int _nameCount = 0;
	private String names;
	protected ArrayList<String> mutedPlayers = new ArrayList<String>();
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
//	private String mutedPlayers;
	private LiteModSpam main;
	private GuiTextField GuiField;

	public GuiSpam(LiteModSpam lite) {
		this.main = lite;
	}

    @SuppressWarnings("unchecked")
	public void initGui() {
		this.buttonList.clear();
		byte var1 = -16;
		
		// the center between the title and the "back to game" button
		int y = (100 + this.height / 2 + 24 + var1) / 2;
		
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height/ 2 + 24 + var1, I18n.format("Back to Game", new Object[0])));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 200, y, 98, 20, I18n.format("Unmute", new Object[0])));
		this.buttonList.add(new GuiButton(2, this.width / 2 + 100, y, 98, 20, I18n.format("Mute", new Object[0])));

        //TODO: look into a auto complete for names entered here.
		GuiField = new GuiTextField(this.fontRendererObj, this.width / 2 - 78, y, 150, 20);
		GuiField.setFocused(true);
		GuiField.setText("");
		initList();
	}

	protected void actionPerformed(GuiButton buttonClicked) {
		switch (buttonClicked.id) {
		case 0:
			this.mc.displayGuiScreen((GuiScreen) null);
			this.mc.setIngameFocus();
			break;
		case 1:
			unMutePlayer(GuiField.getText());
			break;
		case 2:
			mutePlayer(GuiField.getText());
            //TODO: only add a player if they aren't already muted
		}
	}

	public void updateScreen() {
		super.updateScreen();
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		this.GuiField.mouseClicked(par1, par2, par3);
		this.GuiField.mouseClicked(par1, par2, par3);
	}

	protected void keyTyped(char par1, int par2) {
		this.GuiField.textboxKeyTyped(par1, par2);
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawString(this.fontRendererObj, "Mute Players Menu", this.width / 2 - (this.fontRendererObj.getStringWidth("Mute Players Menu") / 2), 100, 16777215);

		this.GuiField.drawTextBox();

		// This is here to keep mute list up to date
		updateList();
		
		super.drawScreen(par1, par2, par3);
	}
	public void initList() {
		try {
			reader = new FileReader(main.getMuteList());
			bufferedReader = new BufferedReader(reader);
            mutedPlayers.clear();
	        while ((curLine = bufferedReader.readLine()) != null) {
				if(!(curLine.equals("null")) && !(curLine.equals("")) && !(curLine.equals(new String()))) {
                    mutedPlayers.add(curLine);
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateList() {
		if(!mutedPlayers.isEmpty()) {
            int count = 0;
            for (String mutedPlayer : mutedPlayers)
            {
                if (!mutedPlayer.equals("") && !(mutedPlayer.equals("null")))
                {
                    int x = 10;
                    int y = 20 + (10 * count);

                    if (y > (this.mc.displayHeight - 100))
                    {
                        x *= count;
                    }

                    this.fontRendererObj.drawString("[" + mutedPlayer + "]", x, y, 0xFF1000);
                    ++count;
                    this.fontRendererObj.drawString("Muted Players:", x, 10, 0xFFFFFF);
                }
            }
		}
	}
	
	public void unMutePlayer(String unmute) {
		curLine = "";
		try {
			curLine = null;
			
			reader = new FileReader(main.getMuteList());
			bufferedReader = new BufferedReader(reader);
			
			//while ((curLine = bufferedReader.readLine()) != null) {
/*			 for (int i = 0; (curLine = bufferedReader.readLine())!= null; i++)
             {
                 ArrayList<String> preText = new ArrayList<String>();
                 preText.add(i, curLine);
                 //String[] words = preText[i].toLowerCase().split(" ");
                 for (int l = 0; l < preText.size(); l++)
                 {
                     if (preText.get(l).equals(unmute.toLowerCase()))
                     {
                         preText.add(l, "");
                     }
                 }
             }
*/			// Remove from visible list instantly
/*              String[] mutedPlayersArray = mutedPlayers.toArray();
				mutedPlayers = "";
				for(int i = 0; i < mutedPlayersArray.length; i++) {
					if(mutedPlayersArray[i].equals(unmute.toLowerCase())) {
						mutedPlayersArray[i] = null;
					}
					mutedPlayers += " " + mutedPlayersArray[i];
				}
*/
			
/*			names = mutedPlayers;
			
			String finalCompilation = new String();
			
			for(int i = 0; i < words.length; i++) {
				finalCompilation += words[i] + "\n";
			}
*/

            if (mutedPlayers.contains(unmute))
            {
                mutedPlayers.remove(unmute);
                System.out.println(unmute + " is now unmuted");
            }

            writer = new FileWriter(main.getMuteList());
            bufferedWriter = new BufferedWriter(writer);
            for (String muted : mutedPlayers)
            {
                bufferedWriter.write(muted);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
			bufferedReader.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mutePlayer(String mute) {
		try
        {
            curLine = null;
            reader = new FileReader(main.getMuteList());
            bufferedReader = new BufferedReader(reader);
/*			while ((curLine = bufferedReader.readLine()) != null) {
				_nameCount++;
				if(!(curLine.equals(null)) && !(curLine.equals("null")) && !(curLine.equals("")) && !(curLine.equals(new String()))) {
					preText += " " + curLine;
				}
            }
*/
/*            for (int i = 0; (curLine = bufferedReader.readLine())!= null; i++)
            {
                String[] preText = new String[0];
                preText[i] = curLine;

                for (int l = 0; l < preText.length; l++)
                {
                    if (preText[l].equals(mute.toLowerCase()))
                    {
                        preText[l] = " " + mute;
                    }
                }
            }
*/
            if(!mutedPlayers.contains(mute))
            {
                mutedPlayers.add(mute);
                System.out.println(mute + " is now muted");
            }

            writer = new FileWriter(main.getMuteList());
            bufferedWriter = new BufferedWriter(writer);
            for (String muted : mutedPlayers)
            {
                bufferedWriter.write(muted);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		initList();
	}

    public final ArrayList<String> getMutedPlayers()
    {
        return mutedPlayers;
    }
}