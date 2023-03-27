import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sistema {


	public static void informe(String nombreFichero) {

		int ColumnasA = contarColumnas(nombreFichero);
		String [][] pacientes = leercsv(nombreFichero);
		int criticos=0;
		int estables=0;
		double media = 0;

		for (int i=1; i<pacientes.length;i++) {
			if (Integer.parseInt(pacientes[i][2])<60) {
				criticos++;
			} else {
				estables++;
			}
			media = media + Integer.parseInt(pacientes[i][2]);
		}
		media = media / (pacientes.length-1);

		System.out.println("\n\n                   Informe general de los pacientes registrados");
		System.out.println("-----------------------------------------------------------------------------------------------------");
		System.out.printf("%"+20+"s","\n          Estables                 Criticos                  Media de mm de O2\n ");
		System.out.println(" ");

		System.out.printf("%"+20+"s",estables);
		System.out.printf("%"+20+"s",criticos);
		System.out.printf("%"+40+"s",media);
		System.out.println("\n");

	}
	//--------------------------------------------------------------------------------------------------------------------
	private static String fecha_y_hora() {
		LocalDateTime FechaYHoraSinFormato = LocalDateTime.now();
		DateTimeFormatter FormatoDeFechaYHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String FechaYHora = FechaYHoraSinFormato.format(FormatoDeFechaYHora);

		return FechaYHora;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void sendMail_Usuarios(String recepient) throws MessagingException, IOException{
		System.out.println("Preparando para enviar el mail...");
		Properties properties = new Properties();

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "*");// Añade todas las propiedades.

		String micorreo = "biosoft.live.uem@gmail.com";
		String contrasena = "MiMailJavaEnviar";

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(micorreo,contrasena);
			}
		});

		Message message = prepareMessageUsuarios(session, micorreo, recepient);

		Transport.send(message);
		System.out.println("Mail enviado correctamente.");

	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void sendMail_LOG(String recepient) throws MessagingException, IOException{
		System.out.println("Preparando para enviar el mail...");
		Properties properties = new Properties();

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "*");// Añade todas las propiedades.

		String micorreo = "biosoft.live.uem@gmail.com";
		String contrasena = "MiMailJavaEnviar";

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(micorreo,contrasena);
			}
		});

		Message message = prepareMessageLOG(session, micorreo, recepient);

		Transport.send(message);
		System.out.println("Mail enviado correctamente.");

	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void sendMail_PacientesT(String recepient) throws MessagingException{
		System.out.println("Preparando para enviar el mail...");
		Properties properties = new Properties();

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		String micorreo = "biosoft.live.uem@gmail.com";
		String contrasena = "MiMailJavaEnviar";

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(micorreo,contrasena);
			}
		});

		Message message = prepareMessagePacientesT(session, micorreo, recepient);

		Transport.send(message);
		System.out.println("Mail enviado correctamente.");
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void sendMail_Pacientes(String recepient, String u) throws MessagingException{
		System.out.println("Preparando para enviar el mail...");
		Properties properties = new Properties();

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		String micorreo = "biosoft.live.uem@gmail.com";
		String contrasena = "MiMailJavaEnviar";

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(micorreo,contrasena);
			}
		});

		Message message = prepareMessagePacientes(session, micorreo, recepient, u);

		Transport.send(message);
		System.out.println("Mail enviado correctamente.");
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static Message prepareMessageUsuarios(Session session, String micorreo, String recepient) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(micorreo));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Log del hospital");

			Multipart emailContent = new MimeMultipart();
			//Cuerpo del mail

			MimeBodyPart textbody = new MimeBodyPart();
			textbody.setText("Aquí se adjuntan los usuarios a fecha: "+fecha_y_hora());

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			MimeBodyPart csvAttachment = new MimeBodyPart();
			csvAttachment.attachFile("Usuarios.csv");

			emailContent.addBodyPart(textbody);
			emailContent.addBodyPart(csvAttachment);

			message.setContent(emailContent);
			return message;
		} catch (Exception ex){
			Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static Message prepareMessageLOG(Session session, String micorreo, String recepient) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(micorreo));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Log del hospital");

			Multipart emailContent = new MimeMultipart();
			//Cuerpo del mail

			MimeBodyPart textbody = new MimeBodyPart();
			textbody.setText("Aquí se adjunta el log a fecha: "+fecha_y_hora());

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			MimeBodyPart csvAttachment = new MimeBodyPart();
			csvAttachment.attachFile("Log.csv");

			emailContent.addBodyPart(textbody);
			emailContent.addBodyPart(csvAttachment);

			message.setContent(emailContent);
			return message;
		} catch (Exception ex){
			Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static Message prepareMessagePacientesT(Session session, String micorreo, String recepient) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(micorreo));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Todos los pacientes");

			Multipart emailContent = new MimeMultipart();
			//Cuerpo del mail

			MimeBodyPart textbody = new MimeBodyPart();
			textbody.setText("Aquí se adjunta la lista de todos los pacientes a fecha: "+fecha_y_hora());

			MimeBodyPart csvAttachment = new MimeBodyPart();
			csvAttachment.attachFile("Pacientes.csv");

			emailContent.addBodyPart(textbody);
			emailContent.addBodyPart(csvAttachment);

			message.setContent(emailContent);
			return message;
		} catch (Exception ex){
			Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static Message prepareMessagePacientes(Session session, String micorreo, String recepient, String Usuario) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(micorreo));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("Mis pacientes");

			Multipart emailContent = new MimeMultipart();
			//Cuerpo del mail

			MimeBodyPart textbody = new MimeBodyPart();
			textbody.setText("Aquí se adjunta la lista de mis pacientes a fecha: "+fecha_y_hora());

			MimeBodyPart csvAttachment = new MimeBodyPart();
			csvAttachment.attachFile(Usuario+".csv");

			emailContent.addBodyPart(textbody);
			emailContent.addBodyPart(csvAttachment);

			message.setContent(emailContent);
			return message;
		} catch (Exception ex){
			Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static String editarDato(String usuario) {

		String paciente = null;
		int salidaInicial = 0;

		while (salidaInicial == 0) {
			System.out.print("\nEscribe a continuación el nombre del paciente de tu lista que quieres editar:");
			Scanner teclado = new Scanner (System.in);
			if (teclado.hasNextLine()) {

				String nombreFichero = (usuario + ".csv") ;
				int ColumnasA = contarColumnas(nombreFichero);
				String [][]pacientes = leercsv(nombreFichero);

				String nombreFicheroP = ("Pacientes.csv") ;
				int ColumnasB = contarColumnas(nombreFicheroP);
				String [][]pacientesT = leercsv(nombreFicheroP);


				paciente = teclado.next().toUpperCase();

				int longitudX =	buscarEnMatriz (pacientes, paciente, 0);
				int longitudY =	buscarEnMatriz (pacientesT,paciente, 0);

				if (longitudX == pacientes.length){
					System.out.println("Error el usuario elegido no existe");
				}
				else{ 

					System.out.print("\n\nEl estatus actual del paciente " + paciente+ " es:"+ pacientes[longitudX][1]+". A que estado quieres cambiarlo?:");	

					teclado = new Scanner (System.in);
					if (teclado.hasNextLine()) {
						String opcionC = teclado.next().toUpperCase();

						System.out.print("\n\nSi quieres guardar "+ opcionC +" como estado del paciente "+ paciente +" pulsa 1,"
								+ "en otro caso pulsa cualquier otra tecla:");

						int Opcion3 = 0;
						teclado = new Scanner(System.in);
						if (teclado.hasNextInt()) {
							Opcion3 = teclado.nextInt();

							if (Opcion3 == 1)  {
								pacientes[longitudX][1] = opcionC;
								pacientesT[longitudY][1] = opcionC;

								File nombreFichero1 = new File("Pacientes.csv");
								nombreFichero1.delete();
								ApendizarFichero (pacientesT, 3, "Pacientes.csv", 0);

								File nombreFichero2 = new File(usuario + ".csv");
								nombreFichero2.delete();
								ApendizarFichero (pacientes, 3, usuario + ".csv", 0);

							}
						}
						salidaInicial = 1;
					}
				}
			}
		}
		return paciente;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static String DarDeBaja (String medico, String Tipo) {

		String medicoPaciente = null;
		int salidaInicial = 0;
		Scanner teclado = null;
		String nombreFichero;
		String [][] usuarios;

		if (Tipo == "M") {
			while (salidaInicial == 0) {
				System.out.print("\nEscribe a continuación el nombre del medico que quieres dar de baja:");
				teclado = new Scanner (System.in);
				if (teclado.hasNextLine()) {

					nombreFichero = ("Usuarios.csv") ;
					int ColumnasA = contarColumnas(nombreFichero);
					usuarios = leercsv(nombreFichero);


					medicoPaciente = teclado.next().toUpperCase();

					int longitudX =	buscarEnMatriz (usuarios, medicoPaciente, 0);

					if (longitudX == usuarios.length){
						System.out.println("Error el usuario elegido no existe");
					}
					else{ 

						System.out.print("\n\nEl usuario elegido es: " + medicoPaciente+ ". Si quieres dar de baja a este usuario pulsa 1,"
								+ "en otro caso pulsa cualquier otra tecla:");	

						int Opcion3 = 0;
						teclado = new Scanner(System.in);
						if (teclado.hasNextInt()) {
							Opcion3 = teclado.nextInt();

							if (Opcion3 == 1)  {

								File nombreFichero1 = new File("Usuarios.csv");
								nombreFichero1.delete();


								String [][] Movimientos1 = new String [longitudX][3];
								for (int i = 0 ; i<longitudX ;i++) {
									for(int j = 0; j<3; j++) {
										Movimientos1[i][j] = usuarios[i][j];
									}
								}
								ApendizarFichero (Movimientos1, 3, "Usuarios.csv", 0);

								String [][] Movimientos2 = new String [usuarios.length-longitudX-1][3];	
								int contador = 0;
								if(longitudX < usuarios.length) {
									for (int i = longitudX+1 ; i< usuarios.length ;i++) {

										for(int j = 0; j<3; j++) {
											Movimientos2[contador][j] = usuarios[i][j];
										}
										contador++;
									}
									ApendizarFichero (Movimientos2, 3, "Usuarios.csv", 0);
								}

								String Original = String.valueOf(medicoPaciente);
								Original = Original + ".csv";
								File nombreFichero2 = new File(Original);
								nombreFichero2.delete();


								System.out.println("Todo ok sin problemas");
								salidaInicial =1;
							}
						}
						salidaInicial =1;	
					}
				}
			}
		}

		else {
			while (salidaInicial == 0) {
				System.out.print("\nEscribe a continuación el nombre del paciente de tu lista que quieres eliminar:");
				teclado = new Scanner (System.in);
				if (teclado.hasNextLine()) {

					nombreFichero = (medico + ".csv") ;
					int ColumnasA = contarColumnas(nombreFichero);
					String [][]pacientes = leercsv(nombreFichero);

					String nombreFicheroP = ("Pacientes.csv") ;
					int ColumnasB = contarColumnas(nombreFicheroP);
					String [][]pacientesT = leercsv(nombreFicheroP);


					medicoPaciente = teclado.next().toUpperCase();

					int longitudX =	buscarEnMatriz (pacientes, medicoPaciente, 0);
					int longitudY =	buscarEnMatriz (pacientesT, medicoPaciente, 0);

					if (longitudX == pacientes.length){
						System.out.println("Error el usuario elegido no existe");
					}
					else{ 

						System.out.print("\n\nEl paciente elegido es: " + medicoPaciente+ ". Si quieres eliminarlo pulsa 1,"
								+ "en otro caso pulsa cualquier otra tecla:");	

						int Opcion3 = 0;
						teclado = new Scanner(System.in);
						if (teclado.hasNextInt()) {
							Opcion3 = teclado.nextInt();

							if (Opcion3 == 1)  {
								//--------------------------------------------------------------
								File nombreFichero1 = new File(medico + ".csv");
								nombreFichero1.delete();
								//--------------------------------------------------------------
								File nombreFichero2 = new File("Pacientes.csv");
								nombreFichero2.delete();

								//--------------------------------------------------------------
								String [][] Movimientos1 = new String [longitudX][3];
								for (int i = 0 ; i<longitudX ;i++) {
									for(int j = 0; j<3; j++) {
										Movimientos1[i][j] = pacientes[i][j];
									}
								}
								ApendizarFichero (Movimientos1, 3, medico + ".csv", 0);
								//--------------------------------------------------------------
								String [][] Movimientos3 = new String [longitudY][3];
								for (int i = 0 ; i<longitudY ;i++) {
									for(int j = 0; j<3; j++) {
										Movimientos3[i][j] = pacientesT[i][j];
									}
								}
								ApendizarFichero (Movimientos3, 3, "Pacientes.csv", 0);
								//---------------------------------------------------------------
								String [][] Movimientos2 = new String [pacientes.length-longitudX-1][3];	
								int contador = 0;
								if(longitudX < pacientes.length) {
									for (int i = longitudX+1 ; i< pacientes.length ;i++) {

										for(int j = 0; j<3; j++) {
											Movimientos2[contador][j] = pacientes[i][j];
										}
										contador++;
									}
									ApendizarFichero (Movimientos2, 3, medico + ".csv", 0);
								}
								//--------------------------------------------------------------
								String [][] Movimientos4 = new String [pacientesT.length-longitudY-1][3];	
								int contador2 = 0;
								if(longitudY < pacientesT.length) {
									for (int i = longitudY+1 ; i< pacientesT.length ;i++) {

										for(int j = 0; j<3; j++) {
											Movimientos4[contador][j] = pacientesT[i][j];
										}
										contador++;
									}
									ApendizarFichero (Movimientos4, 3, "Pacientes.csv", 0);
								}
								//--------------------------------------------------------------

								String Original = String.valueOf(medicoPaciente);
								Original = Original + ".csv";
								File nombreFicheroDel = new File(Original);
								nombreFicheroDel.delete();


								System.out.println("Todo ok sin problemas");
								salidaInicial =1;
							}
						}
						salidaInicial =1;	
					}
				}
			}

		}


		return medicoPaciente;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void Log (String Logout, String u){
		/// gestión log
		String [][] log = new String[1][3];
		LocalDateTime fechaHora = LocalDateTime.now();
		DateTimeFormatter FormatoDeFechaYHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String FechaYHora = fechaHora.format(FormatoDeFechaYHora);
		//escribe los datos
		log [0][0] = FechaYHora.toString();
		log [0][1] = "El usuario " + u;
		log [0][2] = Logout;
		ApendizarFichero(log,3, "Log.csv",0);
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void FicheroNuevo (String FicheroN){
		// funcion que crea un fichero nuevo
		try {
			File myObj = new File(FicheroN + ".csv");
			if (myObj.createNewFile()) {
				System.out.println("");
			} else {
				System.out.println("File already exists.");
			}  
		}catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static String[][] AgrandarMatriz (String [][] MatrizAgrandar, int FilasPlus, int ColumnasMax, int FilasMax){
		// procedimiento para agrandar matriz
		String [][] Movimientos = new String [FilasMax][ColumnasMax];
		for (int i = 0 ; i<MatrizAgrandar.length ;i++) {
			for(int j = 0; j<ColumnasMax; j++) {
				Movimientos[i][j] = MatrizAgrandar[i][j];
			}
		}
		MatrizAgrandar = new String [MatrizAgrandar.length + FilasPlus][ColumnasMax];
		for (int i = 0 ; i<Movimientos.length ;i++) {
			for(int j = 0; j<ColumnasMax; j++) {
				MatrizAgrandar[i][j] = Movimientos[i][j];
			}
		}
		return MatrizAgrandar;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void ApendizarFichero (String[][] Ticket,int ColumnasMax, String ArchivoApendizar, int Contador1) {
		//Procedimiento para apendizar un fichero
		BufferedWriter bw = null;
		try {
			// -------------------- 
			// APPEND MODE SET HERE
			// --------------------
			bw = new BufferedWriter(new FileWriter(ArchivoApendizar, true));
			for (int i = Contador1 ; i<Ticket.length;i++) {
				for(int j = 0; j<ColumnasMax; j++) {
					bw.write(Ticket[i][j]);
					if (j < ColumnasMax-1) {
						bw.write(";");
					}
				}
				if (i < Ticket.length) {
					bw.newLine();
				}

			}
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {                       
			if (bw != null) try {
				bw.close();
			} catch (IOException ioe2) {

			}
		} // end try/catch/finally

	}
	//--------------------------------------------------------------------------------------------------------------------
	public static String añadirAMatriz(String [][] datos, String medicoPaciente, String medico) {

		int Salida1 = 0;
		String FicheroN = "";
		String Opcion2 = "";	
		String opcion3 = null;
		int[] largo = new int[2];
		int Salida2 = 0;

		//Si lo que se quiere añadir es una calle se entrara en esta parte para añadir una calle y sera en el fichero asociado a la localidad 
		while (Salida1 == 0 && medicoPaciente.equals("P")) {
			Salida2 = 0;

			while (Salida2 == 0) {
				System.out.print("\nEscribe el nombre del paciente que quieres añadir:");

				Opcion2 = null;
				Scanner teclado = new Scanner(System.in);
				if (teclado.hasNextLine())  {
					Opcion2 = teclado.nextLine().toUpperCase();

					System.out.print("\nEscribe la cantidad de miligramos de oxigeno en sangre del paciente que quieres añadir:");

					if (teclado.hasNextInt())  {
						opcion3 = teclado.next();

						System.out.print("\n" + "El nombre que has elegido es: " + Opcion2 + " y la cantidad de mm es "+opcion3+". Si quieres guardar tu respuesta pulsa 1,"
								+ " en otro caso pulsa cualquier otra tecla:");
						int Opcion3 = 0;
						teclado = new Scanner(System.in);

						if (teclado.hasNextInt()) {
							Opcion3 = teclado.nextInt();
							if (Opcion3 == 1)  {

								String nombreFichero = (medico + ".csv") ;
								int ColumnasD = contarColumnas(nombreFichero);
								String [][] nombreM = leercsv(nombreFichero);

								String [][]nombreP = new String [1][3];


								nombreP[0][0] = Opcion2;
								nombreP[0][1] = "DESCONOCIDO";
								nombreP[0][2] = opcion3;

								ApendizarFichero(nombreP,3,medico+".csv",0);
								ApendizarFichero(nombreP,3,"Pacientes.csv",0);
								Salida1 = 1;
								Salida2 = 1;
								System.out.println("\n\nGuardado con exito");
							}
							else {
								Salida1 = 1;
								Salida2 = 1;
							}
						}
						else {
							Salida1 = 1;
							Salida2 = 1;
						}
					} else {

						System.out.println("Error no has introducido un número entero. Comienza de nuevo");
					}


				}
				else {
					System.out.println("Error. Introduce el nombre del paciente nuevo");
				}

			}
		}



		//En el caso de que sea una localidad hara lo mismo pero en el fichero de la provincia
		while (Salida1 == 0 && medicoPaciente.equals("M")) {

			System.out.print("\nEscribe el nombre del médico que quieres añadir:");
			Opcion2 = null;
			Scanner teclado = new Scanner(System.in);
			if (teclado.hasNextLine())  {
				Opcion2 = teclado.nextLine().toUpperCase();


				datos = AgrandarMatriz (datos,1, 3, datos.length);
				datos[datos.length-1][0] = Opcion2;
				datos[datos.length-1][1] = "1234";
				datos[datos.length-1][2] = "2";

				FicheroN = datos[datos.length-1][0].toUpperCase();


				// recoger la opción
				System.out.print("\n" + "El nombre que has elegido es: " + Opcion2 + ". Si quieres guardar tu respuesta pulsa 1,"
						+ " en otro caso pulsa cualquier otra tecla:");
				int Opcion3 = 0;
				teclado = new Scanner(System.in);

				if (teclado.hasNextInt()) {
					Opcion3 = teclado.nextInt();
					if (Opcion3 == 1)  {

						// Se crea un fichero nuevo para la localidad						
						FicheroNuevo (FicheroN);
						String [][] Cabecera = new String [1][1];
						Cabecera [0][0] = "              PACIENTE;           ESTADO";
						int Contador1 = 0;
						ApendizarFichero(Cabecera,1, FicheroN+ ".csv",Contador1);
						//gestión de salvar fichero
						String nombreFichero = ("Usuarios.csv") ;

						ApendizarFichero (datos,3,nombreFichero,datos.length-1);



						System.out.println("\nGuardado con exito \n\n");
						Salida1 = 1;

					}
					else {
						Salida1 = 1;
					}		
				}
				else {
					Salida1 = 1;
				}

			}
			else {
				System.out.println("Respuesta incorrecta");
			}


		}

		return Opcion2;
	}
	//--------------------------------------------------------------------------------------------------------------------	
	public static String usuario (String[][] datos) {

		int salidaInicial =0;
		String opcionUsuario = null;
		String contraseña = null;
		Scanner usuario = null;

		//Saca el menu
		while (salidaInicial == 0) {
			System.out.print("\nBienvenido al HospitalManolo!!\n\nIntroduzca el nombre de usuario con el que desea iniciar sesión (Si desea apagar el equipo introduzca 0):");
			usuario = new Scanner (System.in);

			if (usuario.hasNextLine()) {
				opcionUsuario = usuario.next().toUpperCase();

				int longitudX =	buscarEnMatriz (datos, opcionUsuario, 0);

				if (opcionUsuario.equals("0")) {
					System.out.println("Hasta la proxima!!");
					salidaInicial =1;
				}
				else if (longitudX == datos.length){
					System.out.println("Error el usuario elegido no existe");
				}
				else{ 
					System.out.print("\n\nEl usuario elegido es: " + opcionUsuario
							+ "\n\nContraseña (asegurese de que mayusculas sean escritas como tal en caso de que las haya):");

					usuario = new Scanner (System.in);
					if (usuario.hasNextLine()) {
						contraseña = usuario.next();

						if (contraseña.equals(datos[longitudX][1])) {
							salidaInicial =1;
						}
						else {
							System.out.println("Error la contraseña no coincide");
						}
					}
				} 
			}
		}
		return opcionUsuario;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static int buscarEnMatriz (String [][] MatrizBuscar, String ABuscar, int ColumnaBuscar){
		// Procedimiento de buscar en matriz
		int LongitudXX = 0;
		int Contador1 = 1;
		while(Contador1 < MatrizBuscar.length){
			if(ABuscar.equals(MatrizBuscar [Contador1][ColumnaBuscar])) {
				LongitudXX = Contador1 ;
				Contador1 = MatrizBuscar.length;
			}
			Contador1 = Contador1 + 1;
			if(Contador1 == MatrizBuscar.length) {
				LongitudXX = Contador1 ;
			}
		}
		return LongitudXX;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static int contarFilas(String nombreFichero) {
		int contadorF = 0;
		try {
			BufferedReader br = null;
			//Crear un objeto BufferedReader al que se le pasa 
			//un objeto FileReader con el nombre del fichero
			br = new BufferedReader(new FileReader(nombreFichero));
			//Leer la primera línea, guardando en un String
			String texto = br.readLine();        
			//Repetir mientras no se llegue al final del fichero calcular contadorF o sea cuenta filas
			while(texto != null) {
				contadorF = contadorF + 1;
				//Leer la siguiente línea
				texto = br.readLine();  }
			if(br != null)
				br.close(); }
		catch (FileNotFoundException e) {
			System.out.println("Error: Fichero no encontrado");  }
		catch(Exception e) {
			System.out.println("Error de lectura del fichero");
			System.out.println(e.getMessage());}
		return contadorF;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static int contarColumnas(String nombreFichero) {
		int contadorC = 1;
		try {
			// contadorC o de columnas
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(nombreFichero));
			String texto = br.readLine();
			String objetivo = ";";
			//Repetir mientras no se llegue al final de la línea calcular contadorC contador de columnas
			while (texto.indexOf(objetivo) != -1) {
				// Reduce el tamaño de la cadena de la primera línea hasta el ; incluido
				texto = texto.substring(texto.indexOf(
						objetivo)+objetivo.length(),texto.length());
				contadorC = contadorC + 1; }
			if(br != null)
				br.close();}
		catch (FileNotFoundException e) {
			System.out.println("Error: Fichero no encontrado");  }
		catch(Exception e) {
			System.out.println("Error de lectura del fichero");
			System.out.println(e.getMessage());}			
		return contadorC;
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static String[][] leercsv(String nombreFichero) {
		//procedimiento de Leer fichero
		int Filas = contarFilas(nombreFichero);	   		   
		int Columnas = contarColumnas(nombreFichero)	;     
		String [][]   Matriz  ={};   
		Matriz  = new String [Filas][Columnas];    
		try {
			BufferedReader br = null; 
			br = new BufferedReader(new FileReader(nombreFichero));
			String texto = br.readLine();      
			String objetivo = ";";
			int posicion = 0;     
			int contador1 = 0;	
			int contador2 = 0;
			// Corta las líneas del archivo csv por el separador ; y los salva en matriz
			while(contador1 < Filas){
				while(contador2 < Columnas){
					//Inicia columna de la matriz
					if(texto.indexOf(objetivo)>-1) {
						posicion = texto.indexOf(objetivo);
						Matriz [contador1][contador2] = texto.substring(0,posicion);
						texto = texto.substring(texto.indexOf(objetivo)+1,texto.length()); 
					}
					if(texto.indexOf(objetivo) < 0) {
						contador2 = contador2 + 1 ; 		
						Matriz [contador1][contador2] = texto;
					}
					contador2 = contador2 + 1 ;   
				}
				//Cambiamos la fila
				texto = br.readLine();
				contador1 = contador1 + 1;
				contador2 = 0;       
			}
			if(br != null)
				br.close(); }
		catch (FileNotFoundException e) {
			System.out.println("Error: Fichero no encontrado");  }
		catch(Exception e) {
			System.out.println("Error de lectura del fichero");
			System.out.println(e.getMessage());}
		return Matriz;        		             
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static int MenuDeOpciones (int OpcionUsuario, String nombre) {
		//procedimiento de Menú de opciones inicial
		int opcion = 0;

		if (OpcionUsuario == 1) {
			Scanner teclado = new Scanner(System.in);
			// Informar de las opciones
			System.out.print(
					"\n-------- BIENVENIDO/A "+ nombre +" --------" + "\n" +
							"  0  - Cerrar sesión" + "\n" +
							"  1  - Mostrar todos los medicos" + "\n" +
							"  2  - Añadir médico a la plantilla" + "\n" +
							"  3  - Dar de baja a médicos de la plantilla" + "\n" +
							"  4  - Informe general o mostrar todos los pacientes" + "\n" +
							"  5  - Mostrar mis pacientes" + "\n" +
							"  6  - Añadir un paciente" + "\n" +
							"  7  - Eliminar un paciente" + "\n" +
							"  8  - Editar datos de un paciente" + "\n" +
							"  9  - Listar log (Opción unica de administrador)" + "\n" +
							"  10 - Cambiar contraseña" + "\n" +
							"  " + "\n" + 
							"Opción:"
					);

			// recoger la opción
			if (teclado.hasNextInt()) {
				opcion = teclado.nextInt();
			}
			else {
				teclado.next(); // Vaciar la basura (no int) del teclado...
			}
		} else {

			Scanner teclado = new Scanner(System.in);
			// Informar de las opciones
			System.out.print(
					"\n-------- BIENVENIDO/A "+ nombre +" --------" + "\n" +
							"  0 - Cerrar sesión" + "\n" +
							"  1 - Mostrar todos mis pacientes" + "\n" +
							"  2 - Añadir un paciente" + "\n" +
							"  3 - Eliminar un paciente" + "\n" +
							"  4 - Editar datos de un paciente" + "\n" +
							"  5 - Cambiar contraseña" + "\n" +
							"  " + "\n" +
							"Opción:"
					);

			// recoger la opción
			if (teclado.hasNextInt()) {
				opcion = teclado.nextInt();
			}
			else {
				teclado.next(); // Vaciar la basura (no int) del teclado...
			}
		}
		return opcion;
	}
	//--------------------------------------------------------------------------------------------------------------------	
	public static void ListarMatriz(String[][] MatrizLista, int ColumnasX) {
		// procedimiento de Listat matrices
		int LongitudX = 0;
		int Contador1 = 0;
		int Contador2 = 0;
		LongitudX = MatrizLista.length;
		int[] largo = new int[ColumnasX];
		while(Contador1 < LongitudX){
			while(Contador2 < ColumnasX){
				if(Contador1 == 0) {
					largo[Contador2] = MatrizLista [Contador1][Contador2].length() + 2;
				} else {
					System.out.printf("%"+largo[Contador2]+"s", MatrizLista [Contador1][Contador2] + "");
				}
				Contador2++ ;   
			}

			System.out.println("");
			//Cambiamos la fila     
			Contador1++;
			Contador2 = 0 ;
		}
	}
	//--------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) throws MessagingException, IOException {
		// TODO Auto-generated method stub

		String nombreFichero = ("Usuarios.csv") ;
		int columnasA = contarColumnas(nombreFichero);
		String [][] usuarios = leercsv(nombreFichero);
		int filaUsuario = 0;
		int tipoMenu = 0;
		int salidaU = 0;
		int salidaM = 0;
		int opcionMenu;
		String logout = null;
		int Opcion3 = 0;
		Scanner teclado;


		while (salidaU == 0) {

			salidaM = 0;
			String u = usuario (usuarios);

			if (u.equals("0")) {
				salidaU = 1;
			}else {

				filaUsuario =	buscarEnMatriz (usuarios, u, 0);
				tipoMenu = Integer.parseInt(usuarios[filaUsuario][2]);

				while (salidaM == 0) {

					opcionMenu = MenuDeOpciones(tipoMenu, u);

					switch (opcionMenu) {

					case 1:
						if (tipoMenu == 1) {

							nombreFichero = ("Usuarios.csv") ;
							int ColumnasA = contarColumnas(nombreFichero);
							usuarios = leercsv(nombreFichero); 

							//gestión log
							logout = "Abierta opción Mostrar médicos";
							Log (logout, u);

							System.out.println("                    Listado de médicos");
							System.out.println("  -------------------------------------------------------------");
							System.out.println("            Usuario            Contraseña       TipoUsuario");
							ListarMatriz(usuarios, ColumnasA);

							logout = "Cerrada opción Mostrar médicos";
							Log (logout, u);

							System.out.print("\n\nSi quieres enviarte el listado de los usuarios al correo pulsa 1,"
									+ "en otro caso pulsa cualquier otra tecla:");	

							Opcion3 = 0;
							teclado = new Scanner(System.in);
							if (teclado.hasNextInt()) {
								Opcion3 = teclado.nextInt();

								if (Opcion3 == 1)  {

									sendMail_Usuarios("alexgc025@gmail.com");

								}	
							}

						}else {

							nombreFichero = (u+".csv") ;
							int ColumnasA = contarColumnas(nombreFichero);
							usuarios = leercsv(nombreFichero); 

							//gestión log
							logout = "Abierta opción Mostrar los pacientes de:"+u;
							Log (logout, u);

							System.out.println("                    Listado de mis pacientes");
							System.out.println("  -------------------------------------------------------------");
							System.out.println("              PACIENTE           ESTADO      MM DE O2");
							ListarMatriz(usuarios, ColumnasA);

							logout = "Cerrada opción Mostrar los pacientes de:"+u;
							Log (logout, u);

							System.out.print("\n\nSi quieres enviarte el listado de tus pacientes al correo pulsa 1,"
									+ "en otro caso pulsa cualquier otra tecla:");	

							Opcion3 = 0;
							teclado = new Scanner(System.in);
							if (teclado.hasNextInt()) {
								Opcion3 = teclado.nextInt();

								if (Opcion3 == 1)  {

									sendMail_Pacientes("alexgc025@gmail.com", u);

								}	
							}
						}
						break;
					case 2: 

						if (tipoMenu == 1) {

							nombreFichero = ("Usuarios.csv") ;
							int ColumnasD = contarColumnas(nombreFichero);
							usuarios = leercsv(nombreFichero);

							String OpcionM = añadirAMatriz(usuarios, "M", u);

							logout = "Se ha añadido al médico: "+OpcionM;
							Log (logout, u);

						}else {

							String OpcionP = añadirAMatriz(usuarios, "P", u);

							logout = "Se ha añadido al paciente: "+OpcionP;
							Log (logout, u);
						}

						break;
					case 3: 
						if (tipoMenu == 1) {

							nombreFichero = ("Usuarios.csv") ;
							int ColumnasD = contarColumnas(nombreFichero);
							usuarios = leercsv(nombreFichero);

							String OpcionM = DarDeBaja (u, "M");

							logout = "Se ha dado de baja al usuario: "+OpcionM;
							Log (logout, u);

						}else {

							String OpcionM = DarDeBaja (u, "P");

							logout = "Se ha dado de baja al paciente: "+OpcionM;
							Log (logout, u);
						}
						break;
					case 4:  

						if (tipoMenu == 1) {

							nombreFichero = ("Pacientes.csv") ;
							int ColumnasA = contarColumnas(nombreFichero);
							usuarios = leercsv(nombreFichero); 

							System.out.print("\n\nPara mostrar el informe general de los pacientes pulsa 1, para mostrar todos los pacientes pulsa 2."
									+ "Para volver al menu pulsa cualquier otra tecla:");	

							Opcion3 = 0;
							teclado = new Scanner(System.in);
							if (teclado.hasNextInt()) {

								Opcion3 = teclado.nextInt();

								if (Opcion3 == 2)  {

									//gestión log
									logout = "Abierta opción Mostrar todos los pacientes";
									Log (logout, u);

									System.out.println("                    Listado de todos los pacientes");
									System.out.println("  -------------------------------------------------------------");
									System.out.println("              PACIENTE           ESTADO      MM DE O2");
									ListarMatriz(usuarios, ColumnasA);

									logout = "Cerrada opción Mostrar todos los pacientes";
									Log (logout, u);

									System.out.print("\n\nSi quieres enviarte el listado de todos los pacientes registrados al correo pulsa 1,"
											+ "en otro caso pulsa cualquier otra tecla:");	

									Opcion3 = 0;
									teclado = new Scanner(System.in);
									if (teclado.hasNextInt()) {
										Opcion3 = teclado.nextInt();

										if (Opcion3 == 1)  {

											sendMail_PacientesT("alexgc025@gmail.com");

										}	
									}
								} else if (Opcion3 ==1) {

									logout = "Abierta opción Mostrar informe general";
									Log (logout, u);

									informe(nombreFichero);
								}

							}

						}else {


							String paciente = editarDato(u);

							logout = "Se ha editado al paciente: "+paciente;
							Log (logout, u);

						}
						break;
					case 5:
						if (tipoMenu == 1) {

							nombreFichero = (u+".csv") ;
							int ColumnasA = contarColumnas(nombreFichero);
							usuarios = leercsv(nombreFichero); 

							//gestión log
							logout = "Abierta opción Mostrar los pacientes de:"+u;
							Log (logout, u);

							System.out.println("                    Listado de mis pacientes");
							System.out.println("  -------------------------------------------------------------");
							System.out.println("              PACIENTE           ESTADO      MM DE O2");
							ListarMatriz(usuarios, ColumnasA);

							logout = "Cerrada opción Mostrar los pacientes de:"+u;
							Log (logout, u);

							System.out.print("\n\nSi quieres enviarte el listado de mis pacientes al correo pulsa 1,"
									+ "en otro caso pulsa cualquier otra tecla:");	

							Opcion3 = 0;
							teclado = new Scanner(System.in);
							if (teclado.hasNextInt()) {
								Opcion3 = teclado.nextInt();

								if (Opcion3 == 1)  {

									sendMail_Pacientes("alexgc025@gmail.com", u);

								}	
							}

						}else {
							int salidaC = 0;
							while (salidaC == 0) {
								System.out.print("Introduzca la contraseña actual(o para salir itroduce 0):");
								teclado = new Scanner(System.in);
								String opcion = teclado.next();

								if(opcion.equals("0")) {
									salidaC = 1;
								}
								else if (opcion.equals(usuarios[filaUsuario][1])) {
									System.out.print("Introduzca la contraseña nueva:");
									teclado = new Scanner(System.in);
									opcion = teclado.next();

									System.out.print("Introduzca la nueva contraseña de nuevo para confirmar el cambio:");
									teclado = new Scanner(System.in);
									String opcion2 = teclado.next();

									if (opcion.equals(opcion2)) {

										usuarios[filaUsuario][1] = opcion2;

										File fichero = new File("Usuarios.csv");
										fichero.delete();
										File myObj = new File("Usuarios.csv");
										ApendizarFichero (usuarios,3, "Usuarios.csv", 0);

										System.out.println("La contraseña se ha cambiado con exito");

										logout = "Se ha cambiado la contraseña";
										Log (logout, u);
										salidaC=1;
									} else {
										System.out.println("La contraseña no coincide");
									}

								}else {
									System.out.println("Contraseña incorrecta!");
								}
							}
						}
						break;
					case 6:
						if (tipoMenu == 1) {

							String OpcionP = añadirAMatriz(usuarios, "P", u);

							logout = "Se ha añadido al paciente: "+OpcionP;
							Log (logout, u);
						}else {
							System.out.println("Opción incorrecta!");
						}
						break;
					case 7:
						if (tipoMenu == 1) {
							nombreFichero = (u+".csv") ;
							int ColumnasA = contarColumnas(nombreFichero);
							usuarios = leercsv(nombreFichero); 

							String OpcionM = DarDeBaja (u, "P");

							logout = "Se ha dado de baja al paciente: "+OpcionM;
							Log (logout, u);
						}
						break;
					case 8:
						if (tipoMenu == 1) {

							String paciente = editarDato(u);

							logout = "Se ha editado al paciente: "+paciente;
							Log (logout, u);
						}
						break;
					case 9:
						if (tipoMenu == 1) {
							logout = "Abierta opción Listar log";
							Log (logout, u);	

							nombreFichero = ("Log.csv") ;
							int ColumnasA = contarColumnas(nombreFichero);
							String [][]    log1  = {}; 
							log1 = leercsv(nombreFichero); 

							String [][]MatrizLista = log1;
							int ColumnasX = ColumnasA;
							System.out.println("                                         Listado Log");
							System.out.println("            -----------------------------------------------------------------------------------------");
							log1 = leercsv(nombreFichero); 
							MatrizLista = log1;
							System.out.println("            Hora y Fecha del Evento            Usuario                                         Descripcion del Evento");
							ListarMatriz( MatrizLista, ColumnasX);

							//gestión log
							logout = "Cerrada opción Listar log";
							Log (logout, u);


							System.out.print("\n\nSi quieres enviarte el listado log al correo pulsa 1,"
									+ "en otro caso pulsa cualquier otra tecla:");	

							Opcion3 = 0;
							teclado = new Scanner(System.in);
							if (teclado.hasNextInt()) {
								Opcion3 = teclado.nextInt();

								if (Opcion3 == 1)  {

									sendMail_LOG("alexgc025@gmail.com");

								}	
							}
						}
						break;
					case 10:

						nombreFichero = ("Usuarios.csv") ;
						int ColumnasA = contarColumnas(nombreFichero);
						usuarios = leercsv(nombreFichero);
						if (tipoMenu == 1) {
							int salidaC = 0;
							while (salidaC == 0) {
								System.out.print("Introduzca la contraseña actual(o para salir introduce 0):");
								teclado = new Scanner(System.in);
								String opcion = teclado.next();

								if(opcion.equals("0")) {
									salidaC = 1;
								}
								else if (opcion.equals(usuarios[filaUsuario][1])) {
									System.out.print("Introduzca la contraseña nueva:");
									teclado = new Scanner(System.in);
									opcion = teclado.next();

									System.out.print("Introduzca la nueva contraseña de nuevo para confirmar el cambio:");
									teclado = new Scanner(System.in);
									String opcion2 = teclado.next();

									if (opcion.equals(opcion2)) {

										usuarios[filaUsuario][1] = opcion2;

										File fichero = new File("Usuarios.csv");
										fichero.delete();
										File myObj = new File("Usuarios.csv");
										ApendizarFichero (usuarios,3, "Usuarios.csv", 0);

										System.out.println("La contraseña se ha cambiado con exito");

										logout = "Se ha cambiado la contraseña";
										Log (logout, u);
										salidaC=1;
									} else {
										System.out.println("La contraseña no coincide");
									}

								}else {
									System.out.println("Contraseña incorrecta!");
								}
							}
						}else {
							System.out.println("Opción incorrecta!");
						}

						break;
					case 0:
						//Abre la opcion salir
						//gestión log
						logout = "Cierra la sesion";
						Log (logout, u);		

						salidaM=1;
						System.out.println("¡Hasta Pronto!");
						break;
					default:
						//Imprime error si la opcion no es una de las del menu
						System.out.println("Opción incorrecta!");
					}

				}
			}



		}



	}

}