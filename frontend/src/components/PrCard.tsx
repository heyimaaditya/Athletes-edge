interface Pr {
  exerciseName: string;
  value: number;
  unit: string;
  recordDate: string;
}

const PrCard = ({ pr }: { pr: Pr }) => {
  return (
    <div className="bg-gray-700 p-4 rounded-md flex items-center gap-4">
      <div className="text-3xl">🏆</div>
      <div>
        <p className="font-bold text-white">{pr.exerciseName}</p>
        <p className="text-xl font-semibold text-amber-400">
          {pr.value} <span className="text-sm text-gray-300">{pr.unit}</span>
        </p>
        <p className="text-xs text-gray-400">
          on {new Date(pr.recordDate).toLocaleDateString()}
        </p>
      </div>
    </div>
  );
};

export default PrCard;