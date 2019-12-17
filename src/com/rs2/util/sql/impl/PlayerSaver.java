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

import com.rs2.model.Position;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.item.Item;
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
public final class PlayerSaver {

    /**
     * The query to insert or update a new player.
     */
    private static final String PLAYER_INFORMATION_UPDATE = "INSERT INTO prs06_players (id, username, x, y, z, appearance_0, " +
                                                            "appearance_1, appearance_2, appearance_3, appearance_4, appearance_5, " +
                                                            "appearance_6, color_0, color_1, color_2, color_3, color_4, pin, " +
                                                            "tutorial_stage, tutorial_progress, is_male, ban_expires, mute_expires, " +
                                                            "changing_bankpin, deleting_bankpin, pin_append_year, pin_append_date, " +
                                                            "binding_neck_charge, ring_of_forging_life, ring_of_recoil_life, slayer_master, slayer_task, task_amount, using_ancients, brimhaven_open, killed_clue_attacker) " +
                                                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY " +
                                                            "UPDATE x = VALUES(x), y = VALUES(y), z = VALUES(z), appearance_0 = VALUES(appearance_0), " +
                                                            "appearance_1 = VALUES(appearance_1), appearance_2 = VALUES(appearance_2), appearance_3 = VALUES(appearance_3), " +
                                                            "appearance_4 = VALUES(appearance_4), appearance_5 = VALUES(appearance_5), appearance_6 = VALUES(appearance_6), " +
                                                            "color_0 = VALUES(color_0), color_1 = VALUES(color_1), color_2 = VALUES(color_2), " +
                                                            "color_3 = VALUES(color_3), color_4 = VALUES(color_4), pin = VALUES(pin), tutorial_stage = VALUES(tutorial_stage), " +
                                                            "tutorial_progress = VALUES(tutorial_progress), is_male = VALUES(is_male), ban_expires = VALUES(ban_expires), " +
                                                            "mute_expires = VALUES(mute_expires), changing_bankpin = VALUES(changing_bankpin), deleting_bankpin = VALUES(deleting_bankpin)," +
                                                            "pin_append_year = VALUES(pin_append_year), pin_append_date = VALUES(pin_append_date), binding_neck_charge = VALUES(binding_neck_charge), " +
                                                            "ring_of_forging_life = VALUES(ring_of_forging_life), ring_of_recoil_life = VALUES(ring_of_recoil_life), " +
                                                            "slayer_master = VALUES(slayer_master), slayer_task = VALUES(slayer_task), task_amount = VALUES(task_amount), " +
                                                            "using_ancients = VALUES(using_ancients), brimhaven_open = VALUES(brimhaven_open), killed_clue_attacker = VALUES(killed_clue_attacker)";

    private static final String PLAYER_SKILLS_UPDATE = "INSERT INTO prs06_skills (id, player_id, cur_attack, cur_defence, cur_strength, " +
                                                       "cur_hitpoints, cur_ranged, cur_prayer, cur_magic, cur_cooking, " +
                                                       "cur_woodcutting, cur_fletching, cur_fishing, cur_firemaking, " +
                                                       "cur_crafting, cur_smithing, cur_mining, cur_herblore, cur_agility, " +
                                                       "cur_thieving, cur_slayer, cur_farming, cur_runecrafting, exp_attack, " +
                                                       "exp_defence, exp_strength, exp_hitpoints, exp_ranged, exp_prayer, " +
                                                       "exp_magic, exp_cooking, exp_woodcutting, exp_fletching, exp_fishing, " +
                                                       "exp_firemaking, exp_crafting, exp_smithing, exp_mining, exp_herblore, " +
                                                       "exp_agility, exp_thieving, exp_slayer, exp_farming, exp_runecrafting) " +
                                                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                                                       "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                                       "ON DUPLICATE KEY UPDATE cur_attack = VALUES(cur_attack), cur_defence = VALUES(cur_defence), " +
                                                       "cur_strength = VALUES(cur_strength), cur_hitpoints = VALUES(cur_hitpoints), " +
                                                       "cur_ranged = VALUES(cur_ranged), cur_prayer = VALUES(cur_prayer), " +
                                                       "cur_magic = VALUES(cur_magic), cur_cooking = VALUES(cur_cooking), " +
                                                       "cur_woodcutting = VALUES(cur_woodcutting), cur_fletching = VALUES(cur_fletching), " +
                                                       "cur_fishing = VALUES(cur_fishing), cur_firemaking = VALUES(cur_firemaking), " +
                                                       "cur_thieving = VALUES(cur_thieving), cur_slayer = VALUES(cur_slayer), cur_farming = VALUES(cur_farming), " +
                                                       "cur_runecrafting = VALUES(cur_runecrafting), " +
                                                       "exp_attack = VALUES(exp_attack), exp_defence = VALUES(exp_defence), " +
                                                       "exp_strength = VALUES(exp_strength), exp_hitpoints = VALUES(exp_hitpoints), " +
                                                       "exp_ranged = VALUES(exp_ranged), exp_prayer = VALUES(exp_prayer), " +
                                                       "exp_magic = VALUES(exp_magic), exp_cooking = VALUES(exp_cooking), " +
                                                       "exp_woodcutting = VALUES(exp_woodcutting), exp_fletching = VALUES(exp_fletching), " +
                                                       "exp_fishing = VALUES(exp_fishing), exp_firemaking = VALUES(exp_firemaking), " +
                                                       "exp_thieving = VALUES(exp_thieving), exp_slayer = VALUES(exp_slayer), " +
                                                       "exp_farming = VALUES(exp_farming), exp_runecrafting = VALUES(exp_runecrafting)";

    /**
     * The query to insert or update a new contact.
     */
    private static final String PLAYER_CONTACT_UPDATE = getContactQuery();

    /**
     * The map for each of the container queries.
     */
    private static final Map<Integer, String> containerQueries = new HashMap<Integer, String>();

    /**
     * The player to save.
     */
    private final Player player;
    
    /**
     * Constructs a new {@link PlayerSaver};
     *
     * @param player    The player to save the information for.
     */
    public PlayerSaver(Player player) {
        this.player = player;
    }

    /**
     * The inline player information saver SQL worker.
     */
    private class PlayerInformationSaver extends SQLWorker {

        /**
         * Constructs a new {@link PlayerInformationSaver};
         */
        public PlayerInformationSaver() {
            super(PLAYER_INFORMATION_UPDATE);
        }

        @Override
        public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {

            /* Set the basic saving information */
            st.setInt(1, player.getUniqueId());
            st.setString(2, player.getUsername());

            /* Set the position update information */
            Position position = player.getPosition();
            st.setInt(3, position.getX());
            st.setInt(4, position.getY());
            st.setInt(5, position.getZ());

            for(int i = 0; i < player.getAppearance().length; i++) {
                st.setInt(6 + i, player.getAppearance()[i]);
            }

            for(int i = 0; i < player.getColors().length; i++) {
                st.setInt(13 + i, player.getColors()[i]);
            }
            
            String pin = "";
            for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {

                int value = player.getBankPin().getBankPin()[i];

                if(value == -1) {
                    pin = "na";
                    break;
                }

                pin += value;
            }
            
            st.setString(18, pin);

            st.setInt(21, player.getGender());
            
            st.setLong(22, player.getBanExpire());
            st.setLong(23, player.getMuteExpire());

            st.setBoolean(24, player.getBankPin().isChangingBankPin());
            st.setBoolean(25, player.getBankPin().isDeletingBankPin());
            st.setInt(26, player.getBankPin().getPinAppendYear());
            st.setInt(27, player.getBankPin().getPinAppendDay());
            st.setInt(28, player.getBindingNeckCharge());
            st.setInt(29, player.getRingOfForgingLife());
            st.setInt(30, player.getRingOfRecoilLife());

            st.setInt(31, player.getSlayer().slayerMaster);
            st.setString(32, player.getSlayer().slayerTask);
            st.setInt(33, player.getSlayer().taskAmount);
            st.setBoolean(34, player.getMagicBookType() == SpellBook.ANCIENT);
            st.setBoolean(35, player.isBrimhavenDungeonOpen());
            st.setBoolean(36, player.hasKilledClueAttacker());

            st.executeUpdate();
            return null;
        }
    }

    /**
     * The inline container saver SQL worker.
     */
    private class ContainerSaver extends SQLWorker {

        /**
         * The container to save.
         */
        private Container container;

        /**
         * The container id.
         */
        private int id;
        
        /**
         * Constructs a new {@link ContainerSaver};
         */
        public ContainerSaver(Container container, int id) {
            super(getContainerQuery(id, container.capacity()));

            this.container = container;
            this.id = id;
        }

        @Override
        public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {
            int offset = 1;

            for (int i = 0; i < container.capacity(); i++) {
                Item item = container.get(i);

                /* Give the slot at least 10 bits of room */
                st.setInt(offset++, id << 28 | i << 18 | player.getUniqueId());

                /* Set the id of the container */
                st.setInt(offset++, id);

                /* Set all the default information */
                st.setInt(offset++, player.getUniqueId());
                if (item == null) {
                    st.setInt(offset++, -1);
                    st.setInt(offset++, 0);
                    st.setInt(offset++, i);
                    st.setInt(offset++, 0);
                } else {
                    st.setInt(offset++, item.getId());
                    st.setInt(offset++, item.getCount());
                    st.setInt(offset++, i);
                    st.setInt(offset++, item.getTimer());
                }
            }

            /* Execute the update */
            st.executeUpdate();
            
            return null;
        }
    }

    /**
     * The inline contact saver SQL worker.
     */
    private class ContactSaver extends SQLWorker {

        /**
         * Constructs a new {@link ContactSaver};
         */
        public ContactSaver() {
            super(PLAYER_CONTACT_UPDATE);
        }

        @Override
        public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {
            
            int offset = 1;
            
            /* Write each of the ignores */
            long[] ignores = player.getIgnores();         
            for(int i = 0; i < ignores.length; i++) {
                st.setInt(offset++, 1 << 30 | i << 24 | player.getUniqueId());
                st.setInt(offset++, player.getUniqueId());
                st.setInt(offset++, i);
                st.setLong(offset++, ignores[i]);
                st.setBoolean(offset++, true);
            }

            /* Write each of the friends */
            long[] friends = player.getFriends();
            for(int i = 0; i < friends.length; i++) {
                st.setInt(offset++, i << 24 | player.getUniqueId());
                st.setInt(offset++, player.getUniqueId());
                st.setInt(offset++, i);
                st.setLong(offset++, friends[i]);
                st.setBoolean(offset++, false);
            }

            /* Execute the update */
            st.executeUpdate();

            return null;  
        }
    }

    /**
     * The inline skills saver SQL worker.
     */
    private class SkillsSaver extends SQLWorker {

        /**
         * Constructs a new {@link SkillsSaver};
         */
        public SkillsSaver() {
            super(PLAYER_SKILLS_UPDATE);
        }

        @Override
        public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {

            st.setInt(1, player.getUniqueId());
            st.setInt(2, player.getUniqueId());

            for(int i = 0; i <= 20; i++) {
                st.setInt(i + 3, player.getSkill().getLevel()[i]);
                st.setInt(i + 24, (int) player.getSkill().getExp()[i]);
            }

            /* Execute the update */
            st.executeUpdate();

            return null;
        }
    }

    /**
     * Creates a new {@link PlayerInformationSaver};
     * 
     * @return  The created player information saver.
     */
    public SQLWorker createPlayerInformationSaver() {
        return new PlayerInformationSaver();
    }

    /**
     * Creates a new bank saver.
     *
     * @return  The created bank saver.
     */
    public SQLWorker createBankSaver() {
        return new ContainerSaver(player.getBank(), PlayerLoader.BANK_ID);
    }

    /**
     * Creates a new inventory saver.
     *
     * @return  The created inventory saver.
     */
    public SQLWorker createInventorySaver() {
        return new ContainerSaver(player.getInventory().getItemContainer(), PlayerLoader.INVENTORY_ID);
    }

    /**
     * Creates a new equipment saver.
     *
     * @return  The created equipment saver.
     */
    public SQLWorker createEquipmentSaver() {
        return new ContainerSaver(player.getEquipment().getItemContainer(), PlayerLoader.EQUIPMENT_ID);
    }

    /**
     * Creates a new contact saver.
     *
     * @return  The created contact saver.
     */
    public SQLWorker createContactSaver() {
        return new ContactSaver();
    }

    /**
     * Creates a new skills saver.
     * 
     * @return  The created skills saver.
     */
    public SQLWorker createSkillsSaver() {
        return new SkillsSaver();
    }

    /**
     * Gets the query for a container.
     *
     * @param id    The id of the container.
     * @param size  The size of the container.
     * @return      The query.
     */
    private static String getContainerQuery(int id, int size) {

        /* Create the query if it does not exist */
        if(!containerQueries.containsKey(id)) {
            String query = getContainerQuery(size);
            containerQueries.put(id, query);
            return query;
        }

        return containerQueries.get(id);
    }

    /**
     * Gets the query for an container.
     *
     * @return  The query.
     */
    private static String getContainerQuery(int size) {
        /* Create the query string */
        String insertQuery = "INSERT INTO prs06_containers (id, container_id, user_id, item_id, amount, slot, timer) VALUES ";
        String updateQuery = " ON DUPLICATE KEY UPDATE item_id = VALUES(item_id), amount = VALUES(amount), slot = VALUES(slot), timer = VALUES(timer)";

        for(int i = 1; i <= size; i++) {
            insertQuery += "(?, ?, ?, ?, ?, ?, ?)";
            if(i != size) {
                insertQuery += ", ";
            }
        }

        return insertQuery + updateQuery;
    }

    /**
     * Gets the query for contacts.
     *
     * @return  The query.
     */
    private static String getContactQuery() {
        /* Create the query string */
        String insertQuery = "INSERT INTO prs06_contacts (`id`, `player_id`, `slot`, `contact`, `ignore`) VALUES ";
        String updateQuery = " ON DUPLICATE KEY UPDATE contact = VALUES(contact)";

        int amountContacts = 300;

        for(int i = 1; i <= amountContacts; i++) {
            insertQuery += "(?, ?, ?, ?, ?)";
            if(i != amountContacts) {
                insertQuery += ", ";
            }
        }

        return insertQuery + updateQuery;
    }
}
