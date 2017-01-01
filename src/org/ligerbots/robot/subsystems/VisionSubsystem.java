package org.ligerbots.robot.subsystems;

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
public class VisionSubsystem extends Subsystem implements Runnable {

	Relay mLedRing;
	ITable mTable = null;

	public VisionSubsystem() {
		mLedRing = new Relay(RobotMap.RELAY_LED_RING);

		Thread forwardThread = new Thread(this);
		forwardThread.setDaemon(true);
		forwardThread.setName("Packet Forwarding Thread");
		forwardThread.start();
	}
	
	public void setVisionEnabled(boolean enabled) {
		if(mTable == null) {
			mTable = NetworkTable.getTable("Vision");
		}
		mTable.putBoolean("enabled", enabled);
	}

	public void setLedRingOn(boolean on) {
		mLedRing.set(on ? Relay.Value.kForward : Relay.Value.kReverse);
	}

	public void initDefaultCommand() {
	}

	@Override
	public void run() {
		DatagramChannel udpChannel = null;
		InetSocketAddress sendAddress = null;
		ByteBuffer recvPacket = null;
		byte[] feedbackMessage = "👌".getBytes();
		ByteBuffer feedbackPacket = ByteBuffer.allocateDirect(feedbackMessage.length);
		feedbackPacket.put(feedbackMessage);
		long lastFeedbackTime = System.currentTimeMillis();

		// set up UDP
		try {
			udpChannel = DatagramChannel.open();
			udpChannel.socket().setReuseAddress(true);
			udpChannel.socket().bind(new InetSocketAddress(5810));
			udpChannel.configureBlocking(false);

			recvPacket = ByteBuffer.allocateDirect(udpChannel.socket().getReceiveBufferSize());
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				// steal the driver laptop's IP from networktables
				if (sendAddress == null) {
					ConnectionInfo[] connections = NetworkTablesJNI.getConnections();
					for (ConnectionInfo connInfo : connections) {
						// we want the laptop, not the phone
						if (connInfo.remote_id.equals("Android"))
							continue;
						sendAddress = new InetSocketAddress(connInfo.remote_ip, 5810);
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
				if (from != null && System.currentTimeMillis() - lastFeedbackTime > 1000) {
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
					if (magic == 16777216) {
						recvPacket.limit(length + 8);
						recvPacket.position(0);
						udpChannel.send(recvPacket, sendAddress);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
