import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { getMyProfile, getMyStats, getMyWorkouts, getAcwrHistory, getTodaysReadiness } from "../services/dataService";

import StatCard from "../components/StatCard";
import AcwrTrendChart from "../components/AcwrTrendChart";
import Modal from "../components/Modal";
import LogWorkoutForm from "../components/LogWorkoutForm";
import NotificationPopup from "../components/NotificationPopup";
import ReadinessLogger from "../components/ReadinessLogger";
import { useGamification } from "../context/GamificationContext";


interface Profile {
    id: number;
    name: string;
    email: string;
    age: number;
    sport: string;
}
interface Stats {
    totalTrainingLoad: number;
    workoutCount: number;
}
interface Workout {
    id: number;
    type: string;
    durationMinutes: number;
    intensity: number;
    workoutDate: string;
}
interface AcwrDataPoint {
    date: string;
    acwr: number;
}
interface ReadinessLog {
    readinessScore: number;
}
interface Notification {
    message: string;
}

const DashboardPage = () => {
    // --- State Management ---
    const { logout } = useAuth();
    const [profile, setProfile] = useState<Profile | null>(null);
    const [stats, setStats] = useState<Stats | null>(null);
    const [workouts, setWorkouts] = useState<Workout[]>([]);
    const [acwrHistory, setAcwrHistory] = useState<AcwrDataPoint[]>([]);
    const [todaysReadiness, setTodaysReadiness] = useState<ReadinessLog | null>(null);
    const [loading, setLoading] = useState(true);
    const [isWorkoutModalOpen, setIsWorkoutModalOpen] = useState(false);
    const [notification, setNotification] = useState<Notification | null>(null);
    const { fetchProfile: fetchGamificationProfile } = useGamification();
    // --- Data Fetching Logic ---
    const fetchData = async () => {
        try {
            const profileData = await getMyProfile();
            setProfile(profileData);

            if (profileData && profileData.id) {
                
                const [statsData, workoutsData, acwrData, readinessData] = await Promise.all([
                    getMyStats(profileData.id),
                    getMyWorkouts(profileData.id),
                    getAcwrHistory(profileData.id),
                   
                    getTodaysReadiness(profileData.id).catch(() => null)
                ]);

                setStats(statsData);
                setWorkouts(workoutsData);
                setAcwrHistory(acwrData);
                setTodaysReadiness(readinessData);
            }
        } catch (error) {
            console.error("Failed to fetch dashboard data", error);
          
            if ((error as any).response?.status === 401 || (error as any).response?.status === 403) {
                logout();
            }
        } finally {
            setLoading(false);
        }
    };


 
    useEffect(() => {
        setLoading(true);
        fetchData();
    }, []);
  
    useEffect(() => {
        if (!profile) return; 

        const eventSource = new EventSource(
            `http://localhost:9090/api/notifications/subscribe/${profile.id}`
        );

        eventSource.addEventListener('injury-alert', (event) => {
            const alertData = JSON.parse(event.data);
            setNotification({ message: alertData.message });

            
            setTimeout(() => setNotification(null), 10000);
        });
        eventSource.addEventListener('gamification-update', async (event: MessageEvent) => {
            const update = JSON.parse(event.data);
           
            await fetchGamificationProfile(profile.id); 
        });

        eventSource.onerror = (err) => {
            console.error('EventSource failed:', err);
            eventSource.close();
        };

    
        return () => {
            eventSource.close();
        };
    }, [profile]); 

   
    if (loading) {
        return <div className="flex items-center justify-center h-screen text-white">Loading your dashboard...</div>;
    }

    return (
        <>
            {/* Real-time Notification Popup */}
            {notification && (
                <NotificationPopup
                    message={notification.message}
                    onClose={() => setNotification(null)}
                />
            )}

            {/* Header Section */}
            <div className="flex justify-between items-center mb-8">
                <div>
                    <h2 className="text-4xl font-bold">Welcome back, {profile?.name}!</h2>
                    <p className="text-gray-400 mt-2">Here's a quick overview of your performance and recovery.</p>
                </div>
                <button
                    onClick={() => setIsWorkoutModalOpen(true)}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-6 rounded-lg"
                >
                    Log New Workout
                </button>
            </div>

            {/* Top Section: Readiness Score and Key Stats */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <div className="lg:col-span-1">
                    {profile && <ReadinessLogger athleteId={profile.id} todaysLog={todaysReadiness} onSuccess={fetchData} />}
                </div>
                <div className="lg:col-span-2 grid grid-cols-1 md:grid-cols-2 gap-6">
                    <StatCard title="Total Workouts" value={stats?.workoutCount ?? 0} />
                    <StatCard title="Total Training Load" value={Math.round(stats?.totalTrainingLoad ?? 0)} />
                    <StatCard title="Primary Sport" value={profile?.sport ?? 'N/A'} />
                </div>
            </div>

            {/* ACWR Trend Chart */}
            {acwrHistory.length > 0 && <AcwrTrendChart data={acwrHistory} />}

            {/* Recent Workouts List */}
            <div className="mt-10 bg-gray-800 p-6 rounded-lg">
                <h3 className="text-xl font-semibold mb-4">Recent Workouts</h3>
                <div className="space-y-4">
                    {workouts.length > 0 ? (
                        workouts.slice(0, 5).map(workout => (
                            <div key={workout.id} className="bg-gray-700 p-4 rounded-md flex justify-between items-center">
                                <div>
                                    <p className="font-bold">{workout.type}</p>
                                    <p className="text-sm text-gray-400">{new Date(workout.workoutDate).toLocaleDateString()}</p>
                                </div>
                                <div className="text-right">
                                    <p>{workout.durationMinutes} mins</p>
                                    <p className="text-sm text-gray-400">Intensity: {workout.intensity}/10</p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p className="text-gray-400">No workouts logged yet.</p>
                    )}
                </div>
            </div>

            {/* Workout Logging Modal */}
            <Modal isOpen={isWorkoutModalOpen} onClose={() => setIsWorkoutModalOpen(false)} title="Log a New Workout">
                {profile && <LogWorkoutForm athleteId={profile.id} onSuccess={fetchData} onClose={() => setIsWorkoutModalOpen(false)} />}
            </Modal>
        </>
    );
};

export default DashboardPage;