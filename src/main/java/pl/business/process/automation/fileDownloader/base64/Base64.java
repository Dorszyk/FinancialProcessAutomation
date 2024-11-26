package pl.business.process.automation.fileDownloader.base64;

public class Base64 {

    public static void main(String[] args) {

        String password = "password";
        String encodedPassword = java.util.Base64.getEncoder().encodeToString(password.getBytes());
        System.out.println("Original password: " + password);
        System.out.println("Encoded password: " + encodedPassword);
    }
}
