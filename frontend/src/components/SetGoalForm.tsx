import { useState } from 'react';
import { createGoal } from '../services/dataService'; 

interface SetGoalFormProps {
  athleteId: number;
  onSuccess: () => void;
  onClose: () => void;
}

const SetGoalForm = ({ athleteId, onSuccess, onClose }: SetGoalFormProps) => {
    const [description, setDescription] = useState('');
    const [type, setType] = useState('WORKOUT_COUNT');
    const [targetValue, setTargetValue] = useState('10');
    const [endDate, setEndDate] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        try {
            const goalData = {
                athleteId,
                description,
                type,
                targetValue: Number(targetValue),
                startDate: new Date().toISOString().split('T')[0],
                endDate,
            };
            await createGoal(goalData);
            onSuccess();
            onClose();
        } catch (err) {
            setError('Failed to set goal. Please check the values.');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            {error && <p className="text-red-500 mb-4">{error}</p>}
            
            <div className="mb-4">
                <label className="block text-gray-300 mb-2">Goal Description</label>
                <input type="text" value={description} onChange={e => setDescription(e.target.value)} placeholder="e.g., Run 100km this month"
                    className="w-full px-4 py-2 bg-gray-700 text-white rounded-lg" required />
            </div>

            <div className="mb-4">
                <label className="block text-gray-300 mb-2">Goal Type</label>
                <select value={type} onChange={e => setType(e.target.value)} className="w-full px-4 py-2 bg-gray-700 text-white rounded-lg">
                    <option value="WORKOUT_COUNT">Workout Count</option>
                    <option value="TOTAL_TRAINING_LOAD">Total Training Load</option>
                    <option value="NUTRITION_CALORIES">Total Calories</option>
                    <option value="NUTRITION_PROTEIN">Total Protein (grams)</option>
                    <option value="OTHER">Other</option>
                </select>
            </div>

            <div className="flex gap-4 mb-6">
                <div className="w-1/2">
                    <label className="block text-gray-300 mb-2">Target Value</label>
                    <input type="number" value={targetValue} onChange={e => setTargetValue(e.target.value)}
                        className="w-full px-4 py-2 bg-gray-700 text-white rounded-lg" required />
                </div>
                <div className="w-1/2">
                    <label className="block text-gray-300 mb-2">End Date</label>
                    <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)}
                        className="w-full px-4 py-2 bg-gray-700 text-white rounded-lg" required />
                </div>
            </div>

            <div className="flex justify-end gap-4">
                <button type="button" onClick={onClose} className="bg-gray-600 hover:bg-gray-500 text-white font-bold py-2 px-4 rounded-lg">Cancel</button>
                <button type="submit" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-lg">Set Goal</button>
            </div>
        </form>
    );
};

export default SetGoalForm;