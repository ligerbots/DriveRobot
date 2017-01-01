package org.ligerbots.robot.subsystems;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Enumeration;

import org.ligerbots.robot.RobotMap;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

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

	public InetAddress findUsb1() {
		try {
			Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = allInterfaces.nextElement();
				if (networkInterface.getName().equals("usb1") && networkInterface.isUp()) {
					for (InterfaceAddress addr : networkInterface.getInterfaceAddresses()) {
						if (addr == null)
							continue;
						InetAddress usb1Addr = addr.getAddress();
						if (usb1Addr == null)
							continue;
						if (usb1Addr instanceof Inet6Address)
							continue;
						return usb1Addr;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {
		InetAddress usb1Addr = null;
		System.out.println("Finding usb1 address...");
		while (usb1Addr == null) {
			usb1Addr = findUsb1();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Found usb1 address: " + usb1Addr.toString());
		DatagramSocket usb1Sock = null, send = null;
		SocketAddress sendAddress = null;
		InetAddress me = null;
		try {
			usb1Sock = new DatagramSocket(null);
			usb1Sock.setReuseAddress(true);
			usb1Sock.setBroadcast(true);
			usb1Sock.bind(new InetSocketAddress(/* usb1Addr, */5810));
			send = new DatagramSocket(null);
			send.setReuseAddress(true);
			send.setBroadcast(true);
			send.bind(new InetSocketAddress(5809));
			me = InetAddress.getByName("10.28.77.2");
			sendAddress = new InetSocketAddress(InetAddress.getByName("10.28.77.255"), 5810);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Begin forwarding");
		DatagramPacket packet = null;
		try {
			packet = new DatagramPacket(new byte[usb1Sock.getReceiveBufferSize()],
					usb1Sock.getReceiveBufferSize());
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while (true) {
			try {
				usb1Sock.receive(packet);
				if (packet.getAddress().equals(me))
					continue;
				packet.setSocketAddress(sendAddress);
				send.send(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
