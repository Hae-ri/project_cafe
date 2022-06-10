import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class WinUseStamp extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtStamp;
	private int stamp=0;
	private String strphone;
	private String strCoupon;
	private Vector<String> use;
	private int cnt=0; // 사용 스탬프 개수
	
	public  Vector<String> getStamp() {
			Vector<String> use = new Vector<>();
			strCoupon = Integer.toString(cnt);
			use.add(strphone);
			use.add(strCoupon);
		return use;
	}
	
	public WinUseStamp(String strphone) {
		this.strphone = strphone;
		setTitle("스탬프사용");
		setBounds(100, 100, 310, 110);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			txtStamp = new JTextField();
			contentPanel.add(txtStamp);
			txtStamp.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("확인");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						stamp = Integer.parseInt(txtStamp.getText());
						setVisible(false);
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
