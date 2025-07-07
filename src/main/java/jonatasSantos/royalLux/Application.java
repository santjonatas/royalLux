package jonatasSantos.royalLux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.awt.*;
import java.net.URI;
import java.util.Locale;

@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		var context = SpringApplication.run(Application.class, args);
		String port = context.getEnvironment().getProperty("server.port", "8080");
		openSwagger(port);
	}

	private static void openSwagger(String port) {
		String url = "http://localhost:" + port + "/swagger-ui/index.html";
		System.out.println("\n📌 Acesse o Swagger UI: " + url + "\n");

		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(url));
				System.out.println("✅ Swagger UI aberto automaticamente!");
				return;
			}

			String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
			String comando = os.contains("win") ? "cmd /c start " + url :
					os.contains("mac") ? "open " + url :
							"xdg-open " + url;

			new ProcessBuilder(comando.split(" ")).start();
			System.out.println("✅ Swagger UI aberto automaticamente!");

		} catch (Exception e) {
			System.out.println("❌ Erro ao abrir navegador: " + e.getMessage());
		}
	}

}
