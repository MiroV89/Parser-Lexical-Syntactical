package sintactico;

import java.util.ArrayList;

import common.Mensaje;

public class ComprobarErrores {
	public static void comprobarTodo(ArrayList<NonTerminal> lista) {
		int a = comprobarDuplicados(lista);
		int b = comprobarExistencias(lista);
		if((a+b)==0) {
			Mensaje.print("CORRECTO", "No se han encontrado errores");
		}
	}
	public static int comprobarDuplicados(ArrayList<NonTerminal> lista) {
		int errores=0;
		for(NonTerminal n: lista) {
			int coincidencias=0;
			for(NonTerminal nt:lista) {
				if(n.getName().equals(nt.getName())) {
					coincidencias++;
					if(coincidencias==2) {
						errores++;
						Mensaje.print("Coincidencias", "Se han encontrado varios no terminales - "+n.getName());
					}
				}
			}
		}
		return errores;
	}
	
	public static int comprobarExistencias(ArrayList<NonTerminal> lista) {
		int errores=0;
		for(NonTerminal n: lista) {
			for(Produccion p:n.getStructure()) {
				for(Element e: p.getStructure()) {
					if(e instanceof NonTerminal) {
						boolean existe=false;
						for(NonTerminal nt:lista) {
							if(e.getName().equals(nt.getName())) {existe=true;}
						}
						if(!existe) {
							errores++;
							Mensaje.print("No declarado", "No se ha declarado el no terminal "+e.getName());
						}
					}
				}
			}
		}
		return errores;
	}
}
