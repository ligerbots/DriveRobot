package org.ligerbots.robot.subsystems;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.ligerbots.robot.RobotMap;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.ConnectionInfo;
import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;

/**
 *
 */
public class VisionSubsystem extends Subsystem implements Runnable {

	Relay mLedRing;

	public VisionSubsystem() {
		mLedRing = new Relay(RobotMap.RELAY_LED_RING);

		Thread forwardThread = new Thread(this);
		forwardThread.setDaemon(true);
		forwardThread.setName("Packet Forwarding Thread");
		forwardThread.start();
	}

	public void setLedRingOn(boolean on) {
		mLedRing.set(on ? Relay.Value.kForward : Relay.Value.kReverse);
	}

	public void initDefaultCommand() {
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		DatagramSocket udpSocket = null;
		InetSocketAddress sendAddress = null;
		try {
			udpSocket = new DatagramSocket(null);
			udpSocket.setReuseAddress(true);
			udpSocket.bind(new InetSocketAddress(5810));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Begin forwarding");
		DatagramPacket packet = null;
		try {
			packet = new DatagramPacket(
					new byte[udpSocket.getReceiveBufferSize()],
					udpSocket.getReceiveBufferSize());
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		long lastFeedbackTime = System.currentTimeMillis();
		DatagramPacket feedbackPacket = new DatagramPacket(
				new byte[] { 'O', 'K' }, 2);

		while (true) {
			try {
				// steal the driver laptop's IP from networktables
				if (sendAddress == null) {
					ConnectionInfo[] connections = NetworkTablesJNI
							.getConnections();
					for (ConnectionInfo connInfo : connections) {
						if (connInfo.remote_id.equals("Android"))
							continue;
						sendAddress = new InetSocketAddress(connInfo.remote_ip,
								5810);
					}
				}

				udpSocket.receive(packet);

				if (System.currentTimeMillis() - lastFeedbackTime > 1000) {
					lastFeedbackTime = System.currentTimeMillis();
					feedbackPacket.setSocketAddress(packet.getSocketAddress());
					udpSocket.send(feedbackPacket);
				}

				if (sendAddress != null) {
					packet.setSocketAddress(sendAddress);
					udpSocket.send(packet);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
