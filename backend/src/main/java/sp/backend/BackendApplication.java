package sp.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("Samwise - samwise:sam123456 - passwordHash: " + encoder.encode("sam123456"));
        System.out.println("Merry - merryy:mer123456 - passwordHash: " + encoder.encode("mer123456"));
        System.out.println("Pippin - pippin:pip123456 - passwordHash: " + encoder.encode("pip123456"));
        System.out.println("Frodo - frodoo:fro123456 - passwordHash: " + encoder.encode("fro123456"));
    }

}
