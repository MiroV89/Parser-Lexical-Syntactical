package plugeditor.editors;

import java.io.File;
import java.io.StringWriter;
import java.text.Collator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import lexico.GuiLexico;
import sintactico.GuiSintactico;
import tests.TestPage;

import org.eclipse.ui.ide.IDE;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class PLEditor extends MultiPageEditorPart implements IResourceChangeListener{
	private static GuiSintactico guiSintactico=null;
	private static TestPage testpage=null;
	private static TextEditor editor;
	private Font font;
	private StyledText text;
	public PLEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	private String getFilePath() {
		IPath path = ((IFileEditorInput)getEditorInput()).getFile().getLocation();
		if (path != null && path.toString().endsWith("scanner.flex")){
			return path.toOSString().replace("doc"+File.separator+"specs"+File.separator+"scanner.flex", "");
		}
		if (path != null && path.toString().endsWith("parser.cup")) {
			return path.toOSString().replace("doc"+File.separator+"specs"+File.separator+"parser.cup", "");
		}		
		return null;		
	}
	void createPage0() {
		try {
			editor = new TextEditor();
			File file=new File(getFilePath()+"doc"+File.separator+"specs"+File.separator+"scanner.flex");
			IWorkspace workspace= ResourcesPlugin.getWorkspace();   
			IPath location= Path.fromOSString(file.getAbsolutePath());
			IFile ifile= workspace.getRoot().getFileForLocation(location);
			IEditorInput ie = new FileEditorInput(ifile);
			int index = addPage(editor, ie);
			setPageText(index, editor.getTitle());			
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	void createPage1() {
		try {
			editor = new TextEditor();
			File file=new File(getFilePath()+"doc"+File.separator+"specs"+File.separator+"parser.cup");
			IWorkspace workspace= ResourcesPlugin.getWorkspace();   
			IPath location= Path.fromOSString(file.getAbsolutePath());
			IFile ifile= workspace.getRoot().getFileForLocation(location);
			IEditorInput ie = new FileEditorInput(ifile);
			int index = addPage(editor, ie);
			setPageText(index, editor.getTitle());			
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	}
	void createPage2() {
		String path = getFilePath();
		GuiLexico composite = new GuiLexico(this, getContainer(),SWT.NONE,path);		      
	    int index = addPage(composite);
	    setPageText(index, "Lexico");
	}
	void createPage3() {
		String path = getFilePath();
		guiSintactico = new GuiSintactico(getContainer(),SWT.NONE,path);		      
	    int index = addPage(guiSintactico);
	    setPageText(index, "Sintactico");
	}
	void createPage4() {
		String path = getFilePath();
		testpage = new TestPage(getContainer(),SWT.NONE,path);		      
	    int index = addPage(testpage);
	    setPageText(index, "Tests");
	}
	public static void refresh(int page){
		guiSintactico.refresh();
	}
	protected void createPages() {
		createPage0();
		createPage1();
		createPage2();
		createPage3();
		createPage4();
	}
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}
	
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
	}
	
	public static IProject getProj() {
		if(editor!=null)
		return ((FileEditorInput)editor.getEditorInput()).getFile().getProject();
		return null;
	}
	
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}
	
	void setFont() {
		FontDialog fontDialog = new FontDialog(getSite().getShell());
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null)
				font.dispose();
			font = new Font(text.getDisplay(), fontData);
			text.setFont(font);
		}
	}
	
	void sortWords() {

		String editorText =
			editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();

		StringTokenizer tokenizer =
			new StringTokenizer(editorText, " \t\n\r\f!@#\u0024%^&*()-_=+`~[]{};:'\",.<>/?|\\");
		List<String> editorWords = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			editorWords.add(tokenizer.nextToken());
		}

		Collections.sort(editorWords, Collator.getInstance());
		StringWriter displayText = new StringWriter();
		for (int i = 0; i < editorWords.size(); i++) {
			displayText.write(((String) editorWords.get(i)));
			displayText.write(System.getProperty("line.separator"));
		}
		text.setText(displayText.toString());
	}
}
