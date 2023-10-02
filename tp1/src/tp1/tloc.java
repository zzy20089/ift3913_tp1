package tp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class tloc {

	public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("manque ou trop de fichier");
            System.exit(1);
        }

        System.out.println(calculerTLOC(args[0]));
    }

	private static int calculerTLOC(String fichierSource) {
		
		int tloc = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fichierSource))) {
            String ligne;
            boolean enCommentaire = false;

            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim(); 

                if (ligne.startsWith("//") || ligne.isEmpty()) {
                    //c'est un commentaire ou vide
                    continue;
                }

                if (ligne.startsWith("/*")) {
                    // un commentaire commence
                    enCommentaire = true;
                    continue;
                }
                if (ligne.endsWith("*/")) {
                    // un commentaire se termine
                    enCommentaire = false;
                    continue;
                }

                if (!enCommentaire) {
                    // la ligne n'est pas un commentaire
                    tloc++;
                }
            }
        } catch (IOException e) {
            System.err.println("Une erreur : " + e.getMessage());
            System.exit(2);
        }

        return tloc;
	}
	
	
}
