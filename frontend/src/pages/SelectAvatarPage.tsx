import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGamification } from '../context/GamificationContext';
import { getMyProfile } from '../services/dataService';

const avatars = [
    { type: 'BEAR', name: 'The Bear', description: 'Represents raw strength and power.', img: '/avatars/bear_1.jpg' },
    { type: 'WOLF', name: 'The Wolf', description: 'Symbolizes endurance and pack leadership.', img: '/avatars/wolf_1.png' },
    { type: 'EAGLE', name: 'The Eagle', description: 'Embodies agility, precision, and vision.', img: '/avatars/eagle_1.jpg' }
];

const SelectAvatarPage = () => {
    const [selectedAvatar, setSelectedAvatar] = useState<string | null>(null);
    const [athleteId, setAthleteId] = useState<number | null>(null);
    const { selectAvatar } = useGamification();
    const navigate = useNavigate();

    useEffect(() => {
        getMyProfile().then(profile => setAthleteId(profile.id));
    }, []);

    const handleSelect = async () => {
        if (!selectedAvatar || !athleteId) return;
        try {
            await selectAvatar(athleteId, selectedAvatar);
            navigate('/dashboard');
        } catch (error) {
            console.error("Failed to select avatar", error);
        }
    };

    return (
        <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-8">
            <h1 className="text-5xl font-bold mb-4">Choose Your Inner Beast</h1>
            <p className="text-gray-400 mb-12">Your avatar will evolve as you get stronger.</p>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-8 max-w-4xl">
                {avatars.map(avatar => (
                    <div 
                        key={avatar.type}
                        onClick={() => setSelectedAvatar(avatar.type)}
                        className={`bg-gray-800 p-6 rounded-lg cursor-pointer transition-all duration-300 transform hover:scale-105 ${selectedAvatar === avatar.type ? 'ring-4 ring-indigo-500' : 'ring-2 ring-transparent'}`}
                    >
                        <img src={avatar.img} alt={avatar.name} className="w-40 h-40 mx-auto mb-4" />
                        <h2 className="text-2xl font-bold text-center">{avatar.name}</h2>
                        <p className="text-gray-400 text-center mt-2">{avatar.description}</p>
                    </div>
                ))}
            </div>

            <button 
                onClick={handleSelect}
                disabled={!selectedAvatar}
                className="mt-12 bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 px-12 rounded-lg text-xl disabled:opacity-50 disabled:cursor-not-allowed"
            >
                Confirm Selection
            </button>
        </div>
    );
};

export default SelectAvatarPage;