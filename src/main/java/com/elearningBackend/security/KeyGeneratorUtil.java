package com.elearningBackend.security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGeneratorUtil {
    public static String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // Clé de 256 bits (sécurisée)
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération de la clé JWT", e);
        }
    }

    public static double  calculerPoints(int nbMembres) {
        if (nbMembres <= 0) return 0;

        // Tranches : [min, max, coefficient]
        int[][] tranches = {
                {1, 9, 100},
                {10, 18, 125},   // 1.25
                {19, 27, 150},   // 1.5
                {28, 36, 175},   // 1.75
                {37, 45, 200},   // 2
                {46, 54, 225},   // 2.25
                {55, 64, 250}    // 2.5
        };

        double points = 0;

        for (int[] tranche : tranches) {
            int min = tranche[0];
            int max = tranche[1];
            double coef = tranche[2] / 100.0;

            if (nbMembres >= max) {
                points += (max - min + 1) * coef;
            } else if (nbMembres >= min) {
                points += (nbMembres - min + 1) * coef;
                break; // On s'arrête car le reste n'est pas atteint
            }
        }

        return points;
    }

    public static void main(String[] args) {
        String secretKey = generateSecretKey();
        System.out.println("Clé JWT générée : " + secretKey);
        System.out.println(calculerPoints(60));
    }
}

