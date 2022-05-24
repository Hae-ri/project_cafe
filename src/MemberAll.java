import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTable;

public class MemberAll extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public MemberAll() {
		setLayout(null);
		
		JLabel txtPhone = new JLabel("New label");
		txtPhone.setBounds(53, 34, 221, 15);
		add(txtPhone);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 70, 301, 179);
		add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);

	}
}
