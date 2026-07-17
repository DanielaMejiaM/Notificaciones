import java.util.regex.Pattern;
/**
 * Sistema de Notificaciones Empresariales
 * Compilar:  javac NotificationSystem.java
 * Ejecutar:  java NotificationSystem
 */
public final class NotificationSystem {

    public static void main(String[] args) {

        // Array de notificaciones (instancias de cualquiera de los 3 tipos de notificaciones)
        Notification[] notifications = new Notification[] {
                new EmailNotification("ana.perez@empresa.com", "Bienvenida", "Gracias por registrarte."),
                new EmailNotification("carlos@dominio.com", "Factura", "Tu factura está disponible."),
                new SmsNotification("5512345678", "Tu código es 4521"),
                new PushNotification("abc123xyz987", "Tienes un nuevo mensaje"),
                new EmailNotification("luisa@empresa.com", "Recordatorio", "Tu cita es mañana."),
                new SmsNotification("5598765432", "Pago recibido con éxito"),
                new PushNotification("token-9f8e7d6c", "Oferta especial disponible"),
                new PushNotification("device-55aa66bb", "Actualización disponible"),
                new SmsNotification("5511223344", "Tu paquete fue enviado"),
                new EmailNotification("jorge@empresa.com", "Soporte", "Tu ticket fue resuelto."),
                new PushNotification("push-token-001", "Recordatorio de evento"),
                new PushNotification("push-token-002", "Nuevo seguidor")
        };
        procesarNotificaciones(notifications);
    }

    /**
     * Recorre el array de notificaciones, identifica el tipo real de cada una
     */
    private static void procesarNotificaciones(Notification[] notifications) {
        int correos = 0;
        int sms = 0;
        int push = 0;

        for (Notification notification : notifications) {
            switch (notification) {
                case EmailNotification email -> {
                    correos++;
                    System.out.printf("[EMAIL] Para: %s | Asunto: %s%n",
                            email.email(), email.subject());
                }
                case SmsNotification smsNotif -> {
                    sms++;
                    System.out.printf("[SMS]   Para: %s | Mensaje: %s%n",
                            smsNotif.phone(), smsNotif.message());
                }
                case PushNotification pushNotif -> {
                    push++;
                    System.out.printf("[PUSH]  Token: %s | Mensaje: %s%n",
                            pushNotif.token(), pushNotif.message());
                }
            }
        }

        int total = correos + sms + push;

        System.out.println();
        System.out.println("========= RESUMEN =========");
        System.out.println("Correos enviados: " + correos);
        System.out.println("SMS enviados: " + sms);
        System.out.println("Push enviados: " + push);
        System.out.println("Total: " + total);
    }
}

// ============================================================
// Tipo base sellado: solo estos tres records pueden implementarlo
// ============================================================
sealed interface Notification permits EmailNotification, SmsNotification, PushNotification {
}

// ============================================================
// Notificación por Correo Electrónico
// ============================================================
record EmailNotification(String email, String subject, String content) implements Notification {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    // Constructor compacto: valida antes de crear el objeto inmutable
    EmailNotification {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Dirección de correo inválida: " + email);
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("El asunto no puede estar vacío.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío.");
        }
    }
}

// ============================================================
// Notificación SMS
// ============================================================
record SmsNotification(String phone, String message) implements Notification {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    SmsNotification {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(
                    "El número telefónico debe contener exactamente 10 dígitos: " + phone);
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }
    }
}

// ============================================================
// Notificación Push
// ============================================================
record PushNotification(String  token, String message) implements Notification {

    PushNotification {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("El token no puede ser nulo ni estar vacío.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío.");
        }
    }
}
