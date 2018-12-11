package sintactico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Text;
//import org.eclipse.wb.swt.SWTResourceManager;

import common.Mensaje;

public class GuiSintactico extends Composite {
	//Numero de la tecla enter
	int keyEnter=13;
	List listnt;List listt;
	ParserReader pr=null;
	String ruta="";
	String currentNT="";
	String testpath="";
	static ArrayList<NonTerminal> nonterminal = null;
	ArrayList<Token> tokens = null;
	ArrayList<NonTerminal> selectednt = null;
	int alto=60;
	int ancho=300;
	int balto=25;
	int bancho=75;
	int masalto=0;
	private boolean ntselected=false;
	ArrayList<Label> listalbl=new ArrayList<Label>();
	ArrayList<Button> listabtn=new ArrayList<Button>();
	ArrayList<String> listaTests=new ArrayList<String>();
	HashMap<Label,Text> listatxt=new HashMap<Label,Text>();
	HashMap<Element, Label> listaellabel=new HashMap<Element,Label>();
	HashMap<Integer,ArrayList<Text>> listaProd= new HashMap<Integer,ArrayList<Text>>();
	ArrayList<Text> textos = new ArrayList<Text>();
	NonTerminal selected=null;
	private Text text;
	private Button btnRadioButton=null; 
	private Button btnGuardarNT=null; 
	private Button btnAddNoTerminal=null;
	private ArrayList<Object> toBeCleared=new ArrayList<Object>();
	Composite composite = this;
	
	public GuiSintactico(Composite parent, int style, String path) {
		super(parent, SWT.NONE);
		ruta=path;
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				limpiar();
				}
			@Override
			public void mouseDown(MouseEvent e) {}
			@Override
			public void mouseUp(MouseEvent e) {}
			
		});
		
		listnt = new List(this, SWT.BORDER | SWT.V_SCROLL);
		listnt.setBounds(130, 21, 152, 471);
		listnt.addMouseListener(new MouseListener() {			
			@Override
			public void mouseUp(MouseEvent e) {}			
			@Override
			public void mouseDown(MouseEvent e) {}			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				limpiar();
				if(!ntselected) {
					for(int index=0;index<nonterminal.size();index++) {
						if(listnt.isSelected(index)) {
							draw(nonterminal.get(index));
						}
					}
				}
				else {
					for(int index=0;index<nonterminal.size();index++) {
						if(listnt.isSelected(index)) {
							for(Text t: textos) {
								if(t.getText().isEmpty() || t.getText().trim().length()==0) {
									t.setText(nonterminal.get(index).getName());
									break;
								}
							}
						}
					}
				}
			}
		});

		
		listt = new List(this, SWT.BORDER | SWT.V_SCROLL);
		listt.setBounds(20, 21, 77, 471);
		/*listt.addMouseListener(new MouseListener() {			
			@Override
			public void mouseUp(MouseEvent e) {}			
			@Override
			public void mouseDown(MouseEvent e) {}			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if(ntselected){
					for(int index=0;index<tokens.size();index++) {
						if(listt.isSelected(index)) {
							boolean finish=false;
							for(Text t: textos) {
								if(t.getText().isEmpty() || t.getText().trim().length()==0) {
									t.setText(tokens.get(index).getName());
									finish=true;
									break;
								}
							}
							if(!finish) {
								getText(1);
								btnRadioButton.setBounds(ancho, alto, 90, 16);
								masalto=alto;
							}
							
						}
					}
				}
			}
		});*/
		
		Label lblTokens = new Label(this, SWT.NONE);
		lblTokens.setBounds(20, 0, 47, 15);
		lblTokens.setText("Tokens");
		
		Label lblNoTerminales = new Label(this, SWT.NONE);
		lblNoTerminales.setBounds(130, 0, 88, 15);
		lblNoTerminales.setText("No terminales");
		
		Button btnRefresh = new Button(this, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnRefresh.setBounds(291, 467, 91, 25);
		btnRefresh.setText("ACTUALIZAR");
		Button btnGuardar = new Button(this, SWT.NONE);
		btnGuardar.setBounds(388, 467, 75, 25);
		btnGuardar.setText("GUARDAR");
		btnGuardar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					new ParserWriter(path,nonterminal);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});		
		
		Button btnClear = new Button(this, SWT.NONE);
		btnClear.setBounds(291, 10, 75, 25);
		btnClear.setText("Limpiar");
		btnClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				limpiar();
			}
		});
		
		nonTermMenu();	
		
		try {
			pr = new ParserReader(path);
			nonterminal=pr.getNonterminal();
			tokens=pr.getTokens();
			for(NonTerminal nt: nonterminal) {
				if(!nt.getName().equals("error"))
				listnt.add(nt.getName());
			}
			for(Token t:tokens) {
				listt.add(t.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void nonTermMenu() {		
		Label lblNombre = new Label(this, SWT.NONE);
		lblNombre.setBounds(382, 15, 55, 15);
		lblNombre.setText("Nombre");		
		text = new Text(this, SWT.BORDER);
		text.setBounds(443, 12, 142, 21);		
		Button btnAadirProduccion = new Button(this, SWT.NONE);
		btnAadirProduccion.setBounds(742, 10, 123, 25);
		btnAadirProduccion.setText("Añadir Produccion");
		btnAadirProduccion.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				selected.addProduccion(new Produccion());
				limpiar();
				drawTextFieldsForNonTerminal(selected);
				//newProduction();
			}
		});
				
		btnAddNoTerminal = new Button(this, SWT.NONE);
		btnAddNoTerminal.setBounds(591, 10, 134, 25);
		btnAddNoTerminal.setText("A\u00F1adir No Terminal");
		btnAddNoTerminal.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!ntselected) {
					boolean found=false;
					for(int index=0;index<nonterminal.size();index++) {
						if(nonterminal.get(index).getName().equalsIgnoreCase(text.getText())) {
							found=true;
							selected=nonterminal.get(index);
							drawTextFieldsForNonTerminal(nonterminal.get(index));
						}
					}
					if(!found) { 
						NonTerminal nt=new NonTerminal(text.getText());
						nonterminal.add(nt);
						selected=nt;
						drawTextFieldsForNonTerminal(nt);
						}
					//nuevoNT(text);
					//ntselected=true;
				}
			}
		});
		btnGuardarNT = new Button(this, SWT.NONE);
		btnGuardarNT.setVisible(false);
		btnGuardarNT.setBounds(300, 40, 135, 25);
		btnGuardarNT.setText("Guardar No Terminal");
		
		Button btnEditar = new Button(this, SWT.NONE);
		btnEditar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listnt.getSelection();
				for(int index=0;index<nonterminal.size();index++) {
					if(listnt.isSelected(index)) {
						for(NonTerminal nt: nonterminal) {
							if(nt.getName().equals(listnt.getSelection()[0])) {
								limpiar();
								selected=nt;
								drawTextFieldsForNonTerminal(nt);
								break;
							}
						}
					}
				}
			}
		});
		btnEditar.setBounds(441, 40, 75, 25);
		btnEditar.setText("Editar");
		
		Button btnEliminarNoTerminal = new Button(this, SWT.NONE);
		btnEliminarNoTerminal.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int index=0;index<nonterminal.size();index++) {
					if(listnt.isSelected(index)) {
						for(NonTerminal nt: nonterminal) {
							if(nt.getName().equals(listnt.getSelection()[0])) {
								nonterminal.remove(nt);
								listnt.remove(index);
								break;
							}
						}
					}
				}
			}
		});
		btnEliminarNoTerminal.setBounds(591, 40, 134, 25);
		btnEliminarNoTerminal.setText("Eliminar No Terminal");
		
		Button btnCheck = new Button(this, SWT.NONE);
		btnCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ComprobarErrores.comprobarTodo(nonterminal);
			}
		});
		btnCheck.setBounds(469, 467, 75, 25);
		btnCheck.setText("CHECK");
		btnGuardarNT.addSelectionListener(new SelectionAdapter() {			
			@Override
			public void widgetSelected(SelectionEvent e) {
				limpiar();
				listnt.removeAll();
				for(NonTerminal nt: nonterminal) {
					if(!nt.getName().equals("error"))
					listnt.add(nt.getName());
				}
				guardarVisibles();
			}
		});		
	}



	protected void guardarVisibles() {
		String name = selected.getName();
		//Generar estructura
		selected.getStructure().clear();
		for(int i=0;i<listaProd.size();i++) {
			ArrayList<Text> textos = listaProd.get(i);
			Produccion p=new Produccion();
			int empties=0;
			for(Text t: textos) {
				if(!t.getText().trim().equals("")) {
					String nombre = t.getText();
					boolean found=false;
					for(Token tok: tokens) {
						if(!found && tok.getName().equalsIgnoreCase(nombre)) {
							p.add(tok); found = true;
						}
					}
					if(!found)
					for(NonTerminal non: nonterminal) {
						if(!found && non.getName().equalsIgnoreCase(nombre)) {
							p.add(non); found = true;
						}
					}
					if(!found) p.add(new NonTerminal(nombre));
				}
				else {
					empties++;
				}
			}
			if(empties==textos.size()) {
				p.add(new NonTerminal("epsilon"));
			}
			selected.addProduccion(p);
		}
		
		for(NonTerminal nt: nonterminal) {
			if(nt.getName().equals(name)) {
				nt=selected; break;
			}
		}
		
	}

	protected void drawTextFieldsForNonTerminal(NonTerminal nt) {
		Text t = new Text(this,SWT.NONE);
		t.setText(nt.getName());
		t.setVisible(true);
		t.setBounds(ancho, alto+10, 70, 15);
		textos.add(t);
		toBeCleared.add(t);
		ancho=ancho+80;
		alto=alto+30;
		Label lbl = new Label(this, SWT.NONE);
		lbl.setBounds(ancho, alto-20, 30, 15);
		lbl.setText("::=");
		lbl.setVisible(true);
		toBeCleared.add(lbl);
		btnGuardarNT.setVisible(true);
		int i=0;
		for(Produccion p: nt.getStructure()) {
			ArrayList<Text> listaText= new ArrayList<Text>();
			int mialto=alto;
			int miancho=ancho-20;
			Button eliminar= new Button(this,SWT.NONE);
			eliminar.setText("-");
			toBeCleared.add(eliminar);
			eliminar.setBounds(miancho-20, mialto, 15, 15);
			eliminar.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					nt.deleteProduction(p);
					limpiar();
					listnt.removeAll();
					for(NonTerminal nt: nonterminal) {
						if(!nt.getName().equals("error"))
						listnt.add(nt.getName());
					}
					listaProd.clear();
					drawTextFieldsForNonTerminal(nt);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			
			Button btnRadio = new Button(this, SWT.RADIO);
			btnRadio.setBounds(miancho, mialto, 20, 15);
			btnRadio.addSelectionListener(new SelectionListener() {				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Text t2 = new Text(composite,SWT.NONE);
					try {
						Text last = listaText.get(listaText.size()-1);
						Rectangle b=last.getBounds();
						b.x=b.x+80;
						t2.setBounds(b);
					}catch (ArrayIndexOutOfBoundsException aioobe) {
						t2.setBounds(miancho+20, mialto, 70, 15);
					}
					textos.add(t2);
					toBeCleared.add(t2);
					listaText.add(t2);
				}				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			});
			for(Element e: p.getStructure()) {
				Text t1 = new Text(this,SWT.NONE);
				t1.setText(e.getName());
				t1.setBounds(ancho, alto, 70, 15);
				textos.add(t1);
				toBeCleared.add(t1);
				ancho=ancho+80;
				listaText.add(t1);
			}
			
			toBeCleared.add(btnRadio);
			listaProd.put(i, listaText);
			i++;
			ancho=380;
			alto=alto+25;
		}
		
	}


	private Text getText(int num) {
		Text t = new Text(this,SWT.NONE);
		for(int i=0; i<num; i++) {
		t = new Text(this,SWT.NONE);
		t.setVisible(true);
		t.setBounds(ancho-40, alto, 70, 15);
		textos.add(t);
		toBeCleared.add(t);
		ancho=ancho+80;
		}
		return t;
	}
	private void draw(NonTerminal nt) {
		ntselected=true;
		alto=alto+15;
		createProduction(null,nt,ancho,alto,bancho,balto,true);
		ancho=300+6*(nt.getName().length()+4);
		alto=alto+20;
		for(Produccion p: nt.getStructure()) {
			for(Element e: p.getStructure()) {
				if(e instanceof Token || e instanceof NonTerminal) {
					createProduction(nt,e,ancho,alto,bancho,balto,false);
					ancho=ancho+6*(e.getName().length()+5);
				}
			}
			ancho=300+6*(nt.getName().length()+4);
			alto=alto+15;
		}
		ancho=300;
		currentNT=nt.getName();
		text.setText(currentNT);
		nuevoNT(text);
	}

	private void createProduction(NonTerminal padre,Element el, int ancho2,int alto2, int balto2, int bancho2,boolean negrita) {
		String name=el.getName();
		Label lbl = new Label(this, SWT.NONE);
		listaellabel.put(el,lbl);
		lbl.setFont(lbl.getFont());
		lbl.setEnabled(true);
		lbl.setBounds(ancho2, alto2, 6*(name.length()+5), 15);
		if(padre!=null)	lbl.setBounds(ancho2, alto2, 6*(name.length()+5), 15);
		lbl.setText(name);
		if(negrita) {
			//lbl.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			lbl.setText(name+" ::= ");
		}
		Text t1 = new Text(this, SWT.NONE);
		t1.setText(lbl.getText());
		t1.setVisible(false);
		t1.setBounds(ancho2, alto2, 4*name.length(), 15);
		t1.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == keyEnter) {
					lbl.setText(t1.getText());
					t1.setVisible(false);
					lbl.setVisible(true);
					el.name=t1.getText().replaceAll("::=", "").trim();
				}
				
			}
		});
		listatxt.put(lbl, t1);
		listalbl.add(lbl);		
	}
	
	public void refresh() {
		this.redraw();
		listnt.removeAll();listt.removeAll();
		try {
			pr = new ParserReader(ruta);
			for(NonTerminal nt: pr.getNonterminal()) {
				if(!nt.getName().equals("error"))
				listnt.add(nt.getName());
				else {System.out.println("Encontrado error");}
			}
			for(Token t:pr.getTokens()) {
				listt.add(t.getName());
			}
		} catch (IOException e) {
        	Mensaje.print("ERROR", "No se ha podido refrescar la pagina");
			e.printStackTrace();
		}
	}

	private void limpiar() {
		for(Label b: listalbl) {
			b.setVisible(false);
		}
		listatxt.forEach((k,v) -> 
			v.setVisible(false)					
		);
		listatxt.forEach((k,v) -> 
			k.setVisible(false)					
		);
		listalbl.clear();
		alto=60;
		ancho=300;
		for(Object o: toBeCleared) {
			if(o instanceof Label) {
				((Label)o).setVisible(false);
			}
			if(o instanceof Text) {
				((Text)o).setVisible(false);
			}
			if(o instanceof Button) {
				((Button)o).setVisible(false);
			}
			btnGuardarNT.setVisible(false);
			btnAddNoTerminal.setEnabled(true);
			text.setEnabled(true);
			text.setText("");
		}
		textos.clear();currentNT="";
		ntselected=false;
		
	}
	private void nuevoNT(Text text){
		btnGuardarNT.setVisible(true);
		//NewNT(text.getText());
		btnAddNoTerminal.setEnabled(false);
		text.setEnabled(false);
	}

	public static ArrayList<NonTerminal> getNonterminal() {
		return nonterminal;
	}

	public void setNonterminal(ArrayList<NonTerminal> nonterminal) {
		GuiSintactico.nonterminal = nonterminal;
	}
}
