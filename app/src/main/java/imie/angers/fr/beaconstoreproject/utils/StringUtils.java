package imie.angers.fr.beaconstoreproject.utils;

/**
 * Created by Anne on 02/03/2016.
 */
public class StringUtils {

    /**
     * Permet de hasher le mot de passe suivant l'algorithme md5
     * @param md5
     * @return
     */
    public static String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
                StringBuffer sb = new StringBuffer();
            for (byte anArray : array) {

                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
