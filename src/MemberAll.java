import javax.swing.JPanel;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class MemberAll extends JPanel {
	private JTable table;
	private DefaultTableModel dtm;
	private Vector columnNames;
	private Vector stamplist;

	/**
	 * Create the panel.
	 */
	public MemberAll(String strphone, String strstamp, String strdate) {
		Font font = new Font("나눔스퀘어_ac", Font.PLAIN, 13);
		setLayout(null);
		
		JLabel txtPhone = new JLabel("New label");
		txtPhone.setBounds(53, 34, 221, 15);
		add(txtPhone);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 70, 301, 179);
		add(scrollPane);
		
		table = new JTable();
		
		
		columnNames = new Vector<>();
		columnNames.add("적립날짜");
		columnNames.add("개수");
		columnNames.add("지불"); // 현금, 카드

		stamplist = new Vector<>();
		
		
		dtm = new DefaultTableModel(stamplist,columnNames);
		table = new JTable(dtm);
		table.setFont(font);
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(10);
		
		
		table.setAutoCreateRowSorter(true);
		
		scrollPane.setViewportView(table);
		
		JLabel lblNewLabel = new JLabel("님");
		lblNewLabel.setBounds(277, 34, 57, 15);
		add(lblNewLabel);

	}
}
