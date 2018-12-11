package idioma;

public class Lang {
	private static boolean English=false;
	public static String lang="English";
	public static String name="Nombre";
	public static String sym="Simbolo";
	public static String error="Error";
	public static String token="Tokens";
	public static String exp="Expresion";
	public static String add="Añadir";
	public static String del="Borrar";
	public static String msg="Mensaje";
	public static String generate="Generar";
	public static String comment="Comentarios";
	public static String begin="Inicio";
	public static String end="Final";
	public static String overwrite="Este plugin sobreescribe los ficheros scanner.flex y parser.cup, por lo que recomendamos que haga una copia de seguridad.";
	
	public static String errorExists(String value) {
		if(English) return "The symbol "+value+" alredy exists.";
		return "El simbolo "+value+" ya ha sido añadido.";
		}
	
	public static void EN() {
		English=true;
		lang="Español";
		name="Name";
		sym="Symbol";
		error="Error";
		exp="Expression";
		add="Add";
		del="Delete";
		msg="Message";
		generate="Generate";
		comment="Comments";
		begin="Begin";
		end="Final";
		token="Tokens";
		overwrite="This plugin will overwrite your scanner.flex and parser.cup files, so take a minute to make a backup.";
	}
	public static void ES() {
		English=false;
		lang="English";
		name="Nombre";
		sym="Simbolo";
		error="Error";
		exp="Expresion";
		add="Añadir";
		del="Borrar";
		msg="Mensaje";
		generate="Generar";
		comment="Comentarios";
		begin="Inicio";
		end="Final";
		token="Tokens";
		overwrite="Este plugin sobreescribe los ficheros scanner.flex y parser.cup, por lo que recomendamos que haga una copia de seguridad.";
	}
	
	public static boolean isEN() {return English;}
	
}
