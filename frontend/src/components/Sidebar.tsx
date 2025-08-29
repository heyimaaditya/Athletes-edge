import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useGamification } from '../context/GamificationContext';

const Sidebar = () => {
    const { logout, userProfile } = useAuth(); 
    const { profile: gamificationProfile } = useGamification(); 
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

   
    const linkClass = "text-lg hover:text-indigo-400 px-3 py-2 rounded-md block transition-colors";
    const activeLinkClass = "text-lg bg-indigo-600 px-3 py-2 rounded-md block";

   
    const getAvatarImage = (type: string, level: number) => {
        if (!type) return '/avatars/bear_1.jpg'; 
         const extensions = {
            BEAR: 'jpg',
            WOLF: 'png',
            EAGLE: 'jpg'
        };
        const stage = level >= 50 ? 50 : level >= 10 ? 10 : 1;
        const fileType = extensions[type.toUpperCase() as keyof typeof extensions] || 'svg';

        return `/avatars/${type.toLowerCase()}_${stage}.${fileType}`;
    };

    return (
        <aside className="w-64 bg-gray-800 p-6 h-screen sticky top-0 flex flex-col">
            {/* Gamification Profile Section */}
            {gamificationProfile && userProfile ? (
                <div className="text-center mb-8">
                    <img 
                        src={getAvatarImage(gamificationProfile.avatarType, gamificationProfile.level)} 
                        alt={`${gamificationProfile.avatarType} Avatar`}
                        className="w-24 h-24 mx-auto mb-2 transition-transform transform hover:scale-110" 
                    />
                    <p className="font-bold text-lg truncate">{userProfile.name}</p>
                    <p className="text-sm text-amber-400 font-semibold">Level {gamificationProfile.level}</p>
                    {/* XP Progress Bar */}
                    <div className="w-full bg-gray-600 rounded-full h-1.5 mt-2" title={`${gamificationProfile.currentXp} / ${gamificationProfile.nextLevelXp} XP`}>
                        <div 
                            className="bg-amber-400 h-1.5 rounded-full transition-all duration-500" 
                            style={{ width: `${(gamificationProfile.currentXp / gamificationProfile.nextLevelXp) * 100}%` }}
                        ></div>
                    </div>
                    <p className="text-xs text-gray-400 mt-1">
                        {gamificationProfile.currentXp} / {gamificationProfile.nextLevelXp} XP
                    </p>
                </div>
            ) : (
                
                <div className="h-48 flex items-center justify-center text-gray-500 animate-pulse">Loading Profile...</div>
            )}
            
            <nav className="flex-grow">
                <ul>
                    <li className="mb-4">
                        <NavLink to="/dashboard" className={({ isActive }) => isActive ? activeLinkClass : linkClass}>Dashboard</NavLink>
                    </li>
                    <li className="mb-4">
                        <NavLink to="/planner" className={({ isActive }) => isActive ? activeLinkClass : linkClass}>Planner</NavLink>
                    </li>
                    <li className="mb-4">
                        <NavLink to="/goals" className={({ isActive }) => isActive ? activeLinkClass : linkClass}>Goals</NavLink>
                    </li>
                    <li className="mb-4">
                        <NavLink to="/records" className={({ isActive }) => isActive ? activeLinkClass : linkClass}>Personal Records</NavLink>
                    </li>
                    <li className="mb-4">
                        <NavLink to="/coach" className={({ isActive }) => isActive ? activeLinkClass : linkClass}>AI Coach</NavLink>
                    </li>
                    <li className="mb-4">
                        <NavLink to="/avatar" className={({ isActive }) => isActive ? activeLinkClass : linkClass}>My Avatar</NavLink>
                    </li>
                    <li className="mb-4">
                        <NavLink to="/journey" className={({ isActive }) => isActive ? activeLinkClass : linkClass}>
                            My Journey
                        </NavLink>
                    </li>
                </ul>
            </nav>
            <button 
                onClick={handleLogout} 
                className="w-full bg-red-600 hover:bg-red-700 text-white font-bold py-2 rounded-lg transition-colors"
            >
                Logout
            </button>
        </aside>
    );
};

export default Sidebar;