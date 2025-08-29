import ProgressBar from "./ProgressBar";


interface Goal {
  id: number;
  description: string;
  currentValue: number;
  targetValue: number;
  status: 'IN_PROGRESS' | 'COMPLETED';
}

interface GoalCardProps {
  goal: Goal;
}

const GoalCard = ({ goal }: GoalCardProps) => {
  return (
    <div className="bg-gray-700 p-4 rounded-md">
      <div className="flex justify-between items-start">
        <p className="font-bold">{goal.description}</p>
        {goal.status === 'COMPLETED' && (
          <span className="bg-green-500 text-white text-xs font-semibold px-2.5 py-0.5 rounded-full">
            ✓ COMPLETED
          </span>
        )}
      </div>
      <div className="mt-3">
        <ProgressBar currentValue={goal.currentValue} targetValue={goal.targetValue} />
      </div>
    </div>
  );
};

export default GoalCard;