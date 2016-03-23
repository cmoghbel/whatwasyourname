package edu.ucla.cs213a.whatwasyourname;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Communication implements Runnable {
	public static ArrayList<String> PeersIP = new ArrayList<String>();
	private static final int SOCKET_TIMEOUT = 5000;
	static final int PORT = 8988;
	static int bufferSize = 1024;
//	static FaceRecognizer faceRecognizer = new FaceRecognizer();

	public void run() {

		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);

			Log.d(MainConnectActivity.TAG, "Server: Socket opened");
			Socket client = null;
			
//			faceRecognizer.train();
			
			while (true) {
				try {
					client = serverSocket.accept();

					Log.d(MainConnectActivity.TAG, "Server: connection done");

					HandleClient(client);

					client.close();
				} catch (Exception ex) {
					if (client != null) {
						if (client.isConnected()) {
							try {
								client.close();
							} catch (IOException e) {
								// Give up
								e.printStackTrace();
							}
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void HandleClient(Socket client) throws IOException {

		InputStream inputstream = client.getInputStream();
		OutputStream out = client.getOutputStream();
		byte[] buffer = new byte[100];
		client.setSoTimeout(SOCKET_TIMEOUT);
		int readBytes = inputstream.read(buffer, 0, 100);
		String str = new String(buffer, 0, readBytes);
		if (str.equals("IP")) { // IP
			String ip = client.getInetAddress().toString();
			ip = ip.replace("/", "");
			if (!PeersIP.contains(ip)) {
				PeersIP.add(ip);
			}
			Log.d(MainConnectActivity.TAG, ip + " Added");
		} else if (str.equals("Picture")) { // picture
			out.write("OK".getBytes());
			client.setSoTimeout(SOCKET_TIMEOUT);
			readBytes = inputstream.read(buffer, 0, 100);
			str = new String(buffer, 0, readBytes);
			int n = Integer.valueOf(str);
			out.write("OK".getBytes());
			readBytes = 0;
			byte[] inputPic = new byte[n];

			DataInputStream dis = new DataInputStream(inputstream);
			dis.readFully(inputPic);
//			dis.close();
			
//			try
//			{
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			    byte[] buf = new byte[1024];
//			    int length = 0;
//			    while ((length = inputstream.read(buf))!=-1)
//			    {
//			        baos.write(buf,0,length);
//			    }
//			    inputPic = baos.toByteArray();
//			}
//			catch (IOException e) {}
			
//			client.setSoTimeout(SOCKET_TIMEOUT);
//			while (readBytes+bufferSize < n){
//				client.setSoTimeout(SOCKET_TIMEOUT);
//				readBytes += inputstream.read(inputPic,readBytes, readBytes + bufferSize);
//			}
//			client.setSoTimeout(SOCKET_TIMEOUT);
//			readBytes += inputstream.read(inputPic,readBytes,n - readBytes);
			/***
			 * Process picture
			 * 
			 * ***/
			Bitmap bmp2 = BitmapFactory.decodeByteArray(inputPic, 0, inputPic.length);
			Mat faceImage = new Mat();
			org.opencv.android.Utils.bitmapToMat(bmp2, faceImage);
			Imgproc.cvtColor(faceImage, faceImage, Imgproc.COLOR_BGR2GRAY);

			if (MainActivity.mFaceRecognizer.predict(faceImage))
			{
				sendAnswer(MainActivity.subject_label, client);
			}
			else
			{
				sendAnswer("Unknown", client);
			}

		}
		// Log.d("DEBUG", "Buffer read:" + read_str);

	}

	public static void addIP(String ip) {
		if (!PeersIP.contains(ip)) {
			PeersIP.add(ip);
		}
	}

	public static void sendAnswer(String answer, Socket client)
			throws IOException {
		byte[] buffer = new byte[100];
		OutputStream out = client.getOutputStream();
		InputStream is = client.getInputStream();

		out.write(answer.getBytes());

	}

	public static String sendPicture(byte[] pic) {

		for (String ip : PeersIP) {
			Socket socket = new Socket();
			int port = PORT;
			Log.d(MainConnectActivity.TAG, "Opening client socket - ");
			try {
				byte[] buffer = new byte[100];
				socket.bind(null);

				socket.connect((new InetSocketAddress(ip, port)),
						SOCKET_TIMEOUT);

				Log.d(MainConnectActivity.TAG,
						"Client socket - " + socket.isConnected());
				OutputStream stream = socket.getOutputStream();

				InputStream is = socket.getInputStream();
				// socket.setSoTimeout(SOCKET_TIMEOUT);
				stream.write("Picture".getBytes());
				socket.setSoTimeout(SOCKET_TIMEOUT);
				int readBytes = is.read(buffer, 0, 100);
				if (readBytes==-1)
					return "Failed";
				String str = new String(buffer, 0, readBytes);
				if (str.equals("OK")) {
					stream.write(String.valueOf(pic.length).getBytes());
					socket.setSoTimeout(SOCKET_TIMEOUT);
					readBytes = is.read(buffer, 0, 100);
					if (readBytes==-1)
						return "Failed";
					str = new String(buffer, 0, readBytes);
					if (str.equals("OK")) {
						
						DataOutputStream dos = new DataOutputStream(stream);
						dos.write(pic);
//						dos.close();
						
//						int sendBytes = 0;
//						
//						while (sendBytes + 1024 < pic.length)
//						{
//							stream.write(buffer,sendBytes,sendBytes + bufferSize);
//							sendBytes +=1024;
//							
//						}
//						stream.write(buffer,sendBytes,pic.length - sendBytes);
						
						socket.setSoTimeout(SOCKET_TIMEOUT);
						readBytes = is.read(buffer, 0, 100); //Answer
						if (readBytes==-1)
							return "Failed";
						Log.d(MainConnectActivity.TAG, new String(buffer));
						return new String(buffer, 0, readBytes);
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				if (socket != null) {
					if (socket.isConnected()) {
						try {
							socket.close();
						} catch (IOException e) {
							// Give up
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	
	public static boolean copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);

            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d(MainConnectActivity.TAG, e.toString());
            return false;
        }
        return true;
    }


}
