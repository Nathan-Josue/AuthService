# Microservice d'Authentification et Gestion des Utilisateurs

## Vue d'ensemble

Ce microservice fournit une API REST complète pour la gestion de l'authentification et des utilisateurs. Il utilise JWT (JSON Web Tokens) pour l'authentification et propose une documentation interactive via Swagger/OpenAPI.

## Technologies utilisées

- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Security** - Sécurité et authentification
- **Spring Data JPA** - Persistance des données
- **PostgreSQL** - Base de données
- **Hibernate** - ORM
- **JWT (JSON Web Token)** - Authentification stateless
- **SpringDoc OpenAPI** - Documentation API
- **Lombok** - Réduction du code boilerplate
- **Maven** - Gestion des dépendances

## Prérequis

- Java 21 ou supérieur
- Maven 3.6+
- PostgreSQL 13+
- Compte base de données configuré (Neon, Supabase, ou local)

## Configuration

### Variables d'environnement

Le projet utilise les variables d'environnement suivantes :

| Variable | Description | Exemple |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | URL de connexion PostgreSQL | `jdbc:postgresql://localhost:5432/authservice` |
| `SPRING_DATASOURCE_USERNAME` | Nom d'utilisateur BD | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Mot de passe BD | `password` |
| `JWT_SECRET` | Clé secrète pour JWT | `votre-cle-secrete-base64` |
| `JWT_ACCESS_TOKEN_EXPIRATION` | Expiration access token (ms) | `900000` (15 min) |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Expiration refresh token (ms) | `604800000` (7 jours) |

### Fichier de configuration

Les propriétés sont définies dans `src/main/resources/application.properties` avec des valeurs par défaut pour le développement.

## Installation et démarrage

### Option 1 : Utilisation du script de lancement

```bash
chmod +x run.sh
./run.sh
```

### Option 2 : Maven directement

```bash
mvn spring-boot:run
```

### Option 3 : Compilation et exécution

```bash
mvn clean package
java -jar target/MicroService-0.0.1-SNAPSHOT.jar
```

## Documentation API

### Swagger UI

Une fois l'application démarrée, accédez à la documentation interactive :

```
http://localhost:8080/swagger-ui/index.html
```

### Documentation OpenAPI (JSON)

```
http://localhost:8080/v3/api-docs
```

## Endpoints API

### Authentification

#### POST /api/auth/signup
Inscription d'un nouvel utilisateur.

**Corps de la requête :**
```json
{
  "email": "utilisateur@exemple.com",
  "password": "motdepasse123",
  "firstName": "Jean",
  "lastName": "Dupont",
  "telephone": "0612345678"
}
```

**Réponse :**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900000
}
```

#### POST /api/auth/login
Connexion d'un utilisateur existant.

**Corps de la requête :**
```json
{
  "email": "utilisateur@exemple.com",
  "password": "motdepasse123"
}
```

**Réponse :**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900000
}
```

#### POST /api/auth/refresh
Rafraîchir le token d'accès.

**Corps de la requête :**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Réponse :**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900000
}
```

#### POST /api/auth/logout
Déconnexion et révocation du refresh token.

**Corps de la requête :**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Réponse :** `204 No Content`

### Gestion des utilisateurs

**Note :** Ces endpoints nécessitent une authentification (Bearer Token).

#### GET /api/users
Récupérer la liste de tous les utilisateurs.

**Headers :**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Réponse :**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "email": "utilisateur@exemple.com",
    "firstName": "Jean",
    "lastName": "Dupont",
    "createdAt": "2026-01-20T10:30:00Z"
  }
]
```

#### GET /api/users/{id}
Récupérer un utilisateur par son ID.

**Headers :**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Réponse :**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "email": "utilisateur@exemple.com",
  "firstName": "Jean",
  "lastName": "Dupont",
  "createdAt": "2026-01-20T10:30:00Z"
}
```

## Authentification JWT

### Fonctionnement

1. L'utilisateur s'inscrit ou se connecte via `/api/auth/signup` ou `/api/auth/login`
2. Le serveur retourne un **access token** (courte durée, 15 min) et un **refresh token** (longue durée, 7 jours)
3. L'access token est utilisé pour authentifier les requêtes API via le header `Authorization: Bearer {token}`
4. Quand l'access token expire, utilisez le refresh token via `/api/auth/refresh` pour obtenir un nouveau access token
5. À la déconnexion, le refresh token est révoqué via `/api/auth/logout`

### Utilisation avec cURL

**1. Inscription :**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@exemple.com",
    "password": "Test123!",
    "firstName": "Test",
    "lastName": "User",
    "telephone": "0612345678"
  }'
```

**2. Utilisation du token :**
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer VOTRE_ACCESS_TOKEN"
```

### Utilisation avec Swagger UI

1. Accédez à `http://localhost:8080/swagger-ui/index.html`
2. Testez `/api/auth/signup` ou `/api/auth/login` pour obtenir un token
3. Cliquez sur le bouton **"Authorize"** en haut à droite
4. Entrez votre access token (sans le préfixe "Bearer")
5. Cliquez sur **"Authorize"** puis **"Close"**
6. Vous pouvez maintenant tester tous les endpoints protégés

## Structure du projet

```
MicroService/
├── src/
│   ├── main/
│   │   ├── java/com/example/microservice/
│   │   │   ├── config/          # Configuration (OpenAPI, Security)
│   │   │   ├── controller/      # Contrôleurs REST
│   │   │   ├── DTOs/            # Data Transfer Objects
│   │   │   ├── entity/          # Entités JPA
│   │   │   ├── repository/      # Repositories JPA
│   │   │   ├── security/        # Configuration de sécurité
│   │   │   ├── service/         # Logique métier
│   │   │   └── MicroServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── run.sh
└── README.md
```

## Modèle de données

### User
```java
{
  "id": UUID,                      // Identifiant unique (id_user)
  "nom": String,                   // Nom de famille
  "prenom": String,                // Prénom
  "telephone": String (unique),    // Numéro de téléphone (optionnel)
  "email": String (unique),        // Adresse email
  "motDePasseHash": String,        // Mot de passe hashé avec BCrypt
  "dateCreation": LocalDateTime,   // Date de création (auto)
  "dateModification": LocalDateTime, // Date de modification (auto)
  "statut": UserStatus,            // ACTIF, INACTIF, SUSPENDU, EN_ATTENTE
  "enabled": Boolean,              // Compte activé/désactivé
  "roles": Set<Role>               // Rôles de l'utilisateur
}
```

### UserStatus (Enum)
```java
- ACTIF: Compte actif et opérationnel
- INACTIF: Compte désactivé
- SUSPENDU: Compte temporairement suspendu
- EN_ATTENTE: En attente de validation
```

### RefreshToken
```java
{
  "id": UUID,
  "token": String (unique),
  "user": User,
  "expiryDate": Instant,
  "revoked": Boolean
}
```

## Sécurité

- **Mots de passe** : Hashés avec BCrypt (force 10)
- **JWT** : Signés avec HMAC-SHA256
- **CORS** : Désactivé par défaut (à configurer selon vos besoins)
- **CSRF** : Désactivé (API stateless)
- **Session** : Stateless (pas de session côté serveur)

## Développement

### Désactiver la validation SSL (développement uniquement)

Dans `application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://host:5432/db?sslmode=disable
```

### Mode debug

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--logging.level.org.springframework=DEBUG
```

## Tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter les tests avec rapport de couverture
mvn clean test jacoco:report
```

## Production

### Checklist avant déploiement

- [ ] Modifier le secret JWT (`JWT_SECRET`)
- [ ] Utiliser une base de données de production
- [ ] Configurer HTTPS
- [ ] Désactiver Swagger en production (optionnel)
- [ ] Configurer les logs
- [ ] Définir des limites de rate limiting
- [ ] Configurer CORS selon vos besoins
- [ ] Activer les profils Spring appropriés

### Build pour production

```bash
mvn clean package -DskipTests
java -jar target/MicroService-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Troubleshooting

### L'application ne démarre pas

- Vérifiez que PostgreSQL est accessible
- Vérifiez les variables d'environnement
- Vérifiez les logs dans la console

### Erreur d'authentification

- Vérifiez que le token n'est pas expiré
- Vérifiez le format du header : `Authorization: Bearer {token}`
- Vérifiez que le token est valide

### Swagger n'est pas accessible

- Vérifiez que l'application est démarrée
- Accédez à `http://localhost:8080/swagger-ui/index.html` (notez le `/index.html`)
- Vérifiez la configuration de sécurité

## Licence

Apache 2.0

## Contact

Pour toute question ou support, contactez : support@example.com

---

**Note** : Cette application est générée avec Claude Code pour des fins éducatives et de développement.
