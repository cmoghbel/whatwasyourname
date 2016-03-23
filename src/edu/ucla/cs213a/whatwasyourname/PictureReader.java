package edu.ucla.cs213a.whatwasyourname;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class PictureReader implements Runnable{

	public void run() {
		InputStream inputstream = null;
		try {
			inputstream = FileTransferService.sock.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true)
		{      
            byte[] buffer = new byte[100];
            try {
				inputstream.read(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            String read_str = new String(buffer);
            Log.d("DEBUG", "Buffer read:"+read_str);
		}
		
	}

}
