import { useState } from 'react';
import { logRecovery,clearCoachCache } from '../services/dataService';
import ProgressRing from './ProgressRing';

interface ReadinessLog {
    readinessScore: number;
}
interface ReadinessLoggerProps {
    athleteId: number;
    todaysLog: ReadinessLog | null;
    onSuccess: () => void;
}

const ReadinessLogger = ({ athleteId, todaysLog, onSuccess }: ReadinessLoggerProps) => {
    const [sleepQuality, setSleepQuality] = useState(3);
    const [muscleSoreness, setMuscleSoreness] = useState(3);
    const [mood, setMood] = useState(3);

    const handleSubmit = async () => {
        try {
            await logRecovery({ athleteId, sleepQuality, muscleSoreness, mood });
            await clearCoachCache(athleteId);
            onSuccess();
        } catch (error) {
            console.error("Failed to log recovery data", error);
        }
    };
    
    const getReadinessMessage = (score: number) => {
        if (score > 75) return "You are ready for a hard session!";
        if (score > 40) return "Feeling good! Ready for a solid workout.";
        return "Consider a light workout or a rest day today.";
    }

    if (todaysLog) {
        return (
            <div className="bg-gray-800 p-6 rounded-lg text-center">
                <h3 className="text-xl font-semibold mb-4">Today's Readiness</h3>
                <ProgressRing score={todaysLog.readinessScore} />
                <p className="mt-4 text-gray-300">{getReadinessMessage(todaysLog.readinessScore)}</p>
            </div>
        );
    }

    return (
        <div className="bg-gray-800 p-6 rounded-lg">
            <h3 className="text-xl font-semibold mb-4">How are you feeling today?</h3>
            <div className="space-y-4">
                {/* Sleep Quality */}
                <div>
                    <label>Sleep Quality: <span className="font-bold">{sleepQuality}</span>/5</label>
                    <input type="range" min="1" max="5" value={sleepQuality} onChange={e => setSleepQuality(Number(e.target.value))} className="w-full h-2 bg-gray-700 rounded-lg cursor-pointer" />
                </div>
                {/* Muscle Soreness */}
                <div>
                    <label>Muscle Soreness (1=None, 5=Very Sore): <span className="font-bold">{muscleSoreness}</span>/5</label>
                    <input type="range" min="1" max="5" value={muscleSoreness} onChange={e => setMuscleSoreness(Number(e.target.value))} className="w-full h-2 bg-gray-700 rounded-lg cursor-pointer" />
                </div>
                {/* Mood */}
                <div>
                    <label>Mood: <span className="font-bold">{mood}</span>/5</label>
                    <input type="range" min="1" max="5" value={mood} onChange={e => setMood(Number(e.target.value))} className="w-full h-2 bg-gray-700 rounded-lg cursor-pointer" />
                </div>
            </div>
            <button onClick={handleSubmit} className="w-full mt-6 bg-indigo-600 hover:bg-indigo-700 font-bold py-2 rounded-lg">
                Log Today's Feeling
            </button>
        </div>
    );
};

export default ReadinessLogger;