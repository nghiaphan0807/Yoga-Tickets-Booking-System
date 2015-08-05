package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.xml.JasperPrintFactory;
import net.sf.jasperreports.view.JasperViewer;
import utiliti.Utilities;
import constant.Constant;

public class BookingView extends JFrame {
	
	private JButton btnStudio1;
	private JButton btnStudio2;
	private JButton btnStudio3;
	
	private JLabel lblLogo;
	private JLabel lblStudioName;
	
	private JLabel lblDate;
	private JTextField txtDate;
	
	private JLabel lblTime;
	private JTextField txtTime;
	
	private JLabel lblClass;
	private JTextField txtClass;
	
	private JLabel lblMemberType;
	private JComboBox cboMemberType;
	
	private JButton btnReset;
	private JButton btnClear;
	
	private JPanel pnlStudio;
	private JPanel pnlYogaInformation;
	private JPanel pnlMats;
	
	private JLabel lblMatsForStudio1[];
	private JLabel lblMatsForStudio2[];
	private JLabel lblMatsForStudio3[];
	
	private int labelWidth = 40;
	private int labelHeight = 25;
	private int textFieldWidth = 220;
	private int textFieldHeight = 25;
	
	JLabel lblLastComponent = new JLabel();
	JDialog dlgAlreadyBooked;
	JDialog dlgPleaseEnterInput;
	JDialog dlgRemoveMat;
	JDialog dlgRemoveAllMats;

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 2676165373505230462L;

	public BookingView() {
		initGUI();
		registerListeners();
	}

	private void initGUI() {		
		this.setSize(1060, 560);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(Constant.BACKGROUND_COLOR_WHITE);
		
		initStudioPanel();
		this.add(pnlStudio, new GridBagConstraints(0, 0, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 20), 0, 0));
		
		initYogaInformationPanel();
		this.add(pnlYogaInformation, new GridBagConstraints(0, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 65, 20), 0, 0));
		
		initLogoAndMatsPanel();
		this.add(pnlMats, new GridBagConstraints(1, 0, 10, 2, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
	}

	private void initLogoAndMatsPanel() {
		pnlMats = new JPanel();
		pnlMats.setLayout(new GridBagLayout());
		pnlMats.setBackground(Constant.BACKGROUND_COLOR_WHITE);
				
		initLogoComponent();
		initStudioNameComponent();
		initMatsForStudio1();
	}

	private void initMatsForStudio1() {
		if(lblMatsForStudio1 == null) {
			lblMatsForStudio1 = new JLabel[70];
			for(int i = 0; i < 70; i++) {
				lblMatsForStudio1[i] = new JLabel(Utilities.parseString(i + 1));
				lblMatsForStudio1[i].setBackground(Constant.BACKGROUND_COLOR_WHITE);
			}
		}
		
		if(lblMatsForStudio2 != null) {
			for(int i = 0; i < lblMatsForStudio2.length; i++) {
				pnlMats.remove(lblMatsForStudio2[i]);				
			}
		}
		
		if(lblMatsForStudio3 != null) {
			for(int i = 0; i < lblMatsForStudio3.length; i++) {
				pnlMats.remove(lblMatsForStudio3[i]);				
			}
		}
		
		pnlMats.remove(lblLastComponent);
		
		int x = 0, y = 1;
		for(int i = 0; i < 70; i++) {			
			lblMatsForStudio1[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));				
			lblMatsForStudio1[i].setOpaque(true);
			lblMatsForStudio1[i].setHorizontalAlignment(JLabel.CENTER);
			lblMatsForStudio1[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					chooseMats(e);
				}
			});
			Utilities.setCompoenentSize(lblMatsForStudio1[i], 65, 32);
			pnlMats.add(lblMatsForStudio1[i], new GridBagConstraints(x, y, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 5, 5), 0, 0));
			x++;			
			if(x % 10 == 0) {
				x = 0;
				y++;
			} else if(i == lblMatsForStudio1.length - 1) {
				y++;
			}	
		}		
		
		lblLastComponent = new JLabel();
		Utilities.setCompoenentSize(lblLastComponent, 80, 200);
		pnlMats.add(lblLastComponent, new GridBagConstraints(1, y, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
		
		pnlMats.validate();
		pnlMats.repaint();
	}

	protected void chooseMats(MouseEvent e) {
		JLabel lblSelectedMat = (JLabel) e.getSource();
		
		if((lblSelectedMat.getBackground() == Constant.CENTURYON
				|| (lblSelectedMat.getBackground() == Constant.DIAMOND))
					&& getCursor().equals(Cursor.getPredefinedCursor(HAND_CURSOR))) {
			showRemoveMatDialog(lblSelectedMat);
		} else if(lblSelectedMat.getBackground() == Constant.CENTURYON
				|| (lblSelectedMat.getBackground() == Constant.DIAMOND)) {
			showAlreadyBookedDialog();
		} else if(lblSelectedMat.getBackground() == Constant.NORMAL) {
			showPrintDialog(e);
		}
	}
	
	private void showRemoveMatDialog(final JLabel removingMat) {
		dlgRemoveMat = new JDialog(this, "Remove mat", true);
		dlgRemoveMat.setSize(280, 170);
		dlgRemoveMat.setLocationRelativeTo(this);		
		dlgRemoveMat.setLayout(new GridBagLayout());
		
		JLabel lblExistingMessage = new JLabel("Do you really want to remove this mat?");
		lblExistingMessage.setHorizontalAlignment(JLabel.CENTER);
		Utilities.setCompoenentSize(lblExistingMessage, 250, 25);
		dlgRemoveMat.add(lblExistingMessage, new GridBagConstraints(0, 0, 2, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(0, 0, 20, 0), 0, 0));		
		
		JButton btnOK = new JButton("OK");
//		btnOK.setFocusPainted(false);
		Utilities.setCompoenentSize(btnOK, 80, 25);
		dlgRemoveMat.add(btnOK, new GridBagConstraints(0, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 35, 0, 0), 0, 0));
		
		btnOK.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dlgRemoveMat.dispose();
				removingMat.setBackground(Constant.NORMAL);
				setCursor(Cursor.DEFAULT_CURSOR);
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
//		btnCancel.setFocusPainted(false);
		Utilities.setCompoenentSize(btnCancel, 80, 25);
		dlgRemoveMat.add(btnCancel, new GridBagConstraints(1, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
		btnCancel.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dlgRemoveMat.dispose();
			}
		});
		
		dlgRemoveMat.setVisible(true);
	}

	private void showAlreadyBookedDialog() {
		dlgAlreadyBooked = new JDialog(this, "Existing Mat", true);
		dlgAlreadyBooked.setSize(280, 170);
		dlgAlreadyBooked.setLocationRelativeTo(this);		
		dlgAlreadyBooked.setLayout(new GridBagLayout());
		
		JLabel lblExistingMessage = new JLabel("This mat was already booked!");
		lblExistingMessage.setHorizontalAlignment(JLabel.CENTER);
		Utilities.setCompoenentSize(lblExistingMessage, 250, 25);
		dlgAlreadyBooked.add(lblExistingMessage, new GridBagConstraints(0, 0, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(0, 0, 20, 0), 0, 0));		
		
		JButton btnOK = new JButton("OK");
		Utilities.setCompoenentSize(btnOK, 60, 25);
		dlgAlreadyBooked.add(btnOK, new GridBagConstraints(0, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
		
		btnOK.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dlgAlreadyBooked.dispose();
			}
		});
		
		dlgAlreadyBooked.setVisible(true);
	}

	private void showPrintDialog(MouseEvent e) {
		boolean isMissingInput = isMissingInput();
		if(isMissingInput)
			showPleaseEnterDialog();
		else
			printReport(e);
	}

	private void printReport(MouseEvent e) {
		((JLabel)e.getSource()).setBackground(Constant.DIAMOND);
		
		try {			
//			JasperReport report = JasperCompileManager.compileReport(
//					"G://Work/Java/workspace/Yoga-Tickets-Booking-System/report/Yoga-Ticket.jasper");
			// Second, create a map of parameters to pass to the report.
			Map parameters = new HashMap();
			parameters.put("STUDIO_NAME", "DDDD");
			parameters.put("DATE", "AAAA");
			parameters.put("TIME", "AAAA");
			parameters.put("CLASS", "AAAA");
			parameters.put("MAT_NUMBER", "AAAA");
			// Third, get a database connection
//			Connection conn = data
			// Fourth, create JasperPrint using fillReport() method
			
//			E:\Java\myworkspace\Yoga-Tickets-Booking-System\report\Yoga-Ticket.jasper
//			G://Work/Java/workspace/Yoga-Tickets-Booking-System/report/Yoga-Ticket.jasper"
				
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					"E://Java/myworkspace/Yoga-Tickets-Booking-System/report/Yoga-Ticket.jasper",
					parameters);
			JasperViewer.viewReport(jasperPrint);
		} catch (JRException e1) {
			System.out.println(e1);
		}
	}

	private void showPleaseEnterDialog() {
		dlgPleaseEnterInput = new JDialog(this, "Enter input", true);
		dlgPleaseEnterInput.setSize(280, 170);
		dlgPleaseEnterInput.setLocationRelativeTo(this);		
		dlgPleaseEnterInput.setLayout(new GridBagLayout());
		
		JLabel lblExistingMessage = new JLabel("Please enter missing input(s).");
		lblExistingMessage.setHorizontalAlignment(JLabel.CENTER);
		Utilities.setCompoenentSize(lblExistingMessage, 250, 25);
		dlgPleaseEnterInput.add(lblExistingMessage, new GridBagConstraints(0, 0, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(0, 0, 20, 0), 0, 0));		
		
		JButton btnOK = new JButton("OK");
		Utilities.setCompoenentSize(btnOK, 60, 25);
		dlgPleaseEnterInput.add(btnOK, new GridBagConstraints(0, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
		
		btnOK.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dlgPleaseEnterInput.dispose();
			}
		});
		
		dlgPleaseEnterInput.setVisible(true);
	}

	private boolean isMissingInput() {
		if("".equals(txtDate.getText()) && "".equals(txtTime.getText())
				&& "".equals(txtClass.getText()))
				return true;
		return false;
	}

	private void initStudioNameComponent() {
		lblStudioName = new JLabel("Yoga Studio 1");
		lblStudioName.setBackground(Constant.BACKGROUND_COLOR_WHITE);
		lblStudioName.setOpaque(true);
		lblStudioName.setFont(Constant.FONT_STDIO_NAME);
		lblStudioName.setForeground(Constant.FOREGROUND_COLOR_BLUE);
		pnlMats.add(lblStudioName, new GridBagConstraints(5, 0, 5, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 0), 0, 0));
	}

	private void initLogoComponent() {
		ImageIcon logo = getLogo();
		lblLogo = new JLabel(logo);
		lblLogo.setBackground(Constant.BACKGROUND_COLOR_WHITE);
		lblLogo.setOpaque(true);
		Utilities.setCompoenentSize(lblLogo, 320, 120);
		pnlMats.add(lblLogo, new GridBagConstraints(0, 0, 5, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 0), 0, 0));
	}

	private ImageIcon getLogo() {
		String IMAGE_PATH = "images/california_fitness_yoga.png";
		ImageIcon icon = null;
		try {
			BufferedImage img = ImageIO.read(new File(IMAGE_PATH));
			int newLogoWidth = ((int) (img.getWidth()/1.4));
			int newLogoHeight = ((int) (img.getHeight()/1.4));
			Image imgage = img.getScaledInstance(newLogoWidth, newLogoHeight, Image.SCALE_SMOOTH);
			icon = new ImageIcon(imgage);
		} catch (IOException e) {
			System.out.println("Can not retrieve logo");
		}
		
		return icon;
	}

	private void initYogaInformationPanel() {
		pnlYogaInformation = new JPanel(new GridBagLayout());
		TitledBorder borderTitle = BorderFactory.createTitledBorder("Ticket Information");
		pnlYogaInformation.setBorder(borderTitle);
		pnlYogaInformation.setBackground(Constant.BACKGROUND_COLOR_WHITE);
		Utilities.setCompoenentSize(pnlYogaInformation, 300, 400);
		
		initDateComponents();
		initTimeComponents();
		initClassComponents();
		initMemberTypeComponents();
		initResetAndClearComponents();
	}

	private void initResetAndClearComponents() {
		JPanel pnlResetAndClear = new JPanel(new GridBagLayout());
		pnlResetAndClear.setBackground(Constant.BACKGROUND_COLOR_WHITE);
		
		btnReset = new JButton("Reset");
		pnlResetAndClear.add(btnReset, new GridBagConstraints(0, 0, 2, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 30), 0, 0));
		
		btnClear = new JButton("Clear");
		pnlResetAndClear.add(btnClear, new GridBagConstraints(2, 0, 2, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 30, 0, 0), 0, 0));
		
		pnlYogaInformation.add(pnlResetAndClear, new GridBagConstraints(0, 4, 2, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));

		JLabel lblBottomComponent = new JLabel();
		Utilities.setCompoenentSize(lblBottomComponent, 120, 140);
		pnlYogaInformation.add(lblBottomComponent, new GridBagConstraints(0, 5, 4, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
	}

	private void initMemberTypeComponents() {
		lblMemberType = new JLabel("Type");
		lblMemberType.setHorizontalAlignment(JLabel.RIGHT);
		Utilities.setCompoenentSize(lblMemberType, labelWidth, labelHeight);
		pnlYogaInformation.add(lblMemberType, new GridBagConstraints(0, 3, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 30, 20), 0, 0));
		
		String[] memberType = {"CENTURYON", "Diamond", "Normal"};
		cboMemberType = new JComboBox(memberType);
		Utilities.setCompoenentSize(cboMemberType, textFieldWidth, textFieldHeight);
		pnlYogaInformation.add(cboMemberType, new GridBagConstraints(1, 3, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 30, 0), 0, 0));
	}

	private void initClassComponents() {
		lblClass = new JLabel("Class");
		lblClass.setHorizontalAlignment(JLabel.RIGHT);
		Utilities.setCompoenentSize(lblClass, labelWidth, labelHeight);
		pnlYogaInformation.add(lblClass, new GridBagConstraints(0, 2, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 20), 0, 0));
		
		txtClass = new JTextField();
		Utilities.setCompoenentSize(txtClass, textFieldWidth, textFieldHeight);
		pnlYogaInformation.add(txtClass, new GridBagConstraints(1, 2, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 0), 0, 0));
	}

	private void initTimeComponents() {
		lblTime = new JLabel("Time");
		lblTime.setHorizontalAlignment(JLabel.RIGHT);
		Utilities.setCompoenentSize(lblTime, labelWidth, labelHeight);
		pnlYogaInformation.add(lblTime, new GridBagConstraints(0, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 20), 0, 0));
		
		txtTime = new JTextField();
		Utilities.setCompoenentSize(txtTime, textFieldWidth, textFieldHeight);
		pnlYogaInformation.add(txtTime, new GridBagConstraints(1, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 0), 0, 0));
	}

	private void initDateComponents() {
		lblDate = new JLabel("Date");
		lblDate.setHorizontalAlignment(JLabel.RIGHT);
		Utilities.setCompoenentSize(lblDate, labelWidth, labelHeight);
		pnlYogaInformation.add(lblDate, new GridBagConstraints(0, 0, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 20), 0, 0));
		
		txtDate = new JTextField();
		Utilities.setCompoenentSize(txtDate, textFieldWidth, textFieldHeight);
		pnlYogaInformation.add(txtDate, new GridBagConstraints(1, 0, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 20, 0), 0, 0));
	}

	private void initStudioPanel() {
		pnlStudio = new JPanel();
		TitledBorder borderTitle = BorderFactory.createTitledBorder("Studio");
		pnlStudio.setBackground(Constant.BACKGROUND_COLOR_WHITE);
		pnlStudio.setBorder(borderTitle);	
		Utilities.setCompoenentSize(pnlStudio, 300, 175);
		
		btnStudio1 = new JButton("Studio 1");
		pnlStudio.add(btnStudio1);
		
		btnStudio2 = new JButton("Studio 2");
		pnlStudio.add(btnStudio2);
		
		btnStudio3 = new JButton("Studio 3");
		pnlStudio.add(btnStudio3);
	}
	
	private void registerListeners() {
		addActionListenerForBtnStudio1();
		addActionListenerForBtnStudio2();
		addActionListenerForBtnStudio3();
		addActionListenerForBtnClear();
		addActionListenerForBtnReset();
		addMouseListenerForMainFrame();
	}

	private void addActionListenerForBtnReset() {
		btnReset.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showRemoveAllMats();
				removeAllMats();				
			}
		});
	}

	protected void showRemoveAllMats() {
		dlgRemoveAllMats = new JDialog(this, "Remove all mats", true);
		dlgRemoveAllMats.setSize(280, 170);
		dlgRemoveAllMats.setLocationRelativeTo(this);		
		dlgRemoveAllMats.setLayout(new GridBagLayout());
		
		JLabel lblExistingMessage = new JLabel("Do you really want to remove all mats?");
		lblExistingMessage.setHorizontalAlignment(JLabel.CENTER);
		Utilities.setCompoenentSize(lblExistingMessage, 250, 25);
		dlgRemoveAllMats.add(lblExistingMessage, new GridBagConstraints(0, 0, 2, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(0, 0, 20, 0), 0, 0));		
		
		JButton btnOK = new JButton("OK");
//		btnOK.setFocusPainted(false);
		Utilities.setCompoenentSize(btnOK, 80, 25);
		dlgRemoveAllMats.add(btnOK, new GridBagConstraints(0, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 35, 0, 0), 0, 0));
		
		btnOK.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dlgRemoveAllMats.dispose();
				removeAllMats();
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
//		btnCancel.setFocusPainted(false);
		Utilities.setCompoenentSize(btnCancel, 80, 25);
		dlgRemoveAllMats.add(btnCancel, new GridBagConstraints(1, 1, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
		btnCancel.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				dlgRemoveAllMats.dispose();
			}
		});
		
		dlgRemoveAllMats.setVisible(true);	
	}

	protected void removeAllMats() {
		String studioName = lblStudioName.getText().toLowerCase();
		if("yoga studio 1".equals(studioName)) {
			for(int i = 0; i < lblMatsForStudio1.length; i++)
				lblMatsForStudio1[i].setBackground(Constant.NORMAL);
		} else if("yoga studio 2".equals(studioName)) {
			for(int i = 0; i < lblMatsForStudio2.length; i++)
				lblMatsForStudio2[i].setBackground(Constant.NORMAL);
		} else if("yoga studio 3".equals(studioName)) {
			for(int i = 0; i < lblMatsForStudio3.length; i++)
				lblMatsForStudio3[i].setBackground(Constant.NORMAL);
		}
	}

	private void addMouseListenerForMainFrame() {
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(getCursor().equals(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))) {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
	}

	private void addActionListenerForBtnClear() {
		btnClear.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
				setCursor(cursor);
			}
		});
	}

	private void addActionListenerForBtnStudio3() {
		btnStudio3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initMatsForStudio3();
				changeStudioName("Yoga Studio 3");
			}
		});	
	}

	private void initMatsForStudio3() {
		if(lblMatsForStudio3 == null) {
			lblMatsForStudio3 = new JLabel[40];
			for(int i = 0; i < 40; i++) {
				lblMatsForStudio3[i] = new JLabel(Utilities.parseString(i + 1));
				lblMatsForStudio3[i].setBackground(Constant.BACKGROUND_COLOR_WHITE);
			}
		}
		
		if(lblMatsForStudio1 != null) {
			for(int i = 0; i < lblMatsForStudio1.length; i++) {
				pnlMats.remove(lblMatsForStudio1[i]);				
			}
		}
		
		if(lblMatsForStudio2 != null) {
			for(int i = 0; i < lblMatsForStudio2.length; i++) {
				pnlMats.remove(lblMatsForStudio2[i]);				
			}
		}
		
		pnlMats.remove(lblLastComponent);
		
		int x = 0, y = 1;
		for(int i = 0; i < 40; i++) {			
			lblMatsForStudio3[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));				
			lblMatsForStudio3[i].setOpaque(true);
			lblMatsForStudio3[i].setHorizontalAlignment(JLabel.CENTER);
			lblMatsForStudio3[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					chooseMats(e);
				}
			});
			Utilities.setCompoenentSize(lblMatsForStudio3[i], 65, 32);
			pnlMats.add(lblMatsForStudio3[i], new GridBagConstraints(x, y, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 5, 5), 0, 0));
			x++;			
			if(x % 10 == 0) {
				x = 0;
				y++;
			} else if(i == lblMatsForStudio3.length - 1) {
				y++;
			}	
		}		
		
		lblLastComponent = new JLabel();
		Utilities.setCompoenentSize(lblLastComponent, 80, 312);
		pnlMats.add(lblLastComponent, new GridBagConstraints(1, y, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
		
		pnlMats.validate();
		pnlMats.repaint();
	}

	private void addActionListenerForBtnStudio2() {
		btnStudio2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initMatsForStudio2();
				changeStudioName("Yoga Studio 2");
			}
		});	
	}

	private void initMatsForStudio2() {
		if(lblMatsForStudio2 == null) {
			lblMatsForStudio2 = new JLabel[35];
			for(int i = 0; i < 35; i++) {
				lblMatsForStudio2[i] = new JLabel(Utilities.parseString(i + 1));
				lblMatsForStudio2[i].setBackground(Constant.BACKGROUND_COLOR_WHITE);
			}
		}
		
		if(lblMatsForStudio1 != null) {
			for(int i = 0; i < lblMatsForStudio1.length; i++) {
				pnlMats.remove(lblMatsForStudio1[i]);				
			}
		}
		
		if(lblMatsForStudio3 != null) {
			for(int i = 0; i < lblMatsForStudio3.length; i++) {
				pnlMats.remove(lblMatsForStudio3[i]);				
			}
		}
		
		pnlMats.remove(lblLastComponent);
		
		int x = 0, y = 1;
		for(int i = 0; i < 35; i++) {			
			lblMatsForStudio2[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));				
			lblMatsForStudio2[i].setOpaque(true);
			lblMatsForStudio2[i].setHorizontalAlignment(JLabel.CENTER);
			lblMatsForStudio2[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					chooseMats(e);
				}
			});
			Utilities.setCompoenentSize(lblMatsForStudio2[i], 65, 32);
			pnlMats.add(lblMatsForStudio2[i], new GridBagConstraints(x, y, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 5, 5), 0, 0));
			x++;			
			if(x % 10 == 0) {
				x = 0;
				y++;
			} else if(i == lblMatsForStudio2.length - 1) {
				y++;
			}
		}	
		
		lblLastComponent = new JLabel();
		Utilities.setCompoenentSize(lblLastComponent, 80, 312);
		pnlMats.add(lblLastComponent, new GridBagConstraints(1, y, 1, 1, 
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 
				new Insets(0, 0, 0, 0), 0, 0));
		
		pnlMats.validate();
		pnlMats.repaint();
	}

	private void addActionListenerForBtnStudio1() {
		btnStudio1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				initMatsForStudio1();
				changeStudioName("Yoga Studio 1");
			}
		});		
	}

	private void changeStudioName(String studioName) {
		lblStudioName.setText(studioName);
	}
}
