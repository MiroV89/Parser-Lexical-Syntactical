package plugeditor.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class Pages extends PLEditor{
	public void createPageLex(Composite composite) {
		Button fontButton = new Button(composite, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 4;
		fontButton.setLayoutData(gd);
		fontButton.setText("Change Font...");
		
		fontButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setFont();
			}
		});
		
		Button fontButto = new Button(composite, SWT.NONE);
		GridData gd1 = new GridData(GridData.CENTER);
		gd1.horizontalSpan = 8;
		fontButto.setLayoutData(gd1);
		fontButto.setText("Changes");
		
		fontButto.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setFont();
			}
		});
		
		Table table = new Table(composite, SWT.NONE);
		table.setBounds(10, 30, 40, 50);
		table.setLayoutData(new GridData(GridData.END));
		table.setVisible(true);
	
	}
}
