import { createContext, useState, useContext, useCallback } from 'react';
import type { ReactNode } from 'react';
import { getGamificationProfile, selectAvatar as apiSelectAvatar } from '../services/dataService';


interface GamificationProfile {
  athleteId: number;
  avatarType: string;
  level: number;
  currentXp: number;
  nextLevelXp: number;
  skillPoints: number;
}
interface GamificationContextType {
  profile: GamificationProfile | null;
  loading: boolean;
  fetchProfile: (athleteId: number) => Promise<GamificationProfile | null>;
  selectAvatar: (athleteId: number, avatarType: string) => Promise<void>;
}

const GamificationContext = createContext<GamificationContextType | undefined>(undefined);

export const GamificationProvider = ({ children }: { children: ReactNode }) => {
  const [profile, setProfile] = useState<GamificationProfile | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchProfile = useCallback(async (athleteId: number) => {
    setLoading(true);
    try {
      const data = await getGamificationProfile(athleteId);
      setProfile(data);
      return data;
    } catch (error) {
     
      if ((error as any).response?.status !== 404) {
        console.error("Failed to fetch gamification profile", error);
      }
      setProfile(null);
      return null;
    } finally {
      setLoading(false);
    }
  }, []);
  
  const selectAvatar = async (athleteId: number, avatarType: string) => {
    await apiSelectAvatar(athleteId, avatarType);
    await fetchProfile(athleteId); 
  };

  return (
    <GamificationContext.Provider value={{ profile, loading, fetchProfile, selectAvatar }}>
      {children}
    </GamificationContext.Provider>
  );
};

export const useGamification = () => {
  const context = useContext(GamificationContext);
  if (!context) throw new Error('useGamification must be used within a GamificationProvider');
  return context;
};