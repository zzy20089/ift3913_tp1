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
	        if (args.length < 1 || args.length > 4) {
	            System.out.println("format suggere : tls <seuil> -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée>");
	            System.exit(1);
	        }
	        String cheminEntre = null;
	        String sortie = null;
	        //seuil par defaut est 10%
	        double seuil = 10.0;

	        if(args.length==1) {
	        	cheminEntre = args[0];
	        }
	        
	        if(args.length==2) {
	        	seuil=Integer.parseInt(args[0]);
	        	cheminEntre = args[1];
	        }
	        if(args.length==3) {
	        	cheminEntre = args[2];
	        	sortie = args[1]+"\\reponse"+seuil+".csv";
	        }
	        
	        if(args.length==4) {
	        	seuil=Integer.parseInt(args[0]);
	        	cheminEntre = args[3];
	        	sortie = args[2]+"\\reponse"+seuil+".csv";
	        }
	        
	        
	        
	        File dossier = new File(cheminEntre);
	        
	        Map<String, Double> tcmpMap = new HashMap<>();
	        Map<String, Double> tlocMap = new HashMap<>();
	        
	        collecterTous(dossier, tcmpMap, tlocMap);
	        
	        
	        
	        
	        List<String> classesSuspectes = trouverClassesSuspectes(tcmpMap,tlocMap, seuil);
	        
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
	
	private static void collecterTous(File dossier, Map<String, Double> tcmpMap,  Map<String, Double> tlocMap) {
        File[] fichiersDeTest = dossier.listFiles((dir, name) -> name.toLowerCase().endsWith("test.java"));

        for (File fichierDeTest : fichiersDeTest) {
            String nomClasse = getNomDeLaClasse(fichierDeTest.getName());
            int tloc = tp1.tloc.calculerTLOC(fichierDeTest.toString());
            int tassert = tp1.tassert.calculerTASSERT(fichierDeTest.toString());
            double tcmp = (double) tloc / tassert;
            tcmpMap.put(nomClasse, tcmp);
            tlocMap.put(nomClasse, (double) tloc);
        }

        // lire les sous dossiers
        File[] sousDossiers = dossier.listFiles(File::isDirectory);
        if (sousDossiers != null) {
            for (File sousDossier : sousDossiers) {
            	collecterTous(sousDossier, tcmpMap, tlocMap);
            }
        }
    }
	
	private static List<String> trouverClassesSuspectes(Map<String, Double> tcmpMap,Map<String, Double> tlocMap, double seuil) {
        List<String> classesSuspectes1 = new ArrayList<>();
        List<String> classesSuspectes2 = new ArrayList<>();
        List<String> classesSuspectesF = new ArrayList<>();
        int nombreClasses = tcmpMap.size();

        tcmpMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit((long) (nombreClasses * seuil / 100))
                .forEach(entry -> classesSuspectes1.add(entry.getKey()));
        tcmpMap.entrySet().stream()
        		.sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        		.limit((long) (nombreClasses * seuil / 100))
        		.forEach(entry -> classesSuspectes2.add(entry.getKey()));
        for (String str : classesSuspectes1) {
            if (classesSuspectes2.contains(str)) {
            	classesSuspectesF.add(str);
            }
        }

        return classesSuspectesF;
    }

	private static String getNomDeLaClasse(String nomFichier) {
		int lastIndex = nomFichier.lastIndexOf(".java");
        if (lastIndex != -1) {
            return nomFichier.substring(0, lastIndex);
        }
        return nomFichier;
	}
}
