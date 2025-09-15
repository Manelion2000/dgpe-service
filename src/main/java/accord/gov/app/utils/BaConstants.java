package accord.gov.app.utils;

import org.springframework.core.io.ClassPathResource;

public class BaConstants {
    public static final String DEFAULT_USER = "dev@user";
    public static final String ROLE_PREFIX = "BA_";

    /**
     * Constantes des URLs.
     */
    public static class URL {
        public static final  String BASE_URL = "/api";
        public static final  String PRODUCT = "/produits";
        public static final String PROFIL = "/profils";
        public static final String ROLE = "/roles";
        public static final String DOCUMENT = "/documents";
        public static final String CSRF_TOKEN = "/csrf";
        public static final String AUTHENTICATE = "/authenticate";
        public static final String USER = "/users";
        public static final String ACCORD = "/accords";
        public static final String LANGUE = "/langues";
        public static final String TYPE_ACCORD = "/types_accords";
        public static final String TYPE_DOCUMENT_AFF = "/types_doc_affs";
        public static final String PARTIE = "/parties";
        public static final String DOMAINE = "/domaines";
        public static final String PAYS="/pays";

    }

    /**
     * Classes des constantes liées à l'édition.
     */
    public static class REPORTS {
        public static final String LOGO_URL = new ClassPathResource("/images/logo.png").getPath();
        public static final String PARAM_TITLE = "BA_TITLE";

        /**
         * Racines des reports.
         */
        private static final String REPORT_ROOT = "reports/";
        public static final String REPORT_PRODUIT = REPORT_ROOT + "produit.jasper";
    }

}
