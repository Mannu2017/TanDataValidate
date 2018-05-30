package main;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class WorkPage extends JFrame{

	@SuppressWarnings("unused")
	private String[] args;
	private JTextField textField;
	private JTable dataview;
	private JLabel label;
	private JButton btnSave;
	private String pat;
	DefaultTableModel model;
	private JTextField inwardno;
	private JLabel lblNewLabel;
	private JLabel lblAckNo;
	private JTextField ackno;
	private JTextField confack;
	private JLabel lblConfAckNo;
	private JButton btnClose;
	
	public WorkPage() throws IOException {
		File s=new File("C:\\temp\\");
		if(!s.exists()) {
			s.mkdir();
		}
		FileUtils.cleanDirectory(s);
		
		setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().setLayout(null);
		
		label = new JLabel("");
		label.setBounds(0, 0, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-310, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100);
		getContentPane().add(label);
		
		JButton btnBrouse = new JButton("Browse");
		btnBrouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Choose a directory to save your file: ");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					File s1=new File(selectedFile.getAbsolutePath()+"\\done\\");
					if(!s1.exists()) {
						s1.mkdir();
					}
					pat=selectedFile.getAbsolutePath();
					textField.setText(selectedFile.getAbsolutePath());
					model=(DefaultTableModel) dataview.getModel();
					model.setRowCount(0);
					File f=new File(pat);
					File[] fils=f.listFiles();
					Object[] row=new Object[1];
					for(File ff : fils) {
						if(ff.getName().endsWith(".pdf") || ff.getName().endsWith(".PDF")) {
							row[0]=ff.getName();
							model.addRow(row);
						}
					}
					dataview.setRowSelectionInterval(0, 0);
					DataProcess(model.getValueAt(dataview.getSelectedRow(), 0),pat);
				}
			}
		});
		btnBrouse.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-120, 11, 89, 23);
		getContentPane().add(btnBrouse);
		
		textField = new JTextField();
		textField.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-295, 12, 148, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-280, 201, 247, 272);
		getContentPane().add(scrollPane);
		
		dataview = new JTable();
		dataview.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"File Name"
			}
		));
		scrollPane.setViewportView(dataview);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(inwardno.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter Inward No");
					inwardno.requestFocus();
				} else if (ackno.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter Ack No");
					ackno.requestFocus();
				} else if (confack.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter Conf Ack No");
					confack.requestFocus();
				} else {
					if(!confack.getText().equals(ackno.getText())) {
						JOptionPane.showMessageDialog(null, "Ack No and Conf Ack No not match");
					} else {
						File fi=new File(pat+"\\"+model.getValueAt(dataview.getSelectedRow(), 0));
						fi.renameTo(new File(pat+"\\done\\"+inwardno.getText()+"_"+confack.getText()+".pdf"));
						inwardno.setText("");
						ackno.setText("");
						confack.setText("");
						inwardno.requestFocus();
						model.removeRow(dataview.getSelectedRow());
						dataview.setRowSelectionInterval(0, 0);
						DataProcess(model.getValueAt(dataview.getSelectedRow(), 0),pat);
					}
				}
			}
		});
		btnSave.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-275, 136, 89, 23);
		getContentPane().add(btnSave);
		
		inwardno = new JTextField();
		inwardno.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				char vchar = event.getKeyChar();
		        if ((!Character.isDigit(vchar)) || (vchar == '\b') || (vchar == '') || 
		          (inwardno.getText().length() == 10)) {
		          event.consume();
		        }
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					if(inwardno.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter Inward No");
						inwardno.requestFocus();
					} else {
						ackno.requestFocus();
					}
					
				}
			}
		});
		inwardno.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-210, 43, 86, 20);
		getContentPane().add(inwardno);
		inwardno.setColumns(10);
		
		lblNewLabel = new JLabel("Inward No:");
		lblNewLabel.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-290, 46, 72, 14);
		getContentPane().add(lblNewLabel);
		
		lblAckNo = new JLabel("Ack No:");
		lblAckNo.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-290, 74, 72, 14);
		getContentPane().add(lblAckNo);
		
		ackno = new JTextField();
		ackno.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				char vchar = event.getKeyChar();
		        if ((!Character.isDigit(vchar)) || (vchar == '\b') || (vchar == '') || 
		          (ackno.getText().length() == 25)) {
		          event.consume();
		        }
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					if(ackno.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter ackno");
					} else {
					confack.requestFocus();
					}
				}
			}
		});
		ackno.setColumns(10);
		ackno.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-210, 71, 135, 20);
		getContentPane().add(ackno);
		
		confack = new JTextField();
		confack.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				char vchar = event.getKeyChar();
		        if ((!Character.isDigit(vchar)) || (vchar == '\b') || (vchar == '') || 
		          (confack.getText().length() == 25)) {
		          event.consume();
		        }
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					if(inwardno.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter Inward No");
						inwardno.requestFocus();
					} else if (ackno.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter Ack No");
						ackno.requestFocus();
					} else if (confack.getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Please enter Conf Ack No");
						confack.requestFocus();
					} else {
						if(!confack.getText().equals(ackno.getText())) {
							JOptionPane.showMessageDialog(null, "Ack No and Conf Ack No not match");
						} else {
							File fi=new File(pat+"\\"+model.getValueAt(dataview.getSelectedRow(), 0));
							fi.renameTo(new File(pat+"\\done\\"+inwardno.getText()+"_"+confack.getText()+".pdf"));
							inwardno.setText("");
							ackno.setText("");
							confack.setText("");
							inwardno.requestFocus();
							model.removeRow(dataview.getSelectedRow());
							dataview.setRowSelectionInterval(0, 0);
							DataProcess(model.getValueAt(dataview.getSelectedRow(), 0),pat);
						}
					}
				}
			}
		});
		confack.setColumns(10);
		confack.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-210, 99, 135, 20);
		getContentPane().add(confack);
		
		lblConfAckNo = new JLabel("Conf Ack No:");
		lblConfAckNo.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-290, 102, 72, 14);
		getContentPane().add(lblConfAckNo);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnClose.setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()-175, 136, 89, 23);
		getContentPane().add(btnClose);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	protected void DataProcess(Object valueAt, String pat2) {
		System.out.println(pat2+"\\"+valueAt.toString());
		try {
			PDDocument document = PDDocument.load(new File(pat2+"\\"+valueAt.toString()));
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 100, ImageType.RGB);
			ImageIOUtil.writeImage(bim, "C:\\temp\\"+valueAt.toString()+".png", 100);
			document.close();
			Image img=ImageIO.read(new File("C:\\temp\\"+valueAt.toString()+".png"));
			Image image=img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(image));
			
		} catch (InvalidPasswordException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void setArgs(String[] args) {
		this.args=args;
	}
}
