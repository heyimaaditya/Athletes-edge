import { useState, useEffect } from 'react';
import { getMyProfile, getTodaysCoachingTip } from '../services/dataService';

interface Profile { id: number; }
interface CoachTip {
    title: string;
    signal: 'GREEN' | 'YELLOW' | 'RED' | string;
    recommendation: string;
}

const CoachPage = () => {
    const [profile, setProfile] = useState<Profile | null>(null);
    const [tip, setTip] = useState<CoachTip | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCoachData = async () => {
            try {
                const profileData = await getMyProfile();
                setProfile(profileData);
                if (profileData && profileData.id) {
                    const tipData = await getTodaysCoachingTip(profileData.id);
                    setTip(tipData);
                }
            } catch (error) {
                console.error("Failed to get coaching tip", error);
            } finally {
                setLoading(false);
            }
        };
        fetchCoachData();
    }, []);

    const getSignalColor = (signal: string) => {
        if (signal === 'GREEN') return 'border-green-500';
        if (signal === 'YELLOW') return 'border-yellow-500';
        if (signal === 'RED') return 'border-red-500';
        return 'border-gray-600';
    };

    if (loading) return <div>Loading your coaching tip...</div>;

    return (
        <div>
            <h2 className="text-4xl font-bold mb-8">Today's AI Coach</h2>
            {tip ? (
                <div className={`bg-gray-800 p-8 rounded-lg border-l-8 ${getSignalColor(tip.signal)}`}>
                    <h3 className="text-3xl font-bold text-white mb-4">{tip.title}</h3>
                    <p className="text-gray-300 text-lg leading-relaxed">{tip.recommendation}</p>
                </div>
            ) : (
                <div className="bg-gray-800 p-8 rounded-lg">
                    <p className="text-gray-300">Could not retrieve a coaching tip for today. Please make sure you have logged your readiness score.</p>
                </div>
            )}
        </div>
    );
};

export default CoachPage;