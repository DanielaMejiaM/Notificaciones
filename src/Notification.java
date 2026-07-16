import java.util.regex.Pattern;

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
