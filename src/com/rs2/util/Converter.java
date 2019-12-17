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

package com.rs2.util;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.util.sql.SQLCompletionHandler;
import com.rs2.util.sql.SQLEngine;
import com.rs2.util.sql.SQLWorker;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Hadyn Richard
 */
public class Converter {
    
    private static int counter = 0;
    private static volatile boolean next = true;

    public static void main(String[] args) throws Exception {
        try {

            Constants.MYSQL_ENABLED = true;

            final File characterDir = new File("./data/characters/");

            /* Start up the SQL engine */
            SQLEngine.setGameDatabase(new SQLEngine(8, Constants.DB_DRIVER, Constants.GAME_DB_URL, Constants.GAME_DB_USER, Constants.GAME_DB_PASS));

            System.out.println("Preparing to convert " + characterDir.listFiles().length + " character files...");

            List<File> files = Arrays.asList(characterDir.listFiles());
            
            final Iterator<File> fileIterator = files.iterator();
            
            while(true) {
                
                if(!next) {
                    continue;
                }

                if(!fileIterator.hasNext()) {
                    break;
                }
                
                File characterFile = fileIterator.next();

                next = false;

                /* Get the username of the character */
                final String username = characterFile.getName().substring(0, characterFile.getName().indexOf('.')).toLowerCase();

                /* Just create a null selection key player */
                final Player player = new Player(null);
                player.setUsername(username);

                SQLWorker worker = new SQLWorker(SQLEngine.LOOKUP_PLAYER) {
                    @Override
                    public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {
                        st.setString(1, username);
                        return st.executeQuery();
                    }
                };

                /* Look up the player */
                SQLEngine.getGameDatabase().execute(worker, new SQLCompletionHandler() {

                    @Override
                    public void onComplete(ResultSet results) throws SQLException {
                        try {
                            System.out.println("Converting player file [" + ++counter + "/" + characterDir.listFiles().length + "]");

                            if (results.next()) {
                                int userId = results.getInt("uid");
                                player.setUniqueId(userId);

                                /* Read the file */
                                PlayerSave.readFile(PlayerSave.directory, player);

                                PlayerSave.save(player);
                            } else {
                                System.out.println("User `" + username + "` doesn't exist");
                            }
                        } catch(Exception ex) {
                            System.out.println("User `" + username + "` file is corrupt");
                        } finally {
                            next = true;
                        }
                    }

                    @Override
                    public void onException(Exception ex) {
                        System.out.println("Issue with grabbing uid for player `" + username + "`");
                        next = true;
                    }
                });
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
