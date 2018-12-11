package lexico;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import common.SWTResourceManager; 
import common.Mensaje;
import idioma.Lang;
import sintactico.Token;
import plugeditor.editors.*;

public class GuiLexico extends Composite {
	private Text text;
	private Text text_1;
	private final boolean UPCASE=true;
	private final boolean LOWCASE=false;
	private static HashMap<String,String> listTokens = new HashMap<String,String>();
	private static HashMap<String,String> listExpresiones = new HashMap<String,String>();
	private static HashMap<String,String> listErrores = new HashMap<String,String>();
	private static HashMap<Integer, String> comentarios = new HashMap<Integer,String>();
	private ScannerWriter sw;
	private Table table_token;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Table table_ex;
	private Table table_er;
	private Text text_6;
	private Text text_7;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GuiLexico(PLEditor pleditor,Composite parent, int style, String path) {
		super(parent, SWT.NONE);
		Lang.ES();

		table_token = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table_token.setLinesVisible(true);
		table_token.setBounds(30, 85, 318, 353);
		table_token.setHeaderVisible(true);

		TableColumn tblclmnSimbolo = new TableColumn(table_token, SWT.NONE);
		tblclmnSimbolo.setWidth(154);
		tblclmnSimbolo.setText(Lang.sym);
		
		TableColumn tblclmnNombre = new TableColumn(table_token, SWT.NONE);
		tblclmnNombre.setWidth(155);
		tblclmnNombre.setText(Lang.name);
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(30, 58, 113, 21);
		
		text_1 = new Text(this, SWT.BORDER);
		text_1.setBounds(235, 58, 113, 21);
		
		Button btnAddToken = new Button(this, SWT.NONE);
		btnAddToken.setBounds(360, 85, 75, 25);
		btnAddToken.setText(Lang.add);		
		btnAddToken.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
					addToTable(listTokens,table_token, text, text_1,UPCASE);
			}
		});
		
		Button btnBorrarToken = new Button(this, SWT.NONE);
		btnBorrarToken.setBounds(360, 145, 75, 25);
		btnBorrarToken.setText(Lang.del);
		btnBorrarToken.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeFromTable(listTokens,table_token);
			}

		});
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(472, 58, 310, 198);
		
		Label lblNombre_1 = new Label(composite, SWT.NONE);
		lblNombre_1.setText(Lang.name);
		lblNombre_1.setBounds(10, 10, 55, 15);
		
		text_2 = new Text(composite, SWT.BORDER);
		text_2.setBounds(10, 29, 113, 21);
		
		Label lblExpresion = new Label(composite, SWT.NONE);
		lblExpresion.setText(Lang.exp);
		lblExpresion.setBounds(157, 10, 55, 15);
		
		text_3 = new Text(composite, SWT.BORDER);
		text_3.setBounds(157, 31, 113, 21);
		
		table_ex = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_ex.setBounds(10, 56, 202, 132);
		table_ex.setHeaderVisible(true);
		table_ex.setLinesVisible(true);
		
		TableColumn tblclmnNombre_1 = new TableColumn(table_ex, SWT.NONE);
		tblclmnNombre_1.setWidth(100);
		tblclmnNombre_1.setText(Lang.name);
		
		TableColumn tblclmnExpresion = new TableColumn(table_ex, SWT.NONE);
		tblclmnExpresion.setWidth(98);
		tblclmnExpresion.setText(Lang.exp);
		
		Button btnAddExp = new Button(composite, SWT.NONE);
		btnAddExp.setText(Lang.add);
		btnAddExp.setBounds(218, 68, 75, 25);
		btnAddExp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
					addToTable(listExpresiones,table_ex, text_2, text_3,LOWCASE);
			}
		});
		
		Button btnBorrarExp = new Button(composite, SWT.NONE);
		btnBorrarExp.setText(Lang.del);
		btnBorrarExp.setBounds(218, 108, 75, 25);
		btnBorrarExp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeFromTable(listExpresiones,table_ex);
			}

		});
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setBounds(472, 291, 310, 198);
		
		Label lblNombre_2 = new Label(composite_1, SWT.NONE);
		lblNombre_2.setText(Lang.name);
		lblNombre_2.setBounds(10, 10, 55, 15);
		
		text_4 = new Text(composite_1, SWT.BORDER);
		text_4.setBounds(10, 29, 113, 21);
		
		Label lblMensaje = new Label(composite_1, SWT.NONE);
		lblMensaje.setText(Lang.msg);
		lblMensaje.setBounds(157, 10, 55, 15);
		
		text_5 = new Text(composite_1, SWT.BORDER);
		text_5.setBounds(157, 31, 113, 21);
		
		table_er = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table_er.setLinesVisible(true);
		table_er.setHeaderVisible(true);
		table_er.setBounds(10, 58, 202, 130);
		
		TableColumn tblclmnNombre_2 = new TableColumn(table_er, SWT.NONE);
		tblclmnNombre_2.setWidth(100);
		tblclmnNombre_2.setText(Lang.name);
		
		TableColumn tblclmnMensaje = new TableColumn(table_er, SWT.NONE);
		tblclmnMensaje.setWidth(94);
		tblclmnMensaje.setText(Lang.msg);
		
		Button btnAddError = new Button(composite_1, SWT.NONE);
		btnAddError.setText(Lang.add);
		btnAddError.setBounds(218, 69, 75, 25);
		btnAddError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
					addToTable(listErrores,table_er, text_4, text_5, LOWCASE);
			}
		});
		
		Button btnBorrarError = new Button(composite_1, SWT.NONE);
		btnBorrarError.setText(Lang.del);
		btnBorrarError.setBounds(218, 111, 75, 25);
		btnBorrarError.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeFromTable(listErrores,table_er);
			}

		});
		
		Label lblComentarios = new Label(this, SWT.NONE);
		lblComentarios.setText(Lang.comment);
		lblComentarios.setBounds(360, 214, 75, 15);
		
		text_6 = new Text(this, SWT.BORDER);
		text_6.setBounds(354, 235, 38, 21);
		
		text_7 = new Text(this, SWT.BORDER);
		text_7.setBounds(397, 235, 38, 21);
		
		Label lblInicio = new Label(this, SWT.NONE);
		lblInicio.setBounds(354, 260, 37, 15);
		lblInicio.setText(Lang.begin);
		
		Label lblFin = new Label(this, SWT.NONE);
		lblFin.setBounds(397, 260, 55, 15);
		lblFin.setText(Lang.end);
		
		Label lblTokens = new Label(this, SWT.NONE);
		//lblTokens.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblTokens.setBounds(30, 10, 85, 21);
		lblTokens.setText(Lang.token);
		
		Label lblExpresiones = new Label(this, SWT.NONE);
		//lblExpresiones.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblExpresiones.setBounds(472, 31, 85, 21);
		lblExpresiones.setText(Lang.exp);
		
		Label lblErrores = new Label(this, SWT.NONE);
		//lblErrores.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblErrores.setBounds(472, 262, 55, 23);
		lblErrores.setText(Lang.error);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(30, 513, 787, 29);
		lblNewLabel.setText(Lang.overwrite);
		
		
		Button btnGenerar = new Button(this, SWT.BORDER | SWT.FLAT);
		//btnGenerar.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		btnGenerar.setBounds(188, 459, 160, 49);
		btnGenerar.setText(Lang.generate);
		
		Label lblToken = new Label(this, SWT.NONE);
		lblToken.setBounds(40, 37, 55, 15);
		lblToken.setText("Token");
		
		Label lblNombre = new Label(this, SWT.NONE);
		lblNombre.setBounds(247, 37, 55, 15);
		lblNombre.setText("Nombre");
		btnGenerar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(!text_6.getText().isEmpty() && !text_7.getText().isEmpty()){
					comentarios.put(0,text_6.getText());
					comentarios.put(1,text_7.getText());
				}
				try {
					sw.write(listTokens, listExpresiones, listErrores,comentarios);
				} catch (IOException e1) {
					Mensaje.print("ERROR", "Fallo en la lectura-escritura de scanner.flex");
					e1.printStackTrace();
				}
				try {
					new ParserWriter(listTokens,path);
					Mensaje.print("Correcto","Listo!");
				} catch (IOException e) {
					Mensaje.print("ERROR", "Fallo en la lectura-escritura de parser.cup");
					e.printStackTrace();
				}
				
			}
		});
		cargarDatos(path);
	}

	public void cargarDatos(String path) {
		try {			
			sw = new ScannerWriter(path,listTokens,listExpresiones,listErrores,comentarios);
			GuiLexico.listTokens=sw.getTokens();
			GuiLexico.listExpresiones=sw.getExpresiones();
			GuiLexico.listErrores=sw.getErrores();
			fillTable(listErrores, table_er);
			fillTable(listExpresiones, table_ex);
			fillTable(listTokens, table_token);
			if(!comentarios.isEmpty()) {
				text_6.setText(comentarios.get(0));
				text_7.setText(comentarios.get(1));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
    private void addToTable(HashMap<String,String> list,Table table,Text key,Text value, boolean upcase){

		if(list.containsKey(key.getText())){
			Mensaje.print("ERROR","El simbolo \""+key.getText()+"\" ya ha sido añadido");
		}
		else if(list.containsValue(value.getText())){
			Mensaje.print("ERROR","El nombre \""+value.getText()+"\" ya ha sido añadido");
		}
		else if(key.getText().trim().equals("") || value.getText().trim().equals("")) {
			if(key.getText().trim().equals("")) key.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
			if(value.getText().trim().equals("")) value.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		else{
			String valor = value.getText();
			if(upcase) valor=value.getText().toUpperCase();
			list.put(key.getText(),valor);
			TableItem std_item = new TableItem(table,SWT.NONE);
			std_item.setText(0,key.getText());
			std_item.setText(1, valor);
			key.clearSelection();
			value.clearSelection();
			key.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			value.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			key.setText(""); 
			value.setText("");
			
		}
    }
    

	private void removeFromTable(HashMap<String, String> list, Table table) {
		TableItem[] ti = table.getSelection();
		int[] indices= table.getSelectionIndices();
		int i=0;
		for(TableItem item : ti) {
			list.remove(item.getText());
			table.remove(indices[i]);
			i++;
		}
		
	}
    
    private void fillTable(HashMap<String,String> lista, Table table) {
		table.clearAll();
		table.removeAll();
    	if(!lista.isEmpty()) {
		SortedSet<String> keys = new TreeSet<String>(lista.keySet());
		for(String s:keys){
			TableItem std_item = new TableItem(table,SWT.NONE);
			std_item.setText(0,s);
			std_item.setText(1, lista.get(s));
		}
    	}
    }
    
    
    public static ArrayList<Token> getTokenArray(){
    	ArrayList<Token> lista=new ArrayList<Token>();
    	SortedSet<String> keys = new TreeSet<String>(listTokens.keySet());
		for(String s:keys){
			lista.add(new Token(s,listTokens.get(s).toString()));
		}
    	return lista;
    }    
    public static HashMap<String, String> getExpresiones(){
    	return listExpresiones;
    }    
    public static HashMap<String, String> getErrores(){
    	return listExpresiones;
    } 	
	public static HashMap<Integer, String> getComentarios() {
		return comentarios;
	}
}
