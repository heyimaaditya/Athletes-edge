interface ProgressBarProps {
  currentValue: number;
  targetValue: number;
}

const ProgressBar = ({ currentValue, targetValue }: ProgressBarProps) => {
  
  const percentage = Math.min((currentValue / targetValue) * 100, 100);

  return (
    <div>
      <div className="flex justify-between mb-1 text-sm text-gray-300">
        <span>{Math.round(currentValue)}</span>
        <span>{targetValue}</span>
      </div>
      <div className="w-full bg-gray-600 rounded-full h-2.5">
        <div 
          className="bg-green-500 h-2.5 rounded-full transition-all duration-500" 
          style={{ width: `${percentage}%` }}
        ></div>
      </div>
    </div>
  );
};

export default ProgressBar;