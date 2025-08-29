import { useState, useEffect } from 'react';
import { getMyProfile, getPlanForWeek, generateAiPlan } from '../services/dataService';
import { format, startOfWeek, addDays, addWeeks, subWeeks } from 'date-fns';
import { RiArrowLeftSLine, RiArrowRightSLine } from 'react-icons/ri'; 


interface Profile {
  id: number;
}
interface PlannedWorkout {
  id: number;
  plannedDate: string;
  exerciseName: string;
  description: string;
}

const PlannerPage = () => {
    // --- State Management ---
    const [profile, setProfile] = useState<Profile | null>(null);
    // Set the initial week start to the Monday of the current week
    const [currentWeekStart, setCurrentWeekStart] = useState(startOfWeek(new Date(), { weekStartsOn: 1 }));
    const [plan, setPlan] = useState<PlannedWorkout[]>([]);
    const [loading, setLoading] = useState(true);
    const [isGenerating, setIsGenerating] = useState(false);

    // --- Data Fetching Logic ---
    const fetchPlannerData = async () => {
        if (!profile) return;
        setLoading(true);
        try {
            const weekStartDateStr = format(currentWeekStart, 'yyyy-MM-dd');
            const planData = await getPlanForWeek(profile.id, weekStartDateStr);
            setPlan(planData);
        } catch (error) {
            console.error("Failed to fetch planner data", error);
            // Reset plan on error to avoid showing stale data
            setPlan([]); 
        } finally {
            setLoading(false);
        }
    };
    
    // Fetch profile once on component mount
    useEffect(() => {
        getMyProfile().then(setProfile);
    }, []);
    
    // Refetch planner data whenever profile or current week changes
    useEffect(() => {
        fetchPlannerData();
    }, [profile, currentWeekStart]);
    
    // --- Event Handlers ---
    const handleGeneratePlan = async () => {
        if (!profile || isGenerating) return;
        setIsGenerating(true);
        try {
            await generateAiPlan(profile.id);
           
            setTimeout(() => {
                fetchPlannerData();
            }, 1500); 
        } catch (error) {
            console.error("Failed to generate AI plan", error);
            alert("Sorry, we couldn't generate a plan right now. Please try again later.");
        } finally {
            setIsGenerating(false);
        }
    };
    
    // --- UI Helper ---
    // Create an array of 7 date objects for the current week
    const weekDays = Array.from({ length: 7 }).map((_, i) => addDays(currentWeekStart, i));

   
    return (
        <div>
            {/* Header and Week Navigation */}
            <div className="flex flex-col md:flex-row justify-between md:items-center mb-8 gap-4">
                <div>
                    <h2 className="text-4xl font-bold">Workout Planner</h2>
                    <p className="text-gray-400 text-lg">
                        Week of {format(currentWeekStart, 'MMMM do')}
                    </p>
                </div>
                <div className="flex items-center gap-4">
                    <div className="flex items-center gap-2 bg-gray-800 rounded-lg p-1">
                        <button 
                            onClick={() => setCurrentWeekStart(subWeeks(currentWeekStart, 1))} 
                            className="p-2 hover:bg-gray-700 rounded-md transition-colors"
                            aria-label="Previous Week"
                        >
                            <RiArrowLeftSLine size={20} />
                        </button>
                        <button 
                            onClick={() => setCurrentWeekStart(addWeeks(currentWeekStart, 1))} 
                            className="p-2 hover:bg-gray-700 rounded-md transition-colors"
                            aria-label="Next Week"
                        >
                            <RiArrowRightSLine size={20} />
                        </button>
                    </div>
                    <button 
                        onClick={handleGeneratePlan}
                        disabled={isGenerating || !profile}
                        className="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-6 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2 transition-all duration-300"
                    >
                        <span className="text-2xl">✨</span>
                        {isGenerating ? 'Generating Plan...' : 'Generate AI Plan'}
                    </button>
                </div>
            </div>

            {/* Weekly Calendar Grid */}
            {loading ? (
                <div className="text-center p-10">Loading your plan...</div>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-7 gap-4">
                    {weekDays.map(day => {
                        const workoutsForDay = plan.filter(p => 
                          
                            new Date(p.plannedDate).toISOString().split('T')[0] === day.toISOString().split('T')[0]
                        );
                        const isToday = format(day, 'yyyy-MM-dd') === format(new Date(), 'yyyy-MM-dd');

                        return (
                            <div 
                                key={day.toString()} 
                                className={`bg-gray-800 rounded-lg p-3 h-72 flex flex-col ${isToday ? 'border-2 border-indigo-500' : ''}`}
                            >
                                <div className="flex justify-between items-baseline">
                                    <p className="font-bold">{format(day, 'EEE')}</p>
                                    <p className={`text-lg font-bold ${isToday ? 'text-indigo-400' : 'text-gray-400'}`}>
                                        {format(day, 'd')}
                                    </p>
                                </div>
                                
                                <div className="flex-grow overflow-y-auto mt-2 space-y-2 pr-1 custom-scrollbar">
                                    {workoutsForDay.length > 0 ? (
                                        workoutsForDay.map(workout => (
                                            <div key={workout.id} className="bg-indigo-600 p-2 rounded-md text-xs cursor-pointer hover:bg-indigo-500 transition-colors">
                                                <p className="font-semibold text-white">{workout.exerciseName}</p>
                                                <p className="text-indigo-200">{workout.description}</p>
                                            </div>
                                        ))
                                    ) : (
                                        <div className="text-center text-gray-500 text-sm pt-8">Rest Day</div>
                                    )}
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
};

export default PlannerPage;