package com.app.auth_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Security security = new Security();
    private final Frontend frontend = new Frontend();

    public Security getSecurity() {
        return security;
    }

    public Frontend getFrontend() {
        return frontend;
    }

    public static class Security {
        private final Jwt jwt = new Jwt();

        public Jwt getJwt() {
            return jwt;
        }

        public static class Jwt {
            private String secret;
            private long expirationMs = 86400000L;

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public long getExpirationMs() {
                return expirationMs;
            }

            public void setExpirationMs(long expirationMs) {
                this.expirationMs = expirationMs;
            }
        }
    }

    public static class Frontend {
        private String successUrl;

        public String getSuccessUrl() {
            return successUrl;
        }

        public void setSuccessUrl(String successUrl) {
            this.successUrl = successUrl;
        }
    }
}
