package HKSM.app.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import com.google.gson.JsonObject;

import HKSM.app.editor.Listeners.IntField;
import HKSM.data.SaveLoader;
import HKSM.app.editor.Listeners.DocChecker;

/**
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class SaveEditor extends JFrame {
	
	public JsonObject json;
	public File fObject;
	
	//CHARM_NAME_23_BRK   Fragile Heart (Repair)
	//CHARM_NAME_24_BRK   Fragile Greed (Repair)
	//CHARM_NAME_25_BRK   Fragile Strength (Repair)
	
	//CHARM_NAME_36_A White Fragment
	//CHARM_NAME_36_B Kingsoul
	//CHARM_NAME_36_C Void Heart
	
	private static String[] charmNames = new String[]{
			"Gathering Swarm",
			"Wayward Compass",
			"Grubsong",
			"Stalwart Shell",
			"Baldur Shell",
			"Fury of the Fallen",
			"Quick Focus",
			"Lifeblood Heart",
			"Lifeblood Core",
			"Defender's Crest",
			"Flukenest",
			"Thorns of Agony",
			"Mark of Pride",
			"Steady Body",
			"Heavy Blow",
			"Sharp Shadow",
			"Spore Shroom",
			"Longnail",
			"Shaman Stone",
			"Soul Catcher",
			"Soul Eater",
			"Glowing Womb",
			"Fragile Heart",
			"Fragile Greed",
			"Fragile Strength",
			"Nailmaster's Glory",
			"Joni's Blessing",
			"Shape of Unn",
			"Hiveblood",
			"Dream Wielder",
			"Dashmaster",
			"Quick Slash",
			"Spell Twister",
			"Deep Focus",
			"Grubberfly's Elegy",
			"Void Soul"
	};
	
	public SaveEditor(String path, String title){
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(20000);
		String p = "playerData";
		fObject = new File(path);
		
		try {
			json = SaveLoader.loadSave(fObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonObject playerData = json.getAsJsonObject(p);
		
		this.setTitle(title);
		this.setLayout(new BorderLayout());
		
		JTabbedPane tab = new JTabbedPane();
		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
			JMenuItem fileSave = new JMenuItem("Save");
			
		menu.add(file);
			file.add(fileSave);
			
		fileSave.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SaveLoader.saveSave(fObject, json);
			}
		});
			
		JPanel inventoryEditor = new JPanel();
//		JScrollPane inventoryScroll = new JScrollPane(); // not yet implemented
		tab.addTab("Inventory", inventoryEditor);
		
		inventoryEditor.setLayout(new GridLayout(0,3));
		
		// This all could probably be shortened into a for loop of some kind
		inventoryEditor.add(new SaveField(json, new String[]{p, "maxHealthBase"}, "HP", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "MPReserveMax"}, "MP", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "nailDamage"}, "Nail Damage", SaveField.INTEGER));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "geo"}, "Geo", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "dreamOrbs"}, "Dream Orbs", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "permadeathMode"}, "Steel Soul", SaveField.BOOL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasCyclone"}, "Cyclone", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDashSlash"}, "Dash Slash", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasUpwardSlash"}, "Great Slash", SaveField.BOOL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "fireballLevel"}, "Fireball Lv.", SaveField.SPELL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "screamLevel"}, "Scream Lv.", SaveField.SPELL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "quakeLevel"}, "Dive Lv.", SaveField.SPELL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDash", "hasShadowDash"}, "Mothwing Cloak", SaveField.DASH));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasWalljump"}, "Mantis Claw", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasSuperDash"}, "Crystal Dash", SaveField.BOOL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDoubleJump"}, "Monarch Wings", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDreamNail", "hasDreamGate", "dreamNailUpgraded"}, "Dreamnail", SaveField.DREAMNAIL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasAcidArmour"}, "Ismas", SaveField.BOOL));
		
		//Health	| Mana 		| Nail
		//Geo		| Notches   | Steel Soul
		//---------------------------------
		//N-Art1 	| N-Art2	| N-Art3
		//Spell1	| Spell2	| Spell3
		//---------------------------------
		//Dash		| Walljump	| SuperDash
		//2xJump	| Dreamnail | Ismas
		//---------------------------------
		//Lantern	| tram pass | quill
		//City key  | sly key   | white key
		//ww key    | spa key   | kings brand
		
		JPanel charmTab = new JPanel();
		charmTab.setLayout(new BorderLayout());
		
		int usedNotches = playerData.get("charmSlotsFilled").getAsInt();
		int totalNotches = playerData.get("charmSlots").getAsInt();
		
		IntField eNotch = new IntField(usedNotches);
		IntField mNotch = new IntField(totalNotches);
		
		// Now uses boxed listeners
		eNotch.getDocument().addDocumentListener(new DocChecker(playerData, "charmSlotsFilled", eNotch));
		mNotch.getDocument().addDocumentListener(new DocChecker(playerData, "charmSlots", mNotch));
		
		JCheckBox oc = new JCheckBox("Overcharmed");
		oc.addActionListener( new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		          AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		          boolean selected = abstractButton.getModel().isSelected();
		          playerData.addProperty("overcharmed", selected);
		        }
		 });
		
		JPanel charmTop = new JPanel();
		charmTop.setLayout(new GridLayout(0,2));
		charmTop.add( new JLabel("Equipped Notches") );
		charmTop.add( new JLabel("Max Notches") );
		charmTop.add( eNotch );
		charmTop.add( mNotch );
		
		JCheckBox autoCalc = new JCheckBox("Auto-Calculate");
		autoCalc.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){
				if(autoCalc.isSelected()){
					int tNotches = 0;
					for( int i = 0; i < 36; i++){
						String s = Integer.toString(i+1);
						boolean eq = playerData.get("equippedCharm_" + s).getAsBoolean();
						int co = playerData.get("charmCost_" + s).getAsInt();
						if( eq )
							tNotches += co;
					}
					playerData.addProperty("charmSlotsFilled", tNotches);
					eNotch.setText(Integer.toString(tNotches));
					oc.setSelected(eNotch.greaterThan(mNotch));
				}
			}
		});
		charmTop.add( autoCalc );
		charmTop.add( oc );
		
		JPanel charmBottom = new JPanel();
		charmBottom.setLayout(new GridLayout(0,2));
		List<CharmPanel> charmList = new ArrayList<CharmPanel>();
		for( int i = 0; i < 36; i++){
			CharmPanel charm = new CharmPanel(i, charmNames[i], playerData, autoCalc, oc, eNotch, mNotch);
			charmList.add(charm);
		}
		Collections.sort(charmList);
		for( int i = 0; i < 36; i++){
			charmBottom.add(charmList.get(i));
		}
		charmTab.add(charmTop, BorderLayout.PAGE_START);
		charmTab.add(charmBottom, BorderLayout.CENTER);
		JScrollPane charmEditor = new JScrollPane(charmTab);
		tab.addTab("Charms", charmEditor);
		
		this.add(menu, BorderLayout.PAGE_START);
		this.add(tab, BorderLayout.CENTER);
		
		this.pack();
		
		this.setSize(450, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}