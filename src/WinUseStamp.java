import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class WinUseStamp extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private int stamp=0;
	private String strphone;
	private String strCoupon;
	private Vector<String> use;
	private int cnt=0; // 사용 스탬프 개수
	
	public  Vector<String> getStamp() {
			Vector<String> use = new Vector<>();
			use.add(strphone);
			use.add(strCoupon);
		return use;
	}
	
	public WinUseStamp(String strphone, String strCoupon) {
		this.strphone = strphone;
		this.strCoupon = strCoupon;
		
		setTitle("스탬프사용");
		setBounds(100, 100, 310, 110);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblPhone = new JLabel("");
			lblPhone.setFont(new Font("굴림", Font.BOLD, 14));
			contentPanel.add(lblPhone);
			lblPhone.setText(strphone);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("님");
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblCoupon = new JLabel("");
			lblCoupon.setFont(new Font("굴림", Font.BOLD, 14));
			lblCoupon.setForeground(Color.RED);
			contentPanel.add(lblCoupon);
			lblCoupon.setText(strCoupon);
		}
		{
			JLabel lblNewLabel = new JLabel("장 사용");
			contentPanel.add(lblNewLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("확인");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("취소");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
