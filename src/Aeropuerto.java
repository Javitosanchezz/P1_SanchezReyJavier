
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Aeropuerto {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int opcion;

        do {
            System.out.println("================================");
            System.out.println("   GESTION DEL AEROPUERTO");
            System.out.println("================================");
            System.out.println("1.Gestion de pasajeros");
            System.out.println("2.Gestion de vuelos");
            System.out.println("3.Gestion de reservas");
            System.out.println("4.Gestion de equipajes");
            System.out.println("5.Gestion de embarques");
            System.out.println("6.Gestion de incidencias");
            System.out.println("7.Gestion de configuracion");
            System.out.println("8.Operativa del aeropuerto");
            System.out.println("9.Cuadro de mando e informes");
            System.out.println("0. Salir");

			try {
				opcion = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				if (sc.hasNextLine() == false) opcion = 0;
				else opcion = -1;
			}

            switch (opcion) {
                case 1:
                    gestionpasajeros();
                    break;
                case 2:
                    gestionvuelos();
                    break;
                case 3:
                    gestionreservas();
                    break;
                case 4:
                    gestionequipajes();
                    break;
                case 5:
                    gestionembarques();
                    break;
                case 6:
                    gestionincidencias();
                    break;
                case 7:
                    gestionconfiguracion();
                    break;
                case 8:
                    operativaaeropuerto();
                    break;
                case 9:
                    cuadromandos();
                    break;
                case 0:
                    break;                    
                default:
                    System.out.println("Opcion incorrecta.");
            }

		} while (opcion == 0 == false);

        sc.close();
    }

	private static void cuadromandos() {
		boolean salir = false;
		do {
			System.out.println("CUADRO DE MANDO");
			System.out.println("1.Resumen general");
			System.out.println("2.Ocupacion por vuelo");
			System.out.println("3.Exportar informes CSV");
			System.out.println("4.Diagnostico de datos");
			System.out.println("0.Volver");
			int opcionmenu = -1;
			try {
				opcionmenu = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				opcionmenu = -1;
			}
			switch (opcionmenu)
			{
				case 1:
					resumengeneral();
					break;
				case 2:
					mostrarocupacion();
					break;
				case 3:
					exportarinformes();
					break;
				case 4:
					diagnosticodatos();
					break;
				case 0:
					salir = true;
					break;
				default: 
					System.out.println("Opcion incorrecta.");
			}
		} while (salir == false);
	}

	private static void comprobarcheckin() {
		System.out.println("Introduce el codigo de la reserva:");
		String codigoreserva = sc.nextLine();
		System.out.println("Introduce fecha y hora de simulacion AAAA-MM-DDThh:mm:");
		String textosimulacion = sc.nextLine();
		try {
			java.time.LocalDateTime simulacion = java.time.LocalDateTime.parse(textosimulacion);
			String[] reservaencontrada = null;
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (datos.length == 10 && codigoreserva.equalsIgnoreCase(datos[0])) {
						reservaencontrada = datos;
					}
				}
			}
			if (reservaencontrada == null) {
				System.out.println("No existe la reserva.");
				return;
			}
			if ("CONFIRMADA".equalsIgnoreCase(reservaencontrada[7]) == false) {
				System.out.println("La reserva no esta CONFIRMADA.");
				return;
			}
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			NodeList contenedores = doc.getDocumentElement().getElementsByTagName("Vuelos");
			if (contenedores.getLength() == 0) {
				System.out.println("No existe el contenedor de vuelos.");
				return;
			}
			NodeList vuelos = ((Element) contenedores.item(0)).getElementsByTagName("Vuelo");
			Element vuelo = null;
			for (int i = 0; i < vuelos.getLength(); i++) {
				Element actual = (Element) vuelos.item(i);
				if (reservaencontrada[2].equalsIgnoreCase(actual.getAttribute("codigo"))) {
					vuelo = actual;
				}
			}
			if (vuelo == null) {
				System.out.println("No existe el vuelo de la reserva.");
				return;
			}
			if ("CANCELADO".equalsIgnoreCase(vuelo.getAttribute("estado"))) {
				System.out.println("El vuelo esta CANCELADO.");
				return;
			}
			Element destino = (Element) vuelo.getElementsByTagName("Destino").item(0);
			String codigodestino = destino.getAttribute("codigo");
			String tipodestino = "";
			NodeList catalogos = doc.getDocumentElement().getElementsByTagName("Destinos");
			if (catalogos.getLength() > 0) {
				NodeList destinos = ((Element) catalogos.item(0)).getElementsByTagName("Destino");
				for (int i = 0; i < destinos.getLength(); i++) {
					Element actual = (Element) destinos.item(i);
					if (codigodestino.equalsIgnoreCase(actual.getAttribute("codigo"))) {
						tipodestino = actual.getAttribute("tipo");
					}
				}
			}
			if (tipodestino.length() == 0) {
				System.out.println("No se ha encontrado el tipo de destino.");
				return;
			}
			Element salida = (Element) vuelo.getElementsByTagName("Salida").item(0);
			String fecha = salida.getAttribute("fecha");
			String hora = salida.getElementsByTagName("Hora").item(0).getTextContent().trim();
			java.time.LocalDateTime programada = java.time.LocalDateTime.parse(fecha + "T" + hora);
			JsonNode configuracion = new ObjectMapper().readTree(Path.of("datos/configuracion.json").toFile());
			JsonNode franjas = configuracion.at("/aeropuerto/embarque/franjas");
			int apertura = -1;
			int cierre = -1;
			for (JsonNode franja : franjas) {
				if (tipodestino.equalsIgnoreCase(franja.path("tipo").asText())) {
					apertura = franja.path("inicioMinutosAntes").asInt(-1);
					cierre = franja.path("cierreMinutosAntes").asInt(-1);
				}
			}
			if (apertura < 0 || cierre < 0) {
				System.out.println("No existe franja para el destino.");
				return;
			}
			java.time.LocalDateTime inicio = programada.minusMinutes(apertura);
			java.time.LocalDateTime fin = programada.minusMinutes(cierre);
			System.out.println("Ventana: " + inicio + " hasta " + fin + " (limites incluidos)");
			if (simulacion.isBefore(inicio)) {
				System.out.println("Aun es pronto para iniciar el check-in.");
			}
			else if (simulacion.isAfter(fin)) {
				System.out.println("La ventana de check-in esta cerrada.");
			}
			else {
				System.out.println("Se puede iniciar el check-in.");
				if (simulacion.isAfter(fin.minusMinutes(10))) {
					System.out.println("Aviso: la ventana esta proxima al cierre.");
				}
			}
		}
		catch (Exception ex) {
			System.out.println("Fecha, hora o datos no validos para el check-in.");
		}
	}

	private static void consultarpendientes() {
		System.out.println("Introduce el codigo del vuelo:");
		String codigovuelo = sc.nextLine();
		System.out.println("Introduce fecha y hora de simulacion AAAA-MM-DDThh:mm:");
		String textosimulacion = sc.nextLine();
		try {
			java.time.LocalDateTime simulacion = java.time.LocalDateTime.parse(textosimulacion);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			NodeList contenedores = doc.getDocumentElement().getElementsByTagName("Vuelos");
			NodeList vuelos = ((Element) contenedores.item(0)).getElementsByTagName("Vuelo");
			Element vuelo = null;
			for (int i = 0; i < vuelos.getLength(); i++) {
				Element actual = (Element) vuelos.item(i);
				if (codigovuelo.equalsIgnoreCase(actual.getAttribute("codigo"))) {
					vuelo = actual;
				}
			}
			if (vuelo == null) {
				System.out.println("No existe el vuelo.");
				return;
			}
			String codigodestino = ((Element) vuelo.getElementsByTagName("Destino").item(0)).getAttribute("codigo");
			String tipodestino = "";
			NodeList catalogos = doc.getDocumentElement().getElementsByTagName("Destinos");
			NodeList destinos = ((Element) catalogos.item(0)).getElementsByTagName("Destino");
			for (int i = 0; i < destinos.getLength(); i++) {
				Element destino = (Element) destinos.item(i);
				if (codigodestino.equalsIgnoreCase(destino.getAttribute("codigo"))) {
					tipodestino = destino.getAttribute("tipo");
				}
			}
			Element salida = (Element) vuelo.getElementsByTagName("Salida").item(0);
			String hora = salida.getElementsByTagName("Hora").item(0).getTextContent().trim();
			java.time.LocalDateTime programada = java.time.LocalDateTime.parse(salida.getAttribute("fecha") + "T" + hora);
			JsonNode configuracion = new ObjectMapper().readTree(Path.of("datos/configuracion.json").toFile());
			JsonNode franjas = configuracion.at("/aeropuerto/embarque/franjas");
			int apertura = -1;
			int cierre = -1;
			for (JsonNode franja : franjas) {
				if (tipodestino.equalsIgnoreCase(franja.path("tipo").asText())) {
					apertura = franja.path("inicioMinutosAntes").asInt(-1);
					cierre = franja.path("cierreMinutosAntes").asInt(-1);
				}
			}
			java.time.LocalDateTime inicio = programada.minusMinutes(apertura);
			java.time.LocalDateTime fin = programada.minusMinutes(cierre);
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] reserva = linea.split(",", -1);
					if (reserva.length == 10 && codigovuelo.equalsIgnoreCase(reserva[2]) && "CONFIRMADA".equalsIgnoreCase(reserva[7])) {
						boolean encontrado = false;
						try (BufferedReader lector2 = Files.newBufferedReader(Path.of("datos/embarques.csv"))) {
							String lineaembarque;
							while ((lineaembarque = lector2.readLine()) != null) {
								String[] embarque = lineaembarque.split(",", -1);
								if (embarque.length == 6 && reserva[0].equalsIgnoreCase(embarque[1])) {
									encontrado = true;
									System.out.println(reserva[0] + " -> " + embarque[4]);
								}
							}
						}
						if (encontrado == false) {
							System.out.print(reserva[0] + " -> PENDIENTE: ");
							if (simulacion.isBefore(inicio)) {
								System.out.println("antes de apertura");
							}
							else if (simulacion.isAfter(fin)) {
								System.out.println("ventana cerrada");
							}
							else {
								System.out.println("ventana abierta");
							}
						}
					}
				}
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudieron consultar los pendientes.");
		}
	}

	private static void resumenequipajesvuelo() {
		System.out.println("Introduce el codigo del vuelo:");
		String codigovuelo = sc.nextLine();
		java.util.ArrayList<String> reservasvuelo = new java.util.ArrayList<String>();
		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 10 && codigovuelo.equalsIgnoreCase(datos[2])) {
					reservasvuelo.add(datos[0]);
				}
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudieron leer las reservas.");
			return;
		}
		int bultos = 0;
		int perdidos = 0;
		double pesototal = 0;
		int excesos = 0;
		java.util.ArrayList<String> tipos = new java.util.ArrayList<String>();
		java.util.ArrayList<Integer> contadortipos = new java.util.ArrayList<Integer>();
		try {
			JsonNode configuracion = new ObjectMapper().readTree(Path.of("datos/configuracion.json").toFile());
			JsonNode reglas = configuracion.at("/aeropuerto/equipaje/reglas");
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/equipajes.csv"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (datos.length != 6) {
						continue;
					}
					if (reservasvuelo.contains(datos[1]) == false) {
						continue;
					}
					bultos++;
					try {
						pesototal += Double.parseDouble(datos[2]);
					}
					catch (Exception ex) {
						System.out.println("Peso no evaluable en " + datos[0]);
					}
					if ("PERDIDO".equalsIgnoreCase(datos[4])) {
						perdidos++;
					}
					int posiciontipo = -1;
					for (int i = 0; i < tipos.size(); i++) {
						if (tipos.get(i).equalsIgnoreCase(datos[3])) {
							posiciontipo = i;
						}
					}
					if (posiciontipo < 0) {
						tipos.add(datos[3]);
						contadortipos.add(1);
					}
					else {
						contadortipos.set(posiciontipo, contadortipos.get(posiciontipo) + 1);
					}
					for (JsonNode regla : reglas) {
						if (datos[3].equalsIgnoreCase(regla.path("tipo").asText())) {
							double limite = regla.path("pesoMaximo").asDouble(-1);
							try {
								double peso = Double.parseDouble(datos[2]);
								if (limite >= 0 && peso > limite) {
									excesos++;
									System.out.println("Exceso " + datos[0] + ": peso=" + peso + " limite=" + limite + " diferencia=" + (peso - limite));
								}
							}
							catch (Exception ex) {
								System.out.println("Peso no evaluable en " + datos[0]);
							}
						}
					}
				}
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudo leer la configuracion del equipaje.");
			return;
		}
		System.out.println("Vuelo " + codigovuelo + ": bultos=" + bultos + " pesoTotal=" + pesototal + " perdidos=" + perdidos + " excesos=" + excesos);
		for (int i = 0; i < tipos.size(); i++) {
			System.out.println("Tipo " + tipos.get(i) + ": " + contadortipos.get(i));
		}
	}

	private static void impactoincidencia() {
		System.out.println("Introduce el codigo de la incidencia:");
		String codigo = sc.nextLine();
		String codigovuelo = "";
		String lineaencontrada = "";
		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/incidencias.txt"))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split("\\|", -1);
				if (datos.length == 8 && codigo.equalsIgnoreCase(datos[1])) {
					lineaencontrada = linea;
					codigovuelo = datos[2];
				}
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudo leer la incidencia.");
			return;
		}
		if (lineaencontrada.length() == 0) {
			System.out.println("No se ha encontrado la incidencia.");
			return;
		}
		System.out.println("Incidencia: " + lineaencontrada);
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			NodeList contenedores = doc.getDocumentElement().getElementsByTagName("Vuelos");
			NodeList vuelos = ((Element) contenedores.item(0)).getElementsByTagName("Vuelo");
			Element vuelo = null;
			for (int i = 0; i < vuelos.getLength(); i++) {
				Element actual = (Element) vuelos.item(i);
				if (codigovuelo.equalsIgnoreCase(actual.getAttribute("codigo"))) {
					vuelo = actual;
				}
			}
			if (vuelo == null) {
				System.out.println("Vuelo no encontrado; se muestran solo los datos de la incidencia.");
				return;
			}
			Element origen = (Element) vuelo.getElementsByTagName("Origen").item(0);
			Element destino = (Element) vuelo.getElementsByTagName("Destino").item(0);
			System.out.println("Ruta " + origen.getAttribute("codigo") + " -> " + destino.getAttribute("codigo"));
			System.out.println("Estado del vuelo: " + vuelo.getAttribute("estado"));
			java.util.ArrayList<String> reservasvuelo = new java.util.ArrayList<String>();
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (datos.length == 10 && codigovuelo.equalsIgnoreCase(datos[2]) && "CONFIRMADA".equalsIgnoreCase(datos[7])) {
						reservasvuelo.add(datos[0]);
						System.out.println("Reserva confirmada: " + datos[0] + " pasajero=" + datos[1]);
					}
				}
			}
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/equipajes.csv"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (datos.length == 6 && reservasvuelo.contains(datos[1])) {
						System.out.println("Equipaje asociado: " + datos[0]);
					}
				}
			}
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/embarques.csv"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (datos.length == 6 && reservasvuelo.contains(datos[1])) {
						System.out.println("Embarque asociado: " + datos[0]);
					}
				}
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudo mostrar el impacto completo.");
		}
	}

	private static void operativaaeropuerto() {
		boolean salir = false;
		do {
			System.out.println("OPERATIVA DEL AEROPUERTO");
			System.out.println("1.Gestionar reservas");
			System.out.println("2.Gestionar equipajes");
			System.out.println("3.Gestionar embarques");
			System.out.println("4.Gestionar incidencias");
			System.out.println("5.Ocupacion y sobreventa");
			System.out.println("6.Comprobar check-in");
			System.out.println("7.Consultar pendientes de embarque");
			System.out.println("8.Resumen de equipajes por vuelo");
			System.out.println("9.Impacto de una incidencia");
			System.out.println("0.Volver");
			int opcionmenu = -1;
			try {
				opcionmenu = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				opcionmenu = -1;
			}
			switch (opcionmenu) {
			case 1:
				gestionreservas();
				break;
			case 2:
				gestionequipajes();
				break;
			case 3:
				gestionembarques();
				break;
			case 4:
				gestionincidencias();
				break;
			case 5:
				mostrarocupacion();
				break;
			case 6:
				comprobarcheckin();
				break;
			case 7:
				consultarpendientes();
				break;
			case 8:
				resumenequipajesvuelo();
				break;
			case 9:
				impactoincidencia();
				break;
			case 0:
				salir = true;
				break;
			default: System.out.println("Opcion incorrecta.");
			}
		} while (salir == false);
	}

	private static void gestionconfiguracion() {
		Path ruta = Path.of("datos", "configuracion.json");
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode raiz = mapper.readTree(ruta.toFile());
			boolean salir = false;
			do {
				System.out.println("CONFIGURACION");
				System.out.println("1. Mostrar configuracion");
				System.out.println("2. Consultar una ruta JSON Pointer");
				System.out.println("3. Modificar un parametro escalar");
				System.out.println("4. Borrar un parametro opcional");
				System.out.println("0. Volver");
			int opcionmenu = -1;
			try {
				opcionmenu = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				opcionmenu = -1;
			}
			switch (opcionmenu) {
				case 1:
					System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(raiz));
					break;
				case 2:
					System.out.print("Introduce la ruta: ");
					String rutajson = sc.nextLine();
					JsonNode encontrado = raiz.at(rutajson);
					if (encontrado.isMissingNode()) System.out.println("No existe ese parametro.");
					else System.out.println(encontrado.toString());
					break;
				case 3:
					System.out.print("Introduce la ruta: ");
					String rutamodificar = sc.nextLine();
					JsonNode actualmodificar = raiz.at(rutamodificar);
					if (actualmodificar.isMissingNode() == true || actualmodificar.isValueNode() == false) {
						System.out.println("Solo se pueden modificar parametros simples.");
					}
					else {
						System.out.print("Nuevo valor: ");
						String textomodificar = sc.nextLine();
						JsonNode nuevomodificar;
						try {
							if (actualmodificar.isBoolean()) {
								nuevomodificar = mapper.getNodeFactory().booleanNode(Boolean.parseBoolean(textomodificar));
							} else if (actualmodificar.isInt() || actualmodificar.isLong()) {
								nuevomodificar = mapper.getNodeFactory().numberNode(Long.parseLong(textomodificar));
							} else if (actualmodificar.isFloatingPointNumber()) {
								nuevomodificar = mapper.getNodeFactory().numberNode(Double.parseDouble(textomodificar));
							} else {
								nuevomodificar = mapper.getNodeFactory().textNode(textomodificar);
							}
							String padretexto = rutamodificar.substring(0, rutamodificar.lastIndexOf('/'));
							String ultimotexto = rutamodificar.substring(rutamodificar.lastIndexOf('/') + 1).replace("~1", "/").replace("~0", "~");
							JsonNode padremodificar = raiz.at(padretexto);
							if (padremodificar.isObject()) {
								((ObjectNode) padremodificar).set(ultimotexto, nuevomodificar);
								Path temporalmodificar = Path.of("datos/configuracion.json.tmp");
								mapper.writerWithDefaultPrettyPrinter().writeValue(temporalmodificar.toFile(), raiz);
								Files.move(temporalmodificar, ruta, StandardCopyOption.REPLACE_EXISTING);
								System.out.println("Configuracion guardada.");
							} else {
								System.out.println("La ruta no tiene un objeto padre.");
							}
						} catch (Exception ex) {
							System.out.println("Valor o ruta no validos.");
						}
					}
					break;
				case 4:
					System.out.print("Introduce la ruta: "); String rutaborrar = sc.nextLine();
					if (rutaborrar.startsWith("/aeropuerto/equipaje/estadosPermitidos") || rutaborrar.startsWith("/aeropuerto/reservas/estadosPermitidos") || rutaborrar.startsWith("/aeropuerto/vuelos/estadosPermitidos") || rutaborrar.startsWith("/aeropuerto/embarque/estadosPermitidos")) {
						System.out.println("No se borra: es una configuracion imprescindible.");
					}
					else {
						try {
							String padreborrarexto = rutaborrar.substring(0, rutaborrar.lastIndexOf('/'));
							String ultimoborrarexto = rutaborrar.substring(rutaborrar.lastIndexOf('/') + 1).replace("~1", "/").replace("~0", "~");
							JsonNode padreborrar = raiz.at(padreborrarexto);
							if (padreborrar.isObject() && ((ObjectNode) padreborrar).remove(ultimoborrarexto) != null) {
								Path temporalborrar = Path.of("datos/configuracion.json.tmp");
								mapper.writerWithDefaultPrettyPrinter().writeValue(temporalborrar.toFile(), raiz);
								Files.move(temporalborrar, ruta, StandardCopyOption.REPLACE_EXISTING);
								System.out.println("Configuracion guardada.");
							} else {
								System.out.println("No existe ese parametro.");
							}
						} catch (Exception ex) {
							System.out.println("Ruta no valida.");
						}
					}
					break;
				case 0:
					salir = true;
					break;
				default:
					System.out.println("Opcion incorrecta.");
				}
		} while (salir == false);
		} catch (Exception ex) {
			System.out.println("No se pudo leer la configuracion: " + ex.getMessage());
		}
	}


	private static void resumengeneral() {
		int totalpasajeros = 0;
		int totalreservas = 0;
		int totalequipajes = 0;
		int totalembarques = 0;
		int totalincidencias = 0;
		String linearesumen;

		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/pasajeros.csv"))) {
			while ((linearesumen = lector.readLine()) == null == false) {
				totalpasajeros++;
			}
		} catch (Exception ex) {
		}

		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) {
			while ((linearesumen = lector.readLine()) == null == false) {
				totalreservas++;
			}
		} catch (Exception ex) {
		}

		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/equipajes.csv"))) {
			while ((linearesumen = lector.readLine()) == null == false) {
				totalequipajes++;
			}
		} catch (Exception ex) {
		}

		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/embarques.csv"))) {
			while ((linearesumen = lector.readLine()) == null == false) {
				totalembarques++;
			}
		} catch (Exception ex) {
		}

		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/incidencias.txt"))) {
			while ((linearesumen = lector.readLine()) == null == false) {
				totalincidencias++;
			}
		} catch (Exception ex) {
		}

		System.out.println("Pasajeros: " + totalpasajeros);
		System.out.println("Reservas: " + totalreservas);
		System.out.println("Equipajes: " + totalequipajes);
		System.out.println("Embarques: " + totalembarques);
		System.out.println("Incidencias: " + totalincidencias);

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			NodeList contenedores = doc.getDocumentElement().getElementsByTagName("Vuelos");
			System.out.println("Vuelos: " + ((Element) contenedores.item(0)).getElementsByTagName("Vuelo").getLength());
		} catch (Exception ex) {
			System.out.println("No se pudo leer el XML.");
		}
	}

	private static void mostrarocupacion() {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			NodeList vuelos = ((Element) doc.getDocumentElement().getElementsByTagName("Vuelos").item(0)).getElementsByTagName("Vuelo");
			for (int i = 0; i < vuelos.getLength(); i++) {
				Element vuelo = (Element) vuelos.item(i);
				String codigo = vuelo.getAttribute("codigo");
				int confirmadas = 0;
				try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) {
					String linea; int numero = 0;
					while ((linea = lector.readLine()) != null) {
						numero++; String[] datos = linea.split(",", -1);
						if (datos.length == 10 && codigo.equalsIgnoreCase(datos[2]) && "CONFIRMADA".equalsIgnoreCase(datos[7])) {
							confirmadas++;
						}
					}
				}
				int capacidad = capacidadavion(doc, vuelo.getAttribute("avion"));
				int libres = capacidad > 0 ? Math.max(capacidad - confirmadas, 0) : 0;
				int sobreventa = capacidad > 0 ? Math.max(confirmadas - capacidad, 0) : 0;
				String porcentaje = capacidad > 0 ? String.format("%.2f%%", confirmadas * 100.0 / capacidad) : "no calculable";
				System.out.println(codigo + " | capacidad=" + capacidad + " | confirmadas=" + confirmadas + " | libres=" + libres + " | sobreventa=" + sobreventa + " | ocupacion=" + porcentaje);
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudo calcular la ocupacion: " + ex.getMessage());
		}
	}

	private static int capacidadavion(Document doc, String matricula) {
		NodeList aviones = doc.getElementsByTagName("Avion");
		for (int i = 0; i < aviones.getLength(); i++) {
			Element avion = (Element) aviones.item(i);
			if (matricula.equalsIgnoreCase(avion.getAttribute("matricula"))) {
				NodeList pasajeros = avion.getElementsByTagName("Pasajeros");
				if (pasajeros.getLength() > 0) try { return Integer.parseInt(pasajeros.item(0).getTextContent().trim());
					}
					catch (NumberFormatException ex) {
						return 0;
					}
			}
		}
		return 0;
	}

	private static void diagnosticodatos() {
		System.out.println("DIAGNOSTICO (no modifica datos)");
		comprobarcsv("datos/pasajeros.csv", 7, "pasajeros");
		comprobarcsv("datos/reservas.csv", 10, "reservas");
		comprobarcsv("datos/equipajes.csv", 6, "equipajes");
		comprobarcsv("datos/embarques.csv", 6, "embarques");
		comprobarincidencias();
		comprobarreferencias();
	}

	private static void comprobarreferencias() {
		java.util.ArrayList<String> pasajeros = new java.util.ArrayList<String>();
		java.util.ArrayList<String> reservas = new java.util.ArrayList<String>();
		java.util.ArrayList<String> vuelos = new java.util.ArrayList<String>();

		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/pasajeros.csv"))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 7) {
					pasajeros.add(datos[0]);
				}
			}
		}
		catch (Exception ex) {
		}

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			NodeList lista = ((Element) doc.getDocumentElement().getElementsByTagName("Vuelos").item(0)).getElementsByTagName("Vuelo");
			for (int i = 0; i < lista.getLength(); i++) {
				vuelos.add(((Element) lista.item(i)).getAttribute("codigo"));
			}
		}
		catch (Exception ex) {
		}

		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) {
			String linea;
			int numero = 0;
			while ((linea = lector.readLine()) != null) {
				numero++;
				String[] datos = linea.split(",", -1);
				if (datos.length == 10) {
					reservas.add(datos[0]);
					if (pasajeros.contains(datos[1]) == false) {
						System.out.println("reservas.csv, linea " + numero + ": pasajero inexistente " + datos[1]);
					}
					if (vuelos.contains(datos[2]) == false) {
						System.out.println("reservas.csv, linea " + numero + ": vuelo inexistente " + datos[2]);
					}
				}
			}
		}
		catch (Exception ex) {
		}

		comprobarrelacion("datos/equipajes.csv", reservas, 1, "equipajes.csv");
		comprobarrelacion("datos/embarques.csv", reservas, 1, "embarques.csv");
	}

	private static void comprobarrelacion(String archivo, java.util.ArrayList<String> referencias, int posicion, String etiqueta) {
		try (BufferedReader lector = Files.newBufferedReader(Path.of(archivo))) {
			String linea;
			int numero = 0;
			java.util.ArrayList<String> vistos = new java.util.ArrayList<String>();
			while ((linea = lector.readLine()) != null) {
				numero++;
				String[] datos = linea.split(",", -1);
				if (datos.length <= posicion) {
					continue;
				}
				if (referencias.contains(datos[posicion]) == false) {
					System.out.println(etiqueta + ", linea " + numero + ": reserva inexistente " + datos[posicion]);
				}
				if ("embarques.csv".equals(etiqueta) && vistos.contains(datos[posicion])) {
					System.out.println(etiqueta + ", linea " + numero + ": varios embarques para " + datos[posicion]);
				}
				vistos.add(datos[posicion]);
			}
		}
		catch (Exception ex) {
		}
	}

	private static void comprobarcsv(String nombre, int camposesperados, String tipo) {
		java.util.ArrayList<String> codigos = new java.util.ArrayList<String>();
		java.util.ArrayList<String> asientos = new java.util.ArrayList<String>();

		try (BufferedReader lector = Files.newBufferedReader(Path.of(nombre))) {
			String linea;
			int numero = 0;
			while ((linea = lector.readLine()) != null) {
				numero++;
				String[] datos = linea.split(",", -1);
				if (datos.length != camposesperados) {
					System.out.println(nombre + ", linea " + numero + ": campos incorrectos");
					continue;
				}
				if (datos == null) {
					continue;
				}
				if (codigos.contains(datos[0])) {
					System.out.println(nombre + ", linea " + numero + ": codigo duplicado " + datos[0]);
				}
				codigos.add(datos[0]);

				if ("reservas".equals(tipo)) {
					boolean fechavalida = true;
					try {
						java.time.LocalDate.parse(datos[8]);
					}
					catch (Exception ex) {
						fechavalida = false;
					}
					if (fechavalida == false) {
						System.out.println(nombre + ", linea " + numero + ": fecha incorrecta");
					}

					try {
						double precio = Double.parseDouble(datos[5]);
						if (Double.isFinite(precio) == false || precio < 0) {
							System.out.println(nombre + ", linea " + numero + ": precio no valido");
						}
					}
					catch (NumberFormatException ex) {
						System.out.println(nombre + ", linea " + numero + ": precio no numerico");
					}

					String asiento = datos[2] + "|" + datos[3];
					if (asientos.contains(asiento)) {
						System.out.println(nombre + ", linea " + numero + ": asiento repetido en vuelo " + datos[2]);
					}
					asientos.add(asiento);
				}

				if ("equipajes".equals(tipo)) {
					try {
						double peso = Double.parseDouble(datos[2]);
						if (Double.isFinite(peso) == false || peso < 0) {
							System.out.println(nombre + ", linea " + numero + ": peso no valido");
						}
					}
					catch (NumberFormatException ex) {
						System.out.println(nombre + ", linea " + numero + ": peso no numerico");
					}
				}

				if ("embarques".equals(tipo)) {
					boolean fechahoravalida = true;
					try {
						java.time.LocalDateTime.parse(datos[2]);
					}
					catch (Exception ex) {
						fechahoravalida = false;
					}
					if (fechahoravalida == false) {
						System.out.println(nombre + ", linea " + numero + ": fecha y hora incorrectas");
					}
				}
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudo comprobar " + nombre + ": " + ex.getMessage());
		}
	}

	private static void comprobarincidencias() {
		String nombre = "datos/incidencias.txt";
		try (BufferedReader lector = Files.newBufferedReader(Path.of(nombre))) {
			String linea;
			int numero = 0;
			while ((linea = lector.readLine()) != null) {
				numero++;
				String[] datos = linea.split("\\|", -1);
				if (datos.length != 8) {
					System.out.println(nombre + ", linea " + numero + ": se esperaban 8 campos y hay " + datos.length);
					continue;
				}
				boolean fechavalidaincidencia = true;
				try {
					java.time.LocalDate.parse(datos[0]);
				}
				catch (Exception ex) {
					fechavalidaincidencia = false;
				}
				if (fechavalidaincidencia == false) {
					try {
						java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy").parse(datos[0]);
						fechavalidaincidencia = true;
					}
					catch (Exception ex) {
						fechavalidaincidencia = false;
					}
				}
				if (fechavalidaincidencia == false) {
					System.out.println(nombre + ", linea " + numero + ": fecha incorrecta");
				}
				try {
					int minutos = Integer.parseInt(datos[4]);
					if (minutos < 0) {
						System.out.println(nombre + ", linea " + numero + ": duracion negativa");
					}
				}
				catch (NumberFormatException ex) {
					System.out.println(nombre + ", linea " + numero + ": duracion no numerica");
				}
				if ("ABIERTA".equals(datos[7]) == false && "CERRADA".equals(datos[7]) == false) {
					System.out.println(nombre + ", linea " + numero + ": estado no permitido " + datos[7]);
				}
			}
		}
		catch (Exception ex) {
			System.out.println("No se pudo comprobar " + nombre + ": " + ex.getMessage());
		}
	}

	private static void exportarinformes() {
		Path ocupacion = siguientenombre("informes_ocupacion", ".csv");
		Path exceso = siguientenombre("informes_equipajes_exceso", ".csv");
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			try (BufferedWriter escritor = Files.newBufferedWriter(ocupacion)) {
			NodeList vuelos = ((Element) doc.getDocumentElement().getElementsByTagName("Vuelos").item(0)).getElementsByTagName("Vuelo");
				for (int i = 0; i < vuelos.getLength(); i++) {
					Element vuelo = (Element) vuelos.item(i); String codigo = vuelo.getAttribute("codigo");
					int confirmadas = reservasconfirmadas(codigo); int capacidad = capacidadavion(doc, vuelo.getAttribute("avion"));
					escritor.write(codigo + "," + capacidad + "," + confirmadas + "," + Math.max(capacidad - confirmadas, 0) + "," + Math.max(confirmadas - capacidad, 0));
						escritor.newLine();
				}
			}
			try (BufferedWriter escritor = Files.newBufferedWriter(exceso);
				BufferedReader lector = Files.newBufferedReader(Path.of("datos/equipajes.csv"))) {
				String linea; int numero = 0;
				while ((linea = lector.readLine()) != null) { numero++;
					String[] datos = linea.split(",", -1);
					if (datos.length != 6) {
						continue;
					}
					double limite = limiteequipaje(datos[3]);
					if (limite >= 0 && Double.parseDouble(datos[2]) > limite) {
						escritor.write(datos[0] + "," + datos[1] + "," + datos[2] + "," + datos[3] + "," + limite + "," + (Double.parseDouble(datos[2]) - limite));
					escritor.newLine();
					} }
			}
			System.out.println("Exportados: " + ocupacion + " y " + exceso);
		}
		catch (Exception ex) {
			System.out.println("No se pudieron exportar informes: " + ex.getMessage());
		}
	}

	private static int reservasconfirmadas(String vuelo) {
		int total = 0;
		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) { String linea;
			while ((linea = lector.readLine()) != null) { String[] datos = linea.split(",", -1);
			if (datos.length == 10 && vuelo.equalsIgnoreCase(datos[2]) && "CONFIRMADA".equalsIgnoreCase(datos[7])) {
				total++;
			}
			}
		}
		catch (Exception ex) {
		}
		return total;
	}

	private static double limiteequipaje(String tipo) {
		try {
			JsonNode raiz = new ObjectMapper().readTree(Path.of("datos/configuracion.json").toFile()).at("/aeropuerto/equipaje/reglas");
			for (JsonNode regla : raiz) {
				if (tipo.equalsIgnoreCase(regla.path("tipo").asText())) {
					return regla.path("pesoMaximo").asDouble(-1);
				}
			}
		}
		catch (Exception ex) {
		}
		return -1;
	}

	private static Path siguientenombre(String base, String extension) {
		Path ruta = Path.of("datos", base + extension); int numero = 1;
		while (Files.exists(ruta)) {
			ruta = Path.of("datos", base + "_" + numero + extension);
			numero++;
		}
		return ruta;
	}	
	
	private static void gestionvuelos() {
		
		int opcion = 0;
		boolean salir = false;
		salir=false;
		
		do {
			
			System.out.println("1.Consultar Vuelos");
			System.out.println("2.Crear Vuelos");
			System.out.println("3.Modificar Vuelos");
			System.out.println("4.Borrar Vuelos");
			System.out.println("5.Salir");
			
			try {
				opcion = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				opcion = -1;
			}
			
			switch(opcion) {
			
			case 1:
				consultarvuelos();
				break;
			case 2:
				crearvuelos();
				break;
			case 3:
				modificarvuelos();
				break;
			case 4:
				borrarvuelos();
				break;
			case 5:
				salir = true;
				break;
			default: System.out.println("Opcion Incorrecta");
				salir=true;
				break;
			}
			
		}while(salir==false);
		
	}
	
	private static void consultarvuelos() {
		
		int opcion;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar los vuelos");
			System.out.println("2.Buscar por codigo");
			System.out.println("3.Salir");
			
			try {
				opcion = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				opcion = -1;
			}
					
			switch(opcion) {
			
			case 1:
				mostrarvuelos();
				break;
			case 2:
				buscarvuelos();
				break;
			case 3:
				salir = true;
				break;
			default: System.out.println("Incorrecto");
			salir=true;
				break;	
			}
			
		}while(salir==false);
		
		
	}
	
	private static void mostrarvuelos() {

        Document doc = null;
        

        try {
        	
        	Path path = Path.of("datos", "aeropuerto.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringComments(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            doc = builder.parse(path.toFile());
            
            NodeList aeropuertos = doc.getElementsByTagName("Aeropuerto");

            for (int i = 0; i < aeropuertos.getLength(); i++) {
            	
            	NodeList vuelos = doc.getElementsByTagName("Vuelo");
            	
            	for(int x=0;x<vuelos.getLength(); x++) {
            		
            		Element vuelo = (Element) vuelos.item(x);
            		
            		if(vuelo.hasAttributes()) {
            			
            			NamedNodeMap atributos = vuelo.getAttributes();
            			
            			for(int z=0;z<atributos.getLength(); z++) {
            				
            				Node atributo = atributos.item(z);
            				
            				System.out.print(atributo.getNodeName() + " = " + atributo.getNodeValue() + " || ");
            			
            			}
            			
            			System.out.println("\n -------------------");
            		}
            		
            		
            		
            	}

                
            }

        } catch (Exception ex) {

            System.out.println("HA HABIDO UN ERROR");
        }

        
    }
	
	private static void buscarvuelos() {
		
		String respuesta;
		Document doc = null;
		boolean existe = false;
		
		System.out.println("introduce el codigo del vuelo que quieras buscar: ");
		respuesta=sc.nextLine();
		
		try {
        	
        	Path path = Path.of("datos", "aeropuerto.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringComments(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            doc = builder.parse(path.toFile());
            
            NodeList aeropuertos = doc.getElementsByTagName("Aeropuerto");

            for (int i = 0; i < aeropuertos.getLength(); i++) {
            	
            	NodeList vuelos = doc.getElementsByTagName("Vuelo");
            	
            	for(int x=0;x<vuelos.getLength(); x++) {
            		
            		Element vuelo = (Element) vuelos.item(x);
            		
            		if(vuelo.hasAttributes()) {
            			
            			NamedNodeMap atributos = vuelo.getAttributes();
            			
            			for(int z=0;z<atributos.getLength(); z++) {
            				
            				            				
            				Node atributo = atributos.item(z);
            				
            				if(atributo.getNodeName().equalsIgnoreCase("codigo") && atributo.getNodeValue().equalsIgnoreCase(respuesta)) {
            					
            					System.out.print(atributos.item(0).getNodeName() + " " + atributos.item(0).getNodeValue() + " || ");
                				System.out.print(atributos.item(1).getNodeName() + " " + atributos.item(1).getNodeValue() + " || ");
                				System.out.print(atributos.item(2).getNodeName() + " " + atributos.item(2).getNodeValue() + " || ");
                				System.out.println(atributos.item(3).getNodeName() + " " + atributos.item(3).getNodeValue() + " || ");
                				
            					existe = true;
            					break;
            				}
            				
            				
            				
            				
            				
            			}
            			
            			if(existe==true) {
            				
            				break;
            			}
            		
            			
            			
            		}
            		
            		
            		
            	}
            	if(existe==true) {
            		break;
            	}
                
            }
            
            
            
            if(existe==false) {
            	
            	System.out.println("No se ha encontrado ningun vuelo");
            }

        } catch (Exception ex) {

            System.out.println("HA HABIDO UN ERROR");
        }
		
		
		
	}
	
	private static void crearvuelos() {
		String codigo = sc.nextLine();
		String aerolinea = sc.nextLine();
		String avion = sc.nextLine();
		String estado = sc.nextLine();
		try {
			Path ruta = Path.of("datos/aeropuerto.xml");
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ruta.toFile());
			Element raiz = doc.getDocumentElement();
			Element vuelos = (Element) raiz.getElementsByTagName("Vuelos").item(0);
			NodeList lista = vuelos.getElementsByTagName("Vuelo");
			for (int i = 0; i < lista.getLength(); i++) {
				Element actual = (Element) lista.item(i);
				if (codigo.equalsIgnoreCase(actual.getAttribute("codigo"))) {
					System.out.println("Ya existe el vuelo.");
					return;
				}
			}
			Element catalogoaviones = (Element) raiz.getElementsByTagName("Aviones").item(0);
			NodeList listaaviones = catalogoaviones.getElementsByTagName("Avion");
			boolean existeavion = false;
			for (int i = 0; i < listaaviones.getLength(); i++) {
				if (avion.equalsIgnoreCase(((Element) listaaviones.item(i)).getAttribute("matricula"))) {
					existeavion = true;
				}
			}
			if (existeavion == false) {
				System.out.println("No existe el avion.");
				return;
			}
			if (codigo.length() == 0 || aerolinea.length() == 0 || estado.length() == 0) {
				System.out.println("Hay campos obligatorios vacios.");
				return;
			}
			Element nuevovuelo = doc.createElement("Vuelo");
			nuevovuelo.setAttribute("codigo", codigo);
			nuevovuelo.setAttribute("aerolinea", aerolinea);
			nuevovuelo.setAttribute("avion", avion);
			nuevovuelo.setAttribute("estado", estado);
			vuelos.appendChild(nuevovuelo);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(doc), new StreamResult(ruta.toFile()));
			System.out.println("Vuelo añadido.");
		}
		catch (Exception ex) {
			System.out.println("No se pudo añadir el vuelo.");
		}
	}
	
	private static void modificarvuelos() {
		
		String respuesta1,respuesta2,respuesta3,respuesta4;
		Document doc = null;
		boolean existe = false;
		
		System.out.println("introduce el codigo del vuelo que desee modificar: ");
		respuesta1=sc.nextLine();
		System.out.println("Introduce la aerolinea del vuelo");
		respuesta2=sc.nextLine();
		System.out.println("Introduce el avion");
		respuesta3=sc.nextLine();
		System.out.println("Introduce el estado del vuelo");
		respuesta4=sc.nextLine();
		
		try {
        	
        	Path path = Path.of("datos", "aeropuerto.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringComments(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            doc = builder.parse(path.toFile());
            
            NodeList aeropuertos = doc.getElementsByTagName("Aeropuerto");

            for (int i = 0; i < aeropuertos.getLength(); i++) {
            	
            	NodeList vuelos = doc.getElementsByTagName("Vuelo");
            	
            	for(int x=0;x<vuelos.getLength(); x++) {
            		
            		Element vuelo = (Element) vuelos.item(x);
            		
            		if(vuelo.hasAttributes()) {
            			
            			NamedNodeMap atributos = vuelo.getAttributes();
            			
            			for(int z=0;z<atributos.getLength(); z++) {
            				
            				Node atributo = atributos.item(z);
            				
            				if(atributo.getNodeName().equalsIgnoreCase("codigo") && atributo.getNodeValue().equalsIgnoreCase(respuesta1)) {
            					
            					existe=true;
            					break;
            				}
            				
            				
            			
            			}
            			
            			if(existe==true) {
            				
            				vuelo.setAttribute("aerolinea", respuesta2);
            				vuelo.setAttribute("avion", respuesta3);
            				vuelo.setAttribute("estado", respuesta4);
            				break;
            			}
            			
            			
            		}
            		
            	}
            	
            	if(existe==true) {
            		break;
            	}
            	
            	
            }
            
            TransformerFactory factoryescribir = TransformerFactory.newInstance();

            Transformer transformer = factoryescribir.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(path.toFile());

            transformer.transform(source, result);
            
            
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
		}
		
		
	}
	
	private static void borrarvuelos() {
		
		String respuesta1,respuesta2,respuesta3,respuesta4;
		Document doc = null;
		boolean eliminado = false;
		
		System.out.println("introduce el codigo del vuelo que desee eliminar: ");
		respuesta1=sc.nextLine();
		
		
		try {
        	
        	Path path = Path.of("datos", "aeropuerto.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setIgnoringComments(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            doc = builder.parse(path.toFile());
            
            NodeList aeropuertos = doc.getElementsByTagName("Aeropuerto");

            for (int i = 0; i < aeropuertos.getLength(); i++) {
            	
            	NodeList vuelos = doc.getElementsByTagName("Vuelo");
            	Node aeropuerto = aeropuertos.item(i);
            	
            	for(int x=0;x<vuelos.getLength(); x++) {
            		
            		Element vuelo = (Element) vuelos.item(x);
            		
            		if(vuelo.hasAttributes()) {
            			
            			NamedNodeMap atributos = vuelo.getAttributes();
            			
            			for(int z=0;z<atributos.getLength(); z++) {
            				
            				Node atributo = atributos.item(z);
            				
            				if(atributo.getNodeName().equalsIgnoreCase("codigo") && atributo.getNodeValue().equalsIgnoreCase(respuesta1)) {
            					
            					vuelo.getParentNode().removeChild(vuelo);
            					
            					eliminado=true;
            					break;
            				}
            				
            				
            			
            			}
            			
            			if(eliminado==true) {
            				break;
            			}
		
            		}
            	}
            	
            	if(eliminado==true) {
            		
            		break;
            	}
            	
            }
            
            TransformerFactory factoryescribir = TransformerFactory.newInstance();

            Transformer transformer = factoryescribir.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(path.toFile());

            transformer.transform(source, result);
            
            
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
		}
		
	}

	private static void gestionincidencias() {
		
		int opcion = 0;
		boolean salir = false;
		salir=false;
		
		do {
			
			System.out.println("1.CREAR INCIDENCIA");
			System.out.println("2.CONSULTAR INCIDENCIA");
			System.out.println("3.MODIFICAR INCIDENCIA");
			System.out.println("4.ELIMINAR INCIDENCIA");
			System.out.println("5.SALIR");
			
			try {
				opcion = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				opcion = -1;
			}
			
			switch(opcion) {
			
			case 1:
				crearincidencia();
				break;
			case 2:
				consultarincidencia();
				break;
			case 3:
				modificarincidencia();
				break;
			case 4:
				eliminarincidencia();	
				break;
			case 5:
				salir = true;
				break;
			default: System.out.println("Opcion Incorrecta");
				salir=true;
				break;
			}
			
		}while(salir==false);
		
	}
	
	private static void crearincidencia() {
		String fecha = sc.nextLine();
		String codigo = sc.nextLine();
		String vuelo = sc.nextLine();
		String tipo = sc.nextLine();
		String duracion = sc.nextLine();
		String descripcion = sc.nextLine();
		String prioridad = sc.nextLine();
		String estado = sc.nextLine();
		try {
			java.time.LocalDate fechaok;
			try {
				fechaok = java.time.LocalDate.parse(fecha);
			}
			catch (Exception ex) {
				fechaok = java.time.LocalDate.parse(fecha, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			}
			Integer.parseInt(duracion);
			if (fechaok == null || Integer.parseInt(duracion) < 0 || codigo.length() == 0 || vuelo.length() == 0 || descripcion.contains("|") || prioridad.equals("ALTA") == false && prioridad.equals("MEDIA") == false && prioridad.equals("BAJA") == false && prioridad.equals("CRITICA") == false || estado.equals("ABIERTA") == false && estado.equals("CERRADA") == false) {
				System.out.println("Datos de incidencia no validos.");
				return;
			}
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			Element raiz = doc.getDocumentElement();
			Element vuelos = (Element) raiz.getElementsByTagName("Vuelos").item(0);
			NodeList lista = vuelos.getElementsByTagName("Vuelo");
			boolean existevuelo = false;
			for (int i = 0; i < lista.getLength(); i++) {
				if (vuelo.equalsIgnoreCase(((Element) lista.item(i)).getAttribute("codigo"))) {
					existevuelo = true;
				}
			}
			if (existevuelo == false) {
				System.out.println("No existe el vuelo.");
				return;
			}
			Path ruta = Path.of("datos/incidencias.txt");
			BufferedReader lector = Files.newBufferedReader(ruta);
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split("\\|", -1);
				if (datos.length == 8 && codigo.equalsIgnoreCase(datos[1])) {
					lector.close();
					System.out.println("Ya existe la incidencia.");
					return;
				}
			}
			lector.close();
			BufferedWriter escritor = Files.newBufferedWriter(ruta, java.nio.file.StandardOpenOption.APPEND);
			escritor.write(fecha + "|" + codigo + "|" + vuelo + "|" + tipo + "|" + duracion + "|" + descripcion + "|" + prioridad + "|" + estado);
			escritor.newLine();
			escritor.close();
			System.out.println("Incidencia añadida.");
		}
		catch (Exception ex) {
			System.out.println("No se pudo añadir la incidencia.");
		}
	}
	
	private static void consultarincidencia() {
		int opcion;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar todos los datos");
			System.out.println("2.Buscar por codigo");
			System.out.println("3.Salir");
			
			opcion=Integer.parseInt(sc.nextLine());
					
			switch(opcion) {
			
			case 1:
				mostrartodasincidencias();
				break;
			case 2:
				buscarcodigoincidencias();
				break;
			case 3:
				salir = true;
				break;
			default: System.out.println("Incorrecto");
			salir=true;
				break;	
			}
			
		}while(salir==false);
		
		
	}
	
	private static void mostrartodasincidencias() {
		
		try {
			
			Path ruta = Path.of("datos", "incidencias.txt");
			
			if(Files.exists(ruta)) {
				
				try (FileReader fileReader = new FileReader(ruta.toFile());
						BufferedReader bufferedReader = new BufferedReader(fileReader);){
					
					String linea;

		            while ((linea = bufferedReader.readLine()) != null ) {

		                String[] datos = linea.split("\\|");
		                
		                System.out.println(datos[0] + " " +  datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5] + " " + datos[6] + " " + datos[7]);
		                
				}
				
		           
					bufferedReader.close();
					fileReader.close();
				
			}	
				
			}else {
				
				System.out.println("No se ha encontrado el archivo");
			}
			
			
		}catch(Exception ex) {
			
			System.out.println("Ha ocurrido un error");
			
		}
		
	}
	
	private static void buscarcodigoincidencias() {
		
		String respuesta;
		
		System.out.println("Introduce el codigo de la incidencia");
		respuesta=sc.nextLine();
		
		
		try {
			
			Path ruta = Path.of("datos", "incidencias.txt");
			
			if(Files.exists(ruta)) {
				
				try (FileReader fileReader = new FileReader(ruta.toFile());
						BufferedReader bufferedReader = new BufferedReader(fileReader);){
					
					String linea;
					boolean existe=false;

		            while ((linea = bufferedReader.readLine()) != null ) {

		                String[] datos = linea.split("\\|");
		                
		                if(respuesta.equals(datos[1])) {
		                	
		                	
		                	 System.out.println(datos[0] + " " +  datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5] + " " + datos[6] + " " + datos[7]);
				              
		                	existe=true;
		                	
		                }
		                
		                
		                 
				}
		            
		            if(existe == false) {
		            	
		            	System.out.println("No se ha encontrado ninguna incidencia con ese codigo");
		            	
		            }
		            
					bufferedReader.close();
					fileReader.close();
				
			}	
				
			}else {
				
				System.out.println("No se ha encontrado el archivo");
			}
			
			
		}catch(Exception ex) {
			
			System.out.println("Ha ocurrido un error");
			
		}
		
	
		
	}
	
	private static void modificarincidencia() {
		
		String respuesta,linea;
		
		try {
			
			System.out.println("Que incidencia desea modificar? ");
			System.out.println("Introduce el codigo: ");
			
			respuesta=sc.nextLine();
			
			Path path = Path.of("datos", "incidencias.txt");
			
			File temporal = new File("datos","temporal.txt");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				while ((linea = bufferedreader.readLine()) != null ) {
					
					 String[] datos = linea.split("\\|");
					
					if(respuesta.equalsIgnoreCase(datos[1])) {
						
						String respuesta10,respuesta20,respuesta30,respuesta40,respuesta50,respuesta60,respuesta70;
						int respuesta80;
						
						System.out.println("INTRODUCE LOS DATOS NUEVOS");
						System.out.println("----------------------------");
						
						System.out.println("Introduce la fecha: ");
						respuesta10=sc.nextLine();
						System.out.println("Introduce el codigo: ");
						respuesta20=sc.nextLine();
						System.out.println("Introduce el vuelo: ");
						respuesta30=sc.nextLine();
						System.out.println("Introduce el tipo: ");
						respuesta40=sc.nextLine();
						System.out.println("Introduce la duracion: ");
						respuesta80=Integer.parseInt(sc.nextLine());
						System.out.println("Introduce la descripcion: ");
						respuesta50=sc.nextLine();
						System.out.println("Introduce la prioridad: ");
						respuesta60=sc.nextLine();
						System.out.println("Introduce el estado: ");
						respuesta70=sc.nextLine();
						
						
						linea = respuesta10 + "|" + respuesta20 + "|" + respuesta30 + "|" + respuesta40 + "|" + respuesta80 + "|" + respuesta50 + "|" + respuesta60 + "|" + respuesta70;
						
						
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
				}
					
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path , StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex) {
			
			System.out.println("ERROR");
			
		}
		
		
		
		
		
	}
	
	private static void eliminarincidencia() {
		
		String respuesta,linea;
		
		try {
			
			System.out.println("Que incidencia desea modificar? ");
			System.out.println("Introduce el codigo: ");
			
			respuesta=sc.nextLine();
			
			Path path = Path.of("datos", "incidencias.txt");
			
			File temporal = new File("datos","temporal.txt");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				while ((linea = bufferedreader.readLine()) != null ) {
					
					 String[] datos = linea.split("\\|");
					
					if(respuesta.equalsIgnoreCase(datos[1])) {
						
						continue;
						
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path , StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex) {
			
			System.out.println("ERROR");
			
		}
		
		
		
		
		
		
	}

	private static void gestionembarques() {
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar o buscar embarque");
			System.out.println("2.Insertar embarque");
			System.out.println("3.Actualizar embarque");
			System.out.println("4.Borrar embarque");
			System.out.println("5.Salir");
			try {
				respuesta = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				respuesta = -1;
			}
			
			switch(respuesta) {
			 case 1:
				mostrarobuscarembarques();
			 break;
			 case 2:
				insertarembarques();
			 break;
			 case 3:
				actualizarembarques();
			 break;
			 case 4:
				borrarembarques();
			 break;
			 case 5:
				salir = true;
			 break;
			
			}
			
			
		}while(salir == false);
		
	}
	
	private static void mostrarobuscarembarques() {
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar Embarque");
			System.out.println("2.Buscar Embarque");
			System.out.println("3.Salir");
			try {
				respuesta = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				respuesta = -1;
			}
			
			switch(respuesta) {
			
			  case 1:
				mostrarembarques();
			  break;
			  case 2:
				buscarembarques();
			  break;
			  case 3:
				salir = true;
			  break;
			  
			}
			
			
		}while(salir == false);
		
		
	}
	
	private static void mostrarembarques() {
		
		try {
			
			Path path = Path.of("datos", "embarques.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5]);
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
	}
	
	private static void buscarembarques() {
		
		try {
			
			Path path = Path.of("datos", "embarques.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				String respuesta;
				
				System.out.println("Introduce el codigo de registro que quieras buscar: ");
				respuesta=sc.nextLine();
				
				while((linea = bufferedreader.readLine()) != null) {
					
					
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						

						System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5]);
						
						
					}
					
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void insertarembarques() {
		String codigo = sc.nextLine();
		String reserva = sc.nextLine();
		String fechahora = sc.nextLine();
		String puerta = sc.nextLine();
		String estado = sc.nextLine();
		String observaciones = sc.nextLine();
		try {
			java.time.LocalDateTime.parse(fechahora);
			if (codigo.length() == 0 || reserva.length() == 0 || puerta.length() == 0) {
				System.out.println("Hay campos obligatorios vacios.");
				return;
			}
			if (estado.equals("EMBARCADO") == false && estado.equals("EMBARCANDO") == false && estado.equals("NO_EMBARCADO") == false) {
				System.out.println("Estado no permitido.");
				return;
			}
			BufferedReader lector = Files.newBufferedReader(Path.of("datos/embarques.csv"));
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 6 && (codigo.equalsIgnoreCase(datos[0]) || reserva.equalsIgnoreCase(datos[1]))) {
					lector.close();
					System.out.println("Ya existe un embarque para ese registro o reserva.");
					return;
				}
			}
			lector.close();
			lector = Files.newBufferedReader(Path.of("datos/reservas.csv"));
			String vuelocodigo = "";
			String estadreserva = "";
			boolean existe = false;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 10 && reserva.equalsIgnoreCase(datos[0])) {
					existe = true;
					vuelocodigo = datos[2];
					estadreserva = datos[7];
				}
			}
			lector.close();
			if (existe == false) {
				System.out.println("No existe la reserva.");
				return;
			}
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			Element raiz = doc.getDocumentElement();
			Element vuelos = (Element) raiz.getElementsByTagName("Vuelos").item(0);
			NodeList lista = vuelos.getElementsByTagName("Vuelo");
			Element vuelo = null;
			for (int i = 0; i < lista.getLength(); i++) {
				Element actual = (Element) lista.item(i);
				if (vuelocodigo.equalsIgnoreCase(actual.getAttribute("codigo"))) {
					vuelo = actual;
				}
			}
			if (vuelo == null) {
				System.out.println("No existe el vuelo de la reserva.");
				return;
			}
			if ("CANCELADO".equalsIgnoreCase(vuelo.getAttribute("estado")) || "CANCELADA".equalsIgnoreCase(estadreserva) && (estado.equals("EMBARCADO") || estado.equals("EMBARCANDO"))) {
				System.out.println("No se permite embarcar una reserva o vuelo cancelado.");
				return;
			}
			Element ruta = (Element) vuelo.getElementsByTagName("Ruta").item(0);
			Element origen = (Element) ruta.getElementsByTagName("Origen").item(0);
			String puertaxml = "";
			if (origen != null && origen.getElementsByTagName("Puerta").getLength() > 0) {
				puertaxml = origen.getElementsByTagName("Puerta").item(0).getTextContent();
			}
			if (origen == null || puerta.equalsIgnoreCase(origen.getAttribute("puerta")) == false && puerta.equalsIgnoreCase(puertaxml) == false) {
				System.out.println("La puerta no coincide con el vuelo.");
				return;
			}
			BufferedWriter escritor = Files.newBufferedWriter(Path.of("datos/embarques.csv"), java.nio.file.StandardOpenOption.APPEND);
			escritor.write(codigo + "," + reserva + "," + fechahora + "," + puerta + "," + estado + "," + observaciones);
			escritor.newLine();
			escritor.close();
			System.out.println("Embarque añadido.");
		}
		catch (Exception ex) {
			System.out.println("No se pudo añadir el embarque.");
		}
	}
	
	private static void actualizarembarques() {
		
		try {
			String respuesta,linea,respuesta2,respuesta3,respuesta4,respuesta5,respuesta6;
			
			System.out.println("Introduce el codigo del registro que desee modificar");
			respuesta=sc.nextLine();
			
			System.out.println("Introduce el codigo de la reserva: ");
			respuesta2=sc.nextLine();
			System.out.println("Introduce la fecha en formato AAAA-MM-DDThhmm: ");
			respuesta3=sc.nextLine();
			System.out.println("Introduce la puerta de embarque: ");
			respuesta4=sc.nextLine();
			System.out.println("Introduce el estado de embarque: ");
			respuesta5=sc.nextLine();
			System.out.println("Introduce las observaciones: ");
			respuesta6=sc.nextLine();
			
			Path path = Path.of("datos", "embarques.csv");
			
			File temporal = new File("datos", "temporalembarques.csv");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						
						linea = datos[0] + "," + respuesta2 + "," + respuesta3 + "," + respuesta4 + "," + respuesta5 + "," + respuesta6;
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
							
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex){
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void borrarembarques() {
		
		String respuesta,linea;
		
		try {
			
			System.out.println("Que embarque desea eliminar? ");
			System.out.println("Introduce el codigo de registro: ");
			
			respuesta=sc.nextLine();
			
			Path path = Path.of("datos", "embarques.csv");
			
			File temporal = new File("datos","temporalembarques.csv");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				while ((linea = bufferedreader.readLine()) != null ) {
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						
						continue;
						
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path , StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex) {
			
			System.out.println("ERROR");
			
		}
		
		
	}

	private static void gestionequipajes() {
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar o buscar equipaje");
			System.out.println("2.Insertar equipaje");
			System.out.println("3.Actualizar equipaje");
			System.out.println("4.Borrar equipaje");
			System.out.println("5.Salir");
			try {
				respuesta = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				respuesta = -1;
			}
			
			switch(respuesta) {
			 case 1:
				mostrarobuscarequipajes();
			 break;
			 case 2:
				insertarequipajes();
			 break;
			 case 3:
				actualizarequipajes();
			 break;
			 case 4:
				borrarequipajes();
			 break;
			 case 5:
				salir = true;
			 break;
			
			}
			
			
		}while(salir == false);
		
	}
	
	private static void mostrarobuscarequipajes(){
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar Equipaje");
			System.out.println("2.Buscar Equipaje");
			System.out.println("3.Salir");
			try {
				respuesta = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				respuesta = -1;
			}
			
			switch(respuesta) {
			
			  case 1:
				mostrarequipajes();
			  break;
			  case 2:
				buscarequipajes();
			  break;
			  case 3:
				 salir = true;
			  break;
			  
			}
			
			
		}while(salir == false);
		
		
	}
	
	private static void mostrarequipajes() {
		
		try {
			
			Path path = Path.of("datos", "equipajes.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5]);
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void buscarequipajes() {
		
		try {
			
			Path path = Path.of("datos", "equipajes.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				String respuesta;
				
				System.out.println("Introduce el codigo del equipaje que quieras buscar: ");
				respuesta=sc.nextLine();
				
				while((linea = bufferedreader.readLine()) != null) {
					
					
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						

						System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5]);
						
						
					}
					
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void insertarequipajes() {
		String codigo = sc.nextLine();
		String reserva = sc.nextLine();
		String peso = sc.nextLine();
		String tipo = sc.nextLine();
		String estado = sc.nextLine();
		String prioridad = sc.nextLine();
		try {
			double pesonumero = Double.parseDouble(peso);
			if (codigo.length() == 0 || reserva.length() == 0 || Double.isFinite(pesonumero) == false || pesonumero < 0 || codigo.contains(",") || reserva.contains(",") || peso.contains(",") || tipo.contains(",") || estado.contains(",") || prioridad.contains(",")) {
				System.out.println("Datos de equipaje no validos.");
				return;
			}
			if (tipo.equals("FACTURADO") == false && tipo.equals("MANO") == false && tipo.equals("ESPECIAL") == false) {
				System.out.println("Tipo de equipaje no permitido.");
				return;
			}
			if (estado.equals("REGISTRADO") == false && estado.equals("CARGADO") == false && estado.equals("EN_TRANSITO") == false && estado.equals("ENTREGADO") == false && estado.equals("PERDIDO") == false) {
				System.out.println("Estado de equipaje no permitido.");
				return;
			}
			if (prioridad.equals("NORMAL") == false && prioridad.equals("FRAGIL") == false && prioridad.equals("PRIORITARIO") == false) {
				System.out.println("Prioridad no permitida.");
				return;
			}
			BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"));
			String linea;
			boolean existe = false;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 10 && reserva.equalsIgnoreCase(datos[0])) {
					existe = true;
				}
			}
			lector.close();
			if (existe == false) {
				System.out.println("No existe la reserva.");
				return;
			}
			lector = Files.newBufferedReader(Path.of("datos/equipajes.csv"));
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 6 && codigo.equalsIgnoreCase(datos[0])) {
					lector.close();
					System.out.println("Ya existe el equipaje.");
					return;
				}
			}
			lector.close();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode raiz = mapper.readTree(Path.of("datos/configuracion.json").toFile());
			JsonNode reglas = raiz.at("/aeropuerto/equipaje/reglas");
			for (int i = 0; i < reglas.size(); i++) {
				if (tipo.equalsIgnoreCase(reglas.get(i).path("tipo").asText()) && pesonumero > reglas.get(i).path("pesoMaximo").asDouble()) {
					System.out.println("Aviso: peso " + peso + " kg, limite " + reglas.get(i).path("pesoMaximo").asText() + " kg, exceso " + (pesonumero - reglas.get(i).path("pesoMaximo").asDouble()) + " kg.");
				}
			}
			BufferedWriter escritor = Files.newBufferedWriter(Path.of("datos/equipajes.csv"), java.nio.file.StandardOpenOption.APPEND);
			escritor.write(codigo + "," + reserva + "," + peso + "," + tipo + "," + estado + "," + prioridad);
			escritor.newLine();
			escritor.close();
			System.out.println("Equipaje añadido.");
		}
		catch (Exception ex) {
			System.out.println("No se pudo añadir el equipaje.");
		}
	}
	
	private static void actualizarequipajes() {
		
		try {
			String respuesta,linea,respuesta2,respuesta3,respuesta4,respuesta5,respuesta6;
			
			System.out.println("Introduce el codigo del equipaje que desee modificar");
			respuesta=sc.nextLine();
			
			System.out.println("Introduce el codigo de la reserva: ");
			respuesta2=sc.nextLine();
			System.out.println("Introduce el peso en kg: ");
			respuesta3=sc.nextLine();
			System.out.println("Introduce el tipo de equipaje: ");
			respuesta4=sc.nextLine();
			System.out.println("Introduce el estado del equipaje: ");
			respuesta5=sc.nextLine();
			System.out.println("Introduce la prioridad del equipaje: ");
			respuesta6=sc.nextLine();
			
			Path path = Path.of("datos", "equipajes.csv");
			
			File temporal = new File("datos", "temporalequipajes.csv");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						
						linea = datos[0] + "," + respuesta2 + "," + respuesta3 + "," + respuesta4 + "," + respuesta5 + "," + respuesta6;
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
							
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex){
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void borrarequipajes() {
		
		String respuesta,linea;
		
		try {
			
			System.out.println("Que equipaje desea eliminar? ");
			System.out.println("Introduce el codigo: ");
			
			respuesta=sc.nextLine();
			
			Path path = Path.of("datos", "equipajes.csv");
			
			File temporal = new File("datos","temporalequipajes.csv");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				while ((linea = bufferedreader.readLine()) != null ) {
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						
						continue;
						
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path , StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex) {
			
			System.out.println("ERROR");
			
		}
		
		
	}

	private static void gestionreservas() {
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar o buscar reserva");
			System.out.println("2.Insertar reserva");
			System.out.println("3.Actualizar reserva");
			System.out.println("4.Borrar reserva");
			System.out.println("5.Cancelar reserva");
			System.out.println("6.Salir");
			try {
				respuesta = Integer.parseInt(sc.nextLine());
			}
			catch (Exception ex) {
				respuesta = -1;
			}
			
			switch(respuesta) {
			 case 1:
				mostrarobuscarreserva();
			 break;
			 case 2:
			 	insertarreserva();
			 break;
			 case 3:
			 	actualizarreserva();
			 break;
			 case 4:
			 	borrarreserva();
			 break;
			 case 5:
			 	cancelarreservaseguro();
			 break;
			 case 6:
			 	salir = true;
			 break;
			
			}
			
			
		}while(salir == false);
		
		
		
	}
	
	private static void mostrarobuscarreserva() {
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar Reserva");
			System.out.println("2.Buscar Reserva");
			System.out.println("3.Salir");
			respuesta=Integer.parseInt(sc.nextLine());
			
			switch(respuesta) {
			
			  case 1:
			  	mostrarreserva();
			  break;
			  case 2:
			  	buscarreserva();
			  break;
			  case 3:
				salir = true;
			  break;
			  
			}
			
			
		}while(salir == false);
		
		
	}
	
	private static void mostrarreserva() {
		
		try {
			
			Path path = Path.of("datos", "reservas.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5] + " " + datos[6] + " " + datos[7] + " " + datos[8] + " " + datos[9]);
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void buscarreserva() {
		
		try {
			
			Path path = Path.of("datos", "reservas.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				String respuesta;
				
				System.out.println("Introduce el codigo de la reserva que quieras buscar: ");
				respuesta=sc.nextLine();
				
				while((linea = bufferedreader.readLine()) != null) {
					
					
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						

						System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5] + " " + datos[6] + " " + datos[7] + " " + datos[8] + " " + datos[9]);
						
						
					}
					
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}

	private static String unir(String[] datos, String separador) { String resultado = "";
		for (int i = 0;
		i < datos.length;
		 i++) {
			if (i > 0) {
				resultado += separador;
			}
			resultado += datos[i];
		} return resultado;
		}

	private static boolean existecodigocsv(String archivo, String codigo, int posicion, int camposesperados) { try (BufferedReader lector = Files.newBufferedReader(Path.of(archivo))) { String linea;
		while ((linea = lector.readLine()) != null) { String[] datos = linea.split(",", -1);
		if (datos.length == camposesperados && codigo.equalsIgnoreCase(datos[posicion])) return true;
		}
		}
		catch (Exception ex) {
		}
		return false;
		}
	
	private static void cancelarreservaseguro() {
		System.out.print("Codigo de reserva a cancelar: "); String codigo = sc.nextLine();
		if (existecodigocsv("datos/reservas.csv", codigo, 0, 10) == false) {
			System.out.println("No existe la reserva.");
			return;
		}
		String[] datos = null;
		try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"))) { String linea;
			while ((linea = lector.readLine()) != null) { String[] actual = linea.split(",", -1);
			if (actual.length == 10 && codigo.equalsIgnoreCase(actual[0])) { datos = actual;
			break;
			}
		}
		}
		catch (Exception ex) {
		}
		if (datos == null) {
			return;
		}
			datos[7] = "CANCELADA";
			reescribirreserva(codigo, datos, false);
			System.out.println("Reserva cancelada; sus equipajes y embarques no se han modificado.");
	}

	private static void reescribirreserva(String codigo, String[] nueva, boolean borrar) {
		Path original = Path.of("datos/reservas.csv"), temporal = Path.of("datos/reservas.csv.tmp");
			boolean encontrado = false;
		try (BufferedReader lector = Files.newBufferedReader(original);
			BufferedWriter escritor = Files.newBufferedWriter(temporal)) { String linea;
			while ((linea = lector.readLine()) != null) { String[] datos = linea.split(",", -1);
			if (datos.length == 10 && codigo.equalsIgnoreCase(datos[0])) { encontrado = true;
			if (!borrar) { escritor.write(unir(nueva, ","));
			escritor.newLine();
			} } else { escritor.write(linea);
			escritor.newLine();
			} } } catch (Exception ex) { System.out.println("No se pudo reconstruir reservas; no se modifica el original.");
			return;
			}
		try { if (!encontrado) { Files.deleteIfExists(temporal);
			System.out.println("No existe la reserva.");
			return;
			} Files.move(temporal, original, StandardCopyOption.REPLACE_EXISTING);
			System.out.println(borrar ? "Reserva borrada." : "Reserva actualizada.");
			} catch (Exception ex) { System.out.println("No se pudo reemplazar el archivo original.");
			}
	}
	
	private static void insertarreserva() {
		String respuesta1;
		String respuesta2;
		String respuesta3;
		String respuesta4;
		String respuesta5;
		String respuesta6;
		String respuesta7;
		String respuesta8;
		String respuesta9;
		String respuesta10;
		System.out.println("Introduce el codigo de la reserva: ");
		respuesta1=sc.nextLine();
		System.out.println("Introduce el codigo del pasajero: ");
		respuesta2=sc.nextLine();
		System.out.println("Introduce el codigo del vuelo: ");
		respuesta3=sc.nextLine();
		System.out.println("Introduce el asiento: ");
		respuesta4=sc.nextLine();
		System.out.println("Introduce la clase de la reserva: ");
		respuesta5=sc.nextLine();
		System.out.println("Introduce el precio de la reserva: ");
		respuesta6=sc.nextLine();
		System.out.println("Introduce la moneda de la reserva: ");
		respuesta7=sc.nextLine();
		System.out.println("Introduce el estado de la reserva: ");
		respuesta8=sc.nextLine();
		System.out.println("Introduce la fecha de la reserva: ");
		respuesta9=sc.nextLine();
		System.out.println("Introduce el canal de la reserva: ");
		respuesta10=sc.nextLine();
		if (respuesta1.length() == 0 || respuesta2.length() == 0 || respuesta3.length() == 0 || respuesta4.length() == 0 || respuesta5.length() == 0 || respuesta6.length() == 0 || respuesta7.length() == 0 || respuesta8.length() == 0 || respuesta9.length() == 0 || respuesta10.length() == 0) {
			System.out.println("Hay campos obligatorios vacios.");
			return;
		}
		if (respuesta1.contains(",") || respuesta2.contains(",") || respuesta3.contains(",") || respuesta4.contains(",") || respuesta5.contains(",") || respuesta6.contains(",") || respuesta7.contains(",") || respuesta8.contains(",") || respuesta9.contains(",") || respuesta10.contains(",")) {
			System.out.println("Los campos no pueden contener comas.");
			return;
		}
		try {
			double precio = Double.parseDouble(respuesta6);
			if (Double.isFinite(precio) == false || precio < 0) {
				System.out.println("Precio no valido.");
				return;
			}
			java.time.LocalDate.parse(respuesta9);
		}
		catch (Exception ex) {
			System.out.println("Precio o fecha no validos.");
			return;
		}
		if (respuesta8.equals("CONFIRMADA") == false && respuesta8.equals("CANCELADA") == false) {
			System.out.println("Estado no permitido.");
			return;
		}
		if (respuesta10.equals("WEB") == false && respuesta10.equals("APP") == false && respuesta10.equals("TAQUILLA") == false && respuesta10.equals("AGENCIA") == false) {
			System.out.println("Canal no permitido.");
			return;
		}
		try {
			boolean pasajero = false;
			BufferedReader lector = Files.newBufferedReader(Path.of("datos/pasajeros.csv"));
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 7 && respuesta2.equalsIgnoreCase(datos[0])) {
					pasajero = true;
				}
			}
			lector.close();
			if (pasajero == false) {
				System.out.println("No existe el pasajero.");
				return;
			}
			lector = Files.newBufferedReader(Path.of("datos/reservas.csv"));
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 10 && respuesta1.equalsIgnoreCase(datos[0])) {
					System.out.println("Ya existe la reserva.");
					lector.close();
					return;
				}
			}
			lector.close();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Path.of("datos/aeropuerto.xml").toFile());
			Element raiz = doc.getDocumentElement();
			Element contenedor = (Element) raiz.getElementsByTagName("Vuelos").item(0);
			NodeList lista = contenedor.getElementsByTagName("Vuelo");
			Element vuelo = null;
			for (int i = 0; i < lista.getLength(); i++) {
				Element actual = (Element) lista.item(i);
				if (respuesta3.equalsIgnoreCase(actual.getAttribute("codigo"))) {
					vuelo = actual;
				}
			}
			if (vuelo == null) {
				System.out.println("No existe el vuelo.");
				return;
			}
			if ("CANCELADO".equalsIgnoreCase(vuelo.getAttribute("estado"))) {
				System.out.println("El vuelo esta cancelado.");
				return;
			}
			String matricula = vuelo.getAttribute("avion");
			Element contenedoraviones = (Element) raiz.getElementsByTagName("Aviones").item(0);
			NodeList aviones = contenedoraviones.getElementsByTagName("Avion");
			Element avion = null;
			for (int i = 0; i < aviones.getLength(); i++) {
				Element actual = (Element) aviones.item(i);
				if (matricula.equalsIgnoreCase(actual.getAttribute("matricula"))) {
					avion = actual;
				}
			}
			if (avion == null) {
				System.out.println("No existe el avion del vuelo.");
				return;
			}
			boolean clase = false;
			NodeList clases = avion.getElementsByTagName("Clase");
			for (int i = 0; i < clases.getLength(); i++) {
				if (respuesta5.equalsIgnoreCase(((Element) clases.item(i)).getAttribute("tipo"))) {
					clase = true;
				}
			}
			if (clase == false) {
				System.out.println("La clase no esta disponible en el avion.");
				return;
			}
			lector = Files.newBufferedReader(Path.of("datos/reservas.csv"));
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 10 && respuesta3.equalsIgnoreCase(datos[2]) && respuesta4.equalsIgnoreCase(datos[3])) {
					System.out.println("El asiento ya esta asignado.");
					lector.close();
					return;
				}
			}
			lector.close();
			BufferedWriter escritor = Files.newBufferedWriter(Path.of("datos/reservas.csv"), java.nio.file.StandardOpenOption.APPEND);
			escritor.write(respuesta1 + "," + respuesta2 + "," + respuesta3 + "," + respuesta4 + "," + respuesta5 + "," + respuesta6 + "," + respuesta7 + "," + respuesta8 + "," + respuesta9 + "," + respuesta10);
			escritor.newLine();
			escritor.close();
			System.out.println("Reserva añadida.");
		}
		catch (Exception ex) {
			System.out.println("No se pudo añadir la reserva.");
		}
	}	
	
	private static void actualizarreserva() {
		
		try {
			String respuesta,linea,respuesta2,respuesta3,respuesta4,respuesta5,respuesta6,respuesta7, respuesta8, respuesta9, respuesta10;
			
			System.out.println("Introduce el codigo de la reserva que desee modificar");
			respuesta=sc.nextLine();

			System.out.println("Introduce el codigo del pasajero: ");
			respuesta2=sc.nextLine();
			System.out.println("Introduce el codigo del vuelo: ");
			respuesta3=sc.nextLine();
			System.out.println("Introduce el asiento: ");
			respuesta4=sc.nextLine();
			System.out.println("Introduce la clase de la reserva: ");
			respuesta5=sc.nextLine();
			System.out.println("Introduce el precio de la reserva: ");
			respuesta6=sc.nextLine();
			System.out.println("Introduce la moneda de la reserva: ");
			respuesta7=sc.nextLine();
			System.out.println("Introduce el estado de la reserva: ");
			respuesta8=sc.nextLine();
			System.out.println("Introduce la fecha de la reserva: ");
			respuesta9=sc.nextLine();
			System.out.println("Introduce el canal de la reserva: ");
			respuesta10=sc.nextLine();
			
			Path path = Path.of("datos", "reservas.csv");
			
			File temporal = new File("datos", "temporalreservas.csv");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						
						linea = datos[0] + "," + respuesta2 + "," + respuesta3 + "," + respuesta4 + "," + respuesta5 + "," + respuesta6 + "," + respuesta7;
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
							
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex){
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
		
	}
	
	private static void borrarreserva() {
		
		String respuesta,linea;
		
		try {
			
			System.out.println("Que reserva desea eliminar? ");
			System.out.println("Introduce el codigo: ");
			
			respuesta=sc.nextLine();
			
			Path path = Path.of("datos", "reservas.csv");
			
			File temporal = new File("datos","temporalreservas.csv");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				while ((linea = bufferedreader.readLine()) != null ) {
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						
						continue;
						
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path , StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex) {
			
			System.out.println("ERROR");
			
		}
		
		
		
	}

	private static void gestionpasajeros() {
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar o buscar pasajero");
			System.out.println("2.Insertar pasajero");
			System.out.println("3.Actualizar pasajero");
			System.out.println("4.Borrar pasajero");
			System.out.println("5.Salir");
			respuesta=Integer.parseInt(sc.nextLine());
			
			switch(respuesta) {
			 case 1:
				mostrarobuscarpasajeros();
			 break;
			 case 2:
				insertarpasajeros();
			 break;
			 case 3:
				actualizarpasajeros();
			 break;
			 case 4:
				borrarpasajeros();
			 break;
			 case 5:
				salir = true;
			 break;
			
			}
			
			
		}while(salir == false);
		
	}
	
	private static void mostrarobuscarpasajeros() {
		
		int respuesta;
		boolean salir = false;
		
		do {
			
			System.out.println("1.Mostrar Pasajero");
			System.out.println("2.Buscar Pasajero");
			System.out.println("3.Salir");
			respuesta=Integer.parseInt(sc.nextLine());
			
			switch(respuesta) {
			
			  case 1:
				mostrarpasajeros();
			  break;
			  case 2:
				buscarpasajeros();
			  break;
			  case 3:
				salir = true;
			  break;
			  
			}
			
			
		}while(salir == false);
		
		
	}
	
	private static void mostrarpasajeros() {
		
		try {
			
			Path path = Path.of("datos", "pasajeros.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5] + " " + datos[6]);
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void buscarpasajeros() {
		

		try {
			
			Path path = Path.of("datos", "pasajeros.csv");
			
			try (FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);){
				
				String linea;
				
				String respuesta;
				
				System.out.println("Introduce el codigo del pasajero que quieras buscar: ");
				respuesta=sc.nextLine();
				
				while((linea = bufferedreader.readLine()) != null) {
					
					
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						

						System.out.println(datos[0] + " " + datos[1] + " " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5] + " " + datos[6]);
						
						
					}
					
					
				}
				
				bufferedreader.close();
				filereader.close();
			}
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void insertarpasajeros() {
		
		try {
			
			String respuesta1, respuesta2, respuesta3, respuesta4, respuesta5, respuesta6, respuesta7;
			
			System.out.println("Introduce el codigo del pasajero: ");
			respuesta1=sc.nextLine();
			System.out.println("Introduce el nombre del pasajero: ");
			respuesta2=sc.nextLine();
			System.out.println("Introduce los apellidos del pasajero: ");
			respuesta3=sc.nextLine();
			System.out.println("Introduce el DNI del pasajero: ");
			respuesta4=sc.nextLine();
			System.out.println("Introduce el pais del pasajero: ");
			respuesta5=sc.nextLine();
			System.out.println("Introduce el correo del pasajero: ");
			respuesta6=sc.nextLine();
			System.out.println("Introduce el tipo del pasajero: ");
			respuesta7=sc.nextLine();
			if (respuesta1.length() == 0 || respuesta2.length() == 0 || respuesta3.length() == 0 || respuesta4.length() == 0 || respuesta5.length() == 0 || respuesta7.length() == 0) {
				System.out.println("Faltan campos obligatorios.");
				return;
			}
			if (respuesta1.contains(",") || respuesta2.contains(",") || respuesta3.contains(",") || respuesta4.contains(",") || respuesta5.contains(",") || respuesta6.contains(",") || respuesta7.contains(",")) {
				System.out.println("No se permiten comas en los campos.");
				return;
			}
			if (respuesta7.equalsIgnoreCase("ADULTO") == false && respuesta7.equalsIgnoreCase("VIP") == false && respuesta7.equalsIgnoreCase("MENOR") == false) {
				System.out.println("Tipo de pasajero no permitido.");
				return;
			}
			try (BufferedReader lector = Files.newBufferedReader(Path.of("datos/pasajeros.csv"))) {
				String linea;
				while ((linea = lector.readLine()) != null) {
					String[] datos = linea.split(",", -1);
					if (datos.length == 7 && respuesta1.equalsIgnoreCase(datos[0])) {
						System.out.println("El codigo del pasajero ya existe.");
						return;
					}
				}
			}
			catch (Exception ex) {
				System.out.println("No se pudo comprobar el codigo.");
				return;
			}
			
			Path path = Path.of("datos", "pasajeros.csv");
			
			try (FileWriter filewriter = new FileWriter(path.toFile(), true);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				String linea;
				
				linea = respuesta1 + "," + respuesta2 + "," + respuesta3 + "," + respuesta4 + "," + respuesta5 + "," + respuesta6 + "," + respuesta7;
				
				bufferedwriter.write(linea);
				
				bufferedwriter.newLine();
				
				bufferedwriter.close();
				filewriter.close();
			}
			
			
			
		}catch(Exception ex) {
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void actualizarpasajeros() {
		
		try {
			String respuesta,linea,respuesta2,respuesta3,respuesta4,respuesta5,respuesta6,respuesta7;
			
			System.out.println("Introduce el codigo del pasajero que desee modificar");
			respuesta=sc.nextLine();
			
			System.out.println("Introduce el nombre del pasajero: ");
			respuesta2=sc.nextLine();
			System.out.println("Introduce los apellidos del pasajero: ");
			respuesta3=sc.nextLine();
			System.out.println("Introduce el DNI del pasajero: ");
			respuesta4=sc.nextLine();
			System.out.println("Introduce el pais del pasajero: ");
			respuesta5=sc.nextLine();
			System.out.println("Introduce el correo del pasajero: ");
			respuesta6=sc.nextLine();
			System.out.println("Introduce el tipo del pasajero: ");
			respuesta7=sc.nextLine();
			
			Path path = Path.of("datos", "pasajeros.csv");
			
			File temporal = new File("datos", "temporalpasajeros.csv");
			
			try(FileReader filereader = new FileReader(path.toFile());
					BufferedReader bufferedreader = new BufferedReader(filereader);
					FileWriter filewriter = new FileWriter(temporal,false);
					BufferedWriter bufferedwriter = new BufferedWriter(filewriter);){
				
				
				while((linea = bufferedreader.readLine()) != null) {
					
					String[] datos = linea.split(",", -1);
					
					if(respuesta.equalsIgnoreCase(datos[0])) {
						
						linea = datos[0] + "," + respuesta2 + "," + respuesta3 + "," + respuesta4 + "," + respuesta5 + "," + respuesta6 + "," + respuesta7;
					}
					
					bufferedwriter.write(linea);
					bufferedwriter.newLine();
					
							
				}
				bufferedwriter.close();
				filewriter.close();
				bufferedreader.close();
				filereader.close();
			}
			
			Files.move(temporal.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
			
		}catch(Exception ex){
			
			System.out.println("HA HABIDO UN ERROR");
			
		}
		
		
	}
	
	private static void borrarpasajeros() {
		System.out.println("Que pasajero desea eliminar? ");
		String codigo = sc.nextLine();
		try {
			BufferedReader lector = Files.newBufferedReader(Path.of("datos/reservas.csv"));
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length == 10 && codigo.equalsIgnoreCase(datos[1])) {
					lector.close();
					System.out.println("No se puede borrar: el pasajero tiene reservas.");
					return;
				}
			}
			lector.close();
			Path original = Path.of("datos/pasajeros.csv");
			Path temporal = Path.of("datos/temporalpasajeros.csv");
			boolean encontrado = false;
			lector = Files.newBufferedReader(original);
			BufferedWriter escritor = Files.newBufferedWriter(temporal);
			while ((linea = lector.readLine()) != null) {
				String[] datos = linea.split(",", -1);
				if (datos.length > 0 && codigo.equalsIgnoreCase(datos[0])) {
					encontrado = true;
				}
				else {
					escritor.write(linea);
					escritor.newLine();
				}
			}
			lector.close();
			escritor.close();
			if (encontrado == false) {
				Files.deleteIfExists(temporal);
				System.out.println("No existe el pasajero.");
				return;
			}
			Files.move(temporal, original, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Pasajero borrado.");
		}
		catch (Exception ex) {
			System.out.println("No se pudo borrar el pasajero.");
		}
	}
	
}

