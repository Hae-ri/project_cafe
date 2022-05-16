import java.awt.Color;

import com.thehowtotutorial.splashscreen.JSplash;

public class Splash {

	public static void main(String[] args) throws InterruptedException {

		JSplash splash = new JSplash(Splash.class.getResource("/img/main.jpg"),true,true,false,"Ver1.0",null,Color.BLACK,Color.BLACK);

		splash.splashOn();
		for (int i=1;i<=100;i++) {
			if(i<=20) 
				splash.setProgress(i,"오늘도 .. ");
			else if(i<=40) 
				splash.setProgress(i,"향긋한 커피와 ..");
			else if(i<=60) 
				splash.setProgress(i,"함께 ..");
			else if(i<=100) 
				splash.setProgress(i,"화이팅 ..!");
			Thread.sleep(10);
		}
		splash.splashOff();
		
		WinMain win = new WinMain();
		win.setVisible(true);
		

	}

}
