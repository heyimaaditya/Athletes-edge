import { useState } from 'react';

interface LogWorkoutFormProps {
  athleteId: number;
  onSuccess: () => void; 
  onClose: () => void;
}


import { logWorkout } from '../services/dataService'; 

const LogWorkoutForm = ({ athleteId, onSuccess, onClose }: LogWorkoutFormProps) => {
  const [type, setType] = useState('Running');
  const [durationMinutes, setDurationMinutes] = useState('60');
  const [intensity, setIntensity] = useState(5);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!durationMinutes || Number(durationMinutes) <= 0) {
      setError('Please enter a valid duration.');
      return;
    }

    try {
      const workoutData = {
        athleteId,
        type,
        durationMinutes: Number(durationMinutes),
        intensity: Number(intensity),
        workoutDate: new Date().toISOString().split('T')[0] 
      };
      await logWorkout(workoutData);
      onSuccess();
      onClose();
    } catch (err) {
      setError('Failed to log workout. Please try again.');
      console.error(err);
    }
  };
  
  return (
    <form onSubmit={handleSubmit}>
      {error && <p className="text-red-500 mb-4">{error}</p>}
      
      <div className="mb-4">
        <label className="block text-gray-300 mb-2">Workout Type</label>
        <select value={type} onChange={(e) => setType(e.target.value)}
          className="w-full px-4 py-2 bg-gray-700 text-white rounded-lg">
          <option>Running</option>
          <option>Weightlifting</option>
          <option>Cycling</option>
          <option>Swimming</option>
          <option>Yoga</option>
          <option>Other</option>
        </select>
      </div>

      <div className="mb-4">
        <label className="block text-gray-300 mb-2">Duration (minutes)</label>
        <input type="number" value={durationMinutes} onChange={(e) => setDurationMinutes(e.target.value)}
          className="w-full px-4 py-2 bg-gray-700 text-white rounded-lg" />
      </div>

      <div className="mb-6">
        <label className="block text-gray-300 mb-2">Intensity (1-10)</label>
        <div className="flex items-center">
            <input type="range" min="1" max="10" step="0.5" value={intensity} onChange={(e) => setIntensity(Number(e.target.value))}
            className="w-full h-2 bg-gray-700 rounded-lg appearance-none cursor-pointer" />
            <span className="ml-4 text-lg font-bold w-12 text-center">{intensity}</span>
        </div>
      </div>

      <div className="flex justify-end gap-4">
        <button type="button" onClick={onClose} className="bg-gray-600 hover:bg-gray-500 text-white font-bold py-2 px-4 rounded-lg">
          Cancel
        </button>
        <button type="submit" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-lg">
          Log Workout
        </button>
      </div>
    </form>
  );
};

export default LogWorkoutForm;