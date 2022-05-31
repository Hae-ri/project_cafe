import javax.swing.JPanel;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;

public class Member extends JPanel {
	private JLabel [] lblStamp = new JLabel[12];
	private JLabel lblPhone;
	private JLabel lblTotalStamp;
	private int stamp;
	
	/**
	 * Create the panel.
	 */
	public Member(String strphone, String strstamp) {
		Font font = new Font("나눔스퀘어_ac", Font.PLAIN, 13);
		stamp = Integer.parseInt(strstamp);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		add(panel, BorderLayout.NORTH);
		
		lblPhone = new JLabel("");
		lblPhone.setFont(font);
		lblPhone.setText(strphone);
		panel.add(lblPhone);
		
		JLabel lblNewLabel_1 = new JLabel("님 총");
		lblNewLabel_1.setFont(font);
		panel.add(lblNewLabel_1);
		
		lblTotalStamp = new JLabel("New label");
		lblTotalStamp.setFont(font);
		lblTotalStamp.setText(strstamp);
		panel.add(lblTotalStamp);
		
		JLabel lblNewLabel = new JLabel("개");
		lblNewLabel.setFont(font);
		panel.add(lblNewLabel);
		
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
		

		for(int i=0;i<stamp;i++ ) {
			lblStamp[i].setText("");
			lblStamp[i].setIcon(new ImageIcon("C:\\workspace\\New\\Project_Cafe\\img\\stamp.png"));
		}
		

	}
}
