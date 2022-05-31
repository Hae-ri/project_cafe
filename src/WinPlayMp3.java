import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JProgressBar;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class WinPlayMp3 extends JFrame {

	private JPanel contentPane;
	private final JTextField txtSongTitle = new JTextField();
	private final JButton btnLoad = new JButton("불러오기...");
	private final JScrollPane scrollPane = new JScrollPane();
	private final JList lstMusic = new JList();
	private Vector vec;
	private final JButton btnPlay = new JButton("");
	private final JButton btnStop = new JButton("");
	private final JButton btnInfo = new JButton("정보");
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final JLabel lblTotalTime = new JLabel("???초");
	private final JButton btnForeward = new JButton("");
	private final JButton btnBackward = new JButton("");
	JList lstInterval;
	JLabel lblLyrics;
	
	
	Player player;
	Thread playThread;
	Thread resumeThread;
	String fileName;
	FileInputStream fileInputStream;
	BufferedInputStream bufferedInputStream;
	
	long totalLength;
	long pauseLength;
	
	int totalTime;
	Bitstream bitstream;
	String sTitle;
	int index;  // 현재 재생중인 파일의 위치(리스트)
	int seconds;
	int bPause=0;
	int songCount=0;
	boolean bRemain=false;
	boolean bDirect = true;
	int interval=1;
	int cnt = 0;
	Timer timer;
	int previousTime=0;
	DefaultListModel model;
	ListModel modellst;
	
	public WinPlayMp3() {
		//setIconImage(Toolkit.getDefaultToolkit().getImage(WinPlayMp3.class.getResource("/images/mp3icon.png")));
		setForeground(Color.WHITE);
		txtSongTitle.setBounds(12, 22, 619, 21);
		txtSongTitle.setColumns(10);
		initGUI();
		
	}
	private void initGUI() {
		setTitle("음악듣기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1042, 573);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		contentPane.add(txtSongTitle);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblTotalTime.setIcon(new ImageIcon("/img/mp3.PNG"));
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 files", "mp3");
				chooser.setFileFilter(filter);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int ret = chooser.showOpenDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION) {
					String pathName = chooser.getSelectedFile().getParent();
					File path = new File(pathName); // 다 끝나면 이부분으로 대체(아래줄)
					//File path = new File("D:\\JavaTest\\mp3");
					File []fileList = path.listFiles();
					vec = new Vector();
					for(int i=0;i<fileList.length;i++) {
						vec.addElement(fileList[i]);
						songCount++;
					}
					lstMusic.setListData(vec);
				}
			}
		});
		btnLoad.setBounds(643, 21, 97, 23);
		
		contentPane.add(btnLoad);
		scrollPane.setBounds(12, 53, 469, 435);
		
		contentPane.add(scrollPane);
		
		lstMusic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(lstMusic.getSelectedIndex() != -1) {
					txtSongTitle.setText(lstMusic.getSelectedValue().toString());
					
					index = lstMusic.getSelectedIndex();
				}
			}
		});
		
		scrollPane.setViewportView(lstMusic);
		btnPlay.setIcon(new ImageIcon(WinPlayMp3.class.getResource("/img/play.PNG")));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LetsPlay();
			}
		});
		btnPlay.setBounds(557, 53, 50, 50);
		
		contentPane.add(btnPlay);
		btnStop.setIcon(new ImageIcon(WinPlayMp3.class.getResource("/img/pause.PNG")));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				resumeThread = new Thread(runnableResume);
				if(player != null) {
					try {
						//btnStop.setText("재시작");
						ImageIcon icon = new ImageIcon(WinPlayMp3.class.getResource("/img/play.PNG"));
						btnStop.setIcon(icon);
						bPause=0;
						pauseLength = fileInputStream.available();
						player.close();	
						player = null;						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
				}else {
					resumeThread.start();
					bPause=1;
					//btnStop.setText("중지");
					ImageIcon icon = new ImageIcon(WinPlayMp3.class.getResource("/img/pause.PNG"));
					btnStop.setIcon(icon);
					
				}				
			}
		});
		btnStop.setBounds(623, 53, 50, 50);
		
		contentPane.add(btnStop);
		//btnInfo.setIcon(new ImageIcon(WinPlayMp3.class.getResource("/images/musicHelp.png")));
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShowInfo();
			}
		});
		btnInfo.setBounds(869, 53, 57, 48);
		
		contentPane.add(btnInfo);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(493, 109, 433, 379);
		
		contentPane.add(scrollPane_1);
		
		scrollPane_1.setViewportView(lstLyrics);
		lblTotalTime.setBounds(752, 52, 50, 21);
		
		contentPane.add(lblTotalTime);
		btnForeward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(lstMusic.getModel().getSize() > index)
					index = index + 1;
				lstMusic.setSelectedIndex(index);
				txtSongTitle.setText(lstMusic.getSelectedValue().toString());
				ShowTime();
				//=============================================
				playThread = new Thread(runnablePlay);
				if(player != null) {
					player.close();
					playThread.start();
				}
				else {
					playThread.start();	
				}	
			}
		});
		btnForeward.setIcon(new ImageIcon(WinPlayMp3.class.getResource("/img/fore.PNG")));
		btnForeward.setBounds(689, 54, 50, 50);
		
		contentPane.add(btnForeward);
		btnBackward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(index > 0)
					index = index - 1;
				lstMusic.setSelectedIndex(index);
				txtSongTitle.setText(lstMusic.getSelectedValue().toString());
				ShowTime();
				//=============================================
				playThread = new Thread(runnablePlay);
				if(player != null) {
					player.close();
					playThread.start();
				}
				else {
					playThread.start();	
				}				
			}
		});
		btnBackward.setIcon(new ImageIcon(WinPlayMp3.class.getResource("/img/back.PNG")));
		btnBackward.setBounds(491, 53, 50, 50);
		
		contentPane.add(btnBackward);
		progressBar.setStringPainted(true);
		progressBar.setBounds(752, 22, 174, 21);
		
		contentPane.add(progressBar);
		lblRemain.setForeground(Color.RED);
		lblRemain.setFont(new Font("굴림", Font.BOLD, 15));
		lblRemain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(bRemain)
					bRemain=false;
				else
					bRemain=true;
			}
		});
		lblRemain.setBounds(752, 83, 65, 21);
		
		contentPane.add(lblRemain);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(936, 109, 48, 383);
		contentPane.add(scrollPane_2);
		
		model = new DefaultListModel();
		lstInterval = new JList(model);
		lstInterval.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane_2.setViewportView(lstInterval);
		
		JButton btnIntervalTime = new JButton("간격저장");
		btnIntervalTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {						
				int curTime = seconds-previousTime;
				model.addElement(curTime);
				lstInterval.setModel(model);
				previousTime=seconds;
			}
		});
		btnIntervalTime.setBounds(936, 34, 93, 23);
		contentPane.add(btnIntervalTime);
		
		lblLyrics = new JLabel("가사");
		lblLyrics.setForeground(Color.RED);
		lblLyrics.setFont(new Font("굴림", Font.BOLD, 20));
		lblLyrics.setBounds(12, 498, 914, 40);
		contentPane.add(lblLyrics);
		
		JButton btnDBIntervalTime = new JButton("데이터저장");
		btnDBIntervalTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InsertIntervalTimes();
			}
		});
		btnDBIntervalTime.setBounds(936, 80, 93, 23);
		contentPane.add(btnDBIntervalTime);
	}
	
	protected void InsertIntervalTimes() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String temp = "jdbc:mysql://localhost/sampledb?user=root&password=1234";
			Connection con = DriverManager.getConnection(temp);
			Statement stmt = con.createStatement();
			String strInterval = "";
			
			modellst = lstInterval.getModel();
			int len =modellst.getSize();
			System.out.println(len);
			for(int i=0;i<len;i++) {
				lstInterval.setSelectedIndex(i);
				
				if(i<len-1)
					strInterval = strInterval + lstInterval.getSelectedValue()+",";
				else
					strInterval = strInterval + lstInterval.getSelectedValue();
			}
			String id = lstMusic.getSelectedValue().toString();			
			String sql ="insert into musicTBL values('"+id.substring(fileName.lastIndexOf("\\")+1)+"','"+strInterval+"')";
			if(stmt.executeUpdate(sql)>0)
				System.out.println("success insert!!");
			stmt.close();
			con.close();
						
		} catch (SQLException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	protected void ShowInfo() {
		// TODO Auto-generated method stub
		String decoding = "ISO-8859-1";
		String encoding = "EUC-KR";
		File file = new File(txtSongTitle.getText().trim());
		MP3File mp3=null;
		try {
			mp3 = (MP3File) AudioFileIO.read(file);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException e1) {
			e1.printStackTrace();
		}
		//  ID3v24Tag tag24 = mp3.getID3v2TagAsv24();
		AbstractID3v2Tag tag2 = mp3.getID3v2Tag();
		
						
		Tag tag = mp3.getTag();
		String title = tag.getFirst(FieldKey.TITLE);
		String artist = tag.getFirst(FieldKey.ARTIST);
		String album = tag.getFirst(FieldKey.ALBUM);
		String year = tag.getFirst(FieldKey.DISC_TOTAL);
		String genre = tag.getFirst(FieldKey.GENRE);
		String lyrics = tag.getFirst(FieldKey.LYRICS);
		try {
			long start = mp3.getMP3StartByte(file);
			//System.out.println("시작위치:" + start);
		} catch (InvalidAudioFrameException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		System.out.println("Tag : " + tag);
		System.out.println("Song Name : " + title);
		System.out.println("Artist : " + artist);
		System.out.println("Album : " + album);
		System.out.println("Year : " + year);
		System.out.println("Genre : " + genre);
		*/
		sTitle = title;
		//textArea.setText(lyrics);
		//textArea.setCaretPosition(0);
		Vector vLyrics = new Vector();
		
		
		String strLyrics[] = lyrics.split("\n");
		for(int i=0;i<strLyrics.length;i++) {
			if(!strLyrics[i].trim().equals(""))
				vLyrics.addElement(strLyrics[i].trim());
		}
		lstLyrics.setListData(vLyrics);
	}
	
	protected void LetsPlay()  {		
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()/songCount*index);
		lstInterval.removeAll();			
		
		bPause = 1;
		index = lstMusic.getSelectedIndex(); 
		ShowTime();
		
		playThread = new Thread(runnablePlay);
		if(player != null) {
			player.close();
			playThread.start();
		}
		else {
			playThread.start();	
			
			seconds = 0;
			new Timer().schedule(task, 0, 1000);  //시간(진행상황)을 표시하기 위한 타이머
			timer = new Timer();
			timer.schedule(taskInterval, 0, 10); //자막을 보여주기 위한 타이머
		}	
	}
	protected void ShowTime() {
		//============================================			
		Header h = null;
    	FileInputStream file = null;	        	
    	try {
    	    file = new FileInputStream(txtSongTitle.getText().trim());
    	    bitstream = new Bitstream(file);
    	    h = bitstream.readFrame();
    	} catch (FileNotFoundException | BitstreamException ex) {
    		ex.printStackTrace();
    	}
    	
		MP3File mp3=null;
		File file1 = null;
		try {
			file1 = new File(txtSongTitle.getText().trim());
			mp3 = (MP3File) AudioFileIO.read(file1);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	long tn = 0;
    	try {
    	    tn = file.getChannel().size();
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    	
    	try {
			totalTime =(int)Math.floor(h.total_ms((int)(tn-mp3.getMP3StartByte(file1)))/1000);
		} catch (InvalidAudioFrameException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	lblTotalTime.setText(totalTime + "초");	
    	seconds = 0;
		//============================================
    	ShowInfo();
    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String temp = "jdbc:mysql://localhost/sampledb?user=root&password=1234";
			Connection con = DriverManager.getConnection(temp);
			Statement stmt = con.createStatement();
			
			String id = txtSongTitle.getText().trim();
			id = id.substring(id.lastIndexOf("\\")+1);
			//System.out.println(id);			
			//id = id.replaceAll("\\\\","\\\\\\\\");
			String sql ="select * from musicTBL where id='"+id+"'";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			String strInterval="";
			
			if(rs.next()) {
				strInterval = rs.getString(2);
				String arrInterval[] = strInterval.split(",");
				model.removeAllElements();
				for(int i=0;i<arrInterval.length;i++) {
					model.addElement(arrInterval[i]);
				}
				lstInterval.setModel(model);
			}
			stmt.close();
			con.close();
						
		} catch (ClassNotFoundException | SQLException e1) {
			// TODO Auto-generated catch block
			((Throwable) e1).printStackTrace();
		}
	}

	Runnable runnablePlay = new Runnable() {
		public void run() {
			fileName = txtSongTitle.getText().trim();
			//setTitle(fileName + " 재생 중...");
			try {
				fileInputStream = new FileInputStream(fileName);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				player = new Player(bufferedInputStream);
				
				totalLength = fileInputStream.available();
				player.play();				
			} catch (IOException | JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	};
	
	Runnable runnableResume = new Runnable() {
		public void run() {
			fileName = txtSongTitle.getText().trim();
			try {
				fileInputStream = new FileInputStream(fileName);
				bufferedInputStream = new BufferedInputStream(fileInputStream);
				player = new Player(bufferedInputStream);
				
				fileInputStream.skip(totalLength - pauseLength);
				player.play();	
			} catch (IOException | JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	};	
	TimerTask taskInterval = new TimerTask() {
		@Override
		public void run() {
			lstInterval.setSelectedIndex(cnt);
			lstLyrics.setSelectedIndex(cnt);
			lblLyrics.setText((String) lstLyrics.getSelectedValue());
			interval = Integer.parseInt(lstInterval.getSelectedValue().toString());
			System.out.println(interval);
			cnt++;			
			try {
				Thread.sleep(interval*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			if(bPause==1) {
				if(progressBar.getValue()==100) {				
					seconds = 0;
					
					
					if(lstMusic.getModel().getSize() > index)  // 하나의 노래가 종료되면 1초 후 다음곡 재생
						index = index + 1;					
					lstMusic.setSelectedIndex(index);
					txtSongTitle.setText(lstMusic.getSelectedValue().toString());
					try {
						Thread.sleep(1000); //1초 쉰 후
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
					//player.close();	
					//player = null;
					lblRemain.setText((totalTime - seconds) + "초 남음");
					
					LetsPlay();					
				}
				progressBar.setValue(100 * ++seconds / totalTime );
				
				
				if(bRemain) {   // 남은 시간 표시 -2:06
					int t = totalTime - seconds;
					int m = t/60;
					int s = t%60;
					String strTime = "-" + m + ":";
					if(s<10)
						strTime = strTime + "0"+s;
					else
						strTime = strTime + s;
					lblRemain.setText(strTime);
					lblRemain.setForeground(Color.RED);   
				}else {   // 재생된 시간  1:23
					int m = seconds/60;
					int s = seconds%60;
					String strTime = m + ":";
					if(s<10)
						strTime = strTime + "0"+s;
					else
						strTime = strTime + s;
					lblRemain.setText(strTime);
					lblRemain.setForeground(Color.BLUE);
				}
				
				String strTitle="";
				if(seconds%60==0)					
					bDirect = !bDirect;   // 왼쪽/오른쪽 방향으로 이동할 것인지 결정
				if(bDirect) {
					for(int i=0;i<seconds%60;i++)
						strTitle = strTitle + " ";	
					if(sTitle.trim().equals(""))	// mp3 Tag에 정보가 없다면 파일 이름에서 가져오고						
						strTitle = strTitle + fileName.substring(fileName.lastIndexOf("\\")+1) + " 재생중...";
					else
						strTitle = strTitle + "[" + sTitle + "] 재생중..."; //있다면
					setTitle(strTitle);
				}else {
					for(int i=0;i<60-seconds%60;i++)
						strTitle = strTitle + " ";	
					if(sTitle.trim().equals(""))							
						strTitle = strTitle + fileName.substring(fileName.lastIndexOf("\\")+1) + " 재생중...";
					else
						strTitle = strTitle + "[" + sTitle + "] 재생중...";
					setTitle(strTitle);
				}
				lstMusic.setSelectionForeground(Color.BLUE);
			}
		}
		
	};
	
	private final JProgressBar progressBar = new JProgressBar();
	private final JLabel lblRemain = new JLabel("???초");
	private final JList lstLyrics = new JList();
}
