package tp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class tls {
	 public static void main(String[] args) {
		 tassert tassert = new tassert();
		 tloc tloc = new tloc();
	        if (args.length < 1 || args.length > 3) {
	            System.out.println("format incorrect");
	            System.exit(1);
	        }
	        String cheminDossier = null;
	        String sortie = null;

	        for (int i = 0; i < args.length; i++) {
	            if (args[i].equals("-o") && i + 1 < args.length) {
	                sortie = args[i + 1]+"\\reponse";
	                i++; // passer au prochain argument
	            } else {
	                cheminDossier = args[i];
	            }
	        }
	        File dossier = new File(cheminDossier);
	        File[] fichiersDeTest = dossier.listFiles((dir, name) -> name.toLowerCase().endsWith("test.java"));

	        if (fichiersDeTest == null || fichiersDeTest.length == 0) {
	            System.out.println("pas de fichier de test trouve");
	            System.exit(1);
	        }
	        
	        List<String[]> lignesCSV = new ArrayList<>();
	        
	        for (File fichierDeTest : fichiersDeTest) {
	            String nomFichier = fichierDeTest.getName();
	            String cheminRelatif = fichierDeTest.getPath();
	            String nomPaquet = getNomDuPaquet(fichierDeTest);
	            String nomClasse = getNomDeLaClasse(nomFichier);
	            int tloc1 = tp1.tloc.calculerTLOC(fichierDeTest.toString());
	            int tassert1 = tp1.tassert.calculerTASSERT(fichierDeTest.toString());
	            double tcmp = (double) tloc1 / tassert1;

	            String[] ligneCSV = {cheminRelatif, nomPaquet, nomClasse, Integer.toString(tloc1), Integer.toString(tassert1), String.format("%.2f", tcmp)};
	            lignesCSV.add(ligneCSV);
	        }
	        if (sortie == null) {
	            // Sortie sur la ligne de commande
	            for (String[] ligne : lignesCSV) {
	                System.out.println(String.join(", ", ligne));
	            }
	        }else {
	        	try (PrintWriter writer = new PrintWriter(new FileWriter(sortie))) {
	                for (String[] ligne : lignesCSV) {
	                    writer.println(String.join(", ", ligne));
	                }
	                System.out.println("Les résultats ont été écrits dans " + sortie);
	            } catch (IOException e) {
	                System.out.println("Erreur lors de l'écriture dans le fichier de sortie : " + e.getMessage());
	            }
	        }
	        
	        
	        
	 }

	private static String getNomDeLaClasse(String nomFichier) {
		int lastIndex = nomFichier.lastIndexOf(".java");
        if (lastIndex != -1) {
            return nomFichier.substring(0, lastIndex);
        }
        return nomFichier;
	}

	private static String getNomDuPaquet(File fichierDeTest) {
		try (BufferedReader br = new BufferedReader(new FileReader(fichierDeTest))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.startsWith("package ")) {
                    return ligne.substring(8, ligne.length() - 1); // Supprimer "package" et ";"
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
	}
}
