package be.kuleuven.liris.ioutilities;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.deckfour.xes.in.XMxmlParser;
import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.declare.visualizing.AssignmentModel;
import org.processmining.plugins.declare.visualizing.AssignmentModelView;
import org.processmining.plugins.declare.visualizing.AssignmentViewBroker;
import org.processmining.plugins.declare.visualizing.XMLBrokerFactory;

import be.kuleuven.liris.declareexecutionenvironment.model.DeclareModel;

public class IOHelp{
	
	public static String declareFilePath ="";
	public static String logFilePath ="";

	public static DeclareModel loadDeclareModel(){
		JFrame frame = new JFrame();
		final FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
		fd.setDirectory("");// C:\\");
		fd.setFile("*.*");
		frame.setSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 500,
				(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 500));
		fd.setVisible(true);

		String declareFilePath = fd.getDirectory() + File.separatorChar + fd.getFile();
		File toHandle = new File(declareFilePath);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return readDeclareModelFromXML(toHandle);
	}
	
	public static DeclareModel loadDeclareModel(String path) throws Exception{
		File file = new File(path);
		return readDeclareModelFromXML(file);
	}	
	
//	public static DeclareMap loadDeclareMap() throws Exception{
//		JFrame frame = new JFrame();
//		final FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
//		fd.setDirectory("");//C:\\");
//		fd.setFile("*.*");
//		frame.setSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-500,(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()-500));
//		fd.setVisible(true);
//			
//		declareFilePath = fd.getDirectory()+File.separatorChar+fd.getFile();
//		File toHandle = new File(declareFilePath);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		return readDeclareMapFromXML(toHandle);
//	}
//		
//	public static DeclareMap loadDeclareMap(String path) throws Exception{
//		File file = new File(path);
//		return readDeclareMapFromXML(file);
//	}	
//	
//	private static DeclareMap readDeclareMapFromXML(File toHandle) throws Exception {		
//		AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(toHandle.getAbsolutePath());
//		AssignmentModel model = broker.readAssignment();
//		AssignmentModelView view = new AssignmentModelView(model);
//		broker.readAssignmentGraphical(model, view);
//		org.processmining.plugins.declare.visualizing.AssignmentViewBroker brokerCh = org.processmining.plugins.declare.visualizing.XMLBrokerFactory.newAssignmentBroker(toHandle.getAbsolutePath());
//		org.processmining.plugins.declare.visualizing.AssignmentModel modelCh = brokerCh.readAssignment();
//		org.processmining.plugins.declare.visualizing.AssignmentModelView viewCheck = new org.processmining.plugins.declare.visualizing.AssignmentModelView(modelCh);
//		brokerCh.readAssignmentGraphical(modelCh, viewCheck);
//		DeclareMap decModel = new DeclareMap(model, modelCh, view,viewCheck, broker, brokerCh);
//		//System.out.println("No. Constraints: "+model.constraintDefinitionsCount()+"-- No. Activities: "+model.activityDefinitionsCount());		
//		return decModel;
//	}
	
	private static DeclareModel readDeclareModelFromXML(File toHandle){
		AssignmentViewBroker broker = XMLBrokerFactory.newAssignmentBroker(toHandle.getAbsolutePath());
		AssignmentModel model = broker.readAssignment();
		AssignmentModelView view = new AssignmentModelView(model);
		broker.readAssignmentGraphical(model, view);
		return new DeclareModel(broker, model, view, toHandle.getAbsolutePath());
	}
	
	public static XLog loadEventLog() throws Exception{
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(null, "Please select an event log (.mxml/.xes file).");
		FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
		fd.setDirectory("");
		fd.setFile("*.*");
		fd.setVisible(true);
		
		logFilePath = fd.getDirectory()+File.separatorChar+fd.getFile();
		File toHandle2 = new File(logFilePath);
			
		final XLog logIn;
		if(fd.getFile().contains(".xes")){
			XParser parser = new XesXmlParser();
			List<XLog> logs = parser.parse(toHandle2);
			logIn = logs.get(0);
		}else{
			XParser parser = new XMxmlParser();
			List<XLog> logs = parser.parse(toHandle2);
			logIn = logs.get(0);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return logIn;
	}	
	
	public static XLog loadEventLog(String path) throws Exception{		
		File toHandle2 = new File(path);
			
		final XLog logIn;
		if(toHandle2.getName().contains(".xes")){
			XParser parser = new XesXmlParser();
			List<XLog> logs = parser.parse(toHandle2);
			logIn = logs.get(0);
		}else{
			XParser parser = new XMxmlParser();
			List<XLog> logs = parser.parse(toHandle2);
			logIn = logs.get(0);
		}
		return logIn;
	}
	
}
