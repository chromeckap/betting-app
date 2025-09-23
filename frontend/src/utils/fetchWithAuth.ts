import keycloak from '@/lib/keycloak';

export async function fetchWithAuth(input: RequestInfo, init?: RequestInit) {
    if (keycloak.token && keycloak.isTokenExpired()) {
        await keycloak.updateToken(30);
    }

    const headers = {
        ...(init?.headers || {}),
        Authorization: `Bearer ${keycloak.token}`,
    };

    return fetch(input, { ...init, headers });
}
