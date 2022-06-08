import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class WinStamp extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtPhone;
	private String strPhone;
	int DC=0;

	public int getDC() {
		return DC;
	}
	
	private int SelectMember(String strPhone) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String strCon = "jdbc:mysql://localhost/project_cafe?user=root&password=1234";
		Connection con = DriverManager.getConnection(strCon);
		Statement stmt = con.createStatement();
		String sql = "select * from membertbl where mphone like '%" + strPhone + "%' group by mphone;";

		ResultSet rs = stmt.executeQuery(sql);
		int count=0;
		while(rs.next()) {
//			Members[count][0] = rs.getString("mphone");
//			Members[count][1] = Integer.toString(rs.getInt(2));
			count++;	
		}
		return count;

	}
	
	
	public WinStamp() {
		Font font = new Font("나눔스퀘어_ac", Font.PLAIN, 13);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(WinStamp.class.getResource("/img/coffee01.png")));
		setTitle("스탬프 조회");
		setBounds(100, 100, 378, 128);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		txtPhone = new JTextField();
		txtPhone.setFont(font);
		txtPhone.setBounds(107, 26, 116, 21);
		contentPanel.add(txtPhone);
		txtPhone.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("전화번호 :");
		lblNewLabel.setFont(font);
		lblNewLabel.setBounds(28, 29, 69, 15);
		contentPanel.add(lblNewLabel);
		
		JButton btnSelectMember = new JButton("조회");
		btnSelectMember.setFont(font);
		btnSelectMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strPhone = txtPhone.getText().trim();
				WinShowMember dlg;

				if (!strPhone.equals("")) { // 핸드폰 번호 입력 시
					try {
						if(SelectMember(strPhone) > 0 ) {
							try {
								
								dlg = new WinShowMember(strPhone);
								dlg.setModal(true);
								//setVisible(false);
								dlg.setVisible(true);
								
								DC=3000*dlg.getCouponCount();
								setVisible(false);
								
							} catch (ClassNotFoundException | SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}else {
							JOptionPane.showMessageDialog(null, "적립내역이 없습니다.");							
						}
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {// 회원 전부 조회
					WinMember dlg1;
					try {
						dlg1 = new WinMember();
						dlg1.setModal(true);
						dlg1.setVisible(true);	
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSelectMember.setBounds(246, 25, 89, 23);
		contentPanel.add(btnSelectMember);
	}
}
