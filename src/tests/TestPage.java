package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import common.*;

import sintactico.ParserDebug;

public class TestPage extends Composite {
	private Text text_test;
	String ext="";
	String testpath="";
	String rutatests="";
	String ruta="";
	ArrayList<String> listaTests=new ArrayList<String>();
	TreeBuilder treeBuilder=null;
	public TestPage(Composite parent, int style, String path) {
		super(parent, SWT.NONE);
		try {
			new MyParserWriter(path);
		} catch (Exception e3) {
			Mensaje.print("SubParser", "No se ha podido constuir el subparser");
		}
		ruta=path;
		getExtension(path);
		rutatests=path+"doc"+File.separator+"test"+File.separator;
		
		List list = new List(this, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(32, 10, 146, 232);
		cargarTests(list);
		list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					new MyParserWriter(ruta);
				} catch (Exception e2) {
					// No se han podido generar los tests
					e2.printStackTrace();
				}
				String[] seleccionados =list.getSelection();
				File folder = new File(testpath);
				File[] listofFiles = folder.listFiles();
				String path = rutatests+seleccionados[0];
				for(int i=0; i<listofFiles.length; i++) {
					if(path.equalsIgnoreCase(listofFiles[i].getAbsolutePath())) {
						File file = new File(path);
						FileReader fr;
						try {
							fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						String cadena="";
						text_test.setText("");
						while((cadena = br.readLine())!=null) {
							text_test.append(cadena+"\n");
						}
						br.close();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		text_test = new Text(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		text_test.setBounds(32, 265, 260, 232);
		
		
		Tree tree = new Tree(this, SWT.BORDER);
		tree.setBounds(388, 25, 374, 472);		
		
		Button btnProbarTest = new Button(this, SWT.NONE);
		btnProbarTest.setBounds(184, 41, 108, 25);
		btnProbarTest.setText("Probar Test");
		
		btnProbarTest.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				String seleccionado=list.getSelection()[0];
				String path = rutatests+"debug"+File.separator+seleccionado.replace(ext, ".debug");
				String red=ParserDebug.readFile(path);
				tree.removeAll();
				treeBuilder = new TreeBuilder(ruta,red,tree);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		

		Button btnCargarParser = new Button(this, SWT.NONE);
		btnCargarParser.setBounds(184, 10, 108, 25);
		btnCargarParser.setText("Cargar SubParser");
		
		Button btnExportarArbol = new Button(this, SWT.NONE);
		btnExportarArbol.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser choser = new JFileChooser();
				File file=null;
				int returnValue = choser.showSaveDialog(null);
				if(returnValue==JFileChooser.APPROVE_OPTION) {
					file=choser.getSelectedFile();
				}
				try {
					if(treeBuilder!= null)
					treeBuilder.exportToFile(file);
					else Mensaje.print("ARBOL", "No hay ningun arbol generado");
				} catch (IOException e1) {
					Mensaje.print("ERROR", "No se ha podido guardar el archivo");
				}
			}
		});
		btnExportarArbol.setBounds(184, 72, 108, 25);
		btnExportarArbol.setText("Exportar arbol");
		btnCargarParser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)  {
				try{
					new MyParserWriter(path);
					Mensaje.print("Correcto", "Se ha creado la clase subparser en compiler.syntax\n Recuerde poner la extension de los tests.");

				} catch (Exception e1) {
					Mensaje.print("SubParser", "No se ha podido constuir el subparser");
			}		}
		});	
		
	}

		private void cargarTests(List list) {
			testpath=rutatests;
			File folder = new File(testpath);
			File[] listofFiles = folder.listFiles();
			for(int i=0; i<listofFiles.length; i++) {
				if(listofFiles[i].getName().endsWith(ext)) {
					list.add(listofFiles[i].getName());
					listaTests.add(listofFiles[i].getName());
				}
			}
			
		}

		private String getExtension(String ruta) {
			//Leer fichero subparser del alumno y cargar la extension 
			//public static String extension=".muned";
			File f = new File(ruta+File.separator+"src"+File.separator+"compiler"+File.separator+"syntax"+File.separator+"subparser.java");
			try {
				FileReader fr = new FileReader(f);
				@SuppressWarnings("resource")
				BufferedReader b = new BufferedReader(fr);
				String cadena="";
				while((cadena=b.readLine())!=null) {
					if(cadena.contains("public static String extension=")) {
						ext=cadena.replace("public static String extension=","").replaceAll("\"", "").replace(";","").trim();
						return ext;
					}
				}
				b.close();
			} catch (IOException e) {

				Mensaje.print("SubParser", "La clase subparser no ha sido generada todavía. Recuerde generarla.");
			}
			return null;
		}
}