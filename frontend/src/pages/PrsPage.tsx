import { useEffect, useState } from "react";
import { getMyProfile, getMyPrs } from "../services/dataService";
import PrCard from "../components/PrCard";
import Modal from "../components/Modal";
import LogPrForm from "../components/LogPrForm";
import { useGamification } from "../context/GamificationContext"; 


interface Pr {
  id: number; 
  exerciseName: string;
  value: number;
  unit: string;
  recordDate: string;
}
interface Profile { id: number; }

const PrsPage = () => {
    const [profile, setProfile] = useState<Profile | null>(null);
    const [prs, setPrs] = useState<Pr[]>([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const { fetchProfile: fetchGamificationProfile } = useGamification();
    const fetchPrsData = async () => {
        try {
            setLoading(true);
            const profileData = await getMyProfile();
            setProfile(profileData);
            if (profileData && profileData.id) {
                const prsData = await getMyPrs(profileData.id);
                setPrs(prsData);
            }
        } catch (error) {
            console.error("Failed to fetch PRs data", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPrsData();
    }, []);

    const handlePrLogged = async () => {
       await  fetchPrsData(); 
       if(profile?.id){
        await fetchGamificationProfile(profile.id);
       }
        
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-4xl font-bold">Your Trophy Cabinet</h2>
                <button onClick={() => setIsModalOpen(true)} className="bg-amber-500 hover:bg-amber-600 text-white font-bold py-2 px-6 rounded-lg">
                    + Log New PR
                </button>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {prs.length > 0 ? (
                prs.map(pr => <PrCard key={pr.id} pr={pr} />)
              ) : (
                <div className="col-span-full text-center bg-gray-800 p-8 rounded-lg">
                    <p className="text-gray-400">Your trophy cabinet is empty. Log your first Personal Record to start building your legacy!</p>
                </div>
              )}
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)} title="Log a Personal Record">
                {profile && <LogPrForm athleteId={profile.id} onSuccess={handlePrLogged} onClose={() => setIsModalOpen(false)} />}
            </Modal>
        </div>
    );
};

export default PrsPage;