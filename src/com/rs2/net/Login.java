package com.rs2.net;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.security.SecureRandom;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;

public class Login {

	public void handleLogin(Player player, ByteBuffer inData) throws Exception {
		switch (player.getLoginStage()) {
		case CONNECTED:
			if (inData.remaining() < 2) {
				inData.compact();
				return;
			}

			// Validate the request.
			int request = inData.get() & 0xff;
			inData.get(); // Name hash.
			if (request != 14) {
				System.err.println("Invalid login request: " + request);
				player.disconnect();
				return;
			}

			// Write the response.
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(17);
			out.writeLong(0); // First 8 bytes are ignored by the client.
			out.writeByte(0); // The response opcode, 0 for logging in.
			out.writeLong(new SecureRandom().nextLong()); // SSK.
			player.send(out.getBuffer());

			player.setLoginStage(LoginStages.LOGGING_IN);
			break;
		case LOGGING_IN:
			if (inData.remaining() < 2) {
				inData.compact();
				return;
			}

			// Validate the login type.
			int loginType = inData.get();
			if (loginType != 16 && loginType != 18) {
				System.err.println("Invalid login type: " + loginType);
				player.disconnect();
				return;
			}

			// Ensure that we can read all of the login block.
			int blockLength = inData.get() & 0xff;
			int loginEncryptSize = blockLength - (36 + 1 + 1 + 2 + 6);
			if (inData.remaining() < blockLength) {
				inData.compact();
				return;
			}

			// Read the login block.
			StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(inData);
			
			// Set the magic id
			player.setMagicId(in.readByte());

			// Set the client version.
			player.setClientVersion(in.readShort());

			in.readByte(); // Skip the high/low memory version.

			// MAC address
			if(player.getClientVersion() >= 5){
				byte[] mac = new byte[6];
				for (int i = 0; i < 6; i++) {
					mac[i] = (byte) in.readByte();
				}
				player.setMACaddress(Misc.MACtoString(mac));
			}
			// Skip the CRC keys.
			for (int i = 0; i < 9; i++) {
				in.readInt();
			}
			if (Constants.RSA_CHECK) {
				loginEncryptSize--;
				int reportedSize = inData.get() & 0xFF;
				if (reportedSize != loginEncryptSize) {
					System.err.println("Encrypted packet size zero or negative : " + loginEncryptSize);
					player.disconnect();
					return;
				}
				byte[] encryptionBytes = new byte[loginEncryptSize];
				inData.get(encryptionBytes);
				ByteBuffer rsaBuffer = ByteBuffer.wrap(new BigInteger(encryptionBytes).modPow(Constants.RSA_EXPONENT, Constants.RSA_MODULUS).toByteArray());
				int rsaOpcode = rsaBuffer.get() & 0xFF;
				// Validate that the RSA block was decoded properly.
				if (rsaOpcode != 10) {
					System.err.println("Unable to decode RSA block properly!");
					player.disconnect();
					//saveIp(player.getHost());
					return;
				}
				long clientHalf = rsaBuffer.getLong();
				long serverHalf = rsaBuffer.getLong();
				int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf,
						(int) (serverHalf >> 32), (int) serverHalf };
				player.setDecryptor(new ISAACCipher(isaacSeed));
				for (int i = 0; i < isaacSeed.length; i++) {
					isaacSeed[i] += 50;
				}
				player.setEncryptor(new ISAACCipher(isaacSeed));
				rsaBuffer.getInt();
				String username = NameUtil.getRS2String(rsaBuffer).trim();
				String password = NameUtil.getRS2String(rsaBuffer).trim();
				player.setPassword(password);
				player.setUsername(NameUtil.uppercaseFirstLetter(username));
				player.loginTime = System.currentTimeMillis();
				player.regionEnterTime = player.loginTime;
			} else {
				in.readByte(); // Skip RSA block length.
				// Validate that the RSA block was decoded properly.
				int rsaOpcode = in.readByte();
				if (rsaOpcode != 10) {
					System.err.println("Unable to decode RSA block properly!");
					player.disconnect();
					return;
				}
				// Set up the ISAAC ciphers.
				long clientHalf = in.readLong();
				long serverHalf = in.readLong();
				int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
				player.setDecryptor(new ISAACCipher(isaacSeed));
				for (int i = 0; i < isaacSeed.length; i++) {
					isaacSeed[i] += 50;
				}
				player.setEncryptor(new ISAACCipher(isaacSeed));

				// Read the user authentication.
				in.readInt(); // Skip the user ID.
                String username = in.readString().trim();
                String password = in.readString().trim();
                //player.setPassword(password);
                player.setPw(password);
               // System.out.println(player.getPassword());
                player.setUsername(NameUtil.uppercaseFirstLetter(username));
                player.loginTime = System.currentTimeMillis();
            }

            player.setUsernameAsLong(NameUtil.nameToLong(player.getUsername().toLowerCase()));
            player.setLoginStage(LoginStages.AWAITING_LOGIN_COMPLETE);

            if (player.beginLogin() && player.getLoginStage() == LoginStages.AWAITING_LOGIN_COMPLETE) {
            // Switch the player to the cycled reactor.
                synchronized (DedicatedReactor.getInstance()) {
                    DedicatedReactor.getInstance().getSelector().wakeup();
                    player.getKey().interestOps(player.getKey().interestOps() & ~SelectionKey.OP_READ);
                    player.getSocketChannel().register(Server.getSingleton().getSelector(), SelectionKey.OP_READ, player);
                }
            }
        }
    }

	@SuppressWarnings("unused")
	private void saveIp(String host) {
		String filePath = "./data/ip.txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.write(host);
				out.newLine();
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkName(String username) {
		return true;
	}

}
