import { BrowserRouter as Router, Routes, Route, useNavigate, useLocation} from 'react-router-dom';
import { useEffect } from 'react';
import { useAuth } from './context/AuthContext';
import { useGamification } from './context/GamificationContext';
import AppLayout from './components/AppLayout';
import ProtectedRoute from './components/ProtectedRoute';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage';
import DashboardPage from './pages/DashboardPage';
import GoalsPage from './pages/GoalPage';
import PrsPage from './pages/PrsPage';
import PlannerPage from './pages/PlannerPage';
import CoachPage from './pages/CoachPage';
import SelectAvatarPage from './pages/SelectAvatarPage';
import AvatarPage from './pages/AvatarPage';
import JourneyPage from './pages/JourneyPage';


const GamificationGate = () => {
    const { userProfile } = useAuth();
    const { profile: gamificationProfile, loading, fetchProfile } = useGamification();
    const navigate = useNavigate();
    const location = useLocation();

   
    useEffect(() => {
        if (userProfile?.id) {
            fetchProfile(userProfile.id);
        }
    }, [userProfile, fetchProfile]);

 
    useEffect(() => {
       
        if (!loading && userProfile && !gamificationProfile && location.pathname !== '/select-avatar') {
          
            navigate('/select-avatar', { replace: true });
        }
    }, [loading, userProfile, gamificationProfile, navigate, location.pathname]);

    if (loading || (!gamificationProfile && userProfile)) {
        return <div className="min-h-screen bg-gray-900 flex items-center justify-center text-white">Loading Your Profile...</div>;
    }
   
    return <AppLayout />;
};

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        
        {/* Protected Routes */}
        <Route element={<ProtectedRoute />}>
          
            <Route path="/select-avatar" element={<SelectAvatarPage />} />
            
          
            <Route element={<GamificationGate />}>
              
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/planner" element={<PlannerPage />} />
                <Route path="/goals" element={<GoalsPage />} />
                <Route path="/records" element={<PrsPage />} />
                <Route path="/coach" element={<CoachPage />} />
                <Route path="/avatar" element={<AvatarPage />} />
                <Route path="/journey" element={<JourneyPage />} />
            </Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
