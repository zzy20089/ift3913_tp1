package tp1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tropcomp {
	public static void main(String[] args) {
	        if (args.length < 1 || args.length > 3) {
	            System.out.println("format incorrect");
	            System.exit(1);
	        }
	        String cheminEntre = null;
	        String sortie = null;

	        for (int i = 0; i < args.length; i++) {
	            if (args[i].equals("-o") && i + 1 < args.length) {
	                sortie = args[i + 1];
	                i++; // Passer au prochain argument
	            } else {
	            	cheminEntre = args[i];
	            }
	        }
	        
	        File dossier = new File(cheminEntre);
	        File[] fichiersDeTest = dossier.listFiles((dir, name) -> name.toLowerCase().endsWith("test.java"));
	        
	        Map<String, Double> tcmpMap = new HashMap<>();
	        for (File fichierDeTest : fichiersDeTest) {
	            String nomClasse = getNomDeLaClasse(fichierDeTest.getName());
	            int tloc = tp1.tloc.calculerTLOC(fichierDeTest.toString());
	            int tassert = tp1.tassert.calculerTASSERT(fichierDeTest.toString());
	            double tcmp = (double) tloc / tassert;
	            tcmpMap.put(nomClasse, tcmp);
	        }
	        
	        //seuil par defaut est 10%
	        double seuil = 10.0;
	        
	        List<String> classesSuspectes = trouverClassesSuspectes(tcmpMap, seuil);
	        
	        if (sortie == null) {
	            // Sortie sur la ligne de commande
	        	for (String classe : classesSuspectes) {
	                System.out.println(classe);
	            }
	        }else {
	        	try (PrintWriter writer = new PrintWriter(new FileWriter(sortie))) {
	                for (String classe : classesSuspectes) {
	                    writer.println(classe);
	                }
	                System.out.println("Les resultats ont été écrites dans " + sortie);
	            } catch (IOException e) {
	                System.out.println("Erreur lors de l'écriture dans le fichier de sortie : " + e.getMessage());
	            }
	        }
	        
	}
	
	private static List<String> trouverClassesSuspectes(Map<String, Double> tcmpMap, double seuil) {
        List<String> classesSuspectes = new ArrayList<>();
        int nombreClasses = tcmpMap.size();

        tcmpMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit((long) (nombreClasses * seuil / 100))
                .forEach(entry -> classesSuspectes.add(entry.getKey()));

        return classesSuspectes;
    }

	private static String getNomDeLaClasse(String nomFichier) {
		int lastIndex = nomFichier.lastIndexOf(".java");
        if (lastIndex != -1) {
            return nomFichier.substring(0, lastIndex);
        }
        return nomFichier;
	}
}
