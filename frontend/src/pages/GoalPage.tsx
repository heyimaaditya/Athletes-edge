import { useEffect, useState } from "react";
import { getMyProfile, getMyActiveGoals } from "../services/dataService";
import GoalCard from "../components/GoalCard";
import Modal from "../components/Modal";
import SetGoalForm from "../components/SetGoalForm";

interface Goal {
  id: number;
  description: string;
  currentValue: number;
  targetValue: number;
  status: 'IN_PROGRESS' | 'COMPLETED';
}
interface Profile { id: number; } 

const GoalsPage = () => {
    const [profile, setProfile] = useState<Profile | null>(null);
    const [goals, setGoals] = useState<Goal[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchGoalsData = async () => {
        try {
            setLoading(true);
            const profileData = await getMyProfile();
            setProfile(profileData);
            if (profileData && profileData.id) {
                const goalsData = await getMyActiveGoals(profileData.id);
                setGoals(goalsData);
            }
        } catch (error) {
            console.error("Failed to fetch goals data", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGoalsData();
    }, []);

    const handleGoalSet = () => {
        fetchGoalsData(); 
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-4xl font-bold">Your Goals</h2>
                <button onClick={() => setIsModalOpen(true)} className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-6 rounded-lg">
                    Set New Goal
                </button>
            </div>
            <div className="space-y-4">
                {goals.length > 0 ? (
                    goals.map(goal => <GoalCard key={goal.id} goal={goal} />)
                ) : (
                    <p className="text-gray-400 bg-gray-800 p-6 rounded-lg">You haven't set any goals yet. Click "Set New Goal" to get started!</p>
                )}
            </div>
            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Set a New Goal">
                {profile && <SetGoalForm athleteId={profile.id} onSuccess={handleGoalSet} onClose={() => setIsModalOpen(false)} />}
            </Modal>
        </div>
    );
};

export default GoalsPage;