package com.ticketadvantage.services.dao.sites.metallica.captcha;
import java.io.PrintWriter;
	public class my_main {
		public static void main(String[] args) {
			String input_file="dest.png";
			String output_file="333";	
			String tesseract_install_path="E:\\ProgramFiles\\Tesseract-OCR\\tesseract";
			String[] command =
		    {
		        "cmd",
		    };
		    Process p;
			try {
				p = Runtime.getRuntime().exec(command);
			        new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		                new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		                PrintWriter stdin = new PrintWriter(p.getOutputStream());
		                stdin.println("\""+tesseract_install_path+"\" \""+input_file+"\" \""+output_file+"\" -l eng");
		        	    stdin.close();
		                p.waitFor();
		                System.out.println();
		                System.out.println();
		                System.out.println();
		                System.out.println();
		                System.out.println();
		                String str = Read_File.read_a_file(output_file+".txt");
		                System.out.println(str.replaceAll("\\s",""));
		                
		    	} catch (Exception e) {
		 		e.printStackTrace();
			}
		}	
	}	