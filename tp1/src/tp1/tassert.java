package tp1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class tassert {

	public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("manque ou trop de fichier");
            System.exit(1);
        }

        System.out.println(calculerTASSERT(args[0]));
    }

	public static int calculerTASSERT(String fichierSource) {
		
		int tassert = 0;
		List<String> listA =Arrays.asList("assertArrayEquals", "assertEquals","assertFalse","assertNotEquals",
				"assertNotNull","assertNull","assertSame","assertThat","assertThrows","assertTrue","fail");

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
                	for(int i=0 ; i<listA.size();i++) {
                		//verifier si c'est une assertion
                		if(ligne.startsWith(listA.get(i))) {
                			tassert++;
                		}
                	
                	}
                	
                }
            }
        } catch (IOException e) {
            System.err.println("Une erreur : " + e.getMessage());
            System.exit(2);
        }

        return tassert;
	}
	
	
}