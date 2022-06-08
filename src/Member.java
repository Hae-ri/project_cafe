import javax.swing.JPanel;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.Font;

public class Member extends JPanel {
	private JLabel [] lblStamp = new JLabel[12];
	private JLabel lblPhone;
	private JLabel lblTotalStamp;
	private int coupon;
	private String strCoupon;
	private JButton btnCoupon;
	
	public int cnt=0;
	/**
	 * Create the panel.
	 */
	int getCnt() {
		return cnt;
	}
	
	public Member(String strphone, String strstamp) {
		Font font = new Font("나눔스퀘어_ac", Font.PLAIN, 13);
		int stamp = Integer.parseInt(strstamp);
		int coupon = stamp/12;
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblPhone = new JLabel("");
		lblPhone.setFont(font);
		lblPhone.setText(strphone);
		panel.add(lblPhone);
		
		JLabel lblNewLabel_1 = new JLabel("님 총");
		lblNewLabel_1.setFont(font);
		panel.add(lblNewLabel_1);
		
		lblTotalStamp = new JLabel("New label");
		lblTotalStamp.setFont(font);
		lblTotalStamp.setText(strstamp+"("+coupon+")");
		panel.add(lblTotalStamp);
		
		JLabel lblNewLabel = new JLabel("개");
		lblNewLabel.setFont(font);
		panel.add(lblNewLabel);

		JPanel panel_btn = new JPanel();
		panel_btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
		panel.add(panel_btn);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(4, 3, 20, 0));
		
		for (int i=0;i<lblStamp.length;i++) {
	        lblStamp[i] = new JLabel(Integer.toString(i+1));
	        lblStamp[i].setHorizontalAlignment(SwingConstants.CENTER); 
	        int x=i*60;
	        for(int j=0;j<4;j++) {
	        	int y=j*60;
	        	lblStamp[i].setBounds(x,y,50,50);
	        	panel_1.add(lblStamp[i]); 
	        }
		}
		

		if (stamp < 12) {
			for(int i=0;i<stamp;i++ ) {
				lblStamp[i].setText("");
				lblStamp[i].setIcon(new ImageIcon("C:\\workspace\\New\\Project_Cafe\\img\\stamp.png"));
				
			}
		}else {
			Component[] componentList = panel_btn.getComponents();
			for(Component c: componentList) {
				if(c instanceof JButton)
					panel_btn.remove(c);
			}
			panel_btn.revalidate();
			panel_btn.repaint();
			
			stamp = stamp%12; 
			//coupon = stamp/12; // 스탬프 12개당 쿠폰 1개
			btnCoupon = new JButton("사용");
			panel_btn.add(btnCoupon);
			btnCoupon.setFont(new Font("나눔스퀘어 Bold", Font.PLAIN, 12));
			btnCoupon.addActionListener(new ActionListener() { // 음료 버튼 클릭
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// 쿠폰 (*) 사용하시겠습니까?
					int result= JOptionPane.showConfirmDialog(null, "쿠폰을 사용하시겠습니까?","스탬프 사용",JOptionPane.YES_NO_OPTION);
					// 예-몇 개 쓸 것인지 물어보고,
					if(result == JOptionPane.YES_OPTION) {
						// 해당 핸드폰 번호 날려주고(-12),사용하는 쿠폰 개수, *할인금액 3000원 입력, 창 닫기 --> WinStamp에서 받아서 다시 WinOrder로 전달
						strCoupon = JOptionPane.showInputDialog("("+coupon+"개) 사용가능");
						cnt = Integer.parseInt(strCoupon);
						setVisible(false);
					}else
						// 아니오-창 닫기
						setVisible(false);
					}
				});
			
			for(int i=0;i<stamp;i++ ) {
				lblStamp[i].setText("");
				lblStamp[i].setIcon(new ImageIcon("C:\\workspace\\New\\Project_Cafe\\img\\stamp.png"));
			}
		}
		

	}
	
	public int getCoupon() {
		return coupon;
	}
}
