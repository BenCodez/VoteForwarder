package com.Ben12345rocks.VoteForwarder;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import com.vexsoftware.votifier.crypto.RSA;
import com.vexsoftware.votifier.model.Vote;

// TODO: Auto-generated Javadoc
/**
 * The Class BungeeVote.
 */
public class VoteForward {

	/** The instance. */
	static VoteForward instance = new VoteForward();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/** The sock. */
	static ServerSocket sock;

	/**
	 * Gets the single instance of BungeeVote.
	 *
	 * @return single instance of BungeeVote
	 */
	public static VoteForward getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new bungee vote.
	 */
	private VoteForward() {
	}

	/**
	 * Instantiates a new bungee vote.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public VoteForward(Main plugin) {
		VoteForward.plugin = plugin;
	}

	/**
	 * Check offline bungee votes.
	 */
	public void checkOfflineVotes() {
		for (String server : plugin.offline.keySet()) {
			plugin.offline.put(server, new ArrayList<Vote>());
			ArrayList<Vote> votes = plugin.offline.get(server);
			if (votes != null) {
				for (Vote vote : votes) {
					try {
						sendBungeeVoteServer(server, vote);
					} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	/**
	 * Send bungee vote server.
	 *
	 * @param server
	 *            the server
	 * @param vote
	 *            the vote
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws InvalidKeySpecException
	 *             the invalid key spec exception
	 */
	public void sendBungeeVoteServer(String server, Vote vote)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] encodedPublicKey = DatatypeConverter.parseBase64Binary(Config
				.getInstance().getServerKey(server));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		String serverIP = Config.getInstance().getServerIP(server);
		int serverPort = Config.getInstance().getServerPort(server);
		String serviceSite = Config.getInstance()
				.getServerServiceSite(serverIP);
		if (serverIP.length() != 0) {
			try {
				if (serviceSite != null) {
					if (serviceSite.length() > 0) {
						vote.setServiceName(serviceSite);
					}
				}
				String VoteString = "VOTE\n" + vote.getServiceName() + "\n"
						+ vote.getUsername() + "\n" + vote.getAddress() + "\n"
						+ vote.getTimeStamp() + "\n";

				SocketAddress sockAddr = new InetSocketAddress(serverIP,
						serverPort);
				Socket socket = new Socket();
				socket.connect(sockAddr, 1000);
				OutputStream socketOutputStream = socket.getOutputStream();
				socketOutputStream.write(RSA.encrypt(VoteString.getBytes(),
						publicKey));
				socketOutputStream.close();
				socket.close();

			} catch (Exception e) {
				plugin.getLogger().info(
						"Failed to send vote to " + server + "(" + serverIP
								+ ":" + serverPort + "): " + vote.toString()
								+ ", ignore this if server is offline");

				ArrayList<Vote> votes = plugin.offline.get(server);
				if (votes == null) {
					votes = new ArrayList<Vote>();
				}
				if (!votes.contains(vote)) {
					votes.add(vote);
				}
				plugin.offline.put(server, votes);
			}
		}
	}

	/**
	 * Send vote.
	 *
	 * @param vote
	 *            the vote
	 */
	public void sendVote(Vote vote) {
		for (String server : Config.getInstance().getServers()) {
			try {
				sendBungeeVoteServer(server, vote);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}
	}
}