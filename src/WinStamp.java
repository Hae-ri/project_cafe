import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class WinStamp extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtPhone;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WinStamp dialog = new WinStamp();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WinStamp() {
		setTitle("스탬프 조회");
		setBounds(100, 100, 378, 128);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtPhone = new JTextField();
		txtPhone.setBounds(107, 26, 116, 21);
		contentPanel.add(txtPhone);
		txtPhone.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("전화번호 :");
		lblNewLabel.setBounds(28, 29, 69, 15);
		contentPanel.add(lblNewLabel);
		
		JButton btnSelectMember = new JButton("조회");
		btnSelectMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strPhone = txtPhone.getText().trim();
				WinShowMember dlg;
				
				if (!strPhone.equals("")) { // 해당 회원 조회
					try {
						dlg = new WinShowMember(strPhone);
						dlg.setModal(true);
						dlg.setVisible(true);
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					
//				} else { // 회원 전부 조회
//					try {
//						dlg = new WinShowMember();
//						dlg.setModal(true);
//						dlg.setVisible(true);
//					} catch (ClassNotFoundException | SQLException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
					
				}
			}
		});
		btnSelectMember.setBounds(246, 25, 89, 23);
		contentPanel.add(btnSelectMember);
	}
}
