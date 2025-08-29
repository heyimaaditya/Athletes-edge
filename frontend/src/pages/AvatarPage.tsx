import { useGamification } from '../context/GamificationContext';
const extensions = {
    BEAR: 'jpg',
    WOLF: 'png',
    EAGLE: 'jpg'
};
const AvatarPage = () => {
    const { profile } = useGamification();

    const getAvatarImage = (type: string, level: number) => {
        if (!type) return '/avatars/bear_1.jpg';
        const stage = level >= 50 ? 50 : level >= 10 ? 10 : 1;
         const fileType = extensions[type.toUpperCase() as keyof typeof extensions] || 'svg';

        return `/avatars/${type.toLowerCase()}_${stage}.${fileType}`;
    };
    
    if (!profile) {
        return <div>Loading Avatar...</div>;
    }

    return (
        <div>
            <h2 className="text-4xl font-bold mb-8">My Avatar</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                {/* Left: Avatar Display */}
                <div className="md:col-span-1 bg-gray-800 p-8 rounded-lg flex flex-col items-center">
                    <img src={getAvatarImage(profile.avatarType, profile.level)} alt="Avatar" className="w-48 h-48" />
                    <h3 className="text-3xl font-bold mt-4">Level {profile.level} {profile.avatarType}</h3>
                    <p className="text-amber-400 mt-2">{profile.skillPoints} Skill Points Available</p>
                </div>

                {/* Right: Skill Tree */}
                <div className="md:col-span-2 bg-gray-800 p-8 rounded-lg">
                    <h3 className="text-2xl font-bold mb-6">Skill Tree</h3>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        {/* Strength Tree */}
                        <div className="space-y-4">
                            <h4 className="font-bold text-red-500">STRENGTH</h4>
                            <div className="p-3 border border-gray-600 rounded">Unlock Powerlifter Template</div>
                            <div className="p-3 border border-gray-600 rounded">Increase PR XP Bonus by 10%</div>
                        </div>
                        {/* Endurance Tree */}
                        <div className="space-y-4">
                             <h4 className="font-bold text-blue-500">ENDURANCE</h4>
                             <div className="p-3 border border-gray-600 rounded">Unlock Marathon Prep Template</div>
                        </div>
                         {/* Recovery Tree */}
                         <div className="space-y-4">
                             <h4 className="font-bold text-green-500">RECOVERY</h4>
                             <div className="p-3 border border-gray-600 rounded">Increase Readiness Score gains</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AvatarPage;