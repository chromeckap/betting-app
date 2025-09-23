'use client';

import { useAuth } from '@/context/AuthContext';
import keycloak from "@/lib/keycloak";

export default function ProtectedPage() {
    const { authenticated, profile, logout } = useAuth();

    if (!authenticated) return <p>Loading...</p>;

    return (
        <div>
            <h1>Welcome {profile?.username}</h1>
            <button onClick={logout}>Logout</button>
            <>{keycloak.token}</>
        </div>
    );
}
