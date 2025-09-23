import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
   url: process.env.NEXT_PUBLIC_KEYCLOAK_URL ?? (() => { throw new Error("Missing NEXT_PUBLIC_KEYCLOAK_URL"); })(),
   realm: process.env.NEXT_PUBLIC_KEYCLOAK_REALM ?? (() => { throw new Error("Missing NEXT_PUBLIC_KEYCLOAK_REALM"); })(),
   clientId: process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID ?? (() => { throw new Error("Missing NEXT_PUBLIC_KEYCLOAK_CLIENT_ID"); })(),
});

export default keycloak;