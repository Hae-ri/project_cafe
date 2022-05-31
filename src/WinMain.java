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
import javax.swing.JDialog;
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
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(WinMain.class.getResource("/img/coffee01.png")));
		setTitle("CAFE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 505, 382);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnuAdmin = new JMenu("관리자");
		mnuAdmin.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		menuBar.add(mnuAdmin);
		
		JMenuItem mnuSales = new JMenuItem("매출");
		mnuSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinSales dlg = new WinSales();
				dlg.setVisible(true);
				
			}
		});
		mnuSales.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		mnuAdmin.add(mnuSales);
		
		JMenuItem mnuMenu = new JMenuItem("메뉴");
		mnuMenu.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		mnuMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinMenu dlg = new WinMenu();
				//dlg.setModal(true);
				dlg.setVisible(true);
				
			}
		});

		mnuAdmin.add(mnuMenu);
		
		JMenuItem mnuMember = new JMenuItem("회원");
		mnuMember.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		mnuAdmin.add(mnuMember);
		
		JMenu mnuOrder = new JMenu("주문");
		mnuOrder.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		menuBar.add(mnuOrder);
		
		JMenuItem mnuInsertOrder = new JMenuItem("주문처리");
		mnuInsertOrder.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		mnuInsertOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinOrder dlg;
				try {
					dlg = new WinOrder();
					//dlg.setModal(true);
					dlg.setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		mnuOrder.add(mnuInsertOrder);
		
		JMenuItem mnuViewOrder = new JMenuItem("주문확인");
		mnuViewOrder.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
		mnuOrder.add(mnuViewOrder);
		
		JMenu mnuQnA = new JMenu("도움말");
		mnuQnA.setFont(new Font("나눔스퀘어_ac", Font.PLAIN, 12));
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
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(panel_1, popupMenu);
		
		JMenuItem mntmMP3 = new JMenuItem("Music");
		mntmMP3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinPlayMp3 dlg = new WinPlayMp3();
				dlg.setVisible(true);
			}
		});

		popupMenu.add(mntmMP3);
		panel_1.setLayout(null);
		
		JButton btnInsertOrder = new JButton("주문 처리");
		btnInsertOrder.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 24));
		btnInsertOrder.setBounds(31, 79, 130, 120);
		panel_1.add(btnInsertOrder);
		
		JButton btnSales = new JButton("매출 확인");
		btnSales.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 24));
		btnSales.setBounds(173, 79, 130, 120);
		panel_1.add(btnSales);
		
		JButton btnMember = new JButton("적립 확인");
		btnMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WinStamp dlg= new WinStamp();
				dlg.setVisible(true);
			}
		});
		btnMember.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 24));
		btnMember.setBounds(320, 79, 130, 120);
		panel_1.add(btnMember);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.SOUTH);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
