import { createContext, useState, useContext, useEffect, useCallback } from 'react';
import type { ReactNode } from 'react';
import api from '../services/api';

interface UserProfile {
  id: number;
  name: string;
  email: string;
  age: number;
  sport: string;
}


interface AuthContextType {
  token: string | null;
  isAuthenticated: boolean;
  userProfile: UserProfile | null; 
  loading: boolean;
  signup: (userData: any) => Promise<void>;
  login: (credentials: any) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);


export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(localStorage.getItem('authToken'));
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(true); 


  const fetchMyProfile = useCallback(async () => {
    try {
      const response = await api.get('/users/me');
      setUserProfile(response.data);
      return response.data;
    } catch (error) {
      console.error("Failed to fetch user profile, token might be invalid.", error);
      
      setToken(null);
      setUserProfile(null);
      localStorage.removeItem('authToken');
      throw error;
    }
  }, []);

  useEffect(() => {
    const initializeAuth = async () => {
      if (token) {
       
        await fetchMyProfile().catch(() => {}); 
      }
      setLoading(false); 
    };
    initializeAuth();
  }, [token, fetchMyProfile]);
  
  const signup = async (userData: any) => {
    await api.post('/auth/register', userData);
  };

  const login = async (credentials: any) => {
    const response = await api.post('/auth/login', credentials);
    const { token: newToken } = response.data;
    localStorage.setItem('authToken', newToken); 
    setToken(newToken); 
  };

  const logout = () => {
    setToken(null);
    setUserProfile(null);
    localStorage.removeItem('authToken');
  };

  const value = { 
    token, 
    isAuthenticated: !!token && !!userProfile, 
    userProfile, 
    loading, 
    signup, 
    login, 
    logout 
  };
  
  return (
    <AuthContext.Provider value={value}>
      {!loading && children} 
    </AuthContext.Provider>
  );
};


export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};