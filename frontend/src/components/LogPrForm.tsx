import { useState } from 'react';
import { logPr } from '../services/dataService';

interface LogPrFormProps {
  athleteId: number;
  onSuccess: () => void;
  onClose: () => void;
}

const LogPrForm = ({ athleteId, onSuccess, onClose }: LogPrFormProps) => {
    const [exerciseName, setExerciseName] = useState('');
    const [value, setValue] = useState('');
    const [unit, setUnit] = useState('kg');
    const [recordDate, setRecordDate] = useState(new Date().toISOString().split('T')[0]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await logPr({ athleteId, exerciseName, value: Number(value), unit, recordDate });
            onSuccess();
            onClose();
        } catch (err) {
            console.error("Failed to log PR", err);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="mb-4">
                <label className="block text-gray-300 mb-2">Description</label>
                <input type="text" value={exerciseName} onChange={e => setExerciseName(e.target.value)}
                    placeholder="e.g., Deadlift, 5k Run" className="w-full px-4 py-2 bg-gray-700 rounded-lg" required />
            </div>
            <div className="flex gap-4 mb-4">
                <div className="w-2/3">
                    <label className="block text-gray-300 mb-2">Record Value</label>
                    <input type="number" step="0.01" value={value} onChange={e => setValue(e.target.value)}
                        placeholder="e.g., 150 or 1200" className="w-full px-4 py-2 bg-gray-700 rounded-lg" required />
                </div>
                <div className="w-1/3">
                    <label className="block text-gray-300 mb-2">Unit</label>
                    <select value={unit} onChange={e => setUnit(e.target.value)} className="w-full px-4 py-2 bg-gray-700 rounded-lg">
                        <option>kg</option>
                        <option>lbs</option>
                        <option>km</option>
                        <option>miles</option>
                        <option>seconds</option>
                        <option>other</option>
                    </select>
                </div>
            </div>
            <div className="mb-6">
                <label className="block text-gray-300 mb-2">Date Achieved</label>
                <input type="date" value={recordDate} onChange={e => setRecordDate(e.target.value)}
                    className="w-full px-4 py-2 bg-gray-700 rounded-lg" required />
            </div>
            <div className="flex justify-end gap-4">
                <button type="button" onClick={onClose} className="bg-gray-600 hover:bg-gray-500 font-bold py-2 px-4 rounded-lg">Cancel</button>
                <button type="submit" className="bg-amber-500 hover:bg-amber-600 font-bold py-2 px-4 rounded-lg">Log PR</button>
            </div>
        </form>
    );
};

export default LogPrForm;