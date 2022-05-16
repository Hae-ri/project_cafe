import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class WinMain extends JFrame {

	private JPanel contentPane;
	private Clip clip;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WinMain frame = new WinMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WinMain() {
		setTitle("CAFE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 509, 286);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnuAdmin = new JMenu("관리자");
		menuBar.add(mnuAdmin);
		
		JMenuItem mnuSales = new JMenuItem("매출");
		mnuAdmin.add(mnuSales);
		
		JMenuItem mnuStock = new JMenuItem("재고");
		mnuAdmin.add(mnuStock);
		
		JMenuItem mnuMenu = new JMenuItem("메뉴");
		mnuMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMenu dlg = new WinMenu();
				//dlg.setModal(true);
				dlg.setVisible(true);
				
			}
		});

		mnuAdmin.add(mnuMenu);
		
		JMenuItem mnuMember = new JMenuItem("회원");
		mnuAdmin.add(mnuMember);
		
		JMenu mnuOrder = new JMenu("주문");
		menuBar.add(mnuOrder);
		
		JMenuItem mnuInsertOrder = new JMenuItem("주문처리");
		mnuInsertOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinOrder dlg;
				try {
					dlg = new WinOrder();
					dlg.setModal(true);
					dlg.setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		mnuOrder.add(mnuInsertOrder);
		
		JMenuItem mnuViewOrder = new JMenuItem("주문확인");
		mnuOrder.add(mnuViewOrder);
		
		JMenu mnuQnA = new JMenu("도움말");
		menuBar.add(mnuQnA);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		
		/*
		JButton btnMusicPlay = new JButton("음악 재생");
		btnMusicPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel.setText("Play .. ");
				
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("음악파일", "wav");
				chooser.setFileFilter(filter);
				int ret = chooser.showOpenDialog(null);
				String pathName = chooser.getSelectedFile().getPath();
				
				if (ret == JFileChooser.APPROVE_OPTION) {
					try {
						clip = AudioSystem.getClip();
						File audioFile = new File(pathName);
						//File audioFile = new File("/audio/1.wav");
						AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
						clip.open(audioStream);
						clip.start();
						clip.loop(100);
					} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		panel_1.add(btnMusicPlay);
		
		JButton btnMusicStop = new JButton("음악 정지");
		btnMusicStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel.setText("화...홧팅!");
				clip.stop();
			}
		});
		panel_1.add(btnMusicStop);
		*/
		
		
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
	}
}
