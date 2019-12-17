/**
 * Copyright (c) 2012, Hadyn Richard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN 
 * THE SOFTWARE.
 */

package com.rs2.util.sql.impl;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.item.Item;
import com.rs2.util.sql.SQLCompletionHandler;
import com.rs2.util.sql.SQLEngine;
import com.rs2.util.sql.SQLWorker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hadyn Richard
 */
public final class PlayerLoader {
    
    /** Each of the container ids */
    public static final int BANK_ID = 0;
    public static final int INVENTORY_ID = 1;
    public static final int EQUIPMENT_ID = 2;

    /**
     * The player to load.
     */
    private final Player player;

    /**
     * Constructs a new {@link PlayerLoader};
     *
     * @param player    The player to load.
     */
    public PlayerLoader(Player player) {
        this.player = player;
    }

    /**
     * The inline class to load the information about the player.
     */
    private class PlayerInformationLoader implements SQLCompletionHandler {

        @Override
        public void onComplete(ResultSet results) throws SQLException {
             if(results.next()) {

                 /* Update the position of the player */
                 updatePosition(results);

                 player.setGender(results.getInt("is_male"));
                 player.setAutoRetaliate(results.getBoolean("is_auto_retaliate"));
                 player.setFightMode(results.getInt("fight_mode"));
                 player.setScreenBrightness(results.getInt("brightness"));
                 player.setMouseButtons(results.getInt("mouse_buttons"));
                 player.setChatEffects(results.getInt("chat_effects"));
                 player.setSplitPrivateChat(results.getInt("split_private_chat"));
                 player.setAcceptAid(results.getInt("accept_aid"));
                 player.setMusicVolume(results.getInt("music_volume"));
                 player.setEffectVolume(results.getInt("effect_volume"));
                 player.setSpecialAmount(results.getInt("special"));
                 player.setEnergy(results.getInt("energy"));
                 player.getMovementHandler().setRunToggled(results.getBoolean("is_running"));
                 player.setMuteExpire(results.getLong("mute_expires"));
                 player.setBanExpire(results.getLong("ban_expires"));
                 player.getBankPin().setChangingBankPin(results.getBoolean("changing_bankpin"));
                 player.getBankPin().setDeletingBankPin(results.getBoolean("deleting_bankpin"));
                 player.getBankPin().setPinAppendYear(results.getInt("pin_append_year"));
                 player.getBankPin().setPinAppendYear(results.getInt("pin_append_date"));
                 player.setBindingNeckCharge(results.getInt("binding_neck_charge"));
                 player.setRingOfForgingLife(results.getInt("ring_of_forging_life"));
                 player.setRingOfForgingLife(results.getInt("ring_of_recoil_life"));

                 int skullTimer = results.getInt("skull_timer");
                 if(skullTimer > 0) {
                     player.addSkull(player, skullTimer);
                 }

                 String pin = results.getString("pin");
                 if(!pin.equals("na")) {
                     for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {
                         player.getBankPin().getBankPin()[i] = Integer.parseInt(pin.substring(i, i + 1));
                     }
                 }

                 for(int i = 0; i < player.getAppearance().length; i++) {
                     player.getAppearance()[i] = results.getInt("appearance_" + i);
                 }

                 for(int i = 0; i < player.getColors().length; i++) {
                     player.getColors()[i] = results.getInt("color_" + i);
                 }
                 
                 player.getSlayer().slayerMaster = results.getInt("slayer_master");
                 player.getSlayer().slayerTask = results.getString("slayer_task");
                 player.getSlayer().taskAmount = results.getInt("task_amount");
                 player.setMagicBookType(results.getBoolean("using_ancients") ? SpellBook.ANCIENT : SpellBook.MODERN);
                 player.setBrimhavenDungeonOpen(results.getBoolean("brimhaven_open"));
                 player.setKilledClueAttacker(results.getBoolean("killed_clue_attacker"));

             }
        }

        /**
         * Updates the players position from the results.
         *
         * @param results       The results to update the players position from.
         * @throws SQLException An SQLException was encountered.
         */
        private void updatePosition(ResultSet results) throws SQLException {
            Position position = player.getPosition();

            /* Update the X coordinates */
            position.setX(results.getInt("x"));
            position.setLastX(results.getInt("x"));

            /* Update the Y coordinates */
            position.setY(results.getInt("y"));
            position.setLastY(results.getInt("y"));  /* TODO: + 1? */

            /* Update the Z coordinate */
            position.setZ(results.getInt("z"));
        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The inline class to load the information about the players bank.
     */
    private class ContainerLoader implements SQLCompletionHandler {


        /**
         * The container to load.
         */
        private Container container;

        /**
         * Constructs a new {@link ContainerLoader};
         *
         * @param container The container to load.
         */
        public ContainerLoader(Container container) {
            this.container = container;
        }

        @Override
        public void onComplete(ResultSet results) throws SQLException {

            while(results.next()) {

                /* Check if the item is valid */
                int itemId = results.getInt("item_id");
                if(itemId == -1) {
                    continue;
                }

                /* Create the item to add to the bank */
                Item item = new Item(itemId, results.getInt("amount"), results.getInt("timer"));

                /* Add the item to the bank */
                container.add(item, results.getInt("slot"));
            }
        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The inline class to load information about a players contacts.
     */
    private class ContactLoader implements SQLCompletionHandler {

        /**
         * Constructs a new {@link ContactLoader};
         */
        public ContactLoader() {}

        @Override
        public void onComplete(ResultSet results) throws SQLException {

            while(results.next()) {

                /* Get the contact and slot for that contact */
                long contact = results.getLong("contact");
                int slot = results.getInt("slot");

                /* Add it to the appropriate list */
                if(results.getBoolean("ignore")) {
                    player.getIgnores()[slot] = contact;
                } else {
                    player.getFriends()[slot] = contact;
                }
            }
        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
        }
    }

    private class SkillsLoader implements SQLCompletionHandler {

        @Override
        public void onComplete(ResultSet results) throws SQLException {
            while (results.next()) {
                Map<Integer, Integer> skillLevels = new HashMap<Integer, Integer>();
                skillLevels.put(Constants.SKILL_ATTACK,
                        results.getInt("cur_attack"));
                skillLevels.put(Constants.SKILL_DEFENCE,
                        results.getInt("cur_defence"));
                skillLevels.put(Constants.SKILL_STRENGTH,
                        results.getInt("cur_strength"));
                skillLevels.put(Constants.SKILL_HP,
                        results.getInt("cur_hitpoints"));
                skillLevels.put(Constants.SKILL_RANGE,
                        results.getInt("cur_ranged"));
                skillLevels.put(Constants.SKILL_PRAY,
                        results.getInt("cur_prayer"));
                skillLevels.put(Constants.SKILL_MAGE,
                        results.getInt("cur_magic"));
                skillLevels.put(Constants.SKILL_COOK,
                        results.getInt("cur_cooking"));
                skillLevels.put(Constants.SKILL_WC,
                        results.getInt("cur_woodcutting"));
                skillLevels.put(Constants.SKILL_FLETCH,
                        results.getInt("cur_fletching"));
                skillLevels.put(Constants.SKILL_FISH,
                        results.getInt("cur_fishing"));
                skillLevels.put(Constants.SKILL_FM,
                        results.getInt("cur_firemaking"));
                skillLevels.put(Constants.SKILL_CRAFT,
                        results.getInt("cur_crafting"));
                skillLevels.put(Constants.SKILL_SMITH,
                        results.getInt("cur_smithing"));
                skillLevels.put(Constants.SKILL_MINE,
                        results.getInt("cur_mining"));
                skillLevels.put(Constants.SKILL_HERB,
                        results.getInt("cur_herblore"));
                skillLevels.put(Constants.SKILL_AGILITY,
                        results.getInt("cur_agility"));
                skillLevels.put(Constants.SKILL_THIEVE,
                        results.getInt("cur_thieving"));
                skillLevels.put(Constants.SKILL_SLAY,
                        results.getInt("cur_slayer"));
                skillLevels.put(Constants.SKILL_FARM,
                        results.getInt("cur_farming"));
                skillLevels.put(Constants.SKILL_RC,
                        results.getInt("cur_runecrafting"));

                Map<Integer, Integer> skillExp = new HashMap<Integer, Integer>();
                skillExp.put(Constants.SKILL_ATTACK,
                        results.getInt("exp_attack"));
                skillExp.put(Constants.SKILL_DEFENCE,
                        results.getInt("exp_defence"));
                skillExp.put(Constants.SKILL_STRENGTH,
                        results.getInt("exp_strength"));
                skillExp.put(Constants.SKILL_HP,
                        results.getInt("exp_hitpoints"));
                skillExp.put(Constants.SKILL_RANGE,
                        results.getInt("exp_ranged"));
                skillExp.put(Constants.SKILL_PRAY, results.getInt("exp_prayer"));
                skillExp.put(Constants.SKILL_MAGE, results.getInt("exp_magic"));
                skillExp.put(Constants.SKILL_COOK,
                        results.getInt("exp_cooking"));
                skillExp.put(Constants.SKILL_WC,
                        results.getInt("exp_woodcutting"));
                skillExp.put(Constants.SKILL_FLETCH,
                        results.getInt("exp_fletching"));
                skillExp.put(Constants.SKILL_FISH,
                        results.getInt("exp_fishing"));
                skillExp.put(Constants.SKILL_FM,
                        results.getInt("exp_firemaking"));
                skillExp.put(Constants.SKILL_CRAFT,
                        results.getInt("exp_crafting"));
                skillExp.put(Constants.SKILL_SMITH,
                        results.getInt("exp_smithing"));
                skillExp.put(Constants.SKILL_MINE, results.getInt("exp_mining"));
                skillExp.put(Constants.SKILL_HERB,
                        results.getInt("exp_herblore"));
                skillExp.put(Constants.SKILL_AGILITY,
                        results.getInt("exp_agility"));
                skillExp.put(Constants.SKILL_THIEVE,
                        results.getInt("exp_thieving"));
                skillExp.put(Constants.SKILL_SLAY, results.getInt("exp_slayer"));
                skillExp.put(Constants.SKILL_FARM,
                        results.getInt("exp_farming"));
                skillExp.put(Constants.SKILL_RC,
                        results.getInt("exp_runecrafting"));

                System.out.println(skillExp.get(Constants.SKILL_RC));

                for (int i = 0; i <= 20; i++) {
                    player.getSkill().getLevel()[i] = skillLevels.get(i);
                }
                for (int i = 0; i <= 20; i++) {
                    player.getSkill().getExp()[i] = skillExp.get(i);
                }
            }

        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();

        }

    }

    /**
     * Creates a new player information loader.
     *
     * @return  The player information loader.
     */
    public SQLCompletionHandler createPlayerInformationLoader() {
        return new PlayerInformationLoader();
    }

    /**
     * Creates a new bank loader.
     *
     * @return The bank loader.
     */
    public SQLCompletionHandler createBankLoader() {
        return new ContainerLoader(player.getBank());
    }

    /**
     * Creates a new inventory loader.
     *
     * @return The inventory loader.
     */
    public SQLCompletionHandler createInventoryLoader() {
        return new ContainerLoader(player.getInventory().getItemContainer());
    }


    /**
     * Creates a new equipment loader.
     *
     * @return The equipment loader.
     */
    public SQLCompletionHandler createEquipmentLoader() {
        return new ContainerLoader(player.getEquipment().getItemContainer());
    }

    /**
     * Creates a new contact loader.
     *
     * @return  The contact loader.
     */
    public SQLCompletionHandler createContactLoader() {
        return new ContactLoader();
    }

    /**
     * Creates a new skill loader.
     *
     * @return  The skill loader.
     */
    public SQLCompletionHandler createSkillsLoader() {
        return new SkillsLoader();
    }

    /**
     * Creates a new container SQL worker.
     *
     * @param id    The id of the container to load.
     * @return      The created SQL worker.
     */
    public SQLWorker createContainerWorker(final int id) {
        return new SQLWorker(SQLEngine.LOAD_CONTAINER) {
            @Override
            public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {
                st.setInt(1, player.getUniqueId());
                st.setInt(2, id);
                return st.executeQuery();
            }
        };   
    }
}