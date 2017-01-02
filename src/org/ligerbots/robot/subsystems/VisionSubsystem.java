package org.ligerbots.robot.subsystems;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.ligerbots.robot.RobotMap;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.ConnectionInfo;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 *
 */
public class VisionSubsystem extends Subsystem {
	private static final int CS_STREAM_PORT = 5810;
	private static final int DATA_PORT = 5808;
	private static final int CS_FEEDBACK_INTERVAL = 1000;
	private static final int CS_MAGIC_NUMBER = 16777216;

	Relay mLedRing;
	ITable mTable = null;

	public VisionSubsystem() {
		mLedRing = new Relay(RobotMap.RELAY_LED_RING);

		Thread forwardThread = new Thread(this::packetForwardingThread);
		forwardThread.setDaemon(true);
		forwardThread.setName("Packet Forwarding Thread");
		forwardThread.start();

		Thread dataThread = new Thread(this::dataThread);
		dataThread.setDaemon(true);
		dataThread.setName("Vision Data Thread");
		dataThread.start();
	}

	public void setVisionEnabled(boolean enabled) {
		if (mTable == null) {
			mTable = NetworkTable.getTable("Vision");
		}
		mTable.putBoolean("enabled", enabled);
	}

	public void setLedRingOn(boolean on) {
		mLedRing.set(on ? Relay.Value.kForward : Relay.Value.kReverse);
	}

	public void initDefaultCommand() {
	}

	@SuppressWarnings("unused")
	public void dataThread() {
		DatagramChannel udpChannel = null;
		ByteBuffer dataPacket = ByteBuffer.allocateDirect(Double.SIZE / 8 * 6);

		try {
			udpChannel = DatagramChannel.open();
			udpChannel.socket().setReuseAddress(true);
			udpChannel.socket().bind(new InetSocketAddress(DATA_PORT));
			udpChannel.configureBlocking(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// the phone sends processing data over UDP faster than NetworkTables
		// 10fps refresh rate, so here we set up a receiver for the data

		while (true) {
			try {
				dataPacket.position(0);
				SocketAddress from = udpChannel.receive(dataPacket);
				if (from == null)
					continue;

				double rvec_0 = dataPacket.getDouble();
				double rvec_1 = dataPacket.getDouble();
				double rvec_2 = dataPacket.getDouble();
				double tvec_0 = dataPacket.getDouble();
				double tvec_1 = dataPacket.getDouble();
				double tvec_2 = dataPacket.getDouble();

				// now do something with the data
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void packetForwardingThread() {
		DatagramChannel udpChannel = null;
		InetSocketAddress sendAddress = null;
		ByteBuffer recvPacket = null;
		byte[] feedbackMessage = "👌".getBytes();
		ByteBuffer feedbackPacket = ByteBuffer
				.allocateDirect(feedbackMessage.length);
		feedbackPacket.put(feedbackMessage);
		long lastFeedbackTime = System.currentTimeMillis();

		// set up UDP
		try {
			udpChannel = DatagramChannel.open();
			udpChannel.socket().setReuseAddress(true);
			udpChannel.socket().bind(new InetSocketAddress(CS_STREAM_PORT));
			udpChannel.configureBlocking(false);

			recvPacket = ByteBuffer
					.allocateDirect(udpChannel.socket().getReceiveBufferSize());
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				// steal the driver laptop's IP from networktables
				if (sendAddress == null) {
					ConnectionInfo[] connections = NetworkTablesJNI
							.getConnections();
					for (ConnectionInfo connInfo : connections) {
						// we want the laptop, not the phone
						if (connInfo.remote_id.equals("Android"))
							continue;
						sendAddress = new InetSocketAddress(connInfo.remote_ip,
								CS_STREAM_PORT);
					}
				}

				// get a packet from the phone
				SocketAddress from = null;
				recvPacket.limit(recvPacket.capacity());
				recvPacket.position(0);
				from = udpChannel.receive(recvPacket);
				boolean gotPacket = from != null;

				// if we have a packet and it's time to tell the phone we're
				// getting packets then tell the phone we're getting packets
				if (from != null && System.currentTimeMillis()
						- lastFeedbackTime > CS_FEEDBACK_INTERVAL) {
					lastFeedbackTime = System.currentTimeMillis();
					feedbackPacket.position(0);
					udpChannel.send(feedbackPacket, from);
				}

				// if sending packets to the driver laptop turns out to be
				// slower than receiving packets from the phone, then drop
				// everything except the latest packet
				while (from != null) {
					// save the length of what we got last time
					recvPacket.limit(recvPacket.capacity());
					recvPacket.position(0);
					from = udpChannel.receive(recvPacket);
				}

				if (sendAddress != null && gotPacket) {
					// make sure to forward a packet of the same length, by
					// setting the limit on the bytebuffer
					recvPacket.position(0);
					int magic = recvPacket.getInt();
					int length = recvPacket.getInt();
					if (magic == CS_MAGIC_NUMBER) {
						recvPacket.limit(length + 8);
						recvPacket.position(0);
						udpChannel.send(recvPacket, sendAddress);
					}
					// otherwise, it's probably a control packet from the
					// dashboard sending the resolution and fps settings - we
					// don't actually care
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
