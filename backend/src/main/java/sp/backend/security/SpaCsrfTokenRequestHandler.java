/**
 * @author Trung Kien Huynh
 */
package sp.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

import org.springframework.util.StringUtils;

import java.util.function.Supplier;

public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    @Override
    // CSRF-Token für das Frontend bereitstellen
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       Supplier<CsrfToken> csrfToken) {
        // CSRF-Token maskieren und bereitstellen
        xor.handle(request, response, csrfToken);
        // Lazy-Loading umgehen und Token sofort anfordern
        csrfToken.get();
    }

    @Override
    // CSRF-Token auslesen
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        // Wert des CSRF-Headers (ein CSRF-Token)
        String headerValue = request.getHeader(csrfToken.getHeaderName());

        // CSRF-Token im HTTP-Header vorhanden -> nicht durch XOR-Handler dekodiert -> plain
        if (StringUtils.hasText(headerValue)) {
            // Token ohne XOR-Dekodierung auslesen
            return plain.resolveCsrfTokenValue(request, csrfToken);
        }

        // Token mit XOR-Dekodierung auslesen
        return xor.resolveCsrfTokenValue(request, csrfToken);
    }
}