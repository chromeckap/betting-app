'use client';

import React, { createContext, useContext, useEffect, useState } from 'react';
import keycloak from '@/lib/keycloak';

export interface UserProfile {
    username?: string;
    email?: string;
    firstName?: string;
    lastName?: string;
    token?: string;
}

interface AuthContextType {
    keycloak: typeof keycloak | null;
    authenticated: boolean;
    profile: UserProfile | null;
    login: () => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
    keycloak: null,
    authenticated: false,
    profile: null,
    login: () => {},
    logout: () => {},
});

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [authenticated, setAuthenticated] = useState(false);
    const [profile, setProfile] = useState<UserProfile | null>(null);

    useEffect(() => {
        keycloak
            .init({ onLoad: 'login-required' })
            .then(async (auth) => {
                setAuthenticated(auth);

                if (auth) {
                    const userProfile = (await keycloak.loadUserProfile()) as UserProfile;
                    userProfile.token = keycloak.token || '';
                    setProfile(userProfile);
                }
            })
            .catch((err) => console.error('Keycloak init error:', err));
    }, []);

    const login = () => keycloak.login();
    const logout = () => keycloak.logout({ redirectUri: window.location.origin });

    return (
        <AuthContext.Provider
            value={{ keycloak, authenticated, profile, login, logout }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
