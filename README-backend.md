# QMA Backend

This workspace now contains three Spring services behind one gateway:

- `api-gateway` on port `8080`
- `auth-service` on port `8081`
- `qma-service` on port `8082`

## Before You Run

1. Use Java 17+.
2. Set the environment variables from `.env.example`.
3. Make sure `JWT_SECRET` is the same for `auth-service` and `api-gateway`.
4. Set `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` only if you want Google OAuth.

## Run

From the repository root:

```bash
mvn -pl auth-service spring-boot:run
mvn -pl qma-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

## Main Endpoints

- `POST /auth/register`
- `POST /auth/login`
- `GET /auth/me`
- `POST /api/quantity/add`
- `POST /api/quantity/subtract`
- `POST /api/quantity/compare`
- `POST /api/quantity/divide`
- `POST /api/quantity/convert`
